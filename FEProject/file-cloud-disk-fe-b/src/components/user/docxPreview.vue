<template>
  <div ref="docDiv" class="doc-content"></div>
</template>

<script setup lang="ts">
import * as docx from "docx-preview";
import {ref, onMounted, onBeforeUnmount} from "vue";
import getBlobData from "@/utils/getBlobData.js";

const docDiv = ref<HTMLElement | null>(null);

interface Props {
  sourceFilePath: string;
}

const props = defineProps<Props>();

const initDoc = async () => {
  try {
    const response = await getBlobData('/previewDocx', {path: props.sourceFilePath});
    if (!response) {
      throw new Error("Failed to load docx file");
    }

    const blob = new Blob([response], {
      type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
    });
    
    if (docDiv.value) {
      await docx.renderAsync(blob, docDiv.value);
    }
  } catch (error) {
    console.error("Failed to load docx file:", error);
  }
};

onMounted(() => {
  console.log("docx预览组件挂载");
  initDoc();
});

onBeforeUnmount(() => {
  console.log("docx预览组件卸载");
});
</script>

<style lang="scss" scoped>
.doc-content {
  margin: 0px auto;

  :deep .docx-wrapper {
    background: #fff;
    padding: 10px 0px;
  }

  :deep .docx-wrapper > section.docx {
    margin-bottom: 0px;
  }
}
</style>