<template>
  <div class="dark-container">
    <!-- 模式切换确认对话框 -->
    <el-dialog
        v-model="showModeDialog"
        :title="`切换到${switchToMode === 'server' ? '服务端' : '客户端'}模式`"
        width="30%">
      <span style="color: white;font-weight: bold">⚠️警告: 切换模式将清空当前所有同步设置和操作日志，是否继续？</span>
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
            <el-tooltip :content="`最近同步时间: ${row.lastSyncTime}`" placement="right">
              <div class="sync-name">
                <el-icon>
                  <Folder/>
                </el-icon>
                <span>{{ row.syncName }}</span>
              </div>
            </el-tooltip>
          </template>
        </el-table-column>

        <!-- 服务端专属列 -->
        <el-table-column v-if="currentMode === 'server'" label="本地大小" max-width="100" align="center">
          <template #default="{ row }">{{ sizeTostr(row.localSize) }}</template>
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
          <template #default="{ row }">{{ sizeTostr(row.localSize) }}</template>
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
      <el-dialog v-model="showAddDialog" :title="currentMode === 'server' ? '新建同步' : '添加同步链接'"
                 :draggable="true">
        <el-form :model="newSyncForm" label-width="120px" :rules="formRules">
          <!-- 服务端模式表单项 -->
          <el-form-item v-if="currentMode === 'server'" label="同步名称" prop="syncName">
            <el-input v-model="newSyncForm.syncName" placeholder="目前只支持英文、数字构成的同步名称"/>
          </el-form-item>

          <el-form-item v-if="currentMode === 'server'" label="本地路径" prop="localPath">
            <el-input v-model="newSyncForm.localPath" placeholder="绝对路径，如：C:/sync_folder"/>
          </el-form-item>

          <el-form-item v-if="currentMode === 'client'" label="同步Key" prop="key">
            <el-input v-model="newSyncForm.key" placeholder="输入服务端提供的分享链接"/>
          </el-form-item>

          <el-form-item v-if="currentMode === 'client'" label="本地路径" prop="localPath">
            <el-input v-model="newSyncForm.localPath" placeholder="绝对路径，如：C:/sync_folder"/>
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
            <el-input v-model="selectedRow.syncName" disabled/>
          </el-form-item>

          <el-form-item label="本地路径">
            <el-input v-model="settingForm.localPath"/>
          </el-form-item>

          <!-- 客户端模式下显示服务器 IP -->
          <el-form-item v-if="currentMode === 'client'" label="服务器 IP">
            <el-input v-model="settingForm.serverIp" placeholder="请输入服务器 IP 地址"/>
          </el-form-item>

          <el-form-item v-if="currentMode === 'server'" label="版本删除">
            <el-switch v-model="settingForm.versionDelete" disabled/>
          </el-form-item>

          <el-form-item label="本地大小">
            <el-input :value=" sizeTostr(selectedRow.localSize) " disabled/>
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
import {ref, reactive, onMounted, onUnmounted} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Folder} from '@element-plus/icons-vue'
import {sizeTostr} from "@/utils/FileSizeConverter.js";
import JSEncrypt from 'jsencrypt';
import useClipboard from 'vue-clipboard3'

const {toClipboard} = useClipboard();
// 引入 GIF 图片
import serverGif from '@/assets/server.gif'
import clientGif from '@/assets/client.gif'
import {easyRequest, RequestMethods} from "@/utils/RequestTool.js";
import {rsaEncryptUtil} from "@/utils/RSAEncryptUtils.js";
// 定义 GIF 点击跳转逻辑
const handleGifClick = () => {
  window.open("https://icons8.com/", '_blank')
}

// 响应式数据
const currentMode = ref('')
const switchToMode = ref('')
const showModeDialog = ref(false)
const syncList = ref([])
const showAddDialog = ref(false)
const drawerVisible = ref(false)
const selectedRow = ref(null)

// 状态映射
const statusMap = {
  WAIT_CONNECT: '等待连接',
  CONNECTING: '连接中',
  SYNCING: '同步中',
  SYNC_COMPLETE: '同步完成',
  SYNC_FAILED: '同步失败',
  SYNC_STOP: '已暂停'
}


// 初始化加载
onMounted(async () => {
  await getMode();
  await loadSyncList();
})

// 组件卸载时清除定时器
onUnmounted(() => {
  stopPolling();
})
// 模式切换处理
const handleSwitchMode = () => {
  switchToMode.value = currentMode.value === 'server' ? 'client' : 'server'
  showModeDialog.value = true
}

// 获取用户模式
const getMode = () => {
  easyRequest(RequestMethods.GET, '/syncManager/getStatus', null, false).then(response => {
    if (response.statusCode === "SUCCESS") {
      currentMode.value = response.data;
    } else {
      ElMessage.error('获取用户模式失败');
    }
  });
}

