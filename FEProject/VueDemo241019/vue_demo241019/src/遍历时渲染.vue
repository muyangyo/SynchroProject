<template>
  <div>
    <ul>
      <!-- 遍历数字数组 -->
      <li v-for="(item, index) in nums">当前元素: {{ item }} => 下标: {{ index }}</li>
    </ul>
    <div class="goods-list">
      <!-- 遍历对象数组 -->
      <div
          class="goods-item"
          v-for="item in goodsList">
        <p>id = {{ item.id }}</p>
        <p>name = {{ item.name }}</p>
        <p>price = {{ item.price }}</p>
      </div>
      <ul>
        <!-- 遍历对象 -->
        <li v-for="(value, key, index) in obj">
          值: {{ value }} => 键: {{ key }} => 下标: {{ index }}
        </li>
      </ul>

      <ul>
        <!-- 遍历数字 -->
        <li v-for="(item, index) in 5">{{ item }} => {{ index }}</li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import {ref} from 'vue'
// 数字数组
const nums = ref([11, 22, 33, 44])

// 商品列表
const goodsList = ref([
  {id: 1, name: '篮球', price: 299},
  {id: 2, name: '足球', price: 99},
  {id: 3, name: '排球', price: 199}
])

// 准备对象
const obj = {
  id: 10001,
  name: 'bit',
  age: 9
}
</script>

<style scoped>

</style>