<template>
  <div ref="videoDiv"></div>
</template>

<script setup>
import DPlayer from 'dplayer'
import Hls from 'hls.js';
import {ref, reactive, onBeforeUnmount, onMounted} from 'vue'

const videoDiv = ref(null);
const player = reactive({
  instance: null
});

const videoUrl = ref("http://localhost:80/files/video"); //视频地址

onMounted(() => {
  player.instance = new DPlayer({
    // 容器
    container: videoDiv.value,
    // 是否自动播放
    autoplay: true,
    //是否直播
    live: false,
    // 主题色
    theme: '#0093ff',
    // 是否循环播放
    loop: false,
    // 语言(可选值: 'en', 'zh-cn', 'zh-tw')
    lang: 'zh-cn',
    // 是否开启截图(如果开启，视频和视频封面需要允许跨域)
    screenshot: false,
    // 是否开启热键
    hotkey: true,
    // 视频是否预加载(可选值: 'none', 'metadata', 'auto')
    preload: 'auto',
    // 默认音量
    volume: navigator.userAgent.toLowerCase().includes('android') ? 1 : 0.3,
    // 可选的播放速率，可以设置成自定义的数组
    playbackSpeed: [0.5, 0.75, 1, 1.25, 1.5, 1.75, 2, 2.5, 3],
    // 视频信息
    video: {
      url: videoUrl.value,
      type: 'auto',
      // customType: {
      //   customHls: function (video, player) {
      //     const hls = new Hls();
      //     hls.loadSource(video.src);
      //     hls.attachMedia(video);
      //   },
      // },
    },
    // 自定义右键菜单
    contextmenu: [],
    // 自定义进度条提示点
    highlight: [],
    // 阻止多个播放器同时播放，当前播放器播放时暂停其他播放器
    mutex: true
  });
})
;

onBeforeUnmount(() => {
  if (player.instance) {
    player.instance.destroy();
  }
});

</script>

<style lang='scss' scoped>
</style>