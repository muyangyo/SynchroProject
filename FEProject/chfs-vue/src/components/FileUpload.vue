<template>
  <div class="upload-container">
    <el-upload
        ref="uploadRef"
        drag
        multiple
        :http-request="handleCustomUpload"
        :show-file-list="false"
        :auto-upload="false"
        :on-remove="handleRemoveFile"
        class="upload-area"
    >
      <i class="el-icon-upload"></i>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
    </el-upload>

    <div v-for="(file, index) in uploadList" :key="index" class="file-item">
      <div class="file-info">
        <span>{{ file.file.name }}</span>
        <el-progress :percentage="file.progress" style="width: 200px"/>
      </div>
      <div class="file-actions">
        <el-button size="small" @click="pauseUpload(file)">暂停</el-button>
        <el-button size="small" type="danger" @click="cancelUpload(file)">取消</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import SparkMD5 from "spark-md5";

export default {
  data() {
    return {
      uploadList: [], // 保存上传中的文件信息
    };
  },
  methods: {
    async handleCustomUpload(options) {
      const file = options.file;

      // 计算文件 MD5
      const md5 = await this.calculateFileMD5(file);
      const chunkSize = 8 * 1024 * 1024; // 每块大小 8MB
      const totalChunks = Math.ceil(file.size / chunkSize);
      const uploadItem = {
        file,
        md5,
        chunkSize,
        totalChunks,
        currentChunk: 0,
        progress: 0,
        isPaused: false,
        cancelToken: false,
      };

      this.uploadList.push(uploadItem);

      // 开始上传
      await this.uploadChunks(uploadItem);
    },
    async calculateFileMD5(file) {
      const spark = new SparkMD5.ArrayBuffer();
      const chunkSize = 8 * 1024 * 1024; // 每块大小 8 MB
      const totalChunks = Math.ceil(file.size / chunkSize);

      const fileReader = new FileReader();
      let currentChunk = 0;

      return new Promise((resolve, reject) => {
        fileReader.onload = (e) => {
          spark.append(e.target.result);
          currentChunk++;

          if (currentChunk < totalChunks) {
            loadNext();
          } else {
            resolve(spark.end());
          }
        };

        fileReader.onerror = (e) => reject(e);

        function loadNext() {
          const start = currentChunk * chunkSize;
          const end = Math.min(file.size, start + chunkSize);
          fileReader.readAsArrayBuffer(file.slice(start, end));
        }

        loadNext();
      });
    },
    async uploadChunks(uploadItem) {
      const {file, chunkSize, totalChunks, md5} = uploadItem;

      while (uploadItem.currentChunk < totalChunks) {
        if (uploadItem.isPaused || uploadItem.cancelToken) break;

        const start = uploadItem.currentChunk * chunkSize;
        const end = Math.min(file.size, start + chunkSize);
        const chunk = file.slice(start, end);

        const formData = new FormData();
        formData.append("file", chunk);
        formData.append("chunk", uploadItem.currentChunk);
        formData.append("totalChunks", totalChunks);
        formData.append("fileName", file.name);
        formData.append("md5", md5);

        try {
          await this.$axios.post("/upload/chunk", formData);
          uploadItem.currentChunk++;
          uploadItem.progress = Math.ceil(
              (uploadItem.currentChunk / totalChunks) * 100
          );
        } catch (error) {
          console.error("上传失败", error);
          break;
        }
      }
    },
    pauseUpload(uploadItem) {
      uploadItem.isPaused = true;
    },
    cancelUpload(uploadItem) {
      uploadItem.cancelToken = true;
      this.uploadList = this.uploadList.filter((item) => item !== uploadItem);
    },
    handleRemoveFile(file) {
      this.uploadList = this.uploadList.filter(
          (item) => item.file.name !== file.name
      );
    },
  },
};
</script>

<style scoped>
.upload-container {
  margin: 20px;
}

.upload-area {
  border: 2px dashed #d9d9d9;
  padding: 20px;
  text-align: center;
}

.file-item {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.file-info {
  display: flex;
  align-items: center;
}

.file-actions {
  display: flex;
  gap: 10px;
}
</style>
