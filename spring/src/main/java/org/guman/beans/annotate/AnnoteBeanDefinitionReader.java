package org.guman.beans.annotate;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.guman.beans.AbstractBeanDefinitionReader;
import org.guman.beans.io.ResourceLoader;
import org.guman.beans.scanner.PackageScanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author duanhaoran
 * @since 2019/3/10 2:53 PM
 */
public class AnnoteBeanDefinitionReader extends AbstractBeanDefinitionReader {

    private static final String BASE_PACKAGE = "base-package";

    public AnnoteBeanDefinitionReader(ResourceLoader resourceLoader) {
        super(resourceLoader);
    }

    @Override
    public void loadBeanDefinition(String resource) throws Exception {
        doLoadBeanDefinition(resource);
    }

    private void doLoadBeanDefinition(String resource) {
        ServiceLoader<PackageScanner> scanner=ServiceLoader.load(PackageScanner.class);
        List<String> fullyQualifiedClassNameList;
        for (PackageScanner packageScanner: scanner) {
            try {
                fullyQualifiedClassNameList = packageScanner.getFullyQualifiedClassNameList(resource);
                break;
            } catch (IOException e) {
                // ignore
            }
        }

    }

    public String parserXml(String fileName) throws FileNotFoundException {
        URL resource = getClass().getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new FileNotFoundException(fileName + "can not be found !");
        }
        File inputXml = new File(resource.getFile());
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(inputXml);
            Element employees = document.getRootElement();
            for (Iterator i = employees.elementIterator(); i.hasNext(); ) {
                Element employee = (Element) i.next();
                if (employee.attribute(BASE_PACKAGE) != null) {
                    return employee.attribute(BASE_PACKAGE).getValue();
                }
            }
        } catch (DocumentException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
