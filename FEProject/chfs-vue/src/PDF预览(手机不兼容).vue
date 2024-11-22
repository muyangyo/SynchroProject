<template>
  <div>
    <vue-pdf-embed :source="pdfSource"/>
  </div>
</template>

<script setup>
import VuePdfEmbed from 'vue-pdf-embed';
import {optionalRequest} from '@/utils/RequestTool.js';
import {onMounted, onUnmounted, ref} from "vue";

const pdfSource = ref(null);
onMounted(() => {
  loadPdf();
});
const loadPdf = async () => {
  try {
    const response = await optionalRequest({
      method: 'GET',
      url: '/previewPdf',
      responseType: 'blob' // 重要：设置响应类型为blob
    });

    // 将Blob对象转换为URL
    pdfSource.value = URL.createObjectURL(response.data);
  } catch (error) {
    console.error('Failed to load PDF:', error);
  }
}
onUnmounted(() => {
  // 释放URL对象
  URL.revokeObjectURL(pdfSource.value);
});
</script>