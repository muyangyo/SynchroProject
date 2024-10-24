<template>
  <div>
    <!-- 使用三元表达式动态赋予类 -->
    <p :class="isStyle ?'staticDemo':'styleDemo'">样式绑定1</p>
    <!-- 直接 类名: 是否使用来赋予类 -->
    <p :class="{styleDemo: isStyle}">样式绑定2</p>
    <!-- 静态类与动态类共存,二者合并 -->
    <p class="staticDemo" :class="{styleDemo: isStyle,styleDemo: false}">样式绑定3</p>
  </div>
</template>

<script setup>
let isStyle = true;
</script>

<style scoped>
.styleDemo {
  color: red;
  background: black;
}

.staticDemo {
  font-size: 20px;
}
</style>