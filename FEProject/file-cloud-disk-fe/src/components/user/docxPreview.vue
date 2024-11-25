<template>
  <div ref="docDiv" class="doc-content"></div>
</template>

<script setup>
import * as docx from "docx-preview";
import {ref, onMounted, onBeforeUnmount} from "vue";
import getBlobData from "@/utils/getBLOBData.js";

const docDiv = ref(null);

const props = defineProps({
  sourceFileURL: { // 文件 URL
    type: String,
    required: true,
  },
});


const initDoc = async () => {
  try {
    const response = await getBlobData(props.sourceFileURL);
    if (!response) {
      throw new Error("Failed to load docx file");
    }

    const blob = new Blob([response.data], {
      type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
    });
    // 使用 docx-preview 渲染文档
    await docx.renderAsync(blob, docDiv.value);
  } catch (error) {
    console.error("Failed to load docx file:", error);
  }
};

onMounted(() => {
  console.log("docx preview mounted");
  initDoc();
});

onBeforeUnmount(() => {
  console.log("docx preview unmount");
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