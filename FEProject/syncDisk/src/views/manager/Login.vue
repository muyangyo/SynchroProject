<template>
  <div class="login-container">
    <div class="login-box">
      <div style="text-align: center; margin-bottom: 20px; font-size: 36px; font-weight: bold;">
        Login
      </div>
      <el-form :model="form" :rules="rules" ref="loginForm" label-width="80px">
        <el-form-item label="账号" prop="username">
          <el-input v-model="form.username" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="form.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="密钥" prop="secretKey">
          <el-input v-model="form.secretKey" placeholder="请输入密钥（可选）"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm">登录</el-button>
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
import {easyRequest} from "@/utils/RequestTool.js";
import {useKeyStore} from "@/stores/keyStore.js";

const router = useRouter();
const loginForm = ref(null);

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

      const keyStore = useKeyStore();
      keyStore.setPublicKey("04157f76ddfbd1e7afe00077c816062ab7abcd30b7e86669a13c5251c90586838f0638be39f54cad61a2781e84a756707e812afefb03df81d64720d181d27259aa");
      let encryptData = keyStore.encryptData(form.value.password);
      console.log(encryptData);

      ElMessage({
        message: `欢迎回来, ${form.value.username}`,
        type: 'success',
      });
      await router.push('/manager/sync_manager/sync_file_manager');


      /*  // todo: 记得删除
        ElMessage({
          message: `欢迎回来, ${form.value.username}`,
          type: 'success',
        });
        await router.push('/manager/sync_manager/sync_file_manager');*/

      // try {
      //   // 使用 easyRequest 发送登录请求,返回响应
      //   const response = await easyRequest('POST', '/api/login', form.value);
      //
      //   if (response.data.success) {
      //     ElMessage({
      //       message: `欢迎回来, ${form.value.username}`,
      //       type: 'success',
      //     });
      //      router.push('/dashboard'); // 跳转到指定页面
      //   } else {
      //     ElMessage.error(response.data.message || '账号或密码错误');
      //   }
      // } catch (error) {
      //   ElMessage.error(error.response?.data?.message || '登录失败');
      // }
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

/* 亮色主题样式 */
@media (prefers-color-scheme: light) {
  .login-box {
    background-color: #fff; /* 保持背景颜色 */
  }
}

/* 暗色主题样式 */
@media (prefers-color-scheme: dark) {
  .login-box {
    background-color: rgba(41, 42, 48, 0.36); /* 保持背景颜色 */
  }
}
</style>