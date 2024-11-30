<template>
  <div>
    <vue-pdf-embed :source="pdfSource"/>
  </div>
</template>

<script setup>
import VuePdfEmbed from 'vue-pdf-embed';
import {onBeforeUnmount, onMounted, ref} from "vue";
import getBlobData from "@/utils/getBlobData.js";

const pdfSource = ref(null);

const props = defineProps({
  sourceFilePath: { // 文件 URL
    type: String,
    required: true,
  },
});


await onMounted(() => {
  console.log("PDF预览组件挂载");
  try{
    loadPdf();
  }catch(error){
    console.error('Failed to load PDF:', error);
  }
});

const loadPdf = async () => {
  try {
    getBlobData('/previewPdf', {path: props.sourceFilePath}).then(response => {
      pdfSource.value = URL.createObjectURL(response); // 将Blob对象转换为URL
    })
  } catch (error) {
    console.error('Failed to load PDF:', error);
  }
}
await onBeforeUnmount(() => {
  console.log("PDF预览组件销毁");
  URL.revokeObjectURL(pdfSource.value);
});
</script>