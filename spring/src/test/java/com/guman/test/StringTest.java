package com.guman.test;

/**
 * @author duanhaoran
 * @since 2019/3/29 1:18 PM
 */
public class StringTest {
    public static void main(String[] args) {
        String sql = "update %s set wolf_level= ? and wolf_frequency = ? where room_id= ? ";
        System.out.println(String.format(sql,"adad"));
    }
}
