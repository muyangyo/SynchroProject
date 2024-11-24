<template>
  <div>
    <h1>文件映射</h1>
    <input v-model="customPath" placeholder="输入路径"/>
    <button @click="fetchFiles">获取文件</button>
    <button @click="createDirectory">创建文件夹</button>
    <button @click="deleteFile">删除文件</button>
    <ul>
      <li v-for="file in files" :key="file.name">
        {{ file.name }}
      </li>
    </ul>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import {easyRequest, RequestMethods} from '@/utils/RequestTool.js';

const customPath = ref('');
const files = ref([]);

const fetchFiles = async () => {
  try {
    const response = await easyRequest(RequestMethods.GET, `/files/${customPath.value}`,"");
    files.value = response.data;
  } catch (error) {
    console.error('获取文件失败', error);
  }
};

const createDirectory = async () => {
  try {
    await easyRequest(RequestMethods.POST, `/files/create/${customPath.value}`,"");
    alert('文件夹创建成功');
  } catch (error) {
    console.error('创建文件夹失败', error);
  }
};

const deleteFile = async () => {
  try {
    await easyRequest(RequestMethods.DELETE, `/files/delete/${customPath.value}`,"");
    alert('文件删除成功');
  } catch (error) {
    console.error('删除文件失败', error);
  }
};
</script>

<style scoped>
/* 样式可以根据需要进行调整 */
</style>