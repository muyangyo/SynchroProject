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
            <el-button type="primary" :icon="UploadFilled"
                       v-if="route.fullPath !== config.userRouterBaseUrl && haveWritePermission"
                       @click="handleUploadFile()">
              上传文件
            </el-button>
            <el-button type="primary" :icon="FolderAdd"
                       v-if="route.fullPath !== config.userRouterBaseUrl && haveWritePermission"
                       @click="handleCreateFolder()">创建文件夹
            </el-button>
            <el-button type="primary" :icon="Share" @click="showShareLinkList()" v-if="haveReadPermission">我的分享
            </el-button>
            <el-button type="danger" :icon="Delete" @click="showRecycleBin()" v-if="haveReadPermission">回收站
            </el-button>
            <el-button type="info" @click="logout()" @mouseenter="isHover = true" @mouseleave="isHover = false">
              {{ isHover ? `退出` : UserSession.getUserName() }}
            </el-button>
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
                    class="full-width-table custom-table" height="600px">

            <el-table-column prop="fileName" label="文件名" sortable min-width="100px" :show-overflow-tooltip="true">
              <template #default="scope">
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
            <el-table-column prop="operation" label="操作" min-width="100px">
              <template #default="scope">
                <el-tooltip
                    v-if="scope.row.fileType.category !== FILE_CATEGORY.FOLDER &&
                    scope.row.fileType.category !== FILE_CATEGORY.UNKNOWN &&
                    scope.row.fileType.category !== FILE_CATEGORY.COMPRESSED &&
                    scope.row.fileType.category !== FILE_CATEGORY.EXCEL &&
                    scope.row.fileType.category !== FILE_CATEGORY.PPT && haveReadPermission"
                    content="预览" placement="top" :show-arrow="false">
                  <el-button type="primary" size="small" :icon="DataAnalysis"
                             @click="handlePreview(scope.$index, scope.row)"/>
                </el-tooltip>

                <el-tooltip content="下载" placement="top" :show-arrow="false" v-if="haveReadPermission">
                  <el-button type="success" size="small" :icon="Download"
                             @click="handleDownload(scope.$index, scope.row)"/>
                </el-tooltip>

                <el-tooltip content="重命名" placement="top" :show-arrow="false"
                            v-if=" route.fullPath !== config.userRouterBaseUrl && haveWritePermission">
                  <el-button type="warning" size="small" :icon="EditPen"
                             @click="handleRename(scope.$index, scope.row)"/>
                </el-tooltip>

                <el-tooltip content="删除" placement="top" :show-arrow="false"
                            v-if=" route.fullPath !== config.userRouterBaseUrl && haveDeletePermission">
                  <el-button type="danger" size="small" :icon="DeleteFilled"
                             @click="handleDelete(scope.$index, scope.row)"/>
                </el-tooltip>

                <el-tooltip content="分享" placement="top" :show-arrow="false" v-if="haveWritePermission">
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

      <div v-if=" dialogState.visible && dialogState.contentType === 'ShareLinkList' ">
        <UserShareManager></UserShareManager>
      </div>

      <div v-if=" dialogState.visible && dialogState.contentType === 'RecycleBin' ">
        <RecycleBin @refreshFileList="refreshFileList()"></RecycleBin>
      </div>

      <!-- 分享链接 -->
      <div v-if="dialogState.contentType === 'share' && dialogState.visible">
        <div class="share-link-container">
          <div class="share-link">
            <div class="share-link-label">
              <span>链接:</span>
            </div>
            <div class="share-link-content">
              <a :href="shareLink" target="_blank" @click.prevent="copyShareLink">{{ shareLink }}</a>
            </div>
          </div>
          <el-button type="primary" size="small" @click="copyShareLink" class="copy-button">复制链接</el-button>
        </div>
      </div>
    </div>

    <template #footer>
    </template>
  </el-dialog>
  <!-- 音乐悬浮窗-->
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


  <!-- 上传文件弹窗 -->
  <el-dialog
      v-model="uploadDialog.visible"
      title="上传文件"
      width="40%"
      :before-close="handleUploadClose"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      class="upload-dialog"
  >
    <div class="upload-dialog-content">
      <el-progress :percentage="uploadProgress" :text-inside="true" :stroke-width="24"
                   class="upload-progress" :color="colorsForProgress"></el-progress>
      <p class="upload-speed">速度: {{ uploadSpeed }} MB/s</p>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancelUpload">取消</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- ai聊天 -->
  <!-- 新增右下角悬浮按钮 -->
  <div class="chat-float-button" @click="showChatDialog">
    <el-icon :size="30" color="#fff">
      <ChatDotRound/>
    </el-icon>
  </div>

  <!-- 新增聊天对话框 -->
  <el-dialog
      v-model="chatDialogVisible"
      title="云盘智能助手"
      width="600px"
      class="chat-dialog"
      draggable
  >
    <ChatAssistant/>
  </el-dialog>

