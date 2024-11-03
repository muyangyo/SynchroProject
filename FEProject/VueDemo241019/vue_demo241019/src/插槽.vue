<template>
  <Dialog>

<!--    <template v-slot:header>-->
<!--      <h3>友情提示</h3>-->
<!--    </template>-->

    <template v-slot:body>
      <span>请输入正确的手机号</span>
    </template>
  </Dialog>

  <Dialog>

    <template v-slot:header>
      <h4>警告</h4>
    </template>

    <template v-slot:body>
      <p>你确认要退出么?</p>
    </template>

  </Dialog>

</template>

<script setup>
import Dialog from "./components/Dialog.vue";
</script>

<style scoped>

</style>