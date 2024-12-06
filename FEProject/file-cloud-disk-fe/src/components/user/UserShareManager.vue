<template>
  <div class="share-manager">
    <el-table :data="shareData.list">
      <el-table-column prop="url" label="分享链接"/>
      <el-table-column prop="filePath" label="文件夹位置"/>
      <el-table-column prop="createTime" label="创建时间" sortable/>
      <el-table-column prop="status" label="状态" sortable>
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'warning'">
            {{ scope.row.status === 1 ? '正常' : '过期' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="scope">
          <el-button
              type="danger"
              size="small"
              @click="handleDelete(scope.row)"
              :disabled="shareData.list.length === 0"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-container">
      <el-pagination
          background
          layout="prev, pager, next"
          :total="shareData.total"
          :page-size="shareData.pageSize"
          :current-page="shareData.currentPage"
          @current-change="handlePageChange"
      />
      <div class="button-group">
        <el-button
            type="danger"
            @click="handleBatchDeleteShareFile(true)"
            :disabled="shareData.list.length === 0"
        >全部删除
        </el-button>
        <el-button
            type="warning"
            @click="handleBatchDeleteShareFile(false)"
            :disabled="shareData.list.length === 0"
        >删除过期
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {markRaw, onBeforeUnmount, onMounted, ref} from 'vue';
import {ElMessage, ElMessageBox} from 'element-plus';
import {easyRequest, RequestMethods} from "@/utils/RequestTool.js";
import {Delete} from "@element-plus/icons-vue";

const shareData = ref({
  list: [], /* 分享列表 type: array */
  total: 0,/* 总数据条数 type: number */
  pageSize: 0, /* 每页显示条数 type: number */
  currentPage: 0, /* 当前页码 type: number */
});

const getDate = (pageIndex) => {
  easyRequest(RequestMethods.GET, `/file/getShareFileList?page=${pageIndex}`, "", false).then(response => {
    response.data.list.forEach(item => {
      item.url = `${location.origin}/share?shareCode=${item.code}`;
    });

    shareData.value.list = [];
    shareData.value.list = response.data.list;
    shareData.value.total = response.data.total;
    shareData.value.pageSize = response.data.pageSize;
    shareData.value.currentPage = pageIndex;
  });
}

onMounted(() => {
  getDate(1);
});

onBeforeUnmount(() => {
  shareData.value = null;
});

const handlePageChange = (page) => {
  getDate(page);
};

const handleDelete = (row) => {
  ElMessageBox.confirm(
      `确定要删除分享 ${row.url} 吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error',
        icon: markRaw(Delete),
      }
  ).then(() => {
    easyRequest(RequestMethods.DELETE, `/file/deleteShareFile?shareCode=${row.code}`, "", false).then((response) => {
      if (response.statusCode === "SUCCESS") {
        ElMessage({
          type: 'success',
          message: '删除成功',
        });
        getDate(shareData.value.currentPage);
      } else {
        ElMessage({
          type: 'error',
          message: '删除失败',
        });
      }
    });

  });
};

const handleBatchDeleteShareFile = (isDeleteAll) => {
  ElMessageBox.confirm(
      isDeleteAll ? '确定要删除所有分享吗？' : '确定要删除过期分享吗？',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error',
        icon: markRaw(Delete),
      }
  ).then(() => {
    easyRequest(RequestMethods.DELETE, `/file/batchDeleteShareFile?isDeleteAll=${isDeleteAll}`, "", false).then((response) => {
      if (response.statusCode === "SUCCESS") {
        ElMessage({
          type: 'success',
          message: '全部删除成功',
        });
        getDate(shareData.value.currentPage);
      } else {
        ElMessage({
          type: 'error',
          message: '删除失败',
        });
      }
    })
  })
};
</script>

<style scoped>
.share-manager {
}

.pagination-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
}

.el-pagination {
  margin-top: 0; /* 移除默认的 margin-top */
}

.button-group {
  display: flex;
  gap: 10px; /* 按钮之间的间距 */
}
</style>