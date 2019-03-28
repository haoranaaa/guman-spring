package org.guman.beans.scanner;

import java.io.IOException;
import java.util.List;

/**
 * @author duanhaoran
 * @since 2019/3/28 2:35 PM
 */
public interface PackageScanner {

    List<String> getFullyQualifiedClassNameList(String basePackage) throws IOException;

    List<String> getFullyQualifiedClassNameList() throws IOException;
}
