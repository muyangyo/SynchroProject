<template>
  <div class="dark-container">
    <!-- 模式切换确认对话框 -->
    <el-dialog
        v-model="showModeDialog"
        :title="`切换到${switchToMode === 'server' ? '服务端' : '客户端'}模式`"
        width="30%">
      <span style="color: white;font-weight: bold">⚠️警告: 切换模式将清空当前所有同步设置，是否继续？</span>
      <template #footer>
        <el-button @click="showModeDialog = false">取消</el-button>
        <el-button type="danger" @click="confirmSwitchMode">确认切换</el-button>
      </template>
    </el-dialog>

    <div class="main-container">
      <!-- 头部 -->
      <div class="header">
        <h2>同步管理 - {{ currentMode === 'server' ? '服务端' : '客户端' }}
          <img
              :src="currentMode === 'server' ? serverGif : clientGif"
              class="mode-gif"
              @click="handleGifClick"
          />
        </h2>
        <div class="header-actions">
          <el-button type="info" @click="handleSwitchMode">
            切换模式 (当前: {{ currentMode === 'server' ? '服务端' : '客户端' }})
          </el-button>
          <el-button type="primary" @click="showAddDialog = true">
            {{ currentMode === 'server' ? '新增同步' : '添加同步' }}
          </el-button>
        </div>
      </div>

      <!-- 表格 -->
      <el-table :data="syncList" class="dark-table">
        <!-- 公共列 -->
        <el-table-column label="同步名" min-width="100">
          <template #default="{ row }">
            <div class="sync-name">
              <el-icon>
                <Folder/>
              </el-icon>
              <span>{{ row.syncName }}</span>
            </div>
          </template>
        </el-table-column>

        <!-- 服务端专属列 -->
        <el-table-column v-if="currentMode === 'server'" label="本地大小" max-width="100" align="center">
          <template #default="{ row }">{{ row.localSize }}</template>
        </el-table-column>

        <!-- 客户端专属列 -->
        <el-table-column v-if="currentMode === 'client'" label="状态" max-width="200" align="center">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)">
              {{ statusMap[row.status] || row.status }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="currentMode === 'client'" label="本地大小" max-width="200" align="center">
          <template #default="{ row }">{{ row.localSize }}</template>
        </el-table-column>

        <!-- 操作列 -->
        <el-table-column label="操作" min-width="80">
          <template #default="{ row }">
            <el-button size="small" @click="handleSetting(row)">设置</el-button>
            <el-button
                v-if="currentMode === 'client'"
                size="small"
                type="warning"
                @click="handleToggleSync(row)"
            >
              {{ row.status === 'SYNC_STOP' ? '恢复' : '暂停' }}
            </el-button>
            <el-button
                v-if="currentMode === 'server'"
                size="small"
                type="success"
                @click="handleShare(row)"
            >
              分享
            </el-button>
            <el-button
                size="small"
                type="danger"
                @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 新增对话框 -->
      <el-dialog v-model="showAddDialog" :title="currentMode === 'server' ? '新建同步' : '添加同步链接'">
        <el-form :model="newSyncForm" label-width="120px" :rules="formRules">
          <el-form-item v-if="currentMode === 'server'" label="同步名称" prop="syncName">
            <el-input v-model="newSyncForm.syncName" placeholder="目前仅限英文名称" />
          </el-form-item>

          <el-form-item v-if="currentMode === 'server'" label="本地路径" prop="localPath">
            <el-input v-model="newSyncForm.localPath" placeholder="绝对路径，如：C:/sync_folder" />
          </el-form-item>

          <el-form-item v-if="currentMode === 'client'" label="同步链接" prop="syncUrl">
            <el-input v-model="newSyncForm.syncUrl" placeholder="输入服务端提供的分享链接" />
          </el-form-item>
        </el-form>

        <template #footer>
          <el-button @click="showAddDialog = false">取消</el-button>
          <el-button type="primary" @click="confirmAddSync">确认</el-button>
        </template>
      </el-dialog>

      <!-- 设置抽屉 -->
      <el-drawer v-model="drawerVisible" direction="rtl" size="40%">
        <template #header>
          <h3>{{ selectedRow?.syncName }} - 设置</h3>
        </template>

        <el-form v-if="selectedRow" :model="settingForm">
          <el-form-item label="同步名称">
            <el-input v-model="selectedRow.syncName" disabled />
          </el-form-item>

          <el-form-item label="本地路径">
            <el-input v-model="settingForm.localPath" />
          </el-form-item>

          <!-- 客户端模式下显示服务器 IP -->
          <el-form-item v-if="currentMode === 'client'" label="服务器 IP">
            <el-input v-model="settingForm.serverIp" placeholder="请输入服务器 IP 地址" />
          </el-form-item>

          <el-form-item v-if="currentMode === 'server'" label="版本删除">
            <el-switch v-model="settingForm.versionDelete" disabled/>
          </el-form-item>

          <el-form-item label="本地大小">
            <el-input v-model="selectedRow.localSize" disabled/>
          </el-form-item>
        </el-form>

        <template #footer>
          <el-button @click="drawerVisible = false">取消</el-button>
          <el-button type="primary" @click="saveSettings">保存</el-button>
        </template>
      </el-drawer>
    </div>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Folder} from '@element-plus/icons-vue'

