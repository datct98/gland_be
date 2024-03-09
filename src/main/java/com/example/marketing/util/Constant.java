package com.example.marketing.util;

import java.util.HashMap;
import java.util.Map;

public class Constant {
    public static String STATUS_SUCCESS = "00";
    public static String ACCOUNT_EXISTED = "11";
    public static String SYS_ERR = "-11";

    public static int PAGE_SIZE = 10;

    public static Map<String, String> MESSAGE_ERR = Map.of(
            ACCOUNT_EXISTED, "Tài khoản đã tồn tại!",
            SYS_ERR, "Hệ thống tạm thời gián đoạn");


}
