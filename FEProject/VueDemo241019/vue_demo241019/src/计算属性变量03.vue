<template>
  <label>
    <input type="checkbox" v-model="selectAll"> 全选
  </label>

  <ul>
    <li v-for="(item, index) in items" :key="item.name">
      <label>
        <input type="checkbox" v-model="item.selected"> {{ item.name }}
      </label>
    </li>
  </ul>

  <button @click="invertSelection">反选</button>
  <button @click="testGet">触发get方法</button>
</template>

<script setup>
import {ref, computed} from 'vue';

const testGet = () => {
  selectAll.value = !selectAll.value;
}

const items = ref([
  {name: '选项 1', selected: false},
  {name: '选项 2', selected: false},
  {name: '选项 3', selected: false},
  {name: '选项 4', selected: false}
]);

const selectAll = computed({
  get() {
    console.log("触发get方法");
    return items.value.every(item => item.selected);
  },
  set(value) {
    console.log("触发set方法");
    items.value.forEach(item => {
      item.selected = value;
    });
  }
});

const invertSelection = () => {
  items.value.forEach(item => {
    item.selected = !item.selected;
  });
};
</script>

<style scoped>
ul {
  list-style-type: none;
  padding: 0;
}

li {
  margin: 10px 0;
}

button {
  margin-top: 20px;
}
</style>