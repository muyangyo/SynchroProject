<template>
  <button :class="'my-button'" @click="btnClick">
    <slot>按钮</slot>
  </button>
</template>

<script setup>
const btnClick = () => {
  alert("全局按钮被点击");
}
</script>

<style scoped>
.my-button {
  padding: 10px 20px;
  font-size: 16px;
  border: none;
  border-radius: 5px;
  background-color: #00ff37;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.my-button:hover {
  background-color: #03fd39;
}
</style>