</template>
<script setup>
import {markRaw, onMounted, reactive, ref, warn, watch} from 'vue';
import {
  Close,
  DataAnalysis,
  Delete,
  DeleteFilled,
  Download,
  EditPen,
  FolderAdd,
  Share,
  UploadFilled
} from '@element-plus/icons-vue';
import {easyRequest, optionalRequest, RequestMethods} from "@/utils/RequestTool.js";
import {useRoute} from "vue-router";
import {config} from "@/GlobalConfig.js";
import router, {deleteCookie, tokenName} from "@/router/RouterSetting.js";
import {sizeTostr} from "@/utils/FileSizeConverter.js";
import {ElLoading, ElMessage, ElMessageBox} from 'element-plus';
import useClipboard from 'vue-clipboard3'; // 引入 vue-clipboard3
import SparkMD5 from 'spark-md5';
import {UserSession} from "@/utils/UserLocalStoreUtils.js";
import VideoPreview from "@/components/user/videoPreview.vue";
import AudioPreview from "@/components/user/audioPreview.vue";
import TextPreview from "@/components/user/textPreview.vue";
import ImagePreview from "@/components/user/imagePreview.vue";
import DocxPreview from "@/components/user/docxPreview.vue";
import PdfPreview from "@/components/user/pdfPreview.vue";
import IconFromDIY from "@/components/common/iconFromDIY.vue";
import UserShareManager from "@/components/user/UserShareManager.vue";
import RecycleBin from "@/components/user/RecycleBin.vue";
import ChatAssistant from '@/components/user/ChatAssistant.vue'

// 权限相关变量
const haveDeletePermission = ref(UserSession.getPermissions().includes("d"));
const haveWritePermission = ref(UserSession.getPermissions().includes("w"));
const haveReadPermission = ref(UserSession.getPermissions().includes("r"));

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
  // 当有面包屑的时候,刷新掉,要不然会冲突,没有的话则不需要
  if (config.userRouterBaseUrl !== route.fullPath) {
    router.push(config.userRouterBaseUrl);
    return;
  }

  // 获取权限
  easyRequest(RequestMethods.GET, "/user/getPermissions", {}, false, false).then((response) => {
    UserSession.setPermissions(response.data);
    // 权限相关变量
    haveDeletePermission.value = response.data.includes("d");
    haveWritePermission.value = response.data.includes("w");
    haveReadPermission.value = response.data.includes("r");

    if (haveReadPermission.value) {
      easyRequest(RequestMethods.POST, "/file/getFileList", {path: config.userRouterBaseUrl}, false, true).then(response => {
        response.data.forEach((item) => {
          if (item.fileType.category === FILE_CATEGORY.FOLDER) {
            item.fileSize = ''; // 目录不显示大小
          }
        }); // 先处理一下目录
        tableData.value = response.data;
      });
    } else {
      console.warn("没有读取权限，无法获取文件列表!")
    }
  })
});

watch(() => route.fullPath, () => {
  //如果是根目录
  /*  if (route.fullPath === config.userRouterBaseUrl) {
      // 非首次加载页面，更新数据
      easyRequest(RequestMethods.POST, "/file/getFileList", {path: route.fullPath}, false, true).then((response) => {
        handleResponse(response);
      });
    } else {
      // 非根目录，更新数据
      easyRequest(RequestMethods.POST, "/file/getFileList", {path: route.fullPath}, false, true).then((response) => {
        handleResponse(response);
      });
    }*/

  easyRequest(RequestMethods.GET, "/user/getPermissions", {}, false, false).then((response) => {
    UserSession.setPermissions(response.data);
    // 权限相关变量
    haveDeletePermission.value = response.data.includes("d");
    haveWritePermission.value = response.data.includes("w");
    haveReadPermission.value = response.data.includes("r");

    if (haveReadPermission.value) {
      easyRequest(RequestMethods.POST, "/file/getFileList", {path: route.fullPath}, false, true).then((response) => {
        handleResponse(response);
      });
    } else {
      console.warn("没有读取权限，无法获取文件列表!")
    }
  })

  // 面包屑更新
  let temp = config.userRouterBaseUrl.substring(1, config.userRouterBaseUrl.length);
  pathPartsForBreadCrumb.value = (route.fullPath.split('/').filter((item) => item !== '' && item !== temp));
});

