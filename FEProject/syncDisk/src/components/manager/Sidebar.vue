<template>
  <!-- 侧边栏菜单 -->
  <el-menu default-active="1" class="el-menu-vertical-demo" :collapse="showMenu" @open="handleOpen" @close="handleClose"
           @select="handleSelect">
    <!-- 同步功能菜单项 -->
    <el-menu-item index="1" @click="navigateTo('/sync_manager/sync_file_manager')">
      <el-icon>
        <Refresh/>
      </el-icon>
      <template #title>同步功能</template>
    </el-menu-item>

    <!-- 云盘功能菜单项 -->
    <el-menu-item index="2" @click="navigateTo('/About')">
      <el-icon>
        <MostlyCloudy/>
      </el-icon>

      <template #title>云盘功能</template>
    </el-menu-item>

  </el-menu>

</template>

<script setup>
import {useRouter} from 'vue-router';
import {Refresh, MostlyCloudy} from '@element-plus/icons-vue';
import {config} from "@/CustomizeGlobalConfigurations.js";

const router = useRouter();

// 接收父组件传递的 showMenu 属性
defineProps({
  showMenu: {
    type: Boolean,
    default: false
  }
});


// 导航到指定路径
const navigateTo = (path) => {
  path = config.managerBaseUrl + path;
  router.push(path);
};

//todo: 实现菜单展开、关闭、选择事件(保留)
// 处理菜单展开事件
const handleOpen = (key, keyPath) => {
  console.log(`菜单展开: key=${key}, keyPath=${keyPath}`);
};

// 处理菜单关闭事件
const handleClose = (key, keyPath) => {
  console.log(`菜单关闭: key=${key}, keyPath=${keyPath}`);
};

// 处理菜单选择事件
const handleSelect = (index, indexPath) => {
  console.log(`菜单选择: index=${index}, indexPath=${indexPath}`);
};

</script>

<style scoped>
/* 侧边栏菜单样式 */
.el-menu-vertical-demo {
  height: 100%; /* 设置侧边栏全高 */
}

/* 未折叠时的侧边栏宽度 */
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
}
</style>