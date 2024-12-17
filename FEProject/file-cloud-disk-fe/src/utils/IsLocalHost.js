export default function isLocalhost() {
    const hostname = window.location.hostname; // 获取当前页面的主机名
    return hostname === 'localhost' || hostname === '127.0.0.1';
}
