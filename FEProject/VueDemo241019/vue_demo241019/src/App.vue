<script setup>
import {ref} from 'vue'

// 图书列表
const bookList = ref([
  {id: 1, name: '《红楼梦》', author: '曹雪芹'},
  {id: 2, name: '《西游记》', author: '吴承恩'},
  {id: 3, name: '《三国演义》', author: '罗贯中'},
  {id: 4, name: '《水浒传》', author: '施耐庵'}
])
// 删除
const onDel = (i) => {
  // i: 当前点击的下标

  // 删除前先确认
  if (window.confirm('确定删除么?')) {
    // 调用 splice 进行删除
    bookList.value.splice(i, 1)
  }
}
</script>

<template>
  <h3>书架</h3>
  <ul>
    <!-- 无key -->
    <li v-for="(item, index) in bookList">
      <span>{{ item.name }}</span>
      <span>{{ item.author }}</span>
      <button @click="onDel(index)">删除</button>
    </li>
  </ul>

  <ul>
    <!-- 有key且为id -->
    <li
        v-for="(item, index) in bookList"
        :key="item.id">
      <span>{{ item.name }}</span>
      <span>{{ item.author }}</span>
      <button @click="onDel(index)">删除</button>
    </li>
  </ul>
</template>

<style>
#app {
  width: 400px;
  margin: 100px auto;
}

ul {
  list-style: none;
}

ul li {
  display: flex;
  justify-content: space-around;
  padding: 10px 0;
  border-bottom: 1px solid #ccc;
}
</style>