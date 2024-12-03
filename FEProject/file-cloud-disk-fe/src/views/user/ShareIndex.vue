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
          <el-table :data="tableData" :default-sort="{ prop: '文件名', order: 'descending' }"
                    class="full-width-table" height="600px">

            <el-table-column prop="fileName" label="文件名" sortable min-width="100px" :show-overflow-tooltip="true">
              <template #default="scope" style="color: white">
                <div class="cell-content" @click="handleFirstCellClick(scope.$index, scope.row)">
                  <IconFromDIY v-if="scope.row.fileType.category"
                               :name="getIconNameFromCategory(scope.row.fileType.category)"/>
                  <el-tooltip :content="scope.row.fileName" placement="top">
                    <span class="file-name">{{ scope.row.fileName }}</span>
                  </el-tooltip>
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
import {onMounted, ref} from 'vue';
import {Download} from '@element-plus/icons-vue';
import IconFromDIY from "@/components/common/iconFromDIY.vue";
import {easyRequest, RequestMethods} from "@/utils/RequestTool.js";
import {useRoute} from "vue-router";
import {sizeTostr} from "@/utils/FileSizeConverter.js";
import {ElMessage} from 'element-plus';

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

const route = useRoute();

// 初次加载页面时，获取分享文件
onMounted(() => {
  const shareCode = route.query.shareCode;
  if (!shareCode) {
    ElMessage.error('分享链接无效');
    return;
  }

  easyRequest(RequestMethods.GET, `/file/getShareFile?shareCode=${shareCode}`, "", false, true).then(response => {
    if (response.statusCode === "SUCCESS" && response.data) {

    } else {
      ElMessage.error(response.errMsg);
    }
  });
});

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
  // 处理文件点击事件
};

const handleDownload = (index, row) => {
  const downloadUrl = row.downloadUrl;
  if (downloadUrl) {
    window.location.href = downloadUrl;
  } else {
    ElMessage.error('下载链接无效');
  }
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
  /*padding: 20px;*/
  border-radius: 8px;
  min-height: 300px;
  font-size: 16px;
  text-align: center;
}

.cloud-index-wrapper {
  background-color: #333;
}

.cloud-index-title {
  color: #fff;
}

.cloud-index-content {
  background-color: #454444;
  color: #ccc;
}

.full-width-table {
  width: 100%;
  border-radius: 8px;
  border-collapse: collapse;
}

.full-width-table th,
.full-width-table td {
  padding: 12px;
  background-color: #333;
}

.full-width-table td {
  color: #fff;
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