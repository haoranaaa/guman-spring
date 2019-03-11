package org.guman.beans.io;

import java.net.URL;

public class UrlResourceLoader implements ResourceLoader {


    @Override
    public Resource getResource(String location) {
        //根据资源名称查找资源
        URL url = getClass().getClassLoader().getResource(location);
        return new UrlResource(url);
    }
}