const handleCreateFolder = () => {
  ElMessageBox.prompt('请输入文件夹名称', '创建文件夹', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputValue: '',
    inputPattern: /^[^\\/]+$/, // 只允许输入文件名，不允许输入路径
    inputErrorMessage: '文件夹名不能包含路径信息',
    inputType: 'text',
  }).then(({value}) => {
    if (value) {
      const createFolderDTO = {
        parentPath: route.fullPath,
        folderName: value
      };

      easyRequest(RequestMethods.POST, "/file/createFolder", createFolderDTO, false, true).then(response => {
        if (response.statusCode === "SUCCESS") {
          // 重新获取文件列表
          easyRequest(RequestMethods.POST, "/file/getFileList", {path: route.fullPath}, false, true).then((response) => {
            handleResponse(response);
          });
          ElMessage.success("文件夹创建成功!");
        } else {
          ElMessage.error(response.errMsg);
        }
      });
    }
  });
}

// 定义上传相关变量
const uploadDialog = reactive({visible: false,});
const uploadProgress = ref(0); // 上传进度
const uploadSpeed = ref("0"); // 上传速度
const upLoadFile = ref(null); // 需要上传的文件
const uploadCanceled = ref(false); // 上传是否取消
const colorsForProgress = [
  {color: '#f56c6c', percentage: 20},
  {color: '#e6a23c', percentage: 40},
  {color: '#13CE66', percentage: 60},
  {color: '#13cebb', percentage: 80},
  {color: '#1989fa', percentage: 100},
];


// 处理上传文件按钮点击事件
const handleUploadFile = () => {
  uploadDialog.visible = true;
  // 创建文件选择输入框
  const input = document.createElement('input');
  input.type = 'file'; // 设置input类型为file

  input.onchange = (e) => {  //注册 onchange 事件
    e.preventDefault(); // 阻止默认行为
    input.style.display = 'none'; // 隐藏 input 元素
    upLoadFile.value = e.target.files[0]; // 获取选择的文件
    if (upLoadFile.value) {
      startUpload(upLoadFile.value); // 开始上传
    }
  };

  input.click();
};

