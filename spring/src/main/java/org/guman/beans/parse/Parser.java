package org.guman.beans.parse;

import javax.swing.text.Document;
import java.io.InputStream;

/**
 * @author duanhaoran
 * @since 2019/3/28 3:01 PM
 */
public interface Parser {

    Document parse(InputStream inputStream);
}
