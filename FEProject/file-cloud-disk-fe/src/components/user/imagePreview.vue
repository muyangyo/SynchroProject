<template>
  <el-image-viewer :url-list="imageUrls" :initial-index="initialIndex" @close="closeViewer"/>
</template>

<script setup>
import {ref, onMounted, onUnmounted, onBeforeUnmount} from 'vue';
import {ElImageViewer} from 'element-plus';
import getBlobData from "@/utils/getBLOBData.js";

// 定义响应式变量
const imageUrls = ref([]);
const initialIndex = ref(0);

// 定义 props
const props = defineProps({
  sourceFileURL: {
    type: String,
    required: true,
  }
});


// 预览图片的方法
const previewImage = async () => {
  try {
    const response = getBlobData(props.sourceFileURL);
    const url = window.URL.createObjectURL(new Blob([response.data]));
    imageUrls.value = [url];
    initialIndex.value = 0;
  } catch (error) {
    console.error('预览图片失败:', error);
  }
};

// 关闭图片查看器的方法
const closeViewer = () => {
  // 释放资源
  imageUrls.value.forEach(url => window.URL.revokeObjectURL(url));
  imageUrls.value = [];
};

// 挂载时预览图片
onMounted(() => {
  console.log('ImagePreview mounted');
  previewImage();
});

// 销毁时释放资源
onBeforeUnmount(() => {
  console.log('ImagePreview unmounted');
  closeViewer();
});
</script>

<style scoped>
/* 可以根据需要添加样式 */
</style>