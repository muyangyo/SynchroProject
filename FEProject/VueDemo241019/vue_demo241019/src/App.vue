<script setup>
import {nextTick, ref} from 'vue'

// 控制是否显示输入框
const isShowEdit = ref(false);

// 点击编辑按钮
const onEdit = () => {
  isShowEdit.value = true;
  // 问题：当数据变了，发现获取DOM拿不到的
  // 原因：在 vue3 中，当数据变了，DOM的更新是异步的；
  // 也就是说，数据变了，想立即获取最新的DOM是拿不到的，此时DOM并没有更新
  console.log(inputRef.value); // null

  // 解决：利用 nextTick() 这个方法，因为在这个方法的回调中，DOM更新完毕了
  nextTick(() => {
    console.log(inputRef.value);
    inputRef.value.focus();//自动聚焦
  })
}

const inputRef = ref(null)
</script>
<template>
  <div class="box">
    <h3>大标题</h3>
    <button @click="onEdit">编辑</button>
  </div>

  <div v-if="isShowEdit">
    <input
        type="text"
        ref="inputRef"/>
    <button>确认</button>
  </div>
</template>

<style scoped>
.box {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 200px;
  height: 40px;
}
</style>