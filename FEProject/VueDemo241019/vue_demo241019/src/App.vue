<template>
  <nav>
    <router-link to="/find">发现音乐</router-link>
    <router-link to="/friend">朋友</router-link>
  </nav>
  <!-- 一级路由出口 -->
  <router-view/>
</template>

<script setup></script>

<style scoped>
nav a {
  color: #333;
  text-decoration: none;
}

nav a:nth-child(2) {
  margin: 0 80px;
}

nav a.router-link-active {
  background: red;
  color: #fff;
}
</style>