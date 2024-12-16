<template>
  <div>
    <video ref="playerDiv" class="plyr__video-embed" controls>
      <source :src="file.url" :type="file.mime"/>
    </video>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'
import Plyr from 'plyr'
import { easyRequest, RequestMethods } from "@/utils/RequestTool"

interface VideoFile {
  url: string
  mime: string
}

const playerDiv = ref<HTMLVideoElement | null>(null)
let Player: Plyr | null = null

interface Props {
  sourceFilePath: string
}

const props = defineProps<Props>()

const file = ref<VideoFile>({
  url: '',
  mime: '',
})

onMounted(() => {
  console.log("plyr 初始化中...")
  initPlayer()
})

const initPlayer = async () => {
  const response = await easyRequest(
    RequestMethods.POST, 
    '/file/preparingVideo', 
    {path: props.sourceFilePath}, 
    false, 
    true
  )

  if (response.statusCode === "SUCCESS" && response.data && response.data.mountRootPath) {
    file.value.mime = response.data.fileType.mimeType
    file.value.url = ""
    file.value.url = response.data.url

    const videoElement = playerDiv.value
    if (!videoElement) return

    Player = new Plyr(videoElement, {
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
        'play',
        'progress',
        'current-time',
        'mute',
        'volume',
        'settings',
        'pip',
        'fullscreen',
      ],
      settings: ['speed'],
      speed: {
        selected: 1,
        options: [0.5, 0.75, 1, 1.25, 1.5, 1.75, 2, 2.5, 3],
      },
      autoplay: true,
      volume: 0.5,
      loop: {active: false},
      keyboard: {focused: true, global: true},
      tooltips: {controls: true, seek: true},
      ratio: '16:9',
      storage: {enabled: true, key: 'plyr'},
      fullscreen: {enabled: true, fallback: true, iosNative: false},
      ads: {enabled: false},
    })
  }
}

onBeforeUnmount(() => {
  console.log("plyr 销毁中...")
  file.value = {
    url: '',
    mime: '',
  }
  if (Player) {
    Player.destroy()
  }
})
</script>

<style src="plyr/dist/plyr.css"></style>