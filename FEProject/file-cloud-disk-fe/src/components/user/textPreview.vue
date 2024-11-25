<template>
  <div class="file-preview">
    <div class="top-op">
      <div class="encode-select">
        <el-select
            placeholder="选择编码"
            v-model="selectedEncode"
            @change="changeEncode"
            class="select-box">
          <el-option value="utf8" label="UTF-8 编码"></el-option>
          <el-option value="gbk" label="GBK 编码"></el-option>
        </el-select>
        <div class="tips">遇到乱码？尝试切换编码</div>
      </div>
      <div class="copy-btn">
        <el-button type="primary" @click="copy">复制内容</el-button>
      </div>
    </div>
    <div class="file-content">
      <pre>{{ fileContent }}</pre>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted, onUnmounted, onBeforeUnmount} from 'vue';
import {optionalRequest, RequestMethods} from '@/utils/RequestTool.js';
import useClipboard from 'vue-clipboard3';
import getBlobData from "@/utils/getBLOBData.js";

const {toClipboard} = useClipboard();

onMounted(() => {
  console.log('文本预览组件挂载');
  readFile();
});

onBeforeUnmount(() => {
  console.log('文本预览组件销毁');
  fileContent.value = '';
});

const selectedEncode = ref('utf8');
const fileContent = ref('');
const blobResult = ref(null);

const props = defineProps({
  sourceFileURL: { // 文件 URL
    type: String,
    required: true,
  },
});

const readFile = async () => {
  try {
    const result = await getBlobData(props.sourceFileURL);
    if (!result) throw new Error('Failed to fetch file content.');
    blobResult.value = result.data;
    showFileContent();
  } catch (error) {
    console.error('文件读取失败:', error);
  }
};

const changeEncode = (e) => {
  selectedEncode.value = e;
  showFileContent();
};

const showFileContent = () => {
  if (!(blobResult.value instanceof Blob)) {
    console.error('无效的 Blob 对象:', blobResult.value);
    return;
  }

  const reader = new FileReader();
  reader.onload = () => {
    fileContent.value = reader.result;
  };
  reader.readAsText(blobResult.value, selectedEncode.value);
};

// 复制文本内容
const copy = async () => {
  try {
    await toClipboard(fileContent.value);
    console.log('文本已成功复制到剪贴板');
  } catch (error) {
    console.error('复制失败:', error);
  }
};
</script>

<style lang="scss" scoped>
.file-preview {
  width: 100%;
  padding: 0 20px 20px;
  box-sizing: border-box;

  .top-op {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20px;

    .encode-select {
      display: flex;
      align-items: center;

      .select-box {
        margin-right: 10px;
      }

      .tips {
        color: #828282;
        font-size: 14px;
      }
    }

    .copy-btn {
      margin-left: 20px;
    }
  }

  .file-content {
    padding: 15px;
    border: 1px solid #ccc;
    border-radius: 5px;
    background-color: #f9f9f9;
    white-space: pre-wrap; /* 自动换行 */
    word-wrap: break-word; /* 长单词自动换行 */
    line-height: 1.5;
    overflow-x: auto; /* 添加水平滚动条 */
  }

  pre {
    margin: 0;
    white-space: pre-wrap; /* 自动换行 */
    word-wrap: break-word; /* 长单词自动换行 */
    color: black;
    font-family: 'microsoft yahei', system-ui;
    font-size: 14px;
    font-weight: bold;
  }
}
</style>