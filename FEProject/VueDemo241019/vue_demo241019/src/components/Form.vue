<script setup>
// 表单组件提供的校验方法
const validate = () => {
  return Math.random() > 0.5;//这里是演示
}

// 需要对外暴漏，类似与导出
defineExpose({
  ccc: 100,
  validate
})
</script>

<template>
  <div class="form-box">
    账号：<input type="text"/><br>
    密码：<input type="password"/>
  </div>
</template>
<style scoped></style>