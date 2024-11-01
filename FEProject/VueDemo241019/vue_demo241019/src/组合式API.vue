<template>
  <div>
    <p>{{ count }}</p>
    <button @click="increment">+1</button>
  </div>
</template>

<script setup>
import {ref, onBeforeMount, onMounted, onBeforeUpdate, onUpdated, onBeforeUnmount, onUnmounted} from 'vue';

console.log('0-setup: 组件实例初始化');

const count = ref(0);

const increment = () => {
  count.value++;
};

const fn = () => {
  console.log('fn 函数执行了');
};

let timerId = null;

onBeforeMount(() => {
  console.log('1-beforeCreate: 组件实例创建前，无法访问 data 和 methods');
});

onMounted(() => {
  console.log('2-created: 组件实例创建完成，可以访问 data 和 methods');
  fn();
  timerId = setInterval(() => {
    console.log(`定时器输出: ${count.value}`);
  }, 1000);
});

onBeforeMount(() => {
  console.log('3-beforeMount: 组件挂载前，模板尚未编译为真实 DOM');
  console.log('挂载前获取 p 标签:', document.querySelector('p')); // null
});

onMounted(() => {
  console.log('4-mounted: 组件挂载完成，可以操作真实 DOM');
  const pElement = document.querySelector('p');
  console.log('挂载后获取 p 标签:', pElement); // <p>0</p>
  if (pElement) {
    pElement.style.color = 'red';
  }
});

onBeforeUpdate(() => {
  console.log('5-beforeUpdate: 组件更新前，数据已变化，DOM 尚未更新');
  const pElement = document.querySelector('p');
  if (pElement) {
    console.log('更新前 p 标签内容:', pElement.innerText); // 旧内容
  }
});

onUpdated(() => {
  console.log('6-updated: 组件更新完成，DOM 已更新');
  const pElement = document.querySelector('p');
  if (pElement) {
    console.log('更新后 p 标签内容:', pElement.innerText); // 新内容
  }
});

onBeforeUnmount(() => {
  console.log('7-beforeUnmount: 组件卸载前，组件实例仍然可用');
});

onUnmounted(() => {
  console.log('8-unmounted: 组件卸载完成，组件实例已被销毁');
  clearInterval(timerId);
});
</script>

<style scoped>
p {
  font-size: 18px;
}
</style>