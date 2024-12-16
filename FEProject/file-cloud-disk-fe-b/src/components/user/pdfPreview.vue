<template>
  <div>
    <vue-pdf-embed :source="pdfSource"/>
  </div>
</template>

<script setup lang="ts">
import VuePdfEmbed from 'vue-pdf-embed';
import {onBeforeUnmount, onMounted, ref} from "vue";
import getBlobData from "@/utils/getBlobData.js";

const pdfSource = ref<string | null>(null);

interface Props {
  sourceFilePath: string;
}

const props = defineProps<Props>();

onMounted(() => {
  console.log("PDF预览组件挂载");
  try {
    loadPdf();
  } catch (error) {
    console.error('Failed to load PDF:', error);
  }
});

const loadPdf = async () => {
  try {
    const response = await getBlobData('/previewPdf', {path: props.sourceFilePath});
    pdfSource.value = URL.createObjectURL(response);
  } catch (error) {
    console.error('Failed to load PDF:', error);
  }
}

onBeforeUnmount(() => {
  console.log("PDF预览组件销毁");
  if (pdfSource.value) {
    URL.revokeObjectURL(pdfSource.value);
  }
});
</script>