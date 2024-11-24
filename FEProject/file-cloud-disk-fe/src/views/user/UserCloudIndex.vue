<template>
  <el-row :gutter="20" class="cloud-index-container">
    <el-col :span="3"/>
    <el-col :span="18">
      <!-- 页面主体 -->
      <div class="cloud-index-wrapper">
        <!-- 头部 -->
        <div class="cloud-index-header">
          <h2 class="cloud-index-title">我的云盘</h2>
          <div class="cloud-index-actions">
            <el-button type="primary" :icon="UploadFilled">
              上传文件
            </el-button>
            <el-button type="primary" :icon="FolderAdd">创建文件夹</el-button>
            <el-button type="primary" :icon="Share">我的分享</el-button>
          </div>
        </div>
        <!-- 内容 -->
        <div class="cloud-index-content">
          <el-table :data="tableData" :default-sort="{ prop: '文件名', order: 'descending' }"
                    class="full-width-table" height="868px">

            <el-table-column prop="fileName" label="文件名" sortable min-width="100px" :show-overflow-tooltip="true">
              <template #default="scope" style="color: white">
                {{ scope.row.fileName }}
              </template>
            </el-table-column>

            <el-table-column prop="fileSize" label="大小" sortable/>
            <el-table-column prop="modifiedTime" label="修改时间" sortable/>
            <el-table-column prop="operation" label="操作">
              <template #default="scope">
                <el-tooltip content="预览" placement="top" :show-arrow="false">
                  <el-button type="primary" size="small" :icon="DataAnalysis"
                             @click="handlePreview(scope.$index, scope.row)"/>
                </el-tooltip>

                <el-tooltip content="下载" placement="top" :show-arrow="false">
                  <el-button type="success" size="small" :icon="Download"
                             @click="handleDownload(scope.$index, scope.row)"/>
                </el-tooltip>

                <el-tooltip content="重命名" placement="top" :show-arrow="false">
                  <el-button type="warning" size="small" :icon="EditPen"
                             @click="handleRename(scope.$index, scope.row)"/>
                </el-tooltip>

                <el-tooltip content="删除" placement="top" :show-arrow="false">
                  <el-button type="danger" size="small" :icon="DeleteFilled"
                             @click="handleDelete(scope.$index, scope.row)"/>
                </el-tooltip>

                <el-tooltip content="分享" placement="top" :show-arrow="false">
                  <el-button type="info" size="small" :icon="Share" @click="handleShare(scope.$index, scope.row)"/>
                </el-tooltip>

              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-col>
    <el-col :span="3"/>
  </el-row>

  <el-dialog
      v-model="dialogState.visible" :title="dialogState.title" :show-close="false" :width="dialogState.width" draggable
      class="custom-dialog" :key="'userCloudIndexDialogCommon'+Math.random()">

    <template #header>
      <div class="dialog-header">
        <span>{{ dialogState.title }}</span>
        <el-button text @click="dialogState.visible = false" class="close-button">
          <el-icon>
            <Close/>
          </el-icon>
        </el-button>
      </div>
    </template>

    <div class="dialog-body">
      <div v-if="dialogState.contentType === 'preview' && dialogState.visible" :key="file.filePath">
        <video-preview v-if="file.fileType === 'video' " :sourceFileURL="file.filePath" :key="file.filePath"/>
      </div>
      <div v-else-if="dialogState.contentType === 'download'">
      </div>
    </div>


    <template #footer>
    </template>
  </el-dialog>

  <!-- 音乐悬浮窗  TODO: 测试是否能正常销毁和播放-->
  <MusicPreview
      v-if="musicPopover.visible && file.fileType ==='AUDIO' "
      :sourceFileURL="file.filePath"
      @close="musicPopover.visible = false"
  />

</template>

<script setup>
import {ref} from 'vue';
import {
  UploadFilled,
  FolderAdd,
  Share,
  Download,
  EditPen,
  DeleteFilled,
  DataAnalysis,
  Close
} from '@element-plus/icons-vue';
import VideoPreview from "@/components/user/videoPreview.vue";
import MusicPreview from "@/components/user/musicPreview.vue";

//music弹窗配置
const musicPopover = ref({
  visible: false,
  width: '50%',
});

//文件
const file = ref({
  fileName: '',
  fileSize: '',
  modifiedTime: '',
  filePath: '',
  fileType: 'AUDIO',
});

//dialog弹窗配置
const dialogState = ref({
  visible: false,
  title: '',
  width: '50%',
  contentType: '',
});

const handlePreview = (index, row) => {
  if (file.value.fileType === 'AUDIO') {
    musicPopover.value.visible = true;
  } else {
    dialogState.value.visible = true;
    dialogState.value.title = "预览";
    dialogState.value.width = "80%";
    dialogState.value.contentType = 'preview';
  }
};

const handleDownload = (index, row) => {
  dialogState.value.visible = true;
  dialogState.value.title = "下载";
  dialogState.value.width = "50%";
  dialogState.value.contentType = 'download';
};

const handleRename = (index, row) => {
  dialogState.value.visible = true;
  dialogState.value.title = "重命名";
  dialogState.value.width = "50%";
  dialogState.value.contentType = 'rename';
};

const handleShare = (index, row) => {
  dialogState.value.visible = true;
  dialogState.value.title = "分享";
  dialogState.value.width = "50%";
  dialogState.value.contentType = 'share';
};

const handleDelete = (index, row) => {
  dialogState.value.visible = true;
  dialogState.value.title = "删除";
  dialogState.value.width = "50%";
  dialogState.value.contentType = 'delete';
};
// 假数据
const tableData = ref([
  {fileName: '123.txt', fileSize: '10 KB', modifiedTime: '2023-10-01 12:00'},
  {fileName: '文件2.docx', fileSize: '200 KB', modifiedTime: '2023-10-02 14:30'},
  {fileName: '文件3.pdf', fileSize: '500 KB', modifiedTime: '2023-10-03 09:15'},
  {fileName: '文件4.jpg', fileSize: '1 MB', modifiedTime: '2023-10-04 16:45'},
  {fileName: '文件5.mp4', fileSize: '5 MB', modifiedTime: '2023-10-05 10:20'},
]);


</script>

<style scoped>
.custom-dialog {
  border-radius: 8px;
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #333;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
}

.dialog-header span {
  font-size: 25px;
  font-weight: bold;
}

.close-button {
  font-size: 20px;
  color: #999;
}

.close-button:hover {
  color: #7b7b7b;
}

/*.dialog-body {*/
/*!*  padding: 20px;*!  body和footer的分割线*/
/*  border-bottom: 1px solid #e0e0e0;*/
/*}*/

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
  margin-bottom: 20px;
}

.cloud-index-title {
  font-size: 24px;
  font-weight: bold;
}

.cloud-index-actions {
  display: flex;
  gap: 10px;
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


</style>
<style>
.full-width-table {
  width: 100%;
  border-radius: 8px;
  border-collapse: collapse;
}

.full-width-table th,
.full-width-table td {
  padding: 12px;
  background-color: #454444;
}

.full-width-table td {
  color: #fff;
}
</style>