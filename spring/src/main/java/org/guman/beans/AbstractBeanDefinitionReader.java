package org.guman.beans;

import com.google.common.collect.Maps;
import lombok.Getter;
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
     * @param resourceLoader
     */
    protected AbstractBeanDefinitionReader(ResourceLoader resourceLoader) {
        this.registry = Maps.newHashMap();
        this.resourceLoader = resourceLoader;
    }

}