// 引入 GIF 图片
import serverGif from '@/assets/server.gif'
import clientGif from '@/assets/client.gif'

// 定义 GIF 点击跳转逻辑
const handleGifClick = () => {
  window.open("https://icons8.com/", '_blank')
}

// 初始化加载
onMounted(async () => {
  // 模拟API调用
  setTimeout(() => {
    currentMode.value = 'server' // 从后端获取实际模式
    loadSyncList()
  }, 500)
})

// 状态映射
const statusMap = {
  WAIT_CONNECT: '等待连接',
  CONNECTING: '连接中',
  SYNCING: '同步中',
  SYNC_COMPLETE: '同步完成',
  SYNC_FAILED: '同步失败',
  SYNC_STOP: '已暂停'
}

// 响应式数据
const currentMode = ref('')
const switchToMode = ref('')
const showModeDialog = ref(false)
const syncList = ref([])
const showAddDialog = ref(false)
const drawerVisible = ref(false)
const selectedRow = ref(null)

// 表单数据
const newSyncForm = reactive({
  syncName: '',
  localPath: '',
  syncUrl: ''
})

const settingForm = reactive({
  localPath: '',
  serverIp: '', // 新增服务器 IP 字段
  versionDelete: false
})

// 表单验证规则
const formRules = {
  syncName: [
    { required: true, message: '请输入同步名称' },
    { pattern: /^[a-zA-Z]+$/, message: '只能使用英文字符' }
  ],
  localPath: [
    { required: true, message: '请输入本地路径' },
    { pattern: /^[a-zA-Z]:\\?.+/, message: '请输入有效的绝对路径' }
  ],
  syncUrl: [
    { required: true, message: '请输入同步链接' },
    { pattern: /^https?:\/\/.+/i, message: '请输入有效的链接' }
  ]
}

// 状态标签颜色
const statusTagType = (status) => {
  const map = {
    SYNCING: 'primary',
    SYNC_COMPLETE: 'success',
    SYNC_FAILED: 'danger',
    SYNC_STOP: 'warning'
  }
  return map[status] || 'info'
}

// 加载同步列表
const loadSyncList = async () => {
  // 模拟API调用
  syncList.value = currentMode.value === 'server'
      ? [
        {
          id: 1,
          syncName: 'documents',
          localPath: 'C:/sync/docs',
          localSize: '256 MB',
          versionDelete: true
        }
      ]
      : [
          {
            id: 2,
            syncName: 'backup',
            localPath: 'D:/backup',
            localSize: '1.2 GB',
            status: 'SYNCING',
            serverIp: '192.168.1.100' // 模拟客户端模式下的服务器 IP
          }
        ]
}

