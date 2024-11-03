<script setup>
import {ref} from 'vue'
import ParamInDialog from "@/components/ParamInDialog.vue";


const tableData1 = ref([
  {id: 11, name: '狗蛋', age: 18},
  {id: 22, name: '大锤', age: 19},
  {id: 33, name: '铁棍', age: 17}
])  

const tableData2 = ref([
  {id: 21, name: 'Jack', age: 18},
  {id: 32, name: 'Rose', age: 19},
  {id: 43, name: 'Henry', age: 17}
])

// 删除
const onDel = (i) => {
  if (window.confirm('确认删除么?')) {
    tableData1.value.splice(i, 1)
  }
}

// 查看
const onInspect = (i) => {
  alert(JSON.stringify(tableData2.value[i]))
}
</script>
<template>
  <ParamInDialog :data="tableData1">
    <template #solt="{ i }">
      <button @click="onDel(i)">删除</button>
    </template>
  </ParamInDialog>

  <ParamInDialog :data="tableData2">
    <template #solt="{ i }">
      <button @click="onInspect(i)">查看</button>
    </template>
  </ParamInDialog>
</template>

<style>
body {
  background-color: #fff;
}
</style>