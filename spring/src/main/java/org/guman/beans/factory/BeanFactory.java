package org.guman.beans.factory;

/**
 * 标识一个 IoC 容器。
 *
 * 以 BeanFactory 接口为核心发散出的几个类.
 * 用于解决 IoC 容器在 已经获取 Bean 的 定义 的情况下，
 * 如何装配、获取 Bean 实例 的问题。
 * @author duanhaoran
 * @since 2019/3/10 2:19 PM
 */
public interface BeanFactory {

    /**
     * 通过 getBean(String) 方法来 获取bean实例
     * @param beanName
     * @return
     * @throws Exception
     */
    Object getBean(String beanName) throws Exception;
}
