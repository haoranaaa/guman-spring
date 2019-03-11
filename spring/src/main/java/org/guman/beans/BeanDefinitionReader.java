package org.guman.beans;

import javax.xml.parsers.ParserConfigurationException;

/**
 * @author duanhaoran
 * @since 2019/3/10 2:54 PM
 */
public interface BeanDefinitionReader {

    void loadBeanDefinition(String resource) throws Exception;
}
