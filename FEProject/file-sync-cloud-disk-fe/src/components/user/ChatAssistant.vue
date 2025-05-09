<template>
  <div class="compact-chat">
    <!-- API Key配置对话框 -->
    <el-dialog
        v-model="showApiKeyDialog"
        title="API Key配置"
        width="30%"
        :close-on-click-modal="false"
        :show-close="false"
    >
      <el-alert type="info" :closable="false" class="mb-4">
        <template #title>
          请从 Moonshot 平台获取API Key<br>
          首次使用必须配置有效API Key
        </template>
      </el-alert>

      <el-input
          v-model="tempApiKey"
          placeholder="输入您的Moonshot API Key"
          type="password"
          show-password
          @keyup.enter="saveApiKey"
      />

      <template #footer>
        <el-button type="primary" @click="saveApiKey">
          保存配置
        </el-button>
      </template>
    </el-dialog>
    <!-- 消息区域 -->
    <div class="messages-area">
      <el-tooltip
          content="API配置"
          placement="left"
          class="settings-btn-wrapper"
      >
        <el-button
            class="settings-btn"
            :icon="Setting"
            @click="showApiKeyDialog = true"
            circle
            size="small"
        />
      </el-tooltip>

      <div
          v-for="(msg, index) in messages"
          :key="index"
          :class="['message-bubble', msg.role]"
      >
        <template v-if="msg.file">
          <el-icon class="file-icon">
            <Document/>
          </el-icon>
          {{ msg.content }}
          <span class="file-tag">{{ msg.file.filename }}</span>
        </template>
        <template v-else>
          {{ msg.content }}
        </template>
      </div>
      <div v-if="loading" class="loading-text">思考中...</div>
    </div>
    <!-- 输入区域 -->
    <div class="input-area">
      <el-tooltip content="支持PDF、Word、Excel等格式" placement="top">
        <el-button
            class="upload-btn"
            :icon="Upload"
            :disabled="uploading || loading"
            @click="triggerFileUpload"
        />
      </el-tooltip>

      <el-input
          v-model="inputText"
          placeholder="输入问题或上传文件..."
          @keyup.enter="sendMessage"
          :disabled="loading"
      >
        <template #append>
          <el-button
              @click="sendMessage"
              :disabled="loading || !hasContent"
              :icon="Promotion"
              :style="buttonStyle"
          />
        </template>
      </el-input>

      <input
          type="file"
          ref="fileInput"
          hidden
          @change="handleFileUpload"
          accept=".pdf,.docx,.xlsx,.txt,.md"
      />
    </div>
    <!-- 提示信息  -->
    <div class="file-type-tip">支持格式：PDF、DOCX、XLSX、TXT、MD</div>
  </div>
</template>

<script setup>
import {ref, computed, onMounted} from 'vue'
import {Promotion, Upload, Document, Setting} from '@element-plus/icons-vue'
import {ElMessage} from 'element-plus'

const apiUrl = 'https://api.moonshot.cn/v1/chat/completions'
const fileApiUrl = 'https://api.moonshot.cn/v1/files'

// API配置状态
const showApiKeyDialog = ref(false)
const tempApiKey = ref('')
const apiKey = ref('')

// 初始化时检查LocalStorage
onMounted(() => {
  const savedKey = localStorage.getItem('moonshotApiKey')
  if (savedKey) {
    apiKey.value = savedKey
  } else {
    showApiKeyDialog.value = true
  }
})

// 保存API Key
const saveApiKey = () => {
  if (!tempApiKey.value.trim()) {
    ElMessage.warning('请输入有效的API Key')
    return
  }

  apiKey.value = tempApiKey.value.trim()
  localStorage.setItem('moonshotApiKey', apiKey.value)
  showApiKeyDialog.value = false
  ElMessage.success('API配置已保存')
}

// 在发送请求前添加验证逻辑
const validateApiKey = () => {
  if (!apiKey.value) {
    ElMessage.warning('请先配置API Key')
    showApiKeyDialog.value = true
    return false
  }
  return true
}

// 聊天相关状态
const messages = ref([
  {role: 'assistant', content: '您好！我是云盘助手，可以根据您提供的文件内容回答您的问题或者提供建议。'}
])
const inputText = ref('')
const loading = ref(false)

// 文件上传相关状态
const uploading = ref(false)
const fileInput = ref(null)
const attachedFile = ref(null)

// 动态样式计算
const hasContent = computed(() => inputText.value.trim() || attachedFile.value)
const buttonStyle = computed(() => ({
  backgroundColor: hasContent.value ? '#409eff' : '#909399',
  borderColor: hasContent.value ? '#409eff' : '#909399'
}))

// 触发文件选择
const triggerFileUpload = () => {
  if (!uploading.value) fileInput.value.click()
}

