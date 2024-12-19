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
        <h1 class="title">SyncCloud</h1>
        <div style="width: 20px"></div>
        <el-avatar class="avatarStyle" v-if="isLoggedIn" @click="confirmLogout()">{{
            userName.charAt(0).toUpperCase()
          }}
        </el-avatar>
        <el-avatar class="avatarStyle" v-else>U</el-avatar>
      </div>
    </el-col>

    <el-col :span="12">
      <h5 class="designer-title" @click="DesignerUrl()">Designed by MuYangYo</h5>
    </el-col>
  </el-row>

  <el-row :gutter="20" class="main-row">

    <!-- 侧边栏菜单 -->
    <el-menu default-active="2-2" class="el-menu-vertical-demo" :collapse="showMenu" @open="handleOpen"
             @close="handleClose" @select="handleSelect">
      <!-- 同步功能菜单项 -->
<!--      <el-sub-menu index="1">
        &lt;!&ndash; 一级菜单 &ndash;&gt;
        <template #title>
          <el-icon>
            <Refresh/>
          </el-icon>
          <span>同步</span>
        </template>
        &lt;!&ndash; 二级菜单 &ndash;&gt;
        <el-menu-item index="1-1" @click="navigateTo('/manager/sync_manager/sync_file_manager')">
          <template #default>
            <el-icon>
              <Folder/>
            </el-icon>
            同步文件
          </template>
        </el-menu-item>

        <el-menu-item index="1-2" @click="navigateTo('/manager/sync_manager/sync_setting_manager')">
          <template #default>
            <el-icon>
              <Setting/>
            </el-icon>
            同步设置
          </template>
        </el-menu-item>
      </el-sub-menu>-->

      <!-- 云盘功能菜单项 -->
      <el-sub-menu index="2">
        <!-- 一级菜单 -->
        <template #title>
          <el-icon>
            <MostlyCloudy/>
          </el-icon>
          <span>云盘</span>
        </template>

        <!-- 二级菜单 -->
        <el-menu-item index="2-1" @click="navigateTo('/manager/cloud_manager/cloud_log_manager')">
          <template #default>
            <el-icon>
              <List/>
            </el-icon>
            操作日志
          </template>
        </el-menu-item>
        <el-menu-item index="2-2" @click="navigateTo('/manager/cloud_manager/cloud_setting_manager')">
          <template #default>
            <el-icon>
              <Setting/>
            </el-icon>
            云盘设置
          </template>
        </el-menu-item>
      </el-sub-menu>
    </el-menu>

    <!-- 内容区域 -->
    <div class="content-area">
      <router-view></router-view>
    </div>

  </el-row>
</template>

<script setup>
import {markRaw, ref} from 'vue';
import {useRouter} from 'vue-router';
import {ElMessage, ElMessageBox} from 'element-plus';
import {InfoFilled} from "@element-plus/icons-vue";
import {deleteCookie, tokenName} from "@/router/RouterSetting.js";
import {UserSession} from "@/utils/UserLocalStoreUtils.js";
import {easyRequest, RequestMethods} from "@/utils/RequestTool.js";


const router = useRouter();

// 点击图标折叠或者展开菜单
const showMenu = ref(true);

const toggleMenu = () => {
  console.log("菜单" + (showMenu.value === false ? "折叠" : "展开"));
  showMenu.value = !showMenu.value;
};

// 用户登录状态
const isLoggedIn = UserSession.isLoggedIn();
const userName = UserSession.getUserName();

const confirmLogout = () => {
  ElMessageBox.confirm(
      '确定要退出登录吗？',
      '退出确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error',
        draggable: true,
        icon: markRaw(InfoFilled),
      }
  ).then(() => {
    logout();
  }).catch(() => {
  });
};

const logout = () => {
  UserSession.logout();
  deleteCookie(tokenName);

  easyRequest(RequestMethods.POST, "/admin/logout", "", false, false).then(
      (response) => {
        if (response.statusCode === "SUCCESS") {
          ElMessage.success("退出成功");
        }
      }
  );

  router.push("/manager/login");
};

const DesignerUrl = () => {
  window.open("https://github.com/muyangyo");
};

// 导航到指定路径
const navigateTo = (path) => {
  router.push(path);
};

//todo: 实现菜单展开、关闭、选择事件(保留)
// 处理菜单展开事件
const handleOpen = (key, keyPath) => {
  // console.log(`菜单展开: key=${key}, keyPath=${keyPath}`);
};

// 处理菜单关闭事件
const handleClose = (key, keyPath) => {
  // console.log(`菜单关闭: key=${key}, keyPath=${keyPath}`);
};

// 处理菜单选择事件
const handleSelect = (index, indexPath) => {
  // console.log(`菜单选择: index=${index}, indexPath=${indexPath}`);
};
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
  background: linear-gradient(135deg, #ffa500, #47fffc, #56cb7f);
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
  background: linear-gradient(135deg, #ffa500, #47fffc, #60a5d8);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: background 0.3s ease, box-shadow 0.3s ease;
}

.designer-title:hover {
  background: linear-gradient(135deg, #00ffe7, #47fffc, #d86060);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  cursor: pointer;
}

.main-row {
  height: calc(100vh - 100px); /* 减去头部的高度 */
}

/* 侧边栏菜单样式 */
.el-menu-vertical-demo {
  height: 100%; /* 设置侧边栏全高 */
}

/* 未折叠时的侧边栏宽度 */
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
}

.content-area {
  flex: 1; /* 使内容区域占据剩余空间 */
  overflow-y: auto; /* 添加滚动条 */
}

.avatarStyle {
  background: linear-gradient(135deg, #a3f4f2, #ff0000); /* 渐变色背景 */
  -webkit-background-clip: text; /* 确保文本能够显示渐变效果 */
  color: transparent; /* 设置文本颜色为透明 */
  font-weight: bold;
  font-size: 1.2rem;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background 0.3s ease, box-shadow 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border: 2px solid #47fffc;
}
</style>