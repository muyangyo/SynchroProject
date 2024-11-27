<template>
  <div>
    <video ref="playerDiv" class="plyr__video-embed" controls>
      <source :src=" file.url " :type="file.mime"/>
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
    required: true,
  }
});

// 视频文件信息(请求后端接口获取)
const file = ref({
  url: '',
  mime: '',
});

let globalPlayer = null


onMounted(() => {
  console.log("plyr 初始化中...");
  easyRequest(RequestMethods.POST, '/file/preparingVideo', props.sourceFilePath, false,).then(response => {
    if (response.statusCode === "SUCCESS" && response.data && response.data.mountRootPath) {
      file.value.mime = response.data.fileType.mimeType;
      console.warn("视频地址：" + response.data.url)
      file.value.url = "";
      file.value.url = response.data.url; // /api/file/previewVideo
      const videoElement = playerDiv.value;
      globalPlayer = new Plyr(videoElement, {})
      globalPlayer.play()
    }
  })
});

onBeforeUnmount(() => {
  console.log("plyr 销毁中...");
  file.value = null;
  if (globalPlayer) {
    globalPlayer.destroy(); // 销毁 Plyr
  }
});
</script>

<style src="plyr/dist/plyr.css"></style>