// 处理文件上传
const handleFileUpload = async (event) => {
  if (!validateApiKey()) return
  const file = event.target.files[0]
  if (!file) return

  // 文件大小限制（示例限制50MB）
  if (file.size > 50 * 1024 * 1024) {
    ElMessage.error('文件大小不能超过50MB')
    return
  }

  uploading.value = true

  try {
    const formData = new FormData()
    formData.append('file', file)
    formData.append('purpose', 'file-extract')

    const response = await fetch(fileApiUrl, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${apiKey.value}`,
      },
      body: formData
    })

    const data = await response.json()

    if (!response.ok) {
      throw new Error(data.error?.message || '文件上传失败')
    }

    attachedFile.value = {
      id: data.id,
      filename: data.filename,
      bytes: data.bytes
    }

    inputText.value = `[文件: ${data.filename}] ${inputText.value}`
    ElMessage.success('文件上传成功')
  } catch (error) {
    console.error('文件上传失败:', error)
    ElMessage.error(error.message)
  } finally {
    uploading.value = false
    fileInput.value.value = ''
  }
}

// 发送消息
const sendMessage = async () => {
  if (!validateApiKey()) return
  if (!hasContent.value) return

  try {
    // 添加用户消息
    const userMessage = {
      role: 'user',
      content: inputText.value,
      file: attachedFile.value
    }
    messages.value.push(userMessage)

    // 获取文件内容
    let fileContent = ''
    if (attachedFile.value) {
      const contentResponse = await fetch(`${fileApiUrl}/${attachedFile.value.id}/content`, {
        headers: {
          'Authorization': `Bearer ${apiKey.value}`,
        }
      })

      if (!contentResponse.ok) {
        throw new Error('获取文件内容失败')
      }

      fileContent = await contentResponse.text()
    }

    // 构造符合Moonshot要求的消息结构
    const requestMessages = [
      {
        role: "system",
        content: "你是一个云盘助手，请回答用户后续的问题或请结合用户提供的文件内容回答问题"
      }
    ]

    // 如果有文件内容，添加到系统消息
    if (fileContent) {
      requestMessages.push({
        role: "system",
        content: fileContent
      })
    }

    requestMessages.push({
      role: "user",
      content: inputText.value
    })

    // 请求参数调整
    const requestBody = {
      model: "moonshot-v1-32k", // 使用文档中的32K模型
      messages: requestMessages,
      temperature: 0.3,
      max_tokens: 2000
    }

    loading.value = true
    const response = await fetch(apiUrl, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${apiKey.value}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(requestBody)
    })

    if (!response.ok) throw new Error(`请求失败: ${response.status}`)

    const data = await response.json()
    if (data.choices?.[0]?.message?.content) {
      messages.value.push({
        role: 'assistant',
        content: data.choices[0].message.content
      })
    } else {
      throw new Error('无效的响应格式')
    }
  } catch (error) {
    console.error('请求出错:', error)
    messages.value.push({
      role: 'assistant',
      content: `处理出错：${error.message}`
    })
  } finally {
    loading.value = false
    inputText.value = ''
    attachedFile.value = null
  }
}
</script>
<style scoped>
.compact-chat {
  height: 60vh;
  display: flex;
  flex-direction: column;
  background: #1a1a1a;
  border-radius: 8px;
}

.messages-area {
  flex: 1;
  padding: 12px;
  overflow-y: auto;
  min-height: 200px;
}

.message-bubble {
  max-width: 80%;
  padding: 8px 12px;
  margin: 8px;
  border-radius: 12px;
  line-height: 1.4;
  font-size: 0.9em;
  word-break: break-word;
}

.message-bubble.user {
  background: #4a9cff;
  color: white;
  margin-left: auto;
}

.message-bubble.assistant {
  background: #3d3d3d;
  color: #e0e0e0;
  margin-right: auto;
}

.input-area {
  display: flex;
  align-items: center;
  padding: 12px;
  border-top: 1px solid #333;
  gap: 8px;
}

.upload-btn {
  transition: all 0.3s ease;
  background: #2d2d2d;
  border-color: #555;
}

.upload-btn:hover {
  color: #409eff;
  border-color: #409eff;
}

.loading-text {
  color: #888;
  text-align: center;
  padding: 8px;
  font-size: 0.9em;
}

.file-tag {
  display: inline-block;
  padding: 2px 6px;
  margin-left: 8px;
  background: #ffffff22;
  border-radius: 4px;
  font-size: 0.8em;
}

.file-icon {
  margin-right: 6px;
  vertical-align: middle;
}

:deep(.el-input__inner) {
  background: #2d2d2d;
  border-color: #333;
  color: white;
}

:deep(.el-input-group__append) {
  background: #404040;
  border-color: #333;
}

:deep(.el-button) {
  transition: all 0.3s ease;
}

:deep(.el-input__wrapper) {
  background: #2d2d2d;
  border-color: #333;
  color: white;
}

.file-type-tip {
  font-size: 0.8em;
  color: #888;
  margin-top: 4px;
}

.settings-btn {
  margin-right: 8px;
  transition: all 0.3s ease;
}

.settings-btn:hover {
  color: #409eff;
  transform: rotate(90deg);
}

/* 调整对话框样式 */
.api-key-alert {
  margin-bottom: 20px;
  line-height: 1.6;
}

:deep(.el-alert) {
  margin-bottom: 8px;
}

/* 按钮样式 */
.settings-btn-wrapper {
  position: absolute;
  top: 6px;
  right: 6px;
  z-index: 1000;
  padding: 0;
  margin: 0;
}

.settings-btn {
  width: 24px;
  height: 24px;
  padding: 0;
  margin: 0;
  margin-left: 8px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.15);
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}

.settings-btn:hover {
  transform: rotate(90deg);
  background: rgba(64, 158, 255, 0.2);
  border-color: #409eff;
}

/* 调整消息区域定位 */
.messages-area {
  position: relative; /* 新增 */
  flex: 1;
  padding: 8px 12px 12px; /* 顶部留出按钮空间 */
  overflow-y: auto;
  min-height: 200px;
}
</style>