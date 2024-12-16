<template>
  <el-image-viewer :url-list="imageUrls" :initial-index="initialIndex" @close="closeViewer"/>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { ElImageViewer } from 'element-plus'
import getBlobData from "@/utils/getBlobData"

const imageUrls = ref<string[]>([])
const initialIndex = ref<number>(0)

interface Props {
  sourceFilePath: string
}

const props = defineProps<Props>()

const previewImage = async () => {
  try {
    const response = await getBlobData('/previewImage', {path: props.sourceFilePath})
    const url = window.URL.createObjectURL(response)
    imageUrls.value = [url]
    initialIndex.value = 0
  } catch (error) {
    console.error('预览图片失败:', error)
  }
}

const closeViewer = () => {
  imageUrls.value.forEach(url => window.URL.revokeObjectURL(url))
  imageUrls.value = []
}

onMounted(() => {
  console.log('图片预览组件挂载')
  previewImage()
})

onBeforeUnmount(() => {
  console.log('图片预览组件销毁')
  closeViewer()
})
</script>

<style scoped>
/* 可以根据需要添加样式 */
</style>