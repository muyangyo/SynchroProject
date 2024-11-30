<template>
  <!-- 页面主体(不会隐藏的) -->
  <el-row :gutter="20" class="cloud-index-container">
    <el-col :span="1"/>
    <el-col :span="22">
      <!-- 页面主体 -->
      <div class="cloud-index-wrapper">

        <!-- 头部 -->
        <div class="cloud-index-header">
          <h2 class="cloud-index-title">我的云盘</h2>
          <div class="cloud-index-actions">
            <el-button type="primary" :icon="UploadFilled" v-if="route.fullPath !== config.userRouterBaseUrl">
              上传文件
            </el-button>
            <el-button type="primary" :icon="FolderAdd" v-if="route.fullPath !== config.userRouterBaseUrl">创建文件夹
            </el-button>
            <el-button type="primary" :icon="Share">我的分享</el-button>
          </div>
        </div>

        <!-- 面包屑 -->
        <div class="cloud-index-breadcrumb">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="toThePathBreadCrumb(-1)">根目录</el-breadcrumb-item>
            <el-breadcrumb-item
                v-for="(part, index) in pathPartsForBreadCrumb"
                :key="index"
                @click="toThePathBreadCrumb(index)">
              {{ decodeURIComponent(part) }}
            </el-breadcrumb-item>
          </el-breadcrumb>
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
                  <span class="file-name">{{ scope.row.fileName }}</span>
                </div>
              </template>
            </el-table-column>

            <el-table-column prop="fileSize" label="大小" sortable>
              <template #default="scope">
                {{ sizeTostr(scope.row.fileSize) }}
              </template>
            </el-table-column>
            <el-table-column prop="modifiedTime" label="修改时间" sortable/>
            <el-table-column prop="operation" label="操作">
              <template #default="scope">
                <el-tooltip
                    v-if="scope.row.fileType.category !== FILE_CATEGORY.FOLDER &&
                    scope.row.fileType.category !== FILE_CATEGORY.UNKNOWN &&
                    scope.row.fileType.category !== FILE_CATEGORY.COMPRESSED &&
                    scope.row.fileType.category !== FILE_CATEGORY.EXCEL &&
                    scope.row.fileType.category !== FILE_CATEGORY.PPT"
                    content="预览" placement="top" :show-arrow="false">
                  <el-button type="primary" size="small" :icon="DataAnalysis"
                             @click="handlePreview(scope.$index, scope.row)"/>
                </el-tooltip>

                <el-tooltip content="下载" placement="top" :show-arrow="false">
                  <el-button type="success" size="small" :icon="Download"
                             @click="handleDownload(scope.$index, scope.row)"/>
                </el-tooltip>

                <el-tooltip content="重命名" placement="top" :show-arrow="false" v-if=" route.fullPath !== config.userRouterBaseUrl">
                  <el-button type="warning" size="small" :icon="EditPen"
                             @click="handleRename(scope.$index, scope.row)"/>
                </el-tooltip>

                <el-tooltip content="删除" placement="top" :show-arrow="false" v-if=" route.fullPath !== config.userRouterBaseUrl">
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
    <el-col :span="1"/>
  </el-row>

  <!-- 弹窗 -->
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
        <VideoPreview v-if="file.fileType.category === FILE_CATEGORY.VIDEO " :sourceFilePath="file.filePath"
                      :key="file.filePath +Math.random"/>

        <TextPreview
            v-if="file.fileType.category === FILE_CATEGORY.TEXT || file.fileType.category=== FILE_CATEGORY.CODE"
            :sourceFilePath=" file.filePath "
            :key="file.filePath +Math.random"/>

        <DocxPreview v-if="file.fileType.category === FILE_CATEGORY.DOCX " :sourceFilePath=" file.filePath"
                     :key="file.filePath +Math.random"/>

        <PdfPreview v-if="file.fileType.category === FILE_CATEGORY.PDF " :sourceFilePath=" file.filePath"
                    :key="file.filePath +Math.random"/>
      </div>
    </div>


    <template #footer>
    </template>
  </el-dialog>

  <!-- 音乐悬浮窗  TODO: 测试是否能正常销毁和播放-->
  <AudioPreview
      v-if="musicPopover.visible && file.fileType.category === FILE_CATEGORY.AUDIO "
      :sourceFilePath="file.filePath"
      @close="musicPopover.visible = false"
  />

  <!-- 图片预览 -->
  <ImagePreview v-if="file.fileType.category === FILE_CATEGORY.IMAGE && imagePreview.visible"
                :sourceFilePath="file.filePath"
                @close="imagePreview.visible = false"
                :key="file.filePath +Math.random"/>