// 开始上传文件
const startUpload = async (file) => {
  // 初始化
  uploadProgress.value = 0;
  uploadSpeed.value = 0;
  uploadCanceled.value = false;
  if (file.size === 0) {
    ElMessage.error('空文件!');
    return;
  }

  // 分片上传逻辑
  // 1.根据文件大小选择分片大小
  let chunkSize = 1024 * 1024 * 4; // 默认分片大小 4MB
  /*  if (file.size < 10 * 1024 * 1024) {
      // 文件小于10MB，整个文件作为一个分片
      chunkSize = file.size;
    } else if (file.size > 200 * 1024 * 1024) {
      // 文件大于200MB，分片大小16MB
      chunkSize = 1024 * 1024 * 16;
    }*/
  // 2.计算分片数量
  const totalChunks = Math.ceil(file.size / chunkSize);

  // 3.上传分片
  let isBreak = false;// 是否中断上传
  for (let i = 0; i < totalChunks; i++) {
    let startTime = performance.now(); // 开始时间

    if (uploadCanceled.value) {
      isBreak = true;
      break;
    }

    // 3.1 读取分片数据
    const start = i * chunkSize;
    const end = Math.min(start + chunkSize, file.size); // 计算本次实际分片大小(最后一次不能超过文件实际大小)
    const chunk = file.slice(start, end);

    // 3.2计算分片MD5
    const chunkMd5 = await calculateMd5(chunk);

    // 3.3 上传分片
    const uploadResult = await uploadChunk(chunk, i, totalChunks, chunkMd5, file.name);
    if (uploadResult === null || uploadResult.statusCode === "ERROR") {
      ElMessage.error('服务器错误，上传失败!');
      isBreak = true;
      break; // 上传失败，退出循环
    }

    // 更新进度
    uploadProgress.value = Number.parseInt((((i + 1) / totalChunks) * 100).toFixed(2));

    // 更新速度
    const speed = ((end - start) / 1024 / 1024) / ((performance.now() - startTime) / 1000);
    uploadSpeed.value = speed.toFixed(2);
  }
  if (!isBreak) {
    // 重新获取文件列表
    easyRequest(RequestMethods.POST, "/file/getFileList", {path: route.fullPath}, false, true).then((response) => {
      handleResponse(response);
    });

    ElMessage.success('上传成功!');
    uploadDialog.visible = false;
  }

  upLoadFile.value = null; // 上传完毕，清空文件选择框
  setTimeout(() => {
    uploadProgress.value = 0;
    uploadSpeed.value = "0";
  }, 500);
};
// 上传分片
const uploadChunk = async (chunk, chunkIndex, totalChunks, chunkMd5, fileName) => {
  const formData = new FormData();
  formData.append("parentPath", route.fullPath);
  formData.append("file", chunk);
  formData.append("chunkIndex", chunkIndex);
  formData.append("fileName", fileName)
  formData.append("totalChunks", totalChunks);
  formData.append("chunkMd5", chunkMd5);

  return optionalRequest({
    method: RequestMethods.POST,
    url: "/file/uploadChunk",
    dataType: "file",
    data: formData,
    showError: true,
    checkDataFormat: true,
    errorCallback: (errorMsg) => {
      ElMessage.error('上传错误: ' + errorMsg);
    },
  });
};


// 计算MD5
const calculateMd5 = async (chunk) => {
  return new Promise((resolve, reject) => {
    const spark = new SparkMD5.ArrayBuffer();
    const fileReader = new FileReader();

    fileReader.onload = (e) => {
      spark.append(e.target.result);
      const md5 = spark.end();
      resolve(md5);
      spark.destroy(); // 释放内存
    };

    fileReader.onerror = (e) => {
      reject(e);
    };

    fileReader.readAsArrayBuffer(chunk);
  });
};

// 取消上传
const cancelUpload = () => {
  uploadCanceled.value = true;
  uploadDialog.visible = false;
};

// 上传弹窗关闭前的处理
const handleUploadClose = () => {
  if (!uploadCanceled.value && uploadProgress.value < 100) {
    ElMessageBox.confirm('上传未完成，确定要关闭吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }).then(() => {
      cancelUpload();
    }).catch(() => {
      // 取消关闭
    });
  } else {
    cancelUpload();
  }
};


const handleResponse = (response) => {
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
  let messageBoxInstance = null;
  let loading = null; // 初始化 loading 变量

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
  } // 目录需要等待一会儿

  easyRequest(RequestMethods.POST, "/file/preparingDownloadFile", {path: row.filePath}, false, true, 60000) // 设置较长的超时时间
      .then(response => {
        if (messageBoxInstance) {
          ElMessageBox.close(); // 关闭提示框
        }

        if (response.statusCode === "SUCCESS" && response.data) {
          const downloadUrl = response.data.url;
          const fileName = response.data.fileName;

          let completeUrl = `${location.origin}/api${downloadUrl}`;
          const link = document.createElement('a');
          link.href = completeUrl;
          link.setAttribute('download', fileName);
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link)


          /*    (手机不支持,内存占用较大)      loading = ElLoading.service({
                      lock: true,
                      text: '文件下载中，请稍候...',
                      background: 'rgba(32,31,31,0.7)',
                    });
                    // const fileName = response.data.fileName;

                    optionalRequest({
                      method: RequestMethods.GET,
                      responseType: 'blob',
                      url: downloadUrl,
                      dataTypes: 'blob',
                      timeout: 60000 // 设置较长的超时时间
                    }).then(response => {
                      const url = window.URL.createObjectURL(response);
                      const link = document.createElement('a');
                      link.href = url;
                      link.setAttribute('download', row.fileName); // 设置下载文件的名称
                      document.body.appendChild(link);
                      link.click();
                      document.body.removeChild(link);
                      window.URL.revokeObjectURL(url); // 释放 URL
                    }).catch(error => {
                      console.error('文件下载失败', error);
                    }).finally(() => {
                      if (loading) {
                        loading.close(); // 关闭加载提示
                      }
                    });*/

        }
      })
      .catch(error => {
        if (messageBoxInstance) {
          ElMessageBox.close(); // 关闭提示框
        }
        /* if (loading) {
           loading.close(); // 关闭加载提示
         }*/
        console.error('文件下载失败', error);
      });
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
                handleResponse(response);
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
  });
};

