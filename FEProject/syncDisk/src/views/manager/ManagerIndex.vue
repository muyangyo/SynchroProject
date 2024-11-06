<template>
  <!-- 固定首行 -->
  <el-row type="flex" justify="space-between" align="middle" class="header-row">
    <el-col :span="12">
      <div class="header-content">
        <el-tooltip :content="showMenu ? '展开菜单' : '折叠菜单'" placement="bottom">
          <el-icon size="23px" class="icon" @click="toggleMenu()">
            <Menu/>
          </el-icon>
        </el-tooltip>
        <h1 class="title">SyncDisk</h1>
      </div>
    </el-col>
    <el-col :span="12">
      <h5 class="designer-title" @click="DesignerUrl()">Designed by MuYangYo</h5>
    </el-col>
  </el-row>

  <el-row :gutter="20" class="main-row">
    <!-- 侧边栏 -->
    <sidebar :show-menu="showMenu"/>
    <!-- 内容区域 -->
    <div class="content-area">
      <router-view></router-view>
    </div>
  </el-row>
</template>

<script setup>
import {ref} from 'vue';
import Sidebar from '@/components/manager/Sidebar.vue';
import {Menu} from '@element-plus/icons-vue';

// 点击图标折叠或者展开菜单
const showMenu = ref(false);

const toggleMenu = () => {
  console.log("菜单" + (showMenu.value === false ? "折叠" : "展开"));
  showMenu.value = !showMenu.value;
};

const DesignerUrl = () => {
  window.open("https://github.com/muyangyo");
}


</script>

<style lang="scss" scoped>
.header-row {
  padding: 20px;
  background-color: #323333;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  max-height: 100px; /* 设置最大高度 */
}

.header-content {
  display: flex;
  align-items: center;
}

.icon {
  margin-right: 10px;
  cursor: pointer;
  transition: box-shadow 0.3s ease;
}

.icon:hover {
  box-shadow: 0 0 10px rgba(255, 255, 255, 0.8);
  border-radius: 50%;
}

.title {
  font-size: 1.5rem;
  font-weight: bold;
  background: linear-gradient(135deg, #47fffc, #ffa500, #a3e2e9);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.designer-title {
  font-size: 1rem;
  font-weight: normal;
  color: #fff;
  text-align: center;
  margin: 0;
  padding: 5px 10px;
  border-radius: 4px;
  background: linear-gradient(135deg, #47fffc, #ffa500, #a3e2e9);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: background 0.3s ease, box-shadow 0.3s ease;
}

.designer-title:hover {
  background: linear-gradient(135deg, #ffa500, #47fffc, #a3e2e9);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  cursor: pointer;
}

.main-row {
  height: calc(100vh - 100px); /* 减去头部的高度 */
}
</style>