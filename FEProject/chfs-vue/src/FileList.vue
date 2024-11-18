<template>
  <div>
    <el-table :data="fileList" style="width: 100%">
      <el-table-column label="Type" width="80">
        <template #default="scope">
          <el-icon v-if="scope.row.isDirectory">
            <Folder/>
          </el-icon>
          <el-icon v-else>
            <Document/>
          </el-icon>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="Name" width="300"/>
      <el-table-column prop="size" label="Size" width="150">
        <template #default="scope">
          {{ scope.row.isDirectory ? '-' : formatSize(scope.row.size) }}
        </template>
      </el-table-column>
      <el-table-column prop="path" label="Path"/>
    </el-table>
  </div>
</template>

<script>
import {ref, onMounted} from 'vue';
import {ElTable, ElTableColumn, ElIcon} from 'element-plus';
import {Folder, Document} from '@element-plus/icons-vue';
import axios from 'axios';

export default {
  components: {
    ElTable,
    ElTableColumn,
    ElIcon,
    Folder,
    Document,
  },
  setup() {
    const fileList = ref([]);

    const fetchFileList = async () => {
      try {
        const response = await axios.get('http://localhost:8080/files/list');
        fileList.value = response.data;
      } catch (error) {
        console.error('Failed to fetch file list:', error);
      }
    };

    const formatSize = (size) => {
      if (size < 1024) {
        return `${size} B`;
      } else if (size < 1024 * 1024) {
        return `${(size / 1024).toFixed(2)} KB`;
      } else if (size < 1024 * 1024 * 1024) {
        return `${(size / (1024 * 1024)).toFixed(2)} MB`;
      } else {
        return `${(size / (1024 * 1024 * 1024)).toFixed(2)} GB`;
      }
    };

    onMounted(() => {
      fetchFileList();
    });

    return {
      fileList,
      formatSize,
    };
  },
};
</script>

<style scoped>
</style>