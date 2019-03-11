package org.guman.beans.factory;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.guman.beans.BeanDefinition;
import org.guman.beans.BeanPostProcessor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * BeanFactory 的一种抽象类实现，规范了 IoC 容器的基本结构。
 * IoC 容器的结构：AbstractBeanFactory
 * 维护一个 beanDefinitionMap 哈希表用于保存类的定义信息（BeanDefinition）。
 *
 * @author duanhaoran
 * @since 2019/3/10 2:20 PM
 */
@Getter
public abstract class AbstractBeanFactory implements BeanFactory {

    public AbstractBeanFactory() {

    }

    /**
     * 根据name存储，需要实例化的beanDefinition
     */
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionConcurrentMap = new ConcurrentHashMap<>();

    /**
     * 保存注册完成的beanName
     */
    private List<String> beanDefinitionNames = new LinkedList<>();

    private List<BeanPostProcessor> beanPostProcessors = new LinkedList<>();

    protected abstract void injectPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception;

    @Override
    public Object getBean(String name) throws Exception {
        BeanDefinition beanDefinition = this.beanDefinitionConcurrentMap.get(name);
        if (beanDefinition == null) {
            throw new ClassNotFoundException("class " + name + "can not be find !");
        }
        Object bean = beanDefinition.getBean();
        if (bean == null){
            bean = doCreateBean(beanDefinition);
        }
        return bean;
    }

    /**
     * 初始化bean
     * 可以在此做AOP处理，返回的是一个代理对象
     * @param bean
     * @param name
     * @return Object
     * @throws Exception
     */
    private Object initializeBean(Object bean, String name) throws Exception {
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessBeforeInitialization(bean, name);
        }
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessAfterInitialization(bean, name);
        }
        return bean;
    }

    private Object doCreateBean(BeanDefinition beanDefinition) throws Exception {
        Object bean = createInstance(beanDefinition);
        beanDefinition.setBean(bean);
        injectPropertyValues(bean,beanDefinition);
        return bean;
    }

    private Object createInstance(BeanDefinition beanDefinition) throws IllegalAccessException, InstantiationException {
        return beanDefinition.getBeanClass().newInstance();
    }

    public List getBeanOfType(Class<?> clazz){
        return beanDefinitionNames.stream().map(name-> beanDefinitionConcurrentMap.get(name))
                .filter(beanDefinition -> beanDefinition != null
                        && !clazz.isAssignableFrom(beanDefinition.getBeanClass()))
                .collect(Collectors.toList());
    }
    /**
     * 注册某个beanName的定义
     * @param name
     * @param beanDefinition
     */
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanDefinitionConcurrentMap.put(name, beanDefinition);
        beanDefinitionNames.add(name);
    }

    /**
     * 添加处理器
     * @param beanPostProcessor
     */
    public void addBeanPostProcessors(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.add(beanPostProcessor);
    }


    public void preInstantiateSingletons() throws Exception {
        for(String beanName : beanDefinitionNames){
            getBean(beanName);
        }
    };
}
