<template>
  <div>
    <el-button type="primary" @click="handleClick">上传文件</el-button>
  </div>
</template>

<script setup>
import {ref} from 'vue'
import {optionalRequest, RequestMethods} from "@/utils/RequestTool.js";
import SparkMD5 from 'spark-md5'; // 引入 spark-md5 库

const file = ref(null)

// 处理上传文件按钮点击事件
const handleClick = () => {
  file.value = null
  const input = document.createElement('input') // 创建input标签
  input.type = 'file' // 设置input类型为file
  input.onchange = (e) => {  //注册 onchange 事件
    file.value = e.target.files[0] // 获取选择的文件
    console.log(file.value);
    uploadFilePreHandler(file.value); // 上传文件预处理
  }
  input.click() // 点击input标签，弹出文件选择框
}

const uploadFilePreHandler = async (file) => {
  const fileItem = {
    file: file,
    //文件UID
    // uid: file.uid,
    //文件名
    fileName: file.name,
    //已上传大小
    // uploadSize: 0,
    //文件总大小
    totalSize: file.size,
    //进度
    // uploadProgress: 0,
    //暂停
    // pause: false,
    //当前分片
    chunkIndex: 0,
    //父级ID(目前无用)
    // filePid: filePid,
    //错误信息
    errorMsg: null,
  };

  if (fileItem.totalSize === 0) {
    console.log("文件大小为0");
    return;
  }

  let chunkSize = 1024 * 1024 * 8;// 默认分片大小
  if (fileItem.totalSize < 10 * 1024 * 1024) {
    // 文件大小小于10M,直接上传
    chunkSize = fileItem.totalSize;// 分片大小
  } else if (fileItem.totalSize > 200 * 1024 * 1024) {
    // 文件大小大于200M,分片大小为 16 M
    chunkSize = 1024 * 1024 * 16;// 分片大小
  }
  await uploadFile(chunkSize, fileItem);
};

// //开始上传
// const startUpload = (uid) => {
//   let currentFile = getFileByUid(uid);
//   currentFile.pause = false;
//   uploadFile(uid, currentFile.chunkIndex);
// };
// //暂停上传
// const pauseUpload = (uid) => {
//   let currentFile = getFileByUid(uid);
//   currentFile.pause = true;
// };
// //删除文件
// const delUpload = (uid, index) => {
//   delList.value.push(uid);
//   fileList.value.splice(index, 1);
// };

const uploadFile = async (chunkSize, fileItem) => {
  let chunkIndex = fileItem.chunkIndex;

  const file = fileItem.file;
  const fileSize = fileItem.totalSize;// 文件总大小

  const totalChunks = Math.ceil(fileSize / chunkSize); // 计算总分片数,向上取整
  console.log(`文件总大小: ${fileSize} , 分片数: ${totalChunks}`);


  for (let i = chunkIndex; i < totalChunks; i++) {
    // let delIndex = delList.value.indexOf(uid);
    // if (delIndex != -1) {
    //   delList.value.splice(delIndex, 1); // 删除
    //   // console.log(delList.value);
    //   break;
    // }
    // currentFile = getFileByUid(uid);
    // if (currentFile.pause) {
    //   break; // 暂停
    // }

    let start = i * chunkSize;
    let end = start + chunkSize >= fileSize ? fileSize : start + chunkSize;// 计算本次实际分片大小(最后一次不能超过文件实际大小)

    console.log(`开始上传第 ${i + 1} 分片,大小: ${end - start} 字节`);

    let chunkFile = file.slice(start, end);
    // 计算分片的 MD5 值
    const chunkMd5 = await calculateMd5(chunkFile);

    let formData = new FormData();
    formData.append("file", chunkFile);
    formData.append("fileName", file.name);
    formData.append("chunkIndex", i);
    formData.append("totalChunks", totalChunks);
    formData.append("chunkMd5", chunkMd5); // 添加分片的 MD5 值
    // formData.append("fileId", fileItem.fileId);
    // formData.append("filePid", fileItem.filePid); // 父id

    let uploadResult = await optionalRequest({
      method: RequestMethods.POST,
      url: "/upload/chunk",
      dataType: "file",
      data: formData,
      showError: true,
      checkDataFormat: true,
      errorCallback: (errorMsg) => {
        console.log("文件上传错误:" + errorMsg);
      },
    });
    if (uploadResult == null) {
      break;
    }
    // file.fileId = uploadResult.data.fileId; // 上传成功后更新fileId ?
    file.chunkIndex = i; // 上传成功后更新chunkIndex
  }
};
// 计算文件分片的 MD5 值
const calculateMd5 = async (file) => {
  return new Promise((resolve, reject) => {
    const spark = new SparkMD5.ArrayBuffer();
    const fileReader = new FileReader();


    //设置 onload 和 onerror 方法
    fileReader.onload = (e) => {
      spark.append(e.target.result);
      const md5 = spark.end();
      console.log(md5);
      spark.destroy(); // 释放内存
      resolve(md5);
    };

    fileReader.onerror = (e) => {
      reject(e);
    };

    fileReader.readAsArrayBuffer(file); // 读取二进制
  });
};
</script>

<style scoped>
</style>