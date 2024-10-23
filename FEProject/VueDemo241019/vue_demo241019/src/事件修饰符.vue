<script setup>
// 处理按键函数
const onKeyDown = () => {
  console.log('onEntryKeyDown')
}
// p标签的点击事件
const onPClick = () => {
  console.log('onPClick')
}

const onDivClick = () => {
  console.log('onDivClick')
}

const onBtnClick = () => {
  console.log('onBtnClick')
}
</script>

<template>
  <!-- 按键修饰符,按下这个按键后就会执行对应的函数 -->
  <input type="text" @keydown.enter="onKeyDown()"/>

  <!-- .prevent: 阻止默认行为,但是不会阻止冒泡现象 -->
  <a href="https://baidu.com" @click.prevent>百度一下</a>
  <!-- .stop: 阻止冒泡，同名事件不会向上级传递 -->
  <p @click.stop="onPClick"></p>
  <!-- 修饰符的链式调用: 表明两个同时阻止 -->
  <a href="https://baidu.com" @click.stop.prevent>百度一下</a>
  <br>
  <br>

  <!-- 事件修饰符 -->
  <div @click="onDivClick()">
    <btn @click="onBtnClick()">按钮</btn>
  </div>
</template>

<style scoped>
btn {
  width: 20px;
  height: 20px;
  background: white;
}

div {
  width: 400px;
  height: 200px;
  background: plum;
}

div a {
  display: block;
  width: 100px;
  text-decoration: none;
  background: tomato;
  text-align: center;
  color: #fff;
}

div p {
  width: 200px;
  height: 100px;
  background: rebeccapurple;
}
</style>