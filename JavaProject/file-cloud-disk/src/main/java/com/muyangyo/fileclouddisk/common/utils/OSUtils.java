package com.muyangyo.fileclouddisk.common.utils;


import com.muyangyo.fileclouddisk.common.model.enums.SystemType;

public class OSUtils {

    // 获取操作系统名称，并转换为小写
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    // 检查当前操作系统是否为Windows
    public static boolean isWindows() {
        return OS_NAME.contains("win");
    }

    // 检查当前操作系统是否为Linux
    public static boolean isLinux() {
        return OS_NAME.contains("nux") || OS_NAME.contains("nix");
    }

    public static SystemType getSystemType() {
        if (isWindows()) {
            return SystemType.WINDOWS;
        } else if (isLinux()) {
            return SystemType.LINUX;
        } else {
            return SystemType.OTHER;
        }
    }


//    // 检查当前操作系统是否为Mac
//    public static boolean isMac() {
//        return OS_NAME.contains("mac");
//    }
//    // 检查当前操作系统是否为Unix及其变种
//    public static boolean isUnix() {
//        return OS_NAME.contains("nix") || OS_NAME.contains("nux") || OS_NAME.contains("aix");
//    }
//
//    // 检查当前操作系统是否为Solaris
//    public static boolean isSolaris() {
//        return OS_NAME.contains("sunos");
//    }
//
//    // 获取当前操作系统名称
//    public static String getOSName() {
//        return OS_NAME;
//    }

}
