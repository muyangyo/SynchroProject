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

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'
import APlayer from 'aplayer'
import 'aplayer/dist/APlayer.min.css'
import getBlobData from "@/utils/getBlobData"
import { easyRequest, RequestMethods } from "@/utils/RequestTool"

interface AudioFile {
  mime: string
  realFile: string | null
  name: string
  artist: string
}

const playerDiv = ref<HTMLElement | null>(null)
const player = ref<APlayer | null>(null)
const visible = ref<boolean>(true)
const playerWidth = isAndroidDevice() ? 400 : 260

function isAndroidDevice(): boolean {
  return /(iPhone|iPad|iPod|iOS|Android)/i.test(navigator.userAgent)
}

interface Props {
  sourceFilePath: string
}

const props = defineProps<Props>()

const file = ref<AudioFile>({
  mime: 'mp3',
  realFile: null,
  name: '未知歌曲',
  artist: '未知歌手',
})

const initPlayer = () => {
  if (player.value) {
    player.value.destroy()
  }
  if (!playerDiv.value || !file.value.realFile) return

  player.value = new APlayer({
    container: playerDiv.value,
    preload: 'auto',
    theme: '#000000',
    loop: 'one',
    volume: 0.5,
    audio: [{
      url: file.value.realFile,
      name: file.value.name,
      cover: new URL(`@/assets/music_cover.png`, import.meta.url).href,
      artist: file.value.name,
      type: 'auto',
    }]
  })
}

const getFileAndInitPlayer = async () => {
  const response = await easyRequest(
    RequestMethods.POST, 
    "/file/getPreviewAudioInfo", 
    {path: props.sourceFilePath}, 
    false, 
    true
  )
  file.value.name = response.data.fileName
  file.value.mime = response.data.mime

  const blobResponse = await getBlobData('/previewAudio', {path: props.sourceFilePath})
  file.value.realFile = window.URL.createObjectURL(blobResponse)
  initPlayer()
}

const emit = defineEmits<{
  (e: 'close'): void
}>()

const closePlayer = () => {
  if (player.value) {
    player.value.destroy()
  }
  visible.value = false
  emit('close')
}

onMounted(() => {
  console.log('musicPreview组件挂载')
  getFileAndInitPlayer()
})

onBeforeUnmount(() => {
  console.log('musicPreview组件销毁')
  if (file.value.realFile) {
    window.URL.revokeObjectURL(file.value.realFile)
  }
  file.value = {
    mime: 'mp3',
    realFile: null,
    name: '未知歌曲',
    artist: '未知歌手',
  }
  if (player.value) {
    player.value.destroy()
    player.value = null
  }
})
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