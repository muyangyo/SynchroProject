<template>
  <div>
    <p> {{ count1 }}</p>
    <button @click="add(1)">count1 + 1</button>
    <p> {{ count2 }}</p>
    <button @click="add(2)">count2 + 1</button>
    <p> {{ count3 }}</p>
    <button @click="add(3)">count3 + 1</button>
    <p>sum = {{ sum }}</p>
  </div>
</template>

<script setup>
import {computed, ref} from "vue";

let count1 = ref(1);
let count2 = ref(1);
let count3 = ref(1);
let add = (n) => {
  if (n === 1) count1.value++;
  else if (n === 2) count2.value++;
  else count3.value++;
}
// let sum = ref(count1 + count2 + count3); //无法实现,count是一个ref对象,无法直接相加,
// let sum = ref(count1.value + count2.value + count3.value) //没有效果,ref只能监听单个变量
let sum = computed(() => {
  return count1.value + count2.value + count3.value
})
</script>

<style scoped>
</style>