package org.guman.beans.annotate;

import org.guman.beans.AbstractBeanDefinitionReader;
import org.guman.beans.io.ResourceLoader;

/**
 * @author duanhaoran
 * @since 2019/3/10 2:53 PM
 */
public class AnnoteBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public AnnoteBeanDefinitionReader(ResourceLoader resourceLoader) {
        super(resourceLoader);
    }

    @Override
    public void loadBeanDefinition(String resource) {
        doLoadBeanDefinitions(resource);
    }

    private void doLoadBeanDefinitions(String resource) {

    }
}
