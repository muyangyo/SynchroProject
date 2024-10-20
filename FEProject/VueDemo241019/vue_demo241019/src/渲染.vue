<script setup>
import {ref} from 'vue'

// 控制盒子是否可见
const visible = ref(true)

// 成绩
const mark = ref(80)
</script>

<template>
    <!-- v-show 本质:改变样式为不可见-->
    <div
        class="red"
        v-show="visible"></div>

    <!-- v-if配合v-else-if和v-else做多分支渲染(依旧是就近原则)  本质:改变的是是否创建这个标签 -->
    <div v-if="mark >= 90">优秀</div>
    <div v-else-if="mark >= 70">良好</div>
    <div v-else>差</div>
</template>

<style scoped>
.red,
.green {
  width: 200px;
  height: 200px;
}

.red {
  background: red;
}

.green {
  background: green;
}
</style>