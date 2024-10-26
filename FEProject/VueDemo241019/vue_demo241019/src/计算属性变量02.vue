<script setup>
import {computed} from 'vue'

// 简易写法：只能读取，不能修改
// const uname = computed(() => {
// 	return '姬霓太美'
// })

const uname = computed({
  get() {
    return '姬霓太美'
  },

  // val: 要给计算属性赋予的新值
  // 只要给计算属性赋值，都会触发 set 函数的执行
  set(val) {
    console.log(val)
  }
})
</script>

<template>
  <div>
    <input
        type="text"
        v-model="uname"/>
  </div>
</template>

<style scoped></style>