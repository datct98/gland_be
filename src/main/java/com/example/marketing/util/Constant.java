package com.example.marketing.util;

import java.util.Map;

public class Constant {
    public static String STATUS_SUCCESS = "200";
    public static String ACCOUNT_EXISTED = "11";
    public static String ACCOUNT_NOT_EXISTED = "-1";
    public static String SYS_ERR = "-11";
    public static String SCRIPT_NOT_EXISTED = "-111";
    public static String TASK_NOT_EXISTED ="-222";
    public static String TASK_STATUS_NOT_EXISTED ="-333";
    public static String TASK_INFO_NOT_EXISTED ="-444";
    public static String WORK_NOT_EXISTED ="-555";
    public static String DEPARTMENT_NOT_EXISTED ="-666";
    public static String ID_CUSTOM_EXISTED ="-777";
    public static String CONNECTION_NOT_EXISTED ="-888";
    public static String CONFIG_SYSTEM_NOT_EXISTED ="-999";

    public static int PAGE_SIZE = 10;

    public static Map<String, String> MESSAGE_ERR = Map.of(
            ACCOUNT_EXISTED, "Tài khoản đã tồn tại!",
            SYS_ERR, "Hệ thống tạm thời gián đoạn!",
            SCRIPT_NOT_EXISTED, "Không tìm thấy kịch bản!",
            TASK_NOT_EXISTED, "Không tìm thấy công việc",
            TASK_STATUS_NOT_EXISTED, "Không tìm thấy trạng thái công việc",
            TASK_INFO_NOT_EXISTED, "Không tìm thấy thông tin công việc",
            WORK_NOT_EXISTED, "Không tìm thấy công việc",
            DEPARTMENT_NOT_EXISTED, "Không tìm thấy phòng ban",
            ID_CUSTOM_EXISTED, "ID nhập tay đã có rồi!",
            CONNECTION_NOT_EXISTED, "Không tìm thấy dữ liệu kết nối");


}
