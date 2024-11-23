<template>
  <el-table :data="tableData" style="width: 100%">

    <!-- 第一列 -->
    <el-table-column label="名称" width="180">
      <template #default="data">
        <div style="display: flex; align-items: center; cursor: default;" @click="handleRowClick(data.row)">
          <el-icon>
            <Folder/>
          </el-icon>
          <span style="margin-left: 10px">{{ data.row.syncName }}</span>
        </div>
      </template>
    </el-table-column>

    <!-- 第二列 -->
    <el-table-column label="状态" width="180">
      <template #default="data">
        <div style="display: flex; align-items: center">
          <el-tooltip
              :content="data.row.status === 'syncing' ? '同步中...' : '上次同步的时间：'+data.row.theRecentSyncTime "
              placement="right">
            <el-icon>
              <component :is="data.row.status === 'syncing' ? 'Refresh' : 'Check'"/>
            </el-icon>
          </el-tooltip>
        </div>
      </template>
    </el-table-column>

    <!-- 第三列 -->
    <el-table-column label="本地大小" width="180" align="center">
      <template #default="data">
        <div style="display: flex; align-items: center; justify-content: center;">
          <span>{{ data.row.localSize }}</span>
        </div>
      </template>
    </el-table-column>

    <!-- 第四列 -->
    <el-table-column label="全局大小" width="180" align="center">
      <template #default="data">
        <div style="display: flex; align-items: center; justify-content: center;">
          <span>{{ data.row.globalSize }}</span>
        </div>
      </template>
    </el-table-column>

    <!-- 第五列 -->
    <el-table-column label="操作">
      <template #default="data">
        <el-tooltip content="设置" placement="top" :show-arrow="false">
          <el-button size="small" type="info" icon="Setting" circle
                     @click="handleSettingBtn(data.$index, data.row)"/>
        </el-tooltip>
        <el-tooltip content="分享" placement="top" :show-arrow="false">
          <el-button size="small" type="success" icon="Share" circle @click="handleShareBtn(data.$index, data.row)"/>
        </el-tooltip>
        <el-tooltip content="暂停" placement="top" :show-arrow="false">
          <el-button size="small" type="warning" icon="VideoPause" circle
                     @click="handleStopBtn(data.$index, data.row)"/>
        </el-tooltip>
        <el-tooltip content="删除" placement="top" :show-arrow="false">
          <el-button size="small" type="danger" icon="Delete" circle @click="handleDeleteBtn(data.$index, data.row)"/>
        </el-tooltip>
      </template>
    </el-table-column>
  </el-table>

  <!-- 抽屉组件 -->
  <el-drawer v-model="drawerVisible" direction="rtl" size="50%" :show-close="false">
    <!-- 抽屉头 -->
    <template #header>
      <div class="drawer-header">
        <h4 style="margin-right:5px ">{{ "设置" }} </h4>
        <h4 style="">{{ "-" }} </h4>
        <h3 style="margin-left:5px ">{{ selectedRow ? `${selectedRow.syncName}` : '设置' }}</h3>
      </div>
    </template>
    <!-- 抽屉内容 -->
    <template #default>
      <div v-if="selectedRow" class="drawer-body">
        <!-- 根据 syncName 动态生成抽屉内容 -->
        <el-form :model="formData" label-width="80px">
          <el-form-item label="名称">
            <el-input v-model="formData.syncName" disabled></el-input>
          </el-form-item>
          <el-form-item label="状态">
            <el-input v-model="formData.status" disabled></el-input>
          </el-form-item>
          <el-form-item label="本地大小">
            <el-input v-model="formData.localSize" disabled></el-input>
          </el-form-item>
          <el-form-item label="全局大小">
            <el-input v-model="formData.globalSize" disabled></el-input>
          </el-form-item>
          <el-form-item label="同步路径">
            <el-input v-model="formData.syncPath" disabled></el-input>
          </el-form-item>
        </el-form>
      </div>
    </template>
    <!-- 抽屉页脚 -->
    <template #footer>
      <div class="drawer-footer">
        <el-button type="primary" @click="handleSave">保存</el-button>
        <el-button type="default" @click="handleCancel">取消</el-button>
      </div>
    </template>
  </el-drawer>
</template>

<script setup>
import {ref, reactive} from 'vue';
import {easyRequest} from "@/utils/RequestTool.js";

// 点击表格行触发
const handleRowClick = async (row) => {
  console.log(row.syncPath); // todo: 记得实现打开文件夹
  /* try {
     const response = await easyRequest('GET', `/api/openFolder?path=${encodeURIComponent(row.syncPath)}`, null);
     if (response.status === 200) {
       console.log('Folder opened successfully');
     } else {
       console.error('Failed to open folder');
     }
   } catch (error) {
     console.error('Error:', error);
   }*/
}

// 伪表格数据
const tableData = [
  {
    id: 1,
    syncName: '资料',
    status: 'finish',
    globalSize: '10000 MB',
    localSize: '10000 MB',
    theRecentSyncTime: '2022-01-01 12:00:00',
    syncPath: 'C:/Users/admin/Documents/资料'
  },
  {
    id: 2,
    syncName: '代码',
    status: 'syncing',
    globalSize: '100 MB',
    localSize: '100 MB',
    theRecentSyncTime: '2022-01-01 12:01:00',
    syncPath: 'C:/Users/admin/Documents/代码'
  }
];

// 抽屉显示状态
const drawerVisible = ref(false);
// 当前选中的行
const selectedRow = ref(null);
// 表单数据
const formData = reactive({
  syncName: '',
  status: '',
  localSize: '',
  globalSize: '',
  syncPath: ''
});

// 处理设置按钮点击
const handleSettingBtn = (index, row) => {
  console.log('Button down:', index, row);
  selectedRow.value = row;
  // 将选中的行数据赋值给表单数据
  Object.assign(formData, row);
  drawerVisible.value = true;
}

// 处理分享按钮点击
const handleShareBtn = (index, row) => {
  console.log('Share button clicked:', index, row);
}

// 处理暂停按钮点击
const handleStopBtn = (index, row) => {
  console.log('Stop button clicked:', index, row);
}

// 处理删除按钮点击
const handleDeleteBtn = (index, row) => {
  console.log('Delete button clicked:', index, row);
}

// 处理保存按钮点击
const handleSave = () => {
  console.log('Save button clicked:', formData);
  // 这里可以添加保存表单数据的逻辑
  drawerVisible.value = false;
}

// 处理取消按钮点击
const handleCancel = () => {
  console.log('Cancel button clicked');
  drawerVisible.value = false;
}
</script>

<style scoped>
/* 对齐列头 */
.el-table th.el-table__cell > .cell {
  text-align: center;
}

/* 对齐单元格内容 */
.el-table .el-table__cell {
  text-align: center;
}

/* 第三列和第四列的单元格内容居中对齐 */
.el-table .el-table__cell:nth-child(3),
.el-table .el-table__cell:nth-child(4) {
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 抽屉头部样式 */
.drawer-header {
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid #a3a3a3; /* 分隔线 */
  padding-bottom: 10px;
}

/* 抽屉内容样式 */
.drawer-body {
  padding: 20px;
}

/* 抽屉页脚样式 */
.drawer-footer {
  display: flex;
  justify-content: left;
  padding-top: 10px;
  border-top: 1px solid #a3a3a3; /* 分隔线 */
}
</style>