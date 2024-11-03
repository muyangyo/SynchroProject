<script setup>
const props = defineProps({
  // 接收父传子的数组：为了让表格组件的表体数据不能写死
  data: {
    type: Array,
    default: []
  }
})
</script>

<template>
  <table class="my-table">
    <thead>
    <tr>
      <th>序号</th>
      <th>姓名</th>
      <th>年纪</th>
      <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <tr
        v-for="(item, index) in props.data"
        :key="item.id">
      <td>{{ index + 1 }}</td>
      <td>{{ item.name }}</td>
      <td>{{ item.age }}</td>
      <td>
        <slot name="solt" :i="index"></slot>
      </td>
    </tr>
    </tbody>
  </table>
</template>

<style>
.my-table {
  width: 450px;
  text-align: center;
  border: 1px solid #ccc;
  font-size: 24px;
  margin: 30px auto;
}

.my-table thead {
  background-color: #1f74ff;
  color: #fff;
}

.my-table thead th {
  font-weight: normal;
}

.my-table thead tr {
  line-height: 40px;
}

.my-table th,
.my-table td {
  border-bottom: 1px solid #ccc;
  border-right: 1px solid #ccc;
}

.my-table td:last-child {
  border-right: none;
}

.my-table tr:last-child td {
  border-bottom: none;
}

.my-table button {
  width: 65px;
  height: 35px;
  font-size: 18px;
  border: 1px solid #ccc;
  outline: none;
  border-radius: 3px;
  cursor: pointer;
  background-color: #ffffff;
  margin-left: 5px;
}
</style>