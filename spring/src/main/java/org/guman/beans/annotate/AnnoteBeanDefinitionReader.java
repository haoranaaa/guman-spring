package org.guman.beans.annotate;

import org.apache.commons.lang3.StringUtils;
import org.guman.aop.BeanReference;
import org.guman.beans.AbstractBeanDefinitionReader;
import org.guman.beans.BeanDefinition;
import org.guman.beans.PropertyValue;
import org.guman.beans.io.ResourceLoader;
import org.guman.constant.SpringConstant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static java.lang.System.in;

/**
 * @author duanhaoran
 * @since 2019/3/10 2:53 PM
 */
public class AnnoteBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public AnnoteBeanDefinitionReader(ResourceLoader resourceLoader) {
        super(resourceLoader);
    }

    @Override
    public void loadBeanDefinition(String resource) throws Exception {
        doLoadBeanDefinitions(resource);
    }

    private void doLoadBeanDefinitions(String resource) throws Exception {
        //获取工厂
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //获取生成器
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        //解析为document
        Document doc = documentBuilder.parse(in);
        //解析并注册其中的Bean
        registerBeanDefinitions(doc);
        //关闭流
        in.close();
    }

    private void registerBeanDefinitions(Document doc) {
        Element elements = doc.getDocumentElement();
        //解析
        parseBeanDefinitions(elements);

    }

    private void parseBeanDefinitions(Element elements) {
        NodeList childNodes = elements.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if(item instanceof Element){
                Element bean = (Element) item;
                //解析<bean>标签
                processBeanDefinitions(bean);
            }
        }
    }

    private void processBeanDefinitions(Element bean) {
        //获取bean标签的id属性作为bean的Name
        String name = bean.getAttribute(SpringConstant.BeanLabelConstants.ID);
        String className = bean.getAttribute(SpringConstant.BeanLabelConstants.CLAZZ);
        BeanDefinition beanDefinition = new BeanDefinition();
        //解析bean标签下的property子标签
        processProperty(bean, beanDefinition);
        //填充beanDefinition属性
        beanDefinition.setBeanClassName(className);
        //注册类定义
        getRegistry().put(name, beanDefinition);
    }
    /**
     * 解析bean标签下的property子标签
	 * @param bean
	 * @param beanDefinition
	 */
    private void processProperty(Element bean, BeanDefinition beanDefinition) {
        // 获取所有property标签
        NodeList propertyNodes = bean.getElementsByTagName(SpringConstant.BeanLabelConstants.PROPERTY);
        for (int i = 0; i < propertyNodes.getLength(); i++) {
            Node node = propertyNodes.item(i);
            if (node instanceof Element) {
                Element propertyEle = (Element) node;
                String name = propertyEle.getAttribute(SpringConstant.BeanLabelConstants.NAME);
                String value = propertyEle.getAttribute(SpringConstant.BeanLabelConstants.VALUE);
                // 如果是value型的属性值
                if (!StringUtils.isEmpty(value)) {
                    beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue<>(name, value));
                }
                else {
                    // 否则是ref型的属性值
                    String ref = propertyEle.getAttribute(SpringConstant.BeanLabelConstants.REF);
                    if (StringUtils.isEmpty(ref)) {
                        throw new IllegalArgumentException("Configuration problem: <property> element for property '"
                                + name + "' must specify a ref or value");
                    }
                    BeanReference beanReference = new BeanReference(ref);
                    // 保存一个ref型属性值的属性
                    beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue<>(name, beanReference));
                }
            }
        }
    }
}