</template>
<script setup>
import {onMounted, reactive, ref, watch, computed} from 'vue';
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
import IconFromDIY from "@/components/common/iconFromDIY.vue";
import {easyRequest, optionalRequest, RequestMethods} from "@/utils/RequestTool.js";
import {useRoute} from "vue-router";
import {config} from "@/GlobalConfig.js";
import router from "@/router/RouterSetting.js";
import {sizeTostr} from "@/utils/FileSizeConverter.js";
import {ElMessage, ElMessageBox} from 'element-plus';
import getBlobData from "@/utils/getBlobData.js";

// 面包屑数据
const pathPartsForBreadCrumb = ref([]);

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


//文件类型实例
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

// 文件列表数据
const tableData = ref([]);

const route = useRoute();

// 初次加载页面时，获取根目录即可
onMounted(() => {
  if (location.pathname !== config.userRouterBaseUrl) {
    router.push(config.userRouterBaseUrl);
    return;
  }

  easyRequest(RequestMethods.POST, "/file/getFileList", {path: config.userRouterBaseUrl}, false, true).then(response => {
    response.data.forEach((item) => {
      if (item.fileType.category === FILE_CATEGORY.FOLDER) {
        item.fileSize = ''; // 目录不显示大小
      }
    }); // 先处理一下目录
    tableData.value = response.data;
  });
});


watch(() => route.fullPath, () => {
  //如果是根目录
  if (route.fullPath === config.userRouterBaseUrl) {
    // 非首次加载页面，更新数据
    easyRequest(RequestMethods.POST, "/file/getFileList", {path: route.fullPath}, false, true).then((response) => {
      handleResponseForWatch(response);
    });
  } else {
    // 非根目录，更新数据
    easyRequest(RequestMethods.POST, "/file/getFileList", {path: route.fullPath}, false, true).then((response) => {
      handleResponseForWatch(response);
    });
  }
  // 面包屑更新
  let temp = config.userRouterBaseUrl.substring(1, config.userRouterBaseUrl.length);
  pathPartsForBreadCrumb.value = (route.fullPath.split('/').filter((item) => item !== '' && item !== temp));
});

//watch方法抽离出来的请求处理方法
const handleResponseForWatch = (response) => {
  response.data.forEach((item) => {
    if (item.fileType.category === FILE_CATEGORY.FOLDER) {
      item.fileSize = ''; // 目录不显示大小
    }
  }); // 先处理一下目录
  tableData.value = response.data;
};

const toThePathBreadCrumb = (index) => {
  if (index === -1) {
    return config.userRouterBaseUrl;
  } else {
    let to = config.userRouterBaseUrl;
    for (let i = 0; i <= index; i++) {
      to = to + '/' + pathPartsForBreadCrumb.value[i];
    }
    router.push(to).then(() => {
      // URL 驱动面包屑(但这个还不够)
      // index+1 是相对根路径的距离
      while (pathPartsForBreadCrumb.value.length > index + 1) {
        pathPartsForBreadCrumb.value.pop();
      }
    });
  }
};

const handleFirstCellClick = (index, row) => {
  // URL 驱动 面包屑 后续因为 URL 变化驱动 watch 获取数据
  if (row.fileType.category === FILE_CATEGORY.FOLDER) {
    //先跳转
    router.push(`${route.fullPath}/${row.fileName}`).then(() => {
      //驱动面包屑
      let temp = config.userRouterBaseUrl.substring(1, config.userRouterBaseUrl.length);
      pathPartsForBreadCrumb.value = (route.fullPath.split('/').filter((item) => item !== '' && item !== temp));
    });
  }
};

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

const setFileProperties = (row) => {
  file.value.fileName = row.fileName;
  file.value.fileSize = row.fileSize;
  file.value.modifiedTime = row.modifiedTime;
  file.value.filePath = row.filePath;
  file.value.mountRootPath = row.mountRootPath;
  file.value.fileType = row.fileType;
};

