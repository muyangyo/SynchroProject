<template>
  <h3>可折叠面板</h3>

  <!-- 面板区域 -->
  <div class="panel">
    <!-- 标题区域 -->
    <div class="title">
      <h4>自由与爱情</h4>
      <span
          class="btn"
          @click="visible = !visible">
				{{ visible ? '收起' : '展开' }}
			</span>
    </div>
    <!-- 主体内容区域 -->
    <div
        class="container"
        v-show="visible">
      <p>生命诚可贵,</p>
      <p>爱情价更高。</p>
      <p>若为自由故,</p>
      <p>两者皆可抛。</p>
    </div>
  </div>
</template>

<script setup>
import {ref} from "vue";

let visible = ref(false);

</script>

<!-- 注意这里使用了 scss 语言对 css 样式进行设置 -->
<!-- scss支持嵌套选择器 -->
<!-- 需要安装 sass 模块,在项目中运行 pnpm i sass -D 然后重新预运行-->
<style lang="scss">
body {
  background: #ddd;
}

#app {
  width: 400px;
  margin: 20px auto;
  padding: 1em 2em 2em;
  border: 4px solid green;
  border-radius: 1em;
  box-shadow: 3px 3px 3px rgba(0, 0, 0, 0.5);
  background: #fff;

  h3 {
    text-align: center;
  }

}

.panel {

  .title {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border: 1px solid #ccc;
    padding: 0 1em;

    h4 {
      margin: 0;
      line-height: 2;
    }

    .btn {
      cursor: pointer;
    }

  }

  .container {
    border: 1px solid #ccc;
    padding: 0 1em;
    border-top-color: transparent;
  }

}
</style>