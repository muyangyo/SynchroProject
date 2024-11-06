import {useDark, useToggle} from "@vueuse/core";

const config = {
    theme: "system", // "system" 跟随系统主题, "dark" 黑色主题, "light" 白色主题
    baseUrl: "https://example.com",
    userBaseUrl: "/user",
    managerBaseUrl: "/manager"
}

/**
 * 初始化主题
 * @returns toggleDark - 返回一个 toggleDark 函数, 用于控制暗黑模式( config.theme 为空时生效 )
 */
const initTheme = () => {
    const theme = ["system", "dark", "light"];
    const isDark = useDark(); // 初始值为 false, 返回的是一个响应式变量, 用于管理暗黑模式状态
    const toggleDark = useToggle(isDark);

    // 获取系统主题
    const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');

    const setTheme = () => {
        if (config.theme !== null && theme.includes(config.theme)) {
            if (config.theme === "system") {
                // 跟随系统主题
                isDark.value = mediaQuery.matches; // 直接设置 isDark 的值
            } else if (config.theme === "dark") {
                isDark.value = true; // 强制设置为暗黑主题
            } else if (config.theme === "light") {
                isDark.value = false; // 强制设置为明亮主题
            }
        }
    };

    // 初始设置
    setTheme();//没有值的话,请使用 toggleDark 去控制主题

    if (config.theme === "system") {
        // 监听主题变化
        mediaQuery.addEventListener('change', setTheme);
    }

    // 返回 toggleDark 以便在需要时控制暗黑模式
    return {toggleDark};
}

export {initTheme, config}
