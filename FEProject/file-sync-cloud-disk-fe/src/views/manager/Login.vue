<template>
  <div class="login-container">
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
        <el-form-item label="密钥" prop="secretKey">
          <el-input v-model="form.secretKey" @change="form.secretKey = form.secretKey.trim()"
                    placeholder="请输入密钥（可选）"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" native-type="submit" :disabled="isDisabled">登录</el-button>
          <el-button @click="resetForm" :disabled="isDisabled">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import {ElMessage} from 'element-plus';
import {useRouter} from 'vue-router';
import {easyRequest, RequestMethods} from "@/utils/RequestTool.js";
import {config, globalVariable} from "@/GlobalConfig.js";
import isLocalhost from "@/utils/IsLocalHost.js";
import {UserSession, ROLES} from "@/utils/UserLocalStoreUtils.js";
import {rsaEncryptUtil} from "@/utils/RSAEncryptUtils.js";

const router = useRouter(); // 路由
const loginForm = ref(null); // 登录表单


const form = ref({
  username: '',
  password: '',
  secretKey: '',
});

const rules = {
  username: [{required: true, type: 'string', message: '请输入账号', trigger: 'blur'}],
  password: [{required: true, type: 'string', message: '请输入密码', trigger: 'blur'}],
  secretKey: [{required: false, type: 'string'}],
};

const submitForm = () => {
  // 表单整体验证
  loginForm.value.validate(async (valid) => {
    if (valid) {
      let keyResponse = await easyRequest(RequestMethods.GET, '/admin/getPublicKey', "");
      rsaEncryptUtil.setPublicKey(keyResponse.data); // 保存公钥

      // Rsa 加密
      const RequestData = {
        username: rsaEncryptUtil.encryptData(form.value.username),
        password: rsaEncryptUtil.encryptData(form.value.password),
        key: form.value.secretKey ? rsaEncryptUtil.encryptData(form.value.secretKey) : "",
      };

      try {
        // 使用 easyRequest 发送登录请求,返回响应
        const response = await easyRequest(RequestMethods.POST, '/admin/login', RequestData);
        if (response.data === true && response.statusCode === "SUCCESS") {
          UserSession.login(ROLES.admin, form.value.username, "");
          ElMessage({
            message: `欢迎回来, ${form.value.username}`,
            type: 'success',
          });
          await router.push(config.managerRouterBaseUrl); // 跳转到指定页面
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

const isDisabled = ref(false); // 登录按钮是否禁用
onMounted(() => {
  easyRequest(RequestMethods.GET, '/admin/remoteOperationIsOpen', "").then(
      (response) => {

        if (response.data === false) { // 表示限制了只能在本地访问
          localStorage.setItem(globalVariable.ADMIN_OPERATION_IS_OPEN, "false");
          if (!isLocalhost()) { // 不是本地访问则提示
            isDisabled.value = true; // 禁用按钮
            ElMessage.error({
              message: '管理操作只能在 <b>127.0.0.1</b> 访问!<br>如果需要远程操作,请设置允许远程访问!',
              dangerouslyUseHTMLString: true,
              duration: 5000,
              center: true,
            });
          }
        } else {
          localStorage.setItem(globalVariable.ADMIN_OPERATION_IS_OPEN, "true");
        }

      }
  )
});
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
</style>