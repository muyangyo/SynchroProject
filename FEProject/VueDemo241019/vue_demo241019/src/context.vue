<template>
  <p v-html="str"></p>
  <p v-text="str"></p>
  <p>{{ str + "等效于v-text" }}</p>
</template>

<script setup>
import {ref} from "vue";

let str = ref("<h1>你好<\h1>");

</script>


<style scoped>

</style>