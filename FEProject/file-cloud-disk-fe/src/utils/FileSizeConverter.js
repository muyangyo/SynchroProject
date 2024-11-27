export const sizeTostr = (size) => {
    if (size === '') return;
    const KB = 1024;
    const MB = KB * 1024;
    const GB = MB * 1024;
    const TB = GB * 1024;

    let data = "";

    if (size < 0.1 * KB) {
        // 如果小于0.1KB 转化成B
        data = size.toFixed(2) + "B";
    } else if (size < 0.1 * MB) {
        // 如果小于0.1MB 转化成KB
        data = (size / KB).toFixed(2) + "KB";
    } else if (size < GB * 0.8) {
        // 如果小于1GB 转化成MB
        data = (size / MB).toFixed(2) + "MB";
    } else if (size < TB * 0.8) {
        // 其他转化成GB
        data = (size / GB).toFixed(2) + "GB";
    } else {
        // 超出800GB 转化成TB
        data = (size / TB).toFixed(2) + "TB";
    }

    let len = data.indexOf(".");
    let dec = data.substring(len + 1, len + 3);
    if (dec === "00") {
        //当小数点后为00时 去掉小数部分
        return data.substring(0, len) + data.substring(len + 3, len + 5);
    }
    return data;
};