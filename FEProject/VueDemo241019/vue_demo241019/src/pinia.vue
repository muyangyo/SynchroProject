<script setup>
import Add from '@/components/Add.vue'
import Sub from '@/components/Sub.vue'

import {useStockStore} from '@/store/stock'

const stockStore = useStockStore()
</script>

<template>
  <h1>根组件</h1>
  库存总数:
  <input
      type="number"
      v-model.number="stockStore.stock"/>
  <p>stock 的翻倍值 = {{ stockStore.doubleStock }}</p>
  <hr/>
  <Add/>
  <hr/>
  <Sub/>
</template>

<style>
#app {
  width: 350px;
  margin: 100px auto;
  padding: 5px 10px;
  border: 1px solid #ccc;
}
</style>