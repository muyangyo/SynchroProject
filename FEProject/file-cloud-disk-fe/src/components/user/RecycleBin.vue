<template>
  <div class="recycleBin-manager">
    <el-table :data="recycleData.list">
      <el-table-column prop="fileName" label="文件名" :show-overflow-tooltip="true" width="230"/>
      <el-table-column prop="fileOriginalPath" label="原始位置" :show-overflow-tooltip="true" width="280"/>
      <el-table-column prop="deleteTime" label="删除时间" sortable/>
      <el-table-column prop="remainingTime" label="保留时间(默认7天)">
        <template #default="scope">
          <span :style="scope.row.remainingTime.includes('小时') ? 'color: red;' : ''">
            {{ scope.row.remainingTime }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button
              type="danger"
              size="small"
              @click="handleDelete(scope.row)"
              :disabled="recycleData.list.length === 0 || !haveDeletePermission">删除
          </el-button>
          <el-button
              type="success"
              size="small"
              @click="handleRecycleRestore(scope.row)"
              :disabled="recycleData.list.length === 0 || !haveWritePermission">恢复
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="recycleBin-tail-container">
      <div class="button-group">
        <el-button
            type="danger"
            @click="handleDelete(null,true)"
            :disabled="recycleData.list.length === 0 || !haveDeletePermission"
        >全部删除
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {markRaw, onBeforeUnmount, onMounted, ref} from 'vue';
import {ElMessage, ElMessageBox} from 'element-plus';
import {easyRequest, RequestMethods} from "@/utils/RequestTool.js";
import {UserSession} from "@/utils/UserLocalStoreUtils.js";
import {Delete} from "@element-plus/icons-vue";

// 权限相关变量
const haveDeletePermission = ref(UserSession.getPermissions().includes("d"));
const haveWritePermission = ref(UserSession.getPermissions().includes("w"));
const haveReadPermission = ref(UserSession.getPermissions().includes("r"));

const recycleData = ref({
  list: [], /* 列表 type: array */
});

const getDate = () => {
  easyRequest(RequestMethods.GET, `/file/getRecycleBinList`, "", false).then(response => {
    if (response.statusCode === "SUCCESS") {
      recycleData.value.list = []; // 清空原有数据
      recycleData.value.list = response.data; // 赋值新数据
    } else {
      ElMessage.error(response.errMsg ? response.errMsg : '获取回收站列表失败');
    }
  });
}

onMounted(() => {
  if (haveReadPermission.value) {
    getDate();
  } else {
    ElMessage.error("无权限访问回收站列表");
  }
});

const emit = defineEmits();
const refresh = () => {
  emit('refreshFileList');
};

onBeforeUnmount(() => {
  recycleData.value = null;
});

const handleDelete = (row, isDeleteAll = false) => {
  if (isDeleteAll) {
    ElMessageBox.confirm(
        `确定要清空回收站吗？`,
        '删除确认',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'error',
          icon: markRaw(Delete),
        }
    ).then(() => {

      easyRequest(RequestMethods.DELETE, `/file/deleteRecycleBinFile?isDeleteAll=${isDeleteAll}`, "", false).then((response) => {
        if (response.statusCode === "SUCCESS") {
          ElMessage({
            type: 'success',
            message: '删除成功',
          });
          getDate();
        } else {
          ElMessage.error(response.errMsg ? response.errMsg : '删除失败');
        }
      });

    });

  } else {

    easyRequest(RequestMethods.DELETE, `/file/deleteRecycleBinFile?isDeleteAll=${isDeleteAll}&id=${row.id}`, "", false).then((response) => {
      if (response.statusCode === "SUCCESS") {
        ElMessage({
          type: 'success',
          message: '删除成功',
        });
        getDate();
      } else {
        ElMessage.error(response.errMsg ? response.errMsg : '删除失败');
      }
    });

  }

};

const handleRecycleRestore = (row) => {
  easyRequest(RequestMethods.PUT, `/file/restoreRecycleBinFile?id=${row.id}`, "", false).then((response) => {
    if (response.statusCode === "SUCCESS") {
      ElMessage({
        type: 'success',
        message: '恢复成功',
      });
      getDate();
      refresh(); // 刷新父组件的文件列表
    } else {
      ElMessage.error(response.errMsg ? response.errMsg : '恢复失败');
    }
  });
};
</script>

<style scoped>
.recycleBin-manager {
}

.recycleBin-tail-container {
  display: flex;
  justify-content: right;
  align-items: center;
  margin-top: 20px;
}

.el-pagination {
  margin-top: 0; /* 移除默认的 margin-top */
}

.button-group {
  display: flex;
}
</style>