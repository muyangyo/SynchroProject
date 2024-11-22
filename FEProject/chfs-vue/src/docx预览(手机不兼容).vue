<template>
  <div ref="docRef" class="doc-content"></div>
</template>

<script setup>
import * as docx from "docx-preview";
import {ref, onMounted} from "vue";
import {optionalRequest, RequestMethods} from "@/utils/RequestTool.js";

const docRef = ref();

const initDoc = async () => {
  try {
    const response = await optionalRequest({
      method: RequestMethods.GET,
      url: "/previewDocx",
      responseType: "blob",
    });

    if (!response) {
      return;
    }

    // 确保返回的数据是 Blob 类型
    const blob = new Blob([response.data], {
      type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
    });

    // 使用 docx-preview 渲染文档
    await docx.renderAsync(blob, docRef.value);
  } catch (error) {
    console.error("Failed to load docx file:", error);
  }
};

onMounted(() => {
  initDoc();
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