<template>
  <div>
    <h1>File Explorer</h1>
    <div v-if="files.length">
      <ul>
        <li v-for="file in files" :key="file.path">
          <span @click="navigate(file.path)" :class="{'directory': file.isDirectory}">
            {{ file.name }}
          </span>
        </li>
      </ul>
    </div>
    <div v-else>
      <p>No files found.</p>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue';
import {getFiles} from '@/utils/fileService';

const files = ref([]);
const currentPath = ref('C:\\Users\\DR\\Desktop\\tmp1');

const fetchFiles = async (path) => {
  try {
    const response = await getFiles(path);
    files.value = response.data;
  } catch (error) {
    console.error('Error fetching files:', error);
  }
};

const navigate = (path) => {
  currentPath.value = path;
  fetchFiles(path);
};

onMounted(() => {
  fetchFiles(currentPath.value);
});
</script>

<style scoped>
.directory {
  font-weight: bold;
  cursor: pointer;
}
</style>