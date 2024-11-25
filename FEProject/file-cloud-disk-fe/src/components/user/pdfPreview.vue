<template>
  <div>
    <vue-pdf-embed :source="pdfSource"/>
  </div>
</template>

<script setup>
import VuePdfEmbed from 'vue-pdf-embed';
import {onBeforeUnmount, onMounted, ref} from "vue";
import getBlobData from "@/utils/getBLOBData.js";

const pdfSource = ref(null);

const props = defineProps({
  sourceFileURL: { // 文件 URL
    type: String,
    required: true,
  },
});


onMounted(() => {
  console.log("pdfPreview mounted");
  loadPdf();
});

const loadPdf = async () => {
  try {
    const response = await getBlobData(props.sourceFileURL)
    pdfSource.value = URL.createObjectURL(response.data); // 将Blob对象转换为URL
  } catch (error) {
    console.error('Failed to load PDF:', error);
  }
}
onBeforeUnmount(() => {
  console.log("pdfPreview unmounted");
  URL.revokeObjectURL(pdfSource.value);
});
</script>