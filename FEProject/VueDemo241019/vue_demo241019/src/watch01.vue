<template>
  <div>
    <input type="text" v-model="object.text" placeholder="请输入.."/>
  </div>
</template>

<script setup>
import {reactive, ref, watch} from "vue";

let object = reactive({text: "", number: 123});
watch(object, (newValue, oldValue) => {
  console.log(newValue, oldValue);
  console.log("对象的 text 新数据为: " + newValue.text + " | 对象的 text 旧数据为: " + oldValue.text);
})
</script>
<style scoped>

</style>