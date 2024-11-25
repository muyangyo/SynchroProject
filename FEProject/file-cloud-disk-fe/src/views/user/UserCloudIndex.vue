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
                <div class="cell-content" @click="handleFirstCellClick(scope.$index,scope.row)">
                  <IconFromDIY v-if="scope.row.fileType.category"
                               :name="getIconNameFromCategory(scope.row.fileType.category)"/>
                  {{ scope.row.fileName }}
                </div>
              </template>
            </el-table-column>

            <el-table-column prop="fileSize" label="大小" sortable/>
            <el-table-column prop="modifiedTime" label="修改时间" sortable/>
            <el-table-column prop="operation" label="操作">
              <template #default="scope">
                <el-tooltip
                    v-if="scope.row.fileType.category !== FILE_CATEGORY.FOLDER && scope.row.fileType.category !== FILE_CATEGORY.UNKNOWN"
                    content="预览" placement="top" :show-arrow="false">
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
      <!-- 预览 -->
      <div v-if="dialogState.contentType === 'preview' && dialogState.visible" :key="file.filePath +Math.random">
        <VideoPreview v-if="file.fileType.category === FILE_CATEGORY.VIDEO " :sourceFileURL="file.filePath"
                      :key="file.filePath +Math.random"/>

        <TextPreview v-if="file.fileType.category === FILE_CATEGORY.TEXT " :sourceFileURL=" file.filePath"
                     :key="file.filePath +Math.random"/>

        <DocxPreview v-if="file.fileType.category === FILE_CATEGORY.DOCX " :sourceFileURL=" file.filePath"
                     :key="file.filePath +Math.random"/>

        <PdfPreview v-if="file.fileType.category === FILE_CATEGORY.PDF " :sourceFileURL=" file.filePath"
                    :key="file.filePath +Math.random"/>
      </div>
    </div>


    <template #footer>
    </template>
  </el-dialog>

  <!-- 音乐悬浮窗  TODO: 测试是否能正常销毁和播放-->
  <AudioPreview
      v-if="musicPopover.visible && file.fileType.category === FILE_CATEGORY.AUDIO "
      :sourceFileURL="file.filePath"
      @close="musicPopover.visible = false"
  />

  <!-- 图片预览 -->
  <ImagePreview v-if="file.fileType.category === FILE_CATEGORY.IMAGE && imagePreview.visible"
                :sourceFileURL="file.filePath"
                @close="imagePreview.visible = false"
                :key="file.filePath +Math.random"/>

</template>

<script setup>
import {onMounted, ref} from 'vue';
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
import AudioPreview from "@/components/user/audioPreview.vue";
import TextPreview from "@/components/user/textPreview.vue";
import ImagePreview from "@/components/user/imagePreview.vue";
import DocxPreview from "@/components/user/docxPreview.vue";
import PdfPreview from "@/components/user/pdfPreview.vue";
import {easyRequest, RequestMethods} from "@/utils/RequestTool.js";
import IconFromDIY from "@/components/common/iconFromDIY.vue";


const FILE_CATEGORY = {
  IMAGE: 'IMAGE',
  VIDEO: 'VIDEO',
  AUDIO: 'AUDIO',
  TEXT: 'TEXT',
  PDF: 'PDF',
  DOCX: 'DOCX',
  FOLDER: 'FOLDER',
  UNKNOWN: 'UNKNOWN',
};

//文件类型实例
const file = ref({
  fileName: '',
  fileSize: '',
  modifiedTime: '',
  filePath: '',
  fileType: {
    category: FILE_CATEGORY.UNKNOWN,
    typeName: '',
    mimeType: ''
  },
});

const tableData = ref([]);

onMounted(() => {
  easyRequest(RequestMethods.POST, "/file/getFileList", 'root', false).then(
      (response) => {
        response.data.forEach((item) => {
          if (item.fileType.category === FILE_CATEGORY.FOLDER) {
            item.fileSize = ''; // 目录不显示大小
          }
        });
        tableData.value = response.data;
      }
  );
})

// 根据文件类型获取icon名称
const getIconNameFromCategory = (category) => {
  if (category === FILE_CATEGORY.VIDEO) {
    return '#icon-shipin';
  } else if (category === FILE_CATEGORY.IMAGE) {
    return '#icon-tupian';
  } else if (category === FILE_CATEGORY.AUDIO) {
    return '#icon-yinle';
  } else if (category === FILE_CATEGORY.TEXT) {
    return '#icon-a-035_wenben';
  } else if (category === FILE_CATEGORY.PDF) {
    return '#icon-PDF';
  } else if (category === FILE_CATEGORY.DOCX) {
    return '#icon-word';
  } else if (category === FILE_CATEGORY.FOLDER) {
    return '#icon-wenjianjia1';
  } else {
    return '#icon-wenjian';
  }
};

//music弹窗配置
const musicPopover = ref({
  visible: false,
  width: '50%',
});

//图片预览配置
const imagePreview = ref({
  visible: false,
});

//dialog弹窗配置
const dialogState = ref({
  visible: false,
  title: '',
  width: '50%',
  contentType: '',
});

const handlePreview = (index, row) => {
  if (row.fileType.category === FILE_CATEGORY.AUDIO) {
    musicPopover.value.visible = true;
  } else if (row.fileType.category === FILE_CATEGORY.IMAGE) {
    imagePreview.value.visible = true;
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
// 点击第一列触发的事件
const handleFirstCellClick = (index, row) => {
  if (row.fileType.category === FILE_CATEGORY.FOLDER) {
    const path = row.filePath.replace(/\\/g, '/');
    console.log(path);
    // 进入文件夹
    easyRequest(RequestMethods.POST, "/file/getFileList", path, false).then(
        (response) => {
          response.data.forEach((item) => {
            if (item.fileType.category === FILE_CATEGORY.FOLDER) {
              item.fileSize = ''; // 目录不显示大小
            }
          });
          tableData.value = response.data;
        }
    );
  }
};
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


.cell-content {
  display: flex;
  align-items: center;
}

.cell-content .svg-icon {
  margin-right: 8px;
}
</style>