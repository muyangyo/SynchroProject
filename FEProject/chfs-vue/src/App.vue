<template>
  <div class="music">
    <div class="body-content">
      <div ref="playerRef" class="music-player"></div>
    </div>
    <el-button @click="previewAudio">预览音频</el-button>
  </div>
</template>

<script setup>
import {ref, onMounted, onUnmounted} from 'vue';
import {optionalRequest} from '@/utils/RequestTool.js';
import APlayer from 'aplayer';
import 'aplayer/dist/APlayer.min.css';

const playerRef = ref();
const player = ref(null);
const audioUrl = ref('');

const initPlayer = () => {
  if (player.value) {
    player.value.destroy();
  }
  player.value = new APlayer({
    container: playerRef.value,
    audio: [{
      url: audioUrl.value,
      name: 'temp',
      artist: '',
      mini: true,
      cover: new URL(`@/assets/music_cover.png`, import.meta.url).href,
    }]
  });
};

const previewAudio = () => {
  optionalRequest({
    method: 'GET',
    url: '/previewAudio',
    responseType: 'blob' // 设置响应类型为blob
  }).then(response => {
    const url = window.URL.createObjectURL(new Blob([response.data]));
    audioUrl.value = url;
    initPlayer();
  }).catch(error => {
    console.error('预览音频失败:', error);
  });
};

onMounted(() => {
  if (audioUrl.value) {
    initPlayer();
  }
});

onUnmounted(() => {
  if (player.value) {
    player.value.destroy();
  }
});
</script>

<style lang="scss" scoped>
.music {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;

  .body-content {
    text-align: center;
    width: 80%;

    .cover {
      margin: 0px auto;
      width: 200px;
      text-align: center;

      img {
        width: 100%;
      }
    }

    .music-player {
      margin-top: 20px;
    }
  }
}
</style>