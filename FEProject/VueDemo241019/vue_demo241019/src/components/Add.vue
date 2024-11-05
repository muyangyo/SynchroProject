<script setup>
// 按需导入 useStockStore 函数
import { useStockStore } from '@/store/stock.js'
// 调用函数，获取仓库
const stockStore = useStockStore()
</script>
<template>
  <div class="add">
    <h3>Add组件</h3>
    <p>已知库存数: {{ stockStore.stock }}</p>
    <button @click="stockStore.addStock()">库存+1</button>
  </div>
</template>

<style scoped></style>