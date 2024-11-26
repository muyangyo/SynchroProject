<template>
  <div>
    <video ref="playerDiv" class="plyr__video-embed" controls>
      <source :src="file.url ? file.url : props.sourceFilePath" :type="file.mime"/>
    </video>
  </div>
</template>

<script setup>
import {ref, onMounted, onBeforeUnmount} from 'vue';
import Plyr from 'plyr';
import {easyRequest, RequestMethods} from "@/utils/RequestTool.js";

const playerDiv = ref(null);

// 调用组件时传入的视频地址
const props = defineProps({
  sourceFilePath: {
    type: String,
    default: 'https://cdn.plyr.io/static/demo/View_From_A_Blue_Moon_Trailer-576p.mp4'
  }
});

// 视频文件信息(请求后端接口获取)
const file = ref({
  url: '',
  mime: '',
});

easyRequest({
  method: RequestMethods.POST, url: '/file/preparingVideo',
  data: props.sourceFilePath
}).then(response => {
  if (response.statusCode === "SUCCESS" && response.data && response.data.mountRootPath) {
    file.value.mime = response.data.fileType.mimeType;
    file.value.url = response.data.fileType.url;
  }
})

onMounted(() => {
  console.log("plyr 初始化中...");
  playerDiv.value = new Plyr(playerDiv.value, {
    i18n: {
      restart: '重新开始',
      rewind: '倒带',
      play: '播放',
      pause: '暂停',
      seek: '调整进度',
      volume: '音量',
      mute: '静音',
      unmute: '取消静音',
      enterFullscreen: '全屏',
      exitFullscreen: '退出全屏',
      frameTitle: '视频标题',
      settings: '设置',
      menuBack: '返回菜单',
      speed: '速度',
      normal: '正常速度',
      quality: '画质',
      loop: '循环播放',
      playbackRate: '播放速度',
      search: '搜索',
      reset: '重置',
      file: '文件',
      pip: '画中画',
    },
    controls: [
      'play', // 播放按钮
      'progress', // 进度条
      'current-time', // 当前时间
      'mute', // 静音按钮
      'volume', // 音量控制
      'settings', // 设置按钮
      'pip', // 画中画按钮
      'fullscreen', // 全屏按钮
    ],
    settings: ['speed',], // 设置菜单中的选项
    speed: {
      selected: 1, // 默认播放速度
      options: [0.5, 0.75, 1, 1.25, 1.5, 1.75, 2, 2.5, 3], // 可选的播放速度
    },
    autoplay: true, // 是否自动播放
    volume: 0.5, // 默认音量
    loop: {active: false}, // 是否循环播放
    keyboard: {focused: true, global: true}, // 是否开启热键
    tooltips: {controls: true, seek: true}, // 是否显示工具提示
    ratio: '16:9', // 视频比例
    storage: {enabled: true, key: 'plyr'}, // 是否启用本地存储
    fullscreen: {enabled: true, fallback: true, iosNative: false}, // 全屏设置
    ads: {enabled: false}, // 广告设置
  }); // 初始化 Plyr

});

onBeforeUnmount(() => {
  console.log("plyr 销毁中...");
  file.value = null;
  if (playerDiv.value) {
    playerDiv.value.destroy(); // 销毁 Plyr
  }
});
</script>

<style src="plyr/dist/plyr.css"></style>