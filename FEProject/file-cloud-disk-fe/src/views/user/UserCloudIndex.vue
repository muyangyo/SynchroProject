<template>
  <!-- 页面主体(不会隐藏的) -->
  <el-row :gutter="20" class="cloud-index-container">
    <el-col :span="3"/>
    <el-col :span="18">
      <!-- 页面主体 -->
      <div class="cloud-index-wrapper">

        <!-- 头部 -->
        <div class="cloud-index-header">
          <h2 class="cloud-index-title">我的云盘</h2>
          <div class="cloud-index-actions">
            <el-button type="primary" :icon="UploadFilled" v-if="route.fullPath!==config.userBaseUrl">
              上传文件
            </el-button>
            <el-button type="primary" :icon="FolderAdd" v-if="route.fullPath!==config.userBaseUrl">创建文件夹
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
              {{ part }}
            </el-breadcrumb-item>
          </el-breadcrumb>
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
                    scope.row.fileType.category !== FILE_CATEGORY.UNKNOWN&&
scope.row.fileType.category !== FILE_CATEGORY.COMPRESSED && scope.row.fileType.category !== FILE_CATEGORY.EXCEL
&& scope.row.fileType.category !== FILE_CATEGORY.PPT"
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

        <TextPreview v-if="file.fileType.category === FILE_CATEGORY.TEXT " :sourceFilePath=" file.filePath"
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
import {onMounted, reactive, ref, watch} from 'vue';
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
import {easyRequest, RequestMethods} from "@/utils/RequestTool.js";
import {useRoute} from "vue-router";
import {config} from "@/GlobalConfig.js";
import router from "@/router/RouterSetting.js";
import {sizeTostr} from "@/utils/FileSizeConverter.js";

// 面包屑数据
const pathPartsForBreadCrumb = ref([]);

// 文件类型常量
const FILE_CATEGORY = {
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
};

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
      console.warn("用户首页挂载成功"); //TODO: 调试用
    if (location.pathname != config.userBaseUrl) {
      return router.push(config.userBaseUrl)
    }

      easyRequest(RequestMethods.POST, "/file/getFileList", config.userBaseUrl, false).then(response => {
        response.data.forEach((item) => {
          if (item.fileType.category === FILE_CATEGORY.FOLDER) {
            item.fileSize = ''; // 目录不显示大小
          }
        }); // 先处理一下目录
        tableData.value = response.data;


        // 页面刷新直接强制跳回根目录
        router.push(config.userBaseUrl)

      })
    }
)

watch(() => route.fullPath, () => {
  //如果是根目录
  if (route.fullPath === config.userBaseUrl) {
    console.error("由于路由变化，根目录数据更新(bug)");//TODO: 调试用
    // 非首次加载页面，更新数据
    easyRequest(RequestMethods.POST, "/file/getFileList", route.fullPath, false).then((response) => {
          handleResponseForWatch(response)
        }
    )
  } else {
    // 非根目录，更新数据
    easyRequest(RequestMethods.POST, "/file/getFileList", route.fullPath, false).then((response) => {
          handleResponseForWatch(response)
        }
    )
  }
  // 面包屑更新
  let temp = config.userBaseUrl.substring(1, config.userBaseUrl.length);
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
    return config.userBaseUrl;
  } else {
    let to = config.userBaseUrl
    for (let i = 0; i <= index; i++) {
      to = to + '/' + pathPartsForBreadCrumb.value[i];
    }
    router.push(to).then(() => {
          // URL 驱动面包屑(但这个还不够)
          // index+1 是相对根路径的距离
          while (pathPartsForBreadCrumb.value.length > index + 1) {
            pathPartsForBreadCrumb.value.pop();
          }
        }
    );
  }
};

const handleFirstCellClick = (index, row) => {
  // URL 驱动 面包屑 后续因为 URL 变化驱动 watch 获取数据
  if (row.fileType.category === FILE_CATEGORY.FOLDER) {
    //先跳转
    router.push(route.fullPath + '/' + row.fileName).then(() => {
      //驱动面包屑
      let temp = config.userBaseUrl.substring(1, config.userBaseUrl.length);
      pathPartsForBreadCrumb.value = (route.fullPath.split('/').filter((item) => item !== '' && item !== temp));
    })
  }
};

// 根据文件类型获取icon名称
const getIconNameFromCategory = (category) => {
  if (category === FILE_CATEGORY.VIDEO) {
    return '#icon-shipin';
  } else if (category === FILE_CATEGORY.IMAGE) {
    return '#icon-tupian';
  } else if (category === FILE_CATEGORY.AUDIO) {
    return '#icon-yinpin';
  } else if (category === FILE_CATEGORY.TEXT) {
    return '#icon-TXT';
  } else if (category === FILE_CATEGORY.PDF) {
    return '#icon-PDF';
  } else if (category === FILE_CATEGORY.DOCX) {
    return '#icon-word';
  } else if (category === FILE_CATEGORY.FOLDER) {
    return '#icon-wenjianjia';
  } else if (category === FILE_CATEGORY.COMPRESSED) {
    return '#icon-yasuobao';
  } else if (category === FILE_CATEGORY.EXCEL) {
    return '#icon-excel';
  } else if (category === FILE_CATEGORY.PPT) {
    return '#icon-ppt';
  } else if (category === FILE_CATEGORY.CODE) {
    return '#icon-jiaoben';
  } else {
    return '#icon-weizhi';
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
    file.value = row;
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
</style>