package org.guman.context;

import org.guman.beans.BeanDefinition;
import org.guman.beans.annotate.AnnoteBeanDefinitionReader;
import org.guman.beans.factory.AbstractBeanFactory;
import org.guman.beans.factory.AutowireCapableBeanFactory;
import org.guman.beans.io.UrlResourceLoader;

import java.util.Map;

/**
 * @author duanhaoran
 * @since 2019/3/10 6:15 PM
 */
public class AnnotateApplicationContext extends AbstractApplicationContext{

    private String configLocation;

    public AnnotateApplicationContext(String configLocation){
        this(configLocation,new AutowireCapableBeanFactory());
    }
    public AnnotateApplicationContext(String configLocation ,AbstractBeanFactory beanFactory) {
        super(beanFactory);
        this.configLocation = configLocation;

    }

    @Override
    protected void loadBeanDefinitions(AbstractBeanFactory beanFactory) throws Exception {
        AnnoteBeanDefinitionReader annoteBeanDefinitionReader=new AnnoteBeanDefinitionReader(new UrlResourceLoader());
        annoteBeanDefinitionReader.loadBeanDefinition(configLocation);
        for(Map.Entry<String,BeanDefinition > entry:annoteBeanDefinitionReader.getRegistry().entrySet()){
            beanFactory.getBeanDefinitionConcurrentMap().put(entry.getKey(),entry.getValue());
        }
    }
}