// 确认模式切换
const confirmSwitchMode = async () => {
  try {
    easyRequest(RequestMethods.POST, `/syncManager/changeStatus?changeTo=${switchToMode.value}`, null, false, false).then(response => {
      if (response.statusCode === "SUCCESS") {
        // 更新模式
        currentMode.value = switchToMode.value;
        loadSyncList();// 刷新同步列表
        showModeDialog.value = false // 关闭模式切换确认对话框
        ElMessage.success(response.data ? response.data : '模式切换成功') // 提示成功
      } else {
        ElMessage.error('模式切换失败')
      }
    });
  } catch (error) {
    ElMessage.error('模式切换失败')
  }
}

// 加载同步列表
const loadSyncList = async () => {
  easyRequest(RequestMethods.GET, '/syncManager/getSyncInfoList', null, false).then(response => {
    if (response.statusCode === "SUCCESS") {
      stopPolling(); // 停止轮询
      syncList.value = [];// 先清空同步列表
      syncList.value = response.data;
      startPolling(); // 启动轮询
    } else {
      ElMessage.error('加载同步列表失败')
    }
  });
}

// 新增同步表单数据
const newSyncForm = reactive({
  syncName: '', // 只有服务端模式才有同步名称
  localPath: '', // 通用
  key: '' // 只有客户端模式才有同步链接
})

// 表单验证规则
const formRules = {
  syncName: [
    {required: true, message: '请输入同步名称'},
    {
      pattern: /^[a-zA-Z0-9]{3,10}$/,
      message: '同步名称只能为英文或数字，长度限制为3-10个字符'
    }
  ],
  localPath: [
    {
      required: true,
      message: '请输入有效的绝对路径，支持中文路径',
      validator: (rule, value, callback) => {
        // 通用路径格式验证（支持Windows和Unix-like系统）
        const isValid = /^(?:[a-zA-Z]:[\\/]|\\\\|[/])/.test(value)
        isValid ? callback() : callback(new Error(rule.message))
      }
    }
  ],
  key: [
    {required: true, message: '请输入同步链接'},
  ]
};

// 新增同步
const confirmAddSync = () => {

  if (currentMode.value === 'server') {
    let data = {syncName: newSyncForm.syncName, localPath: newSyncForm.localPath};
    easyRequest(RequestMethods.POST, "/syncManager/addSyncInfo", data, true, true).then(
        response => {
          if (response.statusCode === "SUCCESS") {
            ElMessage.success(response.data ? response.data : '新增同步成功');
            loadSyncList(); // 刷新同步列表
          } else {
            ElMessage.error(response.errMsg ? response.errMsg : '新增同步失败');
          }
          Object.keys(newSyncForm).forEach(k => newSyncForm[k] = '') // 清空表单数据
        }
    )
  } else {

    easyRequest(RequestMethods.GET, "/syncManager/getPublicKey", null, false).then(
        response => {
          if (response.statusCode === "SUCCESS" && response.data) {
            rsaEncryptUtil.setPublicKey(response.data); // 保存公钥
            // Rsa 加密
            let encryptedData = {
              key: rsaEncryptUtil.encryptData(newSyncForm.key),
              localPath: rsaEncryptUtil.encryptData(newSyncForm.localPath)
            }

            // 发送请求
            easyRequest(RequestMethods.POST, "/syncManager/addSyncInfo", encryptedData, true, true).then(
                response => {
                  if (response.statusCode === "SUCCESS") {
                    ElMessage.success(response.data ? response.data : '新增同步成功');
                    loadSyncList(); // 刷新同步列表
                  } else {
                    ElMessage.error(response.errMsg ? response.errMsg : '新增同步失败');
                  }
                }
            )

          } else {
            ElMessage.error('获取公钥失败');
          }
          Object.keys(newSyncForm).forEach(k => newSyncForm[k] = '') // 清空表单数据
        }
    )
  }

  showAddDialog.value = false // 关闭新增对话框
}


const settingForm = reactive({
  localPath: '',
  serverIp: '', // 新增服务器 IP 字段(客户端模式)
  versionDelete: false
})

// 点击设置按钮
const handleSetting = (row) => {
  selectedRow.value = row
  settingForm.localPath = row.localPath

  if (currentMode.value === 'client') {
    settingForm.serverIp = row.serverIp || '' // 初始化服务器 IP
  }
  if (currentMode.value === 'server') {
    settingForm.versionDelete = row.versionDelete || false // 初始化版本删除
  }
  drawerVisible.value = true
}


