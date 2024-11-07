<template>
  <div>
    <vue3-video-play
        ref="videoPlayer"
        :width="800"
        :height="450"
        :src="videoSrc"
        :title="title"
    />
    <div v-if="isFastForwarding" class="fast-forward-text">快进中...</div>
    <el-button @mousedown="startFastForward" @mouseup="stopFastForward" @mouseleave="stopFastForward">
      长按快进
    </el-button>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue';
import Vue3VideoPlay from 'vue3-video-play';
import 'vue3-video-play/dist/style.css';
import {ElButton} from 'element-plus';

const videoPlayer = ref(null);
const isFastForwarding = ref(false);
const videoSrc = 'https://media.w3.org/2010/05/sintel/trailer.mp4'; // 替换为你的视频URL
const title = '视频标题'; // 替换为你的视频标题

let fastForwardInterval = null;

const startFastForward = () => {
  isFastForwarding.value = true;
  fastForwardInterval = setInterval(() => {
    if (videoPlayer.value) {
      const currentTime = videoPlayer.value.currentTime;
      videoPlayer.value.currentTime = currentTime + 1.5; // 快进1.5秒
    }
  }, 1000); // 每秒快进1.5秒
};

const stopFastForward = () => {
  isFastForwarding.value = false;
  clearInterval(fastForwardInterval);
};

onMounted(() => {
  // 确保视频播放器已经挂载
  if (videoPlayer.value) {
    console.log('Video player is ready');
  }
});
</script>

<style scoped>
.fast-forward-text {
  color: red;
  font-weight: bold;
  margin-top: 10px;
}
</style>