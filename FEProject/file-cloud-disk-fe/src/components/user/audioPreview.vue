<template>
  <el-popover placement='top-end' :width="playerWidth" :visible="visible" :show-arrow="false"
              popper-style="padding: 0; border: 0;">
    <template #reference>
      <div class="music-player-trigger"></div>
    </template>
    <div class="music-preview">
      <div ref="playerDiv" class="music-player"></div>
      <el-button class="close-button" @click="closePlayer">
        <el-icon>
          <Close/>
        </el-icon>
      </el-button>
    </div>
  </el-popover>
</template>

<script setup>
import {ref, onMounted, onBeforeUnmount} from 'vue';
import APlayer from 'aplayer';
import 'aplayer/dist/APlayer.min.css';
import getBlobData from "@/utils/getBlobData.js";
import {easyRequest, RequestMethods} from "@/utils/RequestTool.js";

const playerDiv = ref(null); // 音乐播放器容器
const player = ref(null); // 音乐播放器实例
const visible = ref(true); // 控制 Popover 的显示与隐藏
let playerWidth = isAndroidDevice === true ? 400 : 260; // 音乐播放器宽度
function isAndroidDevice() {
  return /(iPhone|iPad|iPod|iOS|Android)/i.test(navigator.userAgent);
}

// 外部传入的音频地址
const props = defineProps({
  sourceFilePath: { // 音频地址
    type: String,
    required: true,
  }
});

// 文件信息(请求后端接口获取)
const file = ref({
  mime: 'mp3',
  realFile: null,
  name: '未知歌曲',
  artist: '未知歌手',
});

// 初始化播放器
const initPlayer = () => {
  if (player.value) {
    player.value.destroy();
  }
  player.value = new APlayer({
    container: playerDiv.value,
    preload: 'auto', // 预加载音频
    theme: '#000000', // 主题颜色
    loop: 'one', // 单曲循环
    volume: 0.5, // 音量
    audio: [{
      url: file.value.realFile, // 音频地址
      name: file.value.name, // 歌曲名
      cover: new URL(`@/assets/music_cover.png`, import.meta.url).href, // 封面图
      artist: file.value.name, // 歌手名
      type: 'auto', // 自动识别类型
    }]
  });
};


const getFileAndInitPlayer = () => {
  easyRequest(RequestMethods.POST, "/file/getPreviewAudioInfo", {path: props.sourceFilePath}, false, true).then(response => {
    file.value.name = response.data.fileName;
    file.value.mime = response.data.mime;

    getBlobData('/previewAudio', {path: props.sourceFilePath}).then(response => {
      file.value.realFile = window.URL.createObjectURL(new Blob([response]));
      initPlayer();
    })

  });
};

const closePlayer = () => {
  if (player.value) {
    player.value.destroy();
  }
  visible.value = false;
  emit('close');
};

const emit = defineEmits(['close']);

onMounted(() => {
  console.log('musicPreview组件挂载');
  getFileAndInitPlayer();
});

onBeforeUnmount(() => {
  console.log('musicPreview组件销毁');
  window.URL.revokeObjectURL(file.value.realFile)
  file.value = null;
  if (player.value) {
    player.value.destroy();
    player.value = null;
  }
});
</script>

<style lang="scss" scoped>
.music-preview {
  position: relative;
  width: 100%;
  max-width: v-bind(playerWidth + 'px');
  background-color: rgba(0, 0, 0, 0.8);
  border-radius: 8px;
  padding: 10px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: column;
  align-items: center;

  .music-player {
    width: 100%;
  }

  .close-button {
    position: absolute;
    top: 10px;
    right: 10px;
    font-size: 20px;
    color: #999;
    background: none;
    border: none;
    cursor: pointer;
    padding-top: 0;
    padding-right: 0;

    &:hover {
      color: #7b7b7b;
    }
  }
}

.music-player-trigger {
  display: none;
}

.music-preview {
  padding: 0;
}
</style>

<style>


</style>