// 点击设置里的保存按钮
const saveSettings = () => {
  selectedRow.value.localPath = settingForm.localPath
  if (currentMode.value === 'client') {
    selectedRow.value.serverIp = settingForm.serverIp // 保存服务器 IP
  }
  if (currentMode.value === 'server') {
    selectedRow.value.versionDelete = settingForm.versionDelete
  }
  drawerVisible.value = false
  if (currentMode.value === 'server') {
    let data = {
      syncName: selectedRow.value.syncName,
      localPath: selectedRow.value.localPath,
      versionDelete: selectedRow.value.versionDelete
    }
    easyRequest(RequestMethods.POST, "/syncManager/updateSyncInfo", data, true, true).then(
        response => {
          if (response.statusCode === "SUCCESS") {
            ElMessage.success(response.data ? response.data : '同步设置已保存');
          } else {
            ElMessage.error(response.errMsg ? response.errMsg : '同步设置保存失败');
          }
          loadSyncList(); // 刷新同步列表
        }
    )
  } else {
    let data = {
      syncName: selectedRow.value.syncName,
      localPath: selectedRow.value.localPath,
      serverIp: selectedRow.value.serverIp
    }
    console.warn(data);

    easyRequest(RequestMethods.POST, "/syncManager/updateSyncInfo", data, true, true).then(
        response => {
          if (response.statusCode === "SUCCESS") {
            ElMessage.success(response.data ? response.data : '同步设置已保存');
          } else {
            ElMessage.error(response.errMsg ? response.errMsg : '同步设置保存失败');
          }
          loadSyncList(); // 刷新同步列表
        }
    )
  }
}

const handleShare = async (row) => {
  const encryptor = new JSEncrypt({default_key_size: '2048'}); // 生成 2048 位的密钥对

  // 生成公钥和私钥
  const publicKey = encryptor.getPublicKeyB64();

  let data = {
    syncName: row.syncName,
    key: publicKey,
  }

  easyRequest(RequestMethods.POST, `/syncManager/getSyncShareLink`, data, true, true)
      .then(async response => {
        if (response.statusCode === "SUCCESS" && response.data) {
          let link = encryptor.decrypt(response.data);
          await toClipboard(link);
          ElMessage.success('链接已复制到剪贴板');
        } else {
          ElMessage.error('获取分享链接失败');
        }
      });
}

const handleDelete = async (row) => {
  ElMessageBox.confirm('确定删除该同步项？', '警告', {
    confirmButtonText: '删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    easyRequest(RequestMethods.DELETE, `/syncManager/deleteSyncInfo?syncName=${row.syncName}`, null, true, true).then(
        response => {
          if (response.statusCode === "SUCCESS") {
            ElMessage.success(response.data ? response.data : '删除同步成功');
            loadSyncList(); // 刷新同步列表
          } else {
            ElMessage.error(response.errMsg ? response.errMsg : '删除同步失败');
          }
        })
  }).catch(() => {
  });
}

// 暂停按钮
const handleToggleSync = async (row) => {
  if (currentMode.value !== 'client') return

  const newStatus = row.status === 'SYNC_STOP' ? 'SYNCING' : 'SYNC_STOP'


  const response = await easyRequest(
      RequestMethods.POST,
      `/syncManager/updateSyncInfo`,
      {
        newStatus: newStatus,
        syncName: row.syncName
      },
      true,
      true,
  )

  if (response.statusCode === "SUCCESS") {
    row.status = newStatus
    ElMessage.success(`已${newStatus === 'SYNC_STOP' ? '暂停' : '恢复'}同步`)
    await fetchSyncStatus() // 立刻刷新同步状态
  }
}

//todo: 下面等待验证

// 轮询间隔（5秒）
const POLL_INTERVAL = 2000;
let pollTimer = null;

// 启动轮询
const startPolling = () => {
  if (currentMode.value === 'client' && syncList.value.length > 0) {
    pollTimer = setInterval(fetchSyncStatus, POLL_INTERVAL)
  }
}

// 停止轮询
const stopPolling = () => {
  if (pollTimer) {
    clearInterval(pollTimer)
    pollTimer = null
  }
}

// 获取同步状态
const fetchSyncStatus = async () => {
  if (currentMode.value !== 'client' || syncList.value.length === 0) return

  try {
    const syncNames = syncList.value.map(item => item.syncName)
    const response = await easyRequest(
        RequestMethods.GET,
        `/syncManager/getSyncStatusClientOnly?syncNames=${syncNames.join(',')}`,
        null,
        false
    )

    if (response.statusCode === "SUCCESS") {
      updateSyncStatus(response.data)
    }
  } catch (error) {
    console.error('状态更新失败', error)
  }
}
// 更新同步状态
const updateSyncStatus = (statusList) => {
  statusList.forEach(({syncName, syncStatus}) => {
    const target = syncList.value.find(item => item.syncName === syncName)
    if (target) target.status = syncStatus
  })
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