<template>
  <div>
    <p> {{ count }}</p>
    <!-- 1. 内联/行内代码(一句话的时候)-->
    <button v-on:click="count++">+1</button>
    <!-- 2. 调用无参函数 -->
    <button v-on:click="increase()">+1</button>
    <!-- 3. 调用有参函数 -->
    <button v-on:click="add(5)">+5</button>
    <!-- 4. 简写@ -->
    <button @click="add(5)">+5</button>
  </div>
</template>

<script setup>
import {ref} from "vue";

let count = ref(1);

function increase() {
  count.value++;
}

let add = (n) => {
  count.value += n
} 

</script>

<style scoped>

</style>