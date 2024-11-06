<template>
  <div>
    <h1> hello world</h1>
    <el-button type="primary">Primary</el-button>
    <div class="flex flex-wrap gap-4 items-center">
      <el-select v-model="value" placeholder="Select" size="large" style="width: 240px">
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"/>
      </el-select>
    </div>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import {useDark, useToggle} from '@vueuse/core'

// 定义一个响应式变量用于存储选择的值
const value = ref(null);

// 定义一个响应式数组用于存储选项
const options = ref([
  {label: '选项1', value: 'option1'},
  {label: '选项2', value: 'option2'},
  // 添加更多选项
]);
</script>

<style scoped>

</style>
