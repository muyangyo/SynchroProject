import {fileURLToPath, URL} from 'node:url'
import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
// import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
    plugins: [
        vue(),
        // vueDevTools(), //todo: 打包时关闭
    ],
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        },
    },
    // base: './' // todo: 打包时打开

            server: { //todo: 打包时关闭 这个就是前端代理(分离的时候)
                proxy: {
                    '/api': 'http://localhost:80'
                },
            },
})