// 模式切换处理
const handleSwitchMode = () => {
  switchToMode.value = currentMode.value === 'server' ? 'client' : 'server'
  showModeDialog.value = true
}

const confirmSwitchMode = async () => {
  try {
    // 调用API切换模式
    currentMode.value = switchToMode.value
    syncList.value = []
    await loadSyncList()
    showModeDialog.value = false
    ElMessage.success('模式切换成功')
  } catch (error) {
    ElMessage.error('模式切换失败')
  }
}

// 新增同步
const confirmAddSync = () => {
  if (currentMode.value === 'server') {
    syncList.value.push({
      id: Date.now(),
      syncName: newSyncForm.syncName,
      localPath: newSyncForm.localPath,
      localSize: '0 MB',
      versionDelete: false
    })
  } else {
    // 处理链接逻辑
    syncList.value.push({
      id: Date.now(),
      syncName: newSyncForm.syncUrl.split('/').pop(),
      syncUrl: newSyncForm.syncUrl,
      status: 'WAIT_CONNECT',
      localSize: '0 MB',
      serverIp: '' // 新增时服务器 IP 为空
    })
  }

  showAddDialog.value = false
  Object.keys(newSyncForm).forEach(k => newSyncForm[k] = '')
}

// 操作处理
const handleSetting = (row) => {
  selectedRow.value = row
  settingForm.localPath = row.localPath
  if (currentMode.value === 'client') {
    settingForm.serverIp = row.serverIp || '' // 初始化服务器 IP
  }
  if (currentMode.value === 'server') {
    settingForm.versionDelete = row.versionDelete || false
  }
  drawerVisible.value = true
}

const handleToggleSync = (row) => {
  row.status = row.status === 'SYNC_STOP' ? 'SYNCING' : 'SYNC_STOP'
  ElMessage.info(`已${row.status === 'SYNC_STOP' ? '暂停' : '恢复'}同步`)
}

const handleShare = async (row) => {
  const link = `https://sync-server.com/share/${row.id}`
  await navigator.clipboard.writeText(link)
  ElMessage.success('链接已复制到剪贴板')
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该同步项？', '警告', {
    confirmButtonText: '删除',
    cancelButtonText: '取消',
    type: 'warning'
  })
  syncList.value = syncList.value.filter(item => item.id !== row.id)
}

const saveSettings = () => {
  selectedRow.value.localPath = settingForm.localPath
  if (currentMode.value === 'client') {
    selectedRow.value.serverIp = settingForm.serverIp // 保存服务器 IP
  }
  if (currentMode.value === 'server') {
    selectedRow.value.versionDelete = settingForm.versionDelete
  }
  drawerVisible.value = false
  ElMessage.success('设置已保存')
}
</script>

<style lang="scss" scoped>
.dark-container {
  background-color: var(--el-bg-color-page);
  color: white;
  min-height: 100vh;
  padding: 20px;

  .main-container {
    max-width: 1400px;
    margin: 0 auto;
  }

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h2 {
      color: white;
    }

    .header-actions {
      display: flex;
      gap: 10px;
    }
  }

  .folder-name {
    display: flex;
    align-items: center;
    cursor: pointer;
    padding: 5px 0;

    .el-icon {
      margin-right: 10px;
      font-size: 18px;
    }
  }

  .rotate-icon {
    animation: rotate 2s linear infinite;
  }

  @keyframes rotate {
    from {
      transform: rotate(0deg);
    }
    to {
      transform: rotate(360deg);
    }
  }
}

.sync-name {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 0;

  .el-icon {
    font-size: 18px;
    color: var(--el-color-primary);
  }
}

.dark-table {
  :deep(.el-table__row) {
    .el-button {
      margin-left: 8px;
    }
  }
}

.mode-gif {
  width: 27px;
  height: 27px;
  vertical-align: middle;
  cursor: pointer;
  transition: transform 0.2s ease;

  &:hover {
    transform: scale(1.1);
  }
}
</style>