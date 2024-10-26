<template>
  <div>
    <input type="text" v-model="str" placeholder="请输入.."/>
    <br>
    <input type="text" v-model="object.username" placeholder="用户名"/>
  </div>
</template>

<script setup>
import {reactive, ref, watch, watchEffect} from "vue";

let str = ref("");

watch(str, (newValue, oldValue) => {
  console.log(newValue, oldValue);
  console.log("对象的 text 新数据为: " + newValue + " | 对象的 text 旧数据为: " + oldValue);
})

let object = ref({"username": "", age: 10})
watch(object, (newValue, oldValue) => {
  console.log(newValue, oldValue);
  console.log(newValue === oldValue);
})
</script>


<style scoped></style>