const shareLink = ref('');

const handleShare = (index, row) => {
  dialogState.value.visible = true;
  dialogState.value.title = "分享";
  dialogState.value.width = "50%";
  dialogState.value.contentType = 'share';

  easyRequest(RequestMethods.POST, "/file/createShareFile", {path: row.filePath}, false, true).then(response => {
    if (response.statusCode === "SUCCESS" && response.data) {
      shareLink.value = location.origin + "/#/share?" + response.data + " 链接有效期一天,请及时下载";
    } else {
      ElMessage.error(response.errMsg);
    }
  });
};

const {toClipboard} = useClipboard(); // 使用 vue-clipboard3

const copyShareLink = async () => {
  try {
    await toClipboard(shareLink.value);
    ElMessage.success('链接已复制到剪贴板');
  } catch (e) {
    ElMessage.error('复制失败');
  }
};

const handleDelete = (index, row) => {
  ElMessageBox.confirm(
      `确定要删除文件 ${row.fileName} ？`,
      '删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error',
        icon: markRaw(Delete),
      }
  ).then(() => {
    setFileProperties(row);
    const filePathDTO = {
      path: row.filePath
    };

    easyRequest(RequestMethods.POST, "/file/deleteFile", filePathDTO, false, true)
        .then(response => {
          if (response.statusCode === "SUCCESS") {
            // 删除成功，更新表格数据
            easyRequest(RequestMethods.POST, "/file/getFileList", {path: route.fullPath}, false, true).then((response) => {
              handleResponse(response);
              ElMessage.success("文件删除成功!");
            });
          } else {
            ElMessage.error(response.errMsg);
          }
        })
        .catch(error => {
          console.error('文件删除失败', error);
          ElMessage.error('文件删除失败');
        });
  }).catch(() => {
    // 用户取消操作
  });
};

const isHover = ref(false);
const logout = () => {
  UserSession.logout();
  deleteCookie(tokenName);
  easyRequest(RequestMethods.POST, "/user/logout", "", false, false).then(
      (response) => {
        if (response.statusCode === "SUCCESS") {
          ElMessage.success("退出成功");
        }
      }
  );
  router.push("/user/login");
};


const showShareLinkList = () => {
  // 显示分享链接列表
  dialogState.value.visible = true;
  dialogState.value.title = "分享链接";
  dialogState.value.width = "60%";
  dialogState.value.contentType = 'ShareLinkList';
}

const showRecycleBin = () => {
  // 显示分享链接列表
  dialogState.value.visible = true;
  dialogState.value.title = "回收站";
  dialogState.value.width = "80%";
  dialogState.value.contentType = 'RecycleBin';
}

const refreshFileList = () => {
  // 重新获取文件列表
  easyRequest(RequestMethods.POST, "/file/getFileList", {path: route.fullPath}, false, true).then((response) => {
    handleResponse(response);
  });
}

// 新增聊天对话框状态
const chatDialogVisible = ref(false)

// 新增显示对话框方法
const showChatDialog = () => {
  chatDialogVisible.value = true
}
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
  background-color: #555;
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

.share-link-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  margin-top: 20px;
}

.share-link {
  display: flex;
  align-items: center;
  gap: 10px;
}

.share-link-label {
  font-size: 16px;
  font-weight: bold;
  white-space: nowrap;
}

.share-link-content {
  flex-grow: 1;
  background-color: #333;
  padding: 10px;
  border-radius: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: normal;
  word-break: break-all;
}

.share-link-content a {
  color: #9ecaf9;
  text-decoration: underline;
  cursor: pointer;
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: normal;
  word-break: break-all;
}

.copy-button {
  margin-left: auto;
}