const handlePreview = (index, row) => {
  setFileProperties(row);
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
  try {
    let needSleep = false;
    let messageBoxInstance = null;

    if (row.fileType.category === FILE_CATEGORY.FOLDER) {
      needSleep = true;
      messageBoxInstance = ElMessageBox.alert('后端正在生成压缩包，请耐心等待!', '提示', {
        confirmButtonText: '确定',
        type: 'warning',
        showClose: false,
        closeOnClickModal: false,
        closeOnPressEscape: false,
        showCancelButton: false,
        showConfirmButton: false,
      });
    } // 目录需要等待一会儿

    easyRequest(RequestMethods.POST, "/file/preparingDownloadFile", {path: row.filePath}, false, true, 60000) // 设置较长的超时时间
        .then(response => {
          if (messageBoxInstance) {
            ElMessageBox.close(); // 关闭提示框
          }

          if (response.statusCode === "SUCCESS" && response.data) {
            const downloadUrl = response.data.url;
            const fileName = response.data.fileName;

            // 延迟执行下载请求
            setTimeout(() => {
              optionalRequest({
                method: RequestMethods.GET,
                responseType: 'blob',
                url: downloadUrl,
                dataTypes: 'blob',
                timeout: 60000 // 设置较长的超时时间
              }).then(response => {
                const url = window.URL.createObjectURL(new Blob([response]));
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', fileName); // 设置下载文件的名称
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
              }).catch(error => {
                console.error('文件下载失败', error);
              });
            }, needSleep ? 8000 : 500);
          }
        })
        .catch(error => {
          if (messageBoxInstance) {
            ElMessageBox.close(); // 关闭提示框
          }
          console.error('文件下载失败', error);
        });
  } catch (error) {
    console.error('文件下载失败', error);
  }
};

const handleRename = (index, row) => {
  setFileProperties(row);

  ElMessageBox.prompt('请输入新的文件名', '重命名', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputValue: row.fileName, // 默认值为当前文件名
    inputPattern: /^[^\\/]+$/, // 只允许输入文件名，不允许输入路径
    inputErrorMessage: '文件名不能包含路径信息'
  }).then(({value}) => {
    if (value) {
      const renameFileDTO = {
        oldPath: row.filePath,
        newName: value
      };

      easyRequest(RequestMethods.POST, "/file/renameFile", renameFileDTO, false, true)
          .then(response => {
            if (response.statusCode === "SUCCESS") {
              // 重新获取文件列表
              easyRequest(RequestMethods.POST, "/file/getFileList", {path: route.fullPath}, false, true).then((response) => {
                handleResponseForWatch(response);
              });
              ElMessage.success("文件重命名成功!");
            } else {
              ElMessage.error(response.errMsg);
            }
          })
          .catch(error => {
            console.error('文件重命名失败', error);
            ElMessage.error('文件重命名失败');
          });
    }
  }).catch(() => {
    // 用户取消操作
    ElMessage.info('取消重命名');
  });
};

const handleShare = (index, row) => {
  dialogState.value.visible = true;
  dialogState.value.title = "分享";
  dialogState.value.width = "50%";
  dialogState.value.contentType = 'share';
};

const handleDelete = (index, row) => {
  setFileProperties(row);
  const filePathDTO = {
    path: row.filePath
  };

  easyRequest(RequestMethods.POST, "/file/deleteFile", filePathDTO, false)
      .then(response => {
        if (response.code === 200) {
          // 删除成功，更新表格数据
          tableData.value.splice(index, 1);
          ElMessage.success(response.message);
        } else {
          ElMessage.error(response.message);
        }
      })
      .catch(error => {
        console.error('文件删除失败', error);
        ElMessage.error('文件删除失败');
      });
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
  margin-bottom: 5px;
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

/* 面包屑样式 */
.cloud-index-breadcrumb {
  margin-bottom: 5px;
  padding: 10px;
  border-radius: 8px;
}

.cloud-index-breadcrumb .el-breadcrumb__inner {
  color: #ffffff;
  font-size: 16px;
  font-weight: bold;
}

.cloud-index-breadcrumb .el-breadcrumb__inner:hover {
  color: #409EFF;
  cursor: pointer; /* 确保悬停时显示箭头 */
}

.cloud-index-breadcrumb .el-breadcrumb__separator {
  color: #ffffff;
  font-weight: bold;
}

/* 确保所有面包屑项都显示箭头 */
.cloud-index-breadcrumb .el-breadcrumb__item {
  cursor: pointer;
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

.file-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 200px; /* 根据需要调整最大宽度 */
}
</style>