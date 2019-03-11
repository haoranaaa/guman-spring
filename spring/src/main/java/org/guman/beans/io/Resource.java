package org.guman.beans.io;

import java.io.InputStream;

/**
 * @author duanhaoran
 * @since 2019/3/10 7:12 PM
 */
public interface Resource {
    /**
     * 获取资源的输入流
     * @return
     * @throws Exception
     */
    InputStream getInputStream() throws Exception;
}
