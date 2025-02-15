import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.RandomUtil;

import java.net.NetworkInterface;
import java.util.Enumeration;

public class DeviceIdGenerator {

    // 生成设备唯一码（示例：基于 MAC 地址）

    private static String generateDeviceId() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); // 获取所有网络接口
            while (interfaces.hasMoreElements()) { // 遍历网络接口
                NetworkInterface ni = interfaces.nextElement(); // 获取当前网络接口
                if (ni.isLoopback() || ni.isVirtual() || !ni.isUp()) continue; // 跳过回环接口、虚拟接口、未启动的接口
                byte[] macBytes = ni.getHardwareAddress(); // 获取 MAC 地址
                if (macBytes != null) { // MAC 地址存在
                    return Convert.toHex(macBytes).toUpperCase(); // 将 MAC 地址转换为大写十六进制字符串并返回
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "UNKNOWN_DEVICE";
    }

    public static String generateConfusedDeviceId() {
        String originalDeviceId = generateDeviceId();// 获取原始设备 ID
        int length = originalDeviceId.length();
        String prefix = originalDeviceId.substring(0, length / 2);// 截取前半部分
        String suffix = originalDeviceId.substring(length / 2, length); // 截取后半部分

        String prefixConfused = RandomUtil.randomString(length / 2).toUpperCase();
        String suffixConfused = RandomUtil.randomString(length / 2).toUpperCase();

        return prefixConfused + "-" + prefix + "-" + suffixConfused + "-" + suffix;
    }

    public static String getOriginalDeviceId(String confuseDeviceId) {
        String[] parts = confuseDeviceId.split("-");
        if (parts.length != 4) {
            return "INVALID_DEVICE_ID";
        }
        return parts[1] + parts[3];
    }


    public static void main(String[] args) {
        System.out.println(generateConfusedDeviceId());
        System.out.println(getOriginalDeviceId(generateConfusedDeviceId()));

    }
}