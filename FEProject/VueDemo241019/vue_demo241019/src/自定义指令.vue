<!--suppress ALL -->
<template>
  <div>
    <!-- 使用自定义指令，并绑定表达式 -->
    <div v-color="colorStr">Some Text</div>
  </div>
</template>

<script setup>

import {ref} from 'vue';
// 提供的颜色响应式数据
const colorStr = ref("red");

</script>

<style scoped>

</style>