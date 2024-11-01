<script setup>
import {onMounted, ref} from 'vue'
import Form from './components/Form.vue'

// 准备 ref 响应式数据
const divRef = ref();
const myComponent = ref();

// 组件挂载后
onMounted(() => {
  //捕获普通的 dom 对象
  console.log(divRef.value);
  divRef.value.style.color = 'red';

  //捕获 Vue 组件对象
  console.log(myComponent.value);
  console.log(myComponent.value.validate);
  console.log(myComponent.value.validate());
  console.log(myComponent.value.ccc);
})
</script>

<template>
  <!-- 给目标元素添加ref属性 -->
  <div ref="divRef">Some Text...</div>
  <Form ref="myComponent"/>
</template>

<style scoped></style>