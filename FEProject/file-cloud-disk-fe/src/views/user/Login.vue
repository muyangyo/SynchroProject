<template>
  <div class="login-container">
    <!-- 新增提示横幅 -->
    <div class="warning-banner" v-if="!bannerClose">
      <div class="banner-content">
        <div class="banner-title">⚠️ 提示</div>
        <div class="banner-text">由于本服务器带宽有限，部分功能受限制，此处展示非完全体</div>
        <div class="banner-text">
          测试账号(仅读权限): <span class="highlight-text">123</span>
          密码: <span class="highlight-text">3ora7wfi53</span>
        </div>
        <div class="banner-section">
          <div class="banner-subtitle">Ps:</div>
          <ul class="banner-list">
            <li><span class="highlight-text">手机与平板</span>浏览建议开启<span class="highlight-text">电脑模式</span>
            </li>
            <li>若需要完整体验，请下载 <span class="highlight-text">文件云盘</span> 文件夹内的压缩包</li>
            <li>若需要更多权限，请联系作者QQ: <span class="highlight-text">3560775787</span></li>
          </ul>
        </div>
        <button class="close-button" @click="bannerClose = true">×</button>
      </div>
    </div>

    <div class="login-box">
      <div style="text-align: center; margin-bottom: 20px; font-size: 36px; font-weight: bold;">
        Login
      </div>
      <el-form :model="form" :rules="rules" ref="loginForm" label-width="80px" @submit.native.prevent="submitForm">
        <el-form-item label="账号" prop="username">
          <el-input v-model="form.username" @change="form.username = form.username.trim()"
                    placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="form.password" @change="form.password = form.password.trim()"
                    placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" native-type="submit">登录</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import {ElMessage} from 'element-plus';
import {useRouter} from 'vue-router';
import {easyRequest, RequestMethods} from "@/utils/RequestTool.js";
import {UserSession, ROLES} from "@/utils/UserLocalStoreUtils.js";
import {rsaEncryptUtil} from "@/utils/RSAEncryptUtils.js";

const bannerClose = ref(false);

const router = useRouter();// 路由

const loginForm = ref(null);// 登录表单

const form = ref({
  username: '',
  password: '',
}); // 表单数据

const rules = {
  username: [{required: true, type: 'string', message: '请输入账号', trigger: 'blur'}],
  password: [{required: true, type: 'string', message: '请输入密码', trigger: 'blur'}],
}; // 表单验证规则

// 登录请求
const submitForm = () => {
  // 表单整体验证
  loginForm.value.validate(async (valid) => {
    if (valid) {
      let keyResponse = await easyRequest(RequestMethods.GET, '/user/getPublicKey', "");
      rsaEncryptUtil.setPublicKey(keyResponse.data); // 保存公钥

      // Rsa 加密
      const RequestData = {
        username: rsaEncryptUtil.encryptData(form.value.username),
        password: rsaEncryptUtil.encryptData(form.value.password),
      };

      try {
        // 使用 easyRequest 发送登录请求,返回响应
        const response = await easyRequest(RequestMethods.POST, '/user/login', RequestData);
        if (response.statusCode === "SUCCESS" && response.data === true) {
          UserSession.login(ROLES.user, form.value.username);
          ElMessage({
            message: `欢迎回来, ${form.value.username}`,
            type: 'success',
          });
          await router.push('/user'); // 跳转到指定页面
        } else {
          ElMessage.error(response.errMsg || '账号或密码错误');
        }
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '登录失败');
      }
    }
  });
};


//重置表单
const resetForm = () => {
  loginForm.value.resetFields();
};

</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  backdrop-filter: blur(10px); /* 添加毛玻璃效果 */
}

.login-box {
  width: 300px;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease-in-out;
}

.login-box:hover {
  transform: scale(1.05);
}

.login-box {
  background-color: rgba(41, 42, 48, 0.36); /* 保持背景颜色 */
}

/*!* 亮色主题样式 *!
@media (prefers-color-scheme: light) {
  .login-box {
    background-color: #fff; !* 保持背景颜色 *!
  }
}*/

/* 暗色主题样式 */
/*@media (prefers-color-scheme: dark) {
  .login-box {
    background-color: rgba(41, 42, 48, 0.36); !* 保持背景颜色 *!
  }
}*/


.warning-banner {
  position: fixed;
  top: 20px;
  left: 20px;
  background: rgba(50, 67, 42, 0.69); /* 暗色模式橙色背景 */
  padding: 16px 20px;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.25);
  z-index: 9999;
  max-width: 320px;
  backdrop-filter: blur(4px);
  border: 1px solid rgba(255, 255, 255, 0.15);
}

.banner-content {
  font-size: 13px;
  line-height: 1.7;
  color: rgba(255, 255, 255, 0.9);
}

.banner-title {
  font-size: 15px;
  font-weight: 600;
  margin-bottom: 12px;
  color: #fff;
}

.banner-text {
  margin-bottom: 8px;
}

.banner-section {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.banner-subtitle {
  font-weight: 500;
  margin-bottom: 8px;
  color: #fff;
}

.banner-list {
  margin: 0;
  padding-left: 18px;
}

.banner-list li {
  margin-bottom: 6px;
}

.highlight-text {
  color: rgba(255, 0, 26, 0.9); /* 强调色 */
  font-weight: 500;
  padding: 0 2px;
}

/* 关闭按钮样式 */
.close-button {
  position: absolute;
  top: 8px;
  right: 8px;
  background: none;
  border: none;
  color: rgba(110, 106, 106, 0.9);
  font-size: 20px;
  cursor: pointer;
  padding: 0;
  line-height: 1;
}

.close-button:hover {
  color: rgba(255, 255, 255, 0.96);
}

/* 响应式调整 */
@media (max-width: 480px) {
  .warning-banner {
    top: 10px;
    left: 10px;
    right: 10px;
    max-width: none;
    width: calc(100% - 20px);
  }
}
</style>