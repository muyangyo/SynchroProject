<template>
  <div>
    账号:
    <input
        type="text"
        v-model="loginForm.username"/><br/><br/>
    密码:
    <input
        type="text"
        v-model="loginForm.password"/><br/><br/>
  </div>
</template>

<script setup>
import {ref} from 'vue'
// 登录表单
const loginForm = ref({
  username: '',
  password: ''
})
</script>

<style scoped>
</style>