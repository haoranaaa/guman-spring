package org.guman.utils;

/**
 * @author duanhaoran
 * @since 2019/3/10 2:34 PM
 */
public class StringUtil {

    public static String capitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        char[] chars = str.toCharArray();
        char c = chars[0];
        if (c >= 'a' && c <= 'z') {
            chars[0] = (char) (c - 32);
        }
        return new String(chars);
    }

    public static String deCapitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        char[] chars = str.toCharArray();
        char c = chars[0];
        if (c >= 'A' && c <= 'Z') {
            chars[0] = (char) (c + 32);
        }
        return new String(chars);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotBlank(String string) {
        return !isEmpty(string);
    }

}