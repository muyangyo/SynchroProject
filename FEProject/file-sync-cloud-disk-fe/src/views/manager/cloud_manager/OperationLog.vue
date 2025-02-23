<template>
  <div class="operation-log-manager">
    <!-- 操作日志表格 -->
    <el-table
        :data="operationLogData"
        stripe
        :default-sort="{prop: 'operationTime', order: 'descending'}"
        @sort-change="handleSortChange"
    >
      <el-table-column prop="operationTime" label="操作时间" sortable="custom"/>
      <el-table-column prop="operation" label="操作" :show-overflow-tooltip="true" min-width="150"/>
      <el-table-column prop="userName" label="用户名" sortable/>
      <el-table-column prop="userIp" label="用户IP" sortable/>
      <el-table-column prop="operationLevel" label="操作级别" sortable="custom">
        <template #default="scope">
          <el-tag :type="getTagType(scope.row.operationLevel)">
            {{ scope.row.operationLevel }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页和批量操作 -->
    <div class="pagination-container">
      <el-pagination
          background
          layout="total, prev, pager, next, sizes"
          :page-sizes="[8, 12, 15]"
          :page-size="pageSize"
          :total="total"
          :current-page="currentPage"
          @size-change="handlePageSizeChange"
          @current-change="handlePageChange"
      />

      <div class="button-group">
        <el-button
            type="danger"
            @click="handleDelete"
            :disabled="operationLogData.length === 0">
          删除全部
        </el-button>
      </div>
    </div>
  </div>
</template>
<script setup>
import {markRaw, onBeforeUnmount, onMounted, ref} from 'vue';
import {ElMessage, ElMessageBox} from 'element-plus';
import {Delete} from "@element-plus/icons-vue";
import {easyRequest, RequestMethods} from "@/utils/RequestTool.js";

// 自定义排序函数
const handleSortChange = ({prop, order}) => {
  if (prop === 'operationTime') {
    operationLogData.value.sort((a, b) => {
      const timeA = new Date(a.operationTime).getTime();
      const timeB = new Date(b.operationTime).getTime();
      return order === 'descending' ? timeB - timeA : timeA - timeB;
    });
  } else if (prop === 'operationLevel') {
    const levelOrder = {'IMPORTANT': 3, 'WARNING': 2, 'INFO': 1};
    operationLogData.value.sort((a, b) => {
      return order === 'ascending'
          ? levelOrder[a.operationLevel] - levelOrder[b.operationLevel]
          : levelOrder[b.operationLevel] - levelOrder[a.operationLevel];
    });
  }
};

// 操作日志数据
const operationLogData = ref([]); // 操作日志数据
const total = ref(0); // 总条数
const pageSize = ref(12); // 每页显示条数
const currentPage = ref(1); // 当前页数

const getOperationLogData = (pageIndex) => {
  easyRequest(RequestMethods.GET, `/cloudDiskOperationLog/getLogList?page=${pageIndex}&pageSize=${pageSize.value}`, "", false, false).then(
      (response) => {
        if (response.data && response.statusCode === "SUCCESS") {
          operationLogData.value = response.data.list;
          total.value = response.data.total;
        } else {
          ElMessage.error(response.errMsg ? response.errMsg : '获取操作日志失败!');
        }
      }
  );
};

onMounted(() => {
  currentPage.value = 1;
  getOperationLogData(currentPage.value);
});

onBeforeUnmount(() => {
  operationLogData.value = [];
});

// 处理每页显示条数变化
const handlePageSizeChange = (size) => {
  pageSize.value = size;
  currentPage.value = 1; // 重置当前页为第一页
  getOperationLogData(currentPage.value);
};

// 处理分页变化
const handlePageChange = (page) => {
  currentPage.value = page;
  getOperationLogData(page);
};

// 批量删除操作日志
const handleDelete = () => {
  ElMessageBox.confirm(
      '确定要删除所有操作日志吗？',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error',
        icon: markRaw(Delete),
      }
  ).then(() => {
    easyRequest(RequestMethods.DELETE, `/cloudDiskOperationLog/deleteLog`, "", false, false).then(
        (response) => {
          if (response.data === true && response.statusCode === "SUCCESS") {
            ElMessage.success('操作日志删除成功!');
            operationLogData.value = [];
            total.value = 0; // 更新总条数为0
          } else {
            ElMessage.error(response.errMsg ? response.errMsg : '删除操作日志失败!');
          }
        }
    );
  });
};

// 根据操作级别返回标签类型
const getTagType = (operationLevel) => {
  switch (operationLevel) {
    case 'INFO':
      return 'info';
    case 'WARNING':
      return 'warning';
    case 'IMPORTANT':
      return 'danger';
    default:
      return '';
  }
};
</script>
<style scoped>
.operation-log-manager {
  display: flex;
  flex-direction: column;
  height: calc(100% - 50px);
  padding: 20px;
}

.el-table {
  flex: 1;
  margin-bottom: 20px;
}

.pagination-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.button-group {
  display: flex;
  gap: 10px;
}
</style>