<template>
  <!-- 页面主体(不会隐藏的) -->
  <el-row :gutter="20" class="cloud-index-container">
    <el-col :span="3"/>
    <el-col :span="18">
      <!-- 页面主体 -->
      <div class="cloud-index-wrapper">

        <!-- 头部 -->
        <div class="cloud-index-header">
          <h2 class="cloud-index-title">分享文件</h2>
        </div>

        <!-- 内容 -->
        <div class="cloud-index-content">
          <el-table :data="tableData" :default-sort="{ prop: 'fileName', order: 'descending' }"
                    class="full-width-table custom-table" height="600px">

            <el-table-column prop="fileName" label="文件名" sortable min-width="100px" :show-overflow-tooltip="true">
              <template #default="scope" style="color: white">
                <div class="cell-content" @click="handleFirstCellClick(scope.$index, scope.row)">
                  <IconFromDIY v-if="scope.row.fileType.category"
                               :name="getIconNameFromCategory(scope.row.fileType.category)"/>
                  <span class="file-name">{{ scope.row.fileName }}</span>
                </div>
              </template>
            </el-table-column>

            <el-table-column prop="fileSize" label="大小" sortable>
              <template #default="scope">
                {{ sizeTostr(scope.row.fileSize) }}
              </template>
            </el-table-column>

            <el-table-column prop="operation" label="操作" min-width="100px">
              <template #default="scope">
                <el-tooltip content="下载" placement="top" :show-arrow="false">
                  <el-button type="success" size="small" :icon="Download"
                             @click="handleDownload(scope.$index, scope.row)"/>
                </el-tooltip>
              </template>
            </el-table-column>

          </el-table>
        </div>
      </div>
    </el-col>
    <el-col :span="3"/>
  </el-row>
</template>

<script setup>
import {onMounted, ref, watch} from 'vue';
import {Download} from '@element-plus/icons-vue';
import IconFromDIY from "@/components/common/iconFromDIY.vue";
import {easyRequest, optionalRequest, RequestMethods} from "@/utils/RequestTool.js";
import {useRoute, useRouter} from "vue-router";
import {sizeTostr} from "@/utils/FileSizeConverter.js";
import {ElLoading, ElMessage, ElMessageBox} from 'element-plus';

const route = useRoute();
const router = useRouter();
const shareCode = route.query.shareCode;

// 文件类型常量
const FILE_CATEGORY = Object.freeze({
  IMAGE: 'IMAGE',
  VIDEO: 'VIDEO',
  AUDIO: 'AUDIO',
  TEXT: 'TEXT',
  PDF: 'PDF',
  DOCX: 'DOCX',
  FOLDER: 'FOLDER',
  UNKNOWN: 'UNKNOWN',
  COMPRESSED: 'COMPRESSED', // 压缩文件
  EXCEL: 'EXCEL',
  PPT: 'PPT',
  CODE: 'CODE', // 代码文件(从TEXT中分离出来)
});

// 文件列表数据
const tableData = ref([]);
// 文件对象
const file = ref({
  fileName: '',
  fileSize: '',
  modifiedTime: '',
  filePath: '',
  mountRootPath: '',
  fileType: {
    category: FILE_CATEGORY.UNKNOWN,
    typeName: '',
    mimeType: ''
  },
});
const handleResponse = (response) => {
  response.data.forEach((item) => {
    if (item.fileType.category === FILE_CATEGORY.FOLDER) {
      item.fileSize = ''; // 目录不显示大小
    }
  });
  tableData.value = response.data;
};

// 初次加载页面时，获取分享文件
onMounted(() => {
  if (!shareCode) {
    ElMessage.error('分享链接无效');
    return;
  }

  getShareFileList(route.query.parentPath);
});


const getShareFileList = (parentPath) => {
  easyRequest(RequestMethods.GET, `/file/getShareFile?shareCode=${shareCode}&parentPath=${parentPath || ''}`, "", false, true).then(response => {
    if (response.statusCode === "SUCCESS" && response.data) {
      handleResponse(response);
      tableData.value = response.data;
    } else {
      ElMessage.error(response.errMsg);
    }
  });
}

// 根据文件类型获取icon名称
const getIconNameFromCategory = (category) => {
  const icons = {
    [FILE_CATEGORY.VIDEO]: '#icon-shipin',
    [FILE_CATEGORY.IMAGE]: '#icon-tupian',
    [FILE_CATEGORY.AUDIO]: '#icon-yinpin',
    [FILE_CATEGORY.TEXT]: '#icon-TXT',
    [FILE_CATEGORY.PDF]: '#icon-PDF',
    [FILE_CATEGORY.DOCX]: '#icon-word',
    [FILE_CATEGORY.FOLDER]: '#icon-wenjianjia',
    [FILE_CATEGORY.COMPRESSED]: '#icon-yasuobao',
    [FILE_CATEGORY.EXCEL]: '#icon-excel',
    [FILE_CATEGORY.PPT]: '#icon-PPT',
    [FILE_CATEGORY.CODE]: '#icon-jiaoben',
    [FILE_CATEGORY.UNKNOWN]: '#icon-weizhi',
  };
  return icons[category] || '#icon-weizhi';
};

const handleFirstCellClick = (index, row) => {
  if (row.fileType.category === FILE_CATEGORY.FOLDER) {
    // 处理文件夹点击事件
    const currentParentPath = route.query.parentPath || '';
    let newPath = currentParentPath ? `${currentParentPath}/${row.fileName}` : row.fileName;
    // 更新路由
    router.push({
      query: {
        shareCode: shareCode,
        parentPath: newPath
      }
    });
  }
};


watch(() => route.query.parentPath, () => {
  const currentParentPath = route.query.parentPath || '';
  getShareFileList(currentParentPath);
})
const handleDownload = (index, row) => {
  let messageBoxInstance = null;
  let loading = null;

  if (row.fileType.category === FILE_CATEGORY.FOLDER) {
    messageBoxInstance = ElMessageBox.alert('正在生成压缩包，请耐心等待!', '提示', {
      confirmButtonText: '确定',
      type: 'warning',
      showClose: false,
      closeOnClickModal: false,
      closeOnPressEscape: false,
      showCancelButton: false,
      showConfirmButton: false,
    });
  }

  const parentPath = route.query.parentPath || '';
  optionalRequest({
    method: RequestMethods.GET,
    url: `/file/OutsideFileDownload?shareCode=${shareCode}&fileName=${row.fileName}&parentPath=${parentPath}`,
    responseType: 'blob',
    dataTypes: 'blob',
    timeout: 60000 // 超时时间 60s
  })
      .then(response => {
        if (messageBoxInstance) {
          ElMessageBox.close(); // 关闭提示框
        }
        loading = ElLoading.service({
          lock: true,
          text: '文件下载中，请稍候...',
          background: 'rgba(32,31,31,0.7)',
        });

        const url = window.URL.createObjectURL(response);
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', row.fileName); // 设置下载文件的名称
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url); // 释放 URL
        loading.close();
      })
      .catch(error => {
        if (messageBoxInstance) {
          ElMessageBox.close(); // 关闭提示框
        }
        if (loading) {
          loading.close();
        }
        console.error('文件下载失败', error);
      });
};
</script>

<style scoped>
.cloud-index-container {
  margin-top: 20px;
}

.cloud-index-wrapper {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  background-color: #333;
}

.cloud-index-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}

.cloud-index-title {
  font-size: 24px;
  font-weight: bold;
}

.cloud-index-content {
  background-color: #454444;
  color: #ccc;
}

.custom-table :deep(.el-table__row td) {
  background-color: #555555 !important;
  color: #fff !important;
}

.custom-table :deep(.el-table__row:hover td) {
  background-color: #444 !important;
}

.custom-table :deep(.el-table th) {
  background-color: #555555 !important;
  color: #fff !important;
}

.custom-table ::v-deep(.el-table__header-wrapper th) {
  background: #212121 !important;
  color: white !important;
}

.cell-content {
  display: flex;
  align-items: center;
}

.cell-content .svg-icon {
  margin-right: 8px;
}

.file-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 200px; /* 根据需要调整最大宽度 */
}
</style>