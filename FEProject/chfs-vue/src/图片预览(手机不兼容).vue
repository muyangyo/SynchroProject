<template>
  <div>
    <el-button @click="previewImage">预览图片</el-button>
    <el-image-viewer
        v-if="showViewer"
        :url-list="imageUrls"
        :initial-index="initialIndex"
        @close="closeViewer"
    />
  </div>
</template>

<script setup>
import {ref} from 'vue';
import {optionalRequest} from '@/utils/RequestTool.js';
import {ElImageViewer} from 'element-plus';

// 定义响应式变量
const showViewer = ref(false);
const imageUrls = ref([]);
const initialIndex = ref(0);

// 预览图片的方法
const previewImage = () => {
  optionalRequest({
    method: 'GET',
    url: '/previewImage',
    responseType: 'blob' // 设置响应类型为blob
  }).then(response => {
    const url = window.URL.createObjectURL(new Blob([response.data]));
    imageUrls.value = [url];
    initialIndex.value = 0;
    showViewer.value = true;
  }).catch(error => {
    console.error('预览图片失败:', error);
  });
};

// 关闭图片查看器的方法
const closeViewer = () => {
  showViewer.value = false;
};
</script>