<template>
  <div class="not-found">
    <div class="content">
      <h1 class="title">404</h1>
      <p class="subtitle">Page Not Found</p>
      <p class="description">该页面不存在，请返回上一页</p>
      <el-button type="primary" @click="goBack">返回</el-button>
    </div>
  </div>
</template>

<script setup>
import {useRouter} from 'vue-router';

const router = useRouter();

const goBack = () => {
  router.back();
};
</script>

<style scoped>
.not-found {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  color: #333;
  font-family: 'Arial', sans-serif;
}

.content {
  text-align: center;
  animation: fadeIn 1s ease-in-out;
}

.title {
  font-size: 8rem;
  font-weight: bold;
  margin-bottom: 20px;
  color: #ff4747;
  animation: bounce 1s ease-in-out;
}

.subtitle {
  font-size: 2rem;
  margin-bottom: 10px;
  color: #555;
}

.description {
  font-size: 1.2rem;
  margin-bottom: 30px;
  color: #777;
}

.el-button {
  font-size: 1rem;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-30px);
  }
  60% {
    transform: translateY(-15px);
  }
}
</style>