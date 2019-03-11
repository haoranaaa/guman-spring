package org.guman.aop;

import org.guman.beans.factory.BeanFactory;

/**
 * @author duanhaoran
 * @since 2019/3/10 6:19 PM
 */
public interface BeanFactoryAware {
    /**
     * 注入BeanFactory
     * @param beanFactory
     */
    void setBeanFactory(BeanFactory beanFactory);
}
