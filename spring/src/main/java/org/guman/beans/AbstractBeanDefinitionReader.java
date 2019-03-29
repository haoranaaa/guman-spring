package org.guman.beans;

import com.google.common.collect.Maps;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.guman.beans.annotation.Component;
import org.guman.beans.io.ResourceLoader;


import java.util.Map;

/**
 * @author duanhaoran
 * @since 2019/3/10 2:55 PM
 */
@Getter
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {
    /**
     * 通过String->BeanDefinition存储IOC容器中的类定义
     * String:默认bean标签的id
     */
    protected Map<String, BeanDefinition> registry;

    /**
     * 保存了资源加载器，将加载过后的BeanDefinition保存到registry中
     */
    private ResourceLoader resourceLoader;

    /**
     * 缩小类的实例域
     *
     * @param resourceLoader
     */
    protected AbstractBeanDefinitionReader(ResourceLoader resourceLoader) {
        this.registry = Maps.newHashMap();
        this.resourceLoader = resourceLoader;
    }

    public BeanDefinition createBean(String clazzName) {
        try {
            Class<?> clazz = Class.forName(clazzName);
            Component component = null;
            if((component = clazz.getAnnotation(Component.class)) != null){
                BeanDefinition bean = new BeanDefinition();
                bean.setBeanClass(clazz);
                Object obj = clazz.newInstance();
                bean.setBean(obj);
                String beanName = getBeanName(clazzName);
                String beanClassName = StringUtils.isNotBlank(component.value()) ? component.value() : beanName;
                bean.setBeanClassName(beanClassName);
                return bean;
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            //ignore 跳过
            e.printStackTrace();
        }
        return null;
    }

    private String getBeanName(String clazzName) {
        String[] split = clazzName.split("\\.");
        return split[split.length - 1];
    }
}
