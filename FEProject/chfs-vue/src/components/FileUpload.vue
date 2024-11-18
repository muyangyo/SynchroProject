<template>
  <div class="file-upload">
    <el-upload
        class="upload-demo"
        drag
        :action="uploadUrl"
        :before-upload="beforeUpload"
        :on-progress="handleProgress"
        :on-success="handleSuccess"
        :on-error="handleError"
        :on-remove="handleRemove"
        :file-list="fileList"
        :http-request="customUpload"
        :limit="1"
        :on-exceed="handleExceed"
    >
      <i class="el-icon-upload"></i>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <template #tip>
        <div class="el-upload__tip">只能上传一个文件，且不超过500MB</div>
      </template>
    </el-upload>
    <el-button type="primary" @click="uploadFile">上传文件</el-button>
    <el-button type="danger" @click="cancelUpload">取消上传</el-button>
    <el-button type="warning" @click="pauseUpload">暂停上传</el-button>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import axios from 'axios';
import SparkMD5 from 'spark-md5';

const uploadUrl = 'http://localhost:8080/upload/chunk'; // 后端上传接口
const file = ref(null);
const fileList = ref([]);
const CHUNK_SIZE = 1024 * 1024; // 每个分块的大小为1MB
const chunks = ref([]);
const currentChunk = ref(0);
const uploadRequest = ref(null);
const isUploading = ref(false);

const beforeUpload = (file) => {
  file.value = file;
  chunks.value = Math.ceil(file.size / CHUNK_SIZE);
  return true;
};

const handleProgress = (event, file, fileList) => {
  console.log('Upload progress:', event);
};

const handleSuccess = (response, file, fileList) => {
  console.log('Upload success:', response);
  isUploading.value = false;
};

const handleError = (err, file, fileList) => {
  console.error('Upload error:', err);
  isUploading.value = false;
};

const handleRemove = (file, fileList) => {
  console.log('File removed:', file);
};

const handleExceed = (files, fileList) => {
  console.warn('只能上传一个文件');
};

const customUpload = async (options) => {
  const file = options.file;
  const chunks = Math.ceil(file.size / CHUNK_SIZE);
  const spark = new SparkMD5.ArrayBuffer();
  const fileReader = new FileReader();

  fileReader.onload = async (e) => {
    spark.append(e.target.result);
    const md5 = spark.end();

    for (let i = 0; i < chunks; i++) {
      if (isUploading.value) {
        const start = i * CHUNK_SIZE;
        const end = Math.min(file.size, start + CHUNK_SIZE);
        const chunk = file.slice(start, end);

        const formData = new FormData();
        formData.append('file', chunk);
        formData.append('chunk', i);
        formData.append('totalChunks', chunks);
        formData.append('fileName', file.name);
        formData.append('md5', md5);

        try {
          await axios.post(uploadUrl, formData, {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          });
        } catch (error) {
          console.error('Upload chunk failed:', error);
        }
      }
    }
  };

  fileReader.readAsArrayBuffer(file);
};

const uploadFile = () => {
  if (file.value) {
    isUploading.value = true;
    customUpload({file: file.value});
  }
};

const cancelUpload = () => {
  isUploading.value = false;
  if (uploadRequest.value) {
    uploadRequest.value.abort();
    uploadRequest.value = null;
  }
};

const pauseUpload = () => {
  isUploading.value = false;
  if (uploadRequest.value) {
    uploadRequest.value.abort();
    uploadRequest.value = null;
  }
};
</script>

<style scoped>
.file-upload {
  margin: 20px;
}
</style>