<!--<template>-->
<!--  <div>-->
<!--&lt;!&ndash;    <textarea v-model="intro" cols="30" rows="4" placeholder="请输入自我介绍"></textarea>&ndash;&gt;-->
<!--  </div>-->
<!--</template>-->

<!--<script setup>-->
<!--// // 自我介绍-->
<!--// import {ref} from "vue";-->
<!--//-->
<!--// const intro = ref('')-->
<!--</script>-->

<!--<style scoped>-->
<!--</style>-->

<!--<template>-->
<!--  <div>-->
<!--    &lt;!&ndash; 下菜菜单 &ndash;&gt;-->
<!--    <select v-model="city">-->
<!--      <option value="BJ">北京</option>-->
<!--      <option value="SH">上海</option>-->
<!--      <option value="SZ">深圳</option>-->
<!--      <option value="HZ">杭州</option>-->
<!--    </select>-->
<!--  </div>-->
<!--</template>-->

<!--<script setup>-->
<!--//  收集城市-->
<!--import {ref} from "vue";-->

<!--const city = ref('SH') //默认选中上海-->

<!--</script>-->

<!--<style scoped>-->

<!--</style>-->

<!--<template>-->
<!--  <div>-->
<!--    &lt;!&ndash; 单选框：多个当中只能选择一个，需要给单选框手动添加 value 属性 &ndash;&gt;-->
<!--    <input type="radio" value="a" v-model="blood"/>A-->
<!--    <input type="radio" value="b" v-model="blood"/>B-->
<!--    <input type="radio" value="ab" v-model="blood"/>AB-->
<!--    <input type="radio" value="o" v-model="blood"/>O-->
<!--  </div>-->
<!--</template>-->

<!--<script setup>-->

<!--// 收集血型-->
<!--import {ref} from "vue";-->

<!--const blood = ref('ab')-->
<!--</script>-->

<!--<style scoped>-->


<!--</style>-->
<!--<template>-->
<!--  <div>-->
<!--    <input type="checkbox" v-model="isAgree"/>是否同意用户协议-->
<!--  </div>-->
<!--</template>-->

<!--<script setup>-->
<!--import {ref} from "vue";-->
<!--// 是否同意用户协议-->
<!--const isAgree = ref(false)-->
<!--</script>-->

<!--<style scoped>-->

<!--</style>-->

<template>
  <div>
    <input v-model="hobby" type="checkbox" value="LQ"/>篮球
    <input v-model="hobby" type="checkbox" value="ZQ"/>足球
    <input v-model="hobby" type="checkbox" value="YMQ"/>羽毛球
    <input v-model="hobby" type="checkbox" value="PPQ"/>乒乓球
    <br/>
    <input v-model="hobby" type="checkbox" value="PB"/>跑步
    <input v-model="hobby" type="checkbox" value="YY"/>游戏
    <input v-model="hobby" type="checkbox" value="PLT"/>普拉提
    <input v-model="hobby" type="checkbox" value="LDW"/>拉丁舞
  </div>
</template>
<script setup>
// 收集爱好
import {ref} from "vue";

const hobby = ref(['ZQ', 'PB']) //默认选择足球和跑步
</script>

<style scoped>

</style>