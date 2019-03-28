package org.guman.beans.scanner;

import org.guman.utils.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * @author duanhaoran
 * @since 2019/3/28 2:35 PM
 */
public class ClassPathPackageScanner implements PackageScanner{

    private String basePackage;
    private ClassLoader cl;

    public ClassPathPackageScanner(String basePackage) {
        this.basePackage = basePackage;
        this.cl = getClass().getClassLoader();

    }

    public ClassPathPackageScanner(String basePackage, ClassLoader cl) {
        this.basePackage = basePackage;
        this.cl = cl;
    }

    @Override
    public List<String> getFullyQualifiedClassNameList(String basePackage) throws IOException {

        return doScan(basePackage, new ArrayList<>());
    }

    @Override
    public List<String> getFullyQualifiedClassNameList() throws IOException {

        return doScan(basePackage, new ArrayList<>());
    }


    private List<String> doScan(String basePackage, List<String> nameList) throws IOException {

        String splashPath = StringUtil.dotToSplash(basePackage);

        URL url = cl.getResource(splashPath);
        String filePath = StringUtil.getRootPath(url);
        List<String> names = null;
        if (isJarFile(filePath)) {
            names = readFromJarFile(filePath, splashPath);
        } else {
            names = readFromDirectory(filePath);
        }

        for (String name : names) {
            if (isClassFile(name)) {
                nameList.add(toFullyQualifiedName(name, basePackage));
            } else {
                doScan(basePackage + "." + name, nameList);
            }
        }

        return nameList;
    }

    private String toFullyQualifiedName(String shortName, String basePackage) {
        StringBuilder sb = new StringBuilder(basePackage);
        sb.append('.');
        sb.append(StringUtil.trimExtension(shortName));

        return sb.toString();
    }

    private List<String> readFromJarFile(String jarPath, String splashedPackageName) throws IOException {

        JarInputStream jarIn = new JarInputStream(new FileInputStream(jarPath));
        JarEntry entry = jarIn.getNextJarEntry();

        List<String> nameList = new ArrayList<>();
        while (null != entry) {
            String name = entry.getName();
            if (name.startsWith(splashedPackageName) && isClassFile(name)) {
                nameList.add(name);
            }

            entry = jarIn.getNextJarEntry();
        }

        return nameList;
    }

    private List<String> readFromDirectory(String path) {
        File file = new File(path);
        String[] names = file.list();

        if (null == names) {
            return null;
        }

        return Arrays.asList(names);
    }

    private boolean isClassFile(String name) {
        return name.endsWith(".class");
    }

    private boolean isJarFile(String name) {
        return name.endsWith(".jar");
    }

    /**
     * For test purpose.
     */
    public static void main(String[] args) throws Exception {
        PackageScanner scan = new ClassPathPackageScanner("org");
        List<String> fullyQualifiedClassNameList = scan.getFullyQualifiedClassNameList();
        fullyQualifiedClassNameList.forEach(System.out::println);
    }


}
