package org.guman.context;

import org.guman.beans.BeanPostProcessor;
import org.guman.beans.factory.AbstractBeanFactory;

import java.util.List;

/**
 * @author duanhaoran
 * @since 2019/3/10 6:09 PM
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    private AbstractBeanFactory beanFactory;

    @Override
    public Object getBean(String name) throws Exception {
        return beanFactory.getBean(name);
    }

    public AbstractApplicationContext(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void refresh() throws Exception {

        loadBeanDefinitions(beanFactory);

        registerBeanPostProcessors(beanFactory);

        onRefresh();
    }

    /**
     * 从 BeanFactory 中的bean的定义找实现 BeanPostProcessor接口的类(例如：AspectAwareAdvisorAutoProxyCreator.java)
     * 注册到 AbstractBeanFactory 维护的 BeanPostProcessor 列表中去(用于在bean实例化后对其进行一些其他处理，
     * 可以看看getBean方法()中的initializeBean()的调用)。
     * 后面调用getBean方法通过AspectJAwareAdvisorAutoProxyCreator#postProcessAfterInitialization()方法中调用了
     * getBeansOfType方法保证了 PointcutAdvisor 的实例化顺序优于普通 Bean。
     *
     * @param beanFactory
     */
    private void registerBeanPostProcessors(AbstractBeanFactory beanFactory) throws Exception {
        List<BeanPostProcessor> beanPostProcessors = beanFactory.getBeanOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            beanFactory.addBeanPostProcessors((beanPostProcessor));
        }
    }


    /**
     * 默认以单例方式实例化所有Bean
     */
    private void onRefresh() throws Exception {
        beanFactory.preInstantiateSingletons();
    }

    /**
     * 由子类决定由哪种方式加载BeanDefinition并保存到BeanFactory中
     *
     * @param beanFactory
     * @throws Exception
     */
    protected abstract void loadBeanDefinitions(AbstractBeanFactory beanFactory) throws Exception;

}