/*原本的全局样式 */
.full-width-table {
  width: 100%;
  border-radius: 8px;
  border-collapse: collapse;
}

.full-width-table th,
.full-width-table td {
  padding: 12px;
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
  cursor: pointer;
  text-overflow: ellipsis;
  max-width: 200px;
}

.custom-table :deep(.el-table__row td) {
  background-color: #555555;
  color: #fff;
}

.custom-table :deep(.el-table th) {
  background-color: #555555;
  color: #fff;
}

.custom-table ::v-deep(.el-table__header-wrapper th) {
  background: #212121;
  color: white;
}

.share-link-container {
  margin: 0;
}

.upload-dialog {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.upload-dialog-content {
  padding: 20px 20px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.upload-progress {
  width: 100%;
  margin-bottom: 20px;
}

.upload-speed {
  font-size: 16px;
  color: #333;
}

/* 以下为移动端样式 */
.cloud-index-container {
  margin-top: 10px;
}

.cloud-index-wrapper {
  border-radius: 8px;
  padding: 12px;
}

/* 响应式布局调整 */
@media screen and (max-width: 768px) {
  .el-col {
    width: 100% !important;
    padding: 0 8px !important;
  }

  .cloud-index-header {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }

  .cloud-index-actions {
    flex-wrap: wrap;
    gap: 8px;
  }

  .cloud-index-actions .el-button {
    width: 100%;
    margin: 0 !important;
  }

  .cloud-index-title {
    font-size: 20px;
  }
}

/* 表格移动端适配 */
.custom-table {
  overflow-x: auto;
}

.custom-table :deep(.el-table) {
  min-width: 600px; /* 保持表格最小宽度 */
}

.custom-table :deep(.el-table__body) {
  font-size: 14px;
}

/* 操作按钮调整 */
.custom-table :deep(.el-button) {
  margin: 2px;
  padding: 6px 8px;
}

/* 面包屑导航优化 */
.cloud-index-breadcrumb {
  overflow-x: auto;
  white-space: nowrap;
  -webkit-overflow-scrolling: touch;
  margin-bottom: 12px;
}

.cloud-index-breadcrumb .el-breadcrumb {
  font-size: 14px;
}

/* 文件名显示优化 */
.file-name {
  max-width: 60vw;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 弹窗移动端适配 */
.custom-dialog {
  max-width: 90vw !important;
}

.dialog-header span {
  font-size: 20px;
}

/* 上传进度条优化 */
.upload-progress {
  width: 100%;
  margin: 10px 0;
}

/* 操作按钮点击区域扩大 */
.cell-content {
  padding: 8px 0;
}

/* 移动端隐藏非必要元素 */
@media screen and (max-width: 480px) {
  .el-table-column--selection .cell {
    padding-left: 8px !important;
    padding-right: 8px !important;
  }

  /* 隐藏修改时间列 */
  .el-table__header-wrapper th:nth-child(3),
  .el-table__body-wrapper td:nth-child(3) {
    display: none;
  }
}

/* 优化按钮显示 */
@media screen and (max-width: 400px) {
  .el-button span {
    display: none;
  }

  .el-button i {
    margin-right: 0 !important;
  }
}

/* 分享链接优化 */
.share-link-content {
  max-width: 80vw;
}

/* 增加点击反馈 */
.el-button:active {
  opacity: 0.7;
}

/*ai聊天窗口*/
/* 新增悬浮按钮样式 */
.chat-float-button {
  position: fixed;
  right: 40px;
  bottom: 40px;
  width: 60px;
  height: 60px;
  background-color: #4a9cff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  transition: all 0.3s ease;
  z-index: 9999;
}

.chat-float-button:hover {
  background-color: #367abd;
  transform: scale(1.1);
}

/* 调整对话框样式 */
:deep(.chat-dialog) {
  border-radius: 12px;
  background-color: #1a1a1a;
}

:deep(.chat-dialog .el-dialog__header) {
  background-color: #2d2d2d;
  margin: 0;
  padding: 16px;
  border-radius: 12px 12px 0 0;
}

:deep(.chat-dialog .el-dialog__title) {
  color: #fff;
  font-size: 18px;
}

:deep(.chat-dialog .el-dialog__body) {
  padding: 0;
  height: 70vh;
  background-color: #1a1a1a;
}
</style>