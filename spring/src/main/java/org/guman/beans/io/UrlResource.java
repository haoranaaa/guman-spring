package org.guman.beans.io;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author duanhaoran
 * @since 2019/3/10 7:12 PM
 */
public class UrlResource implements Resource {

    /**
     * 通过该属性加载资源
     */
    private final URL url;

    public UrlResource(URL url) {
        this.url = url;
    }

    /**
     * 通过URL加载资源
     * @return
     * @throws Exception
     */
    @Override
    public InputStream getInputStream() throws Exception {
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        return urlConnection.getInputStream();
    }
}
