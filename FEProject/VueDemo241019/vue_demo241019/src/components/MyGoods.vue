<script setup>
import {ref} from 'vue';

const props = defineProps({
  idx: {
    type: Number, //限定 idx 必须是数字
    required: true, //限定是否为必须的
    default: 0, //默认值,但是如果有 required = true 则无效
    /* 自定义检验 value(传过来的参数) 的函数 */
    validator: (value) => {
      return value >= 0;//必须返回一个布尔值
    }
  },
  imgUrl: {},
  title: String,
  price: {
    type: Number,
    required: true
  }
});

const emit = defineEmits();//取一个函数

// 每次减少的价格
const x = ref(3);

// 点击砍价按钮
const onCut = () => {
  emit('dePrice', props.idx, x.value); //调用 父级 的 dePrice 方法,参数为...
};
</script>

<template>
  <div class="item">
    <img :src="imgUrl" alt="商品图片"/>
    <p class="name">{{ title }}</p>
    <p class="price">
      <span>{{ price }}.00</span>
      <button @click="onCut">砍一刀</button>
    </p>
  </div>
</template>

<style scoped lang="scss">
.item {
  width: 240px;
  margin-left: 10px;
  padding: 20px 30px;
  transition: all 0.5s;
  margin-bottom: 20px;

  &:nth-child(4n) {
    margin-left: 0;
  }

  &:hover {
    box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.2);
    transform: translate3d(0, -4px, 0);
    cursor: pointer;
  }

  img {
    width: 100%;
  }

  .name {
    font-size: 18px;
    margin-bottom: 10px;
    color: #666;
  }

  .price {
    display: flex;
    align-items: center;
    height: 36px;
    font-size: 22px;
    color: firebrick;

    button {
      margin-left: 50px;
      font-size: 14px;
    }
  }

  .price::before {
    content: '¥';
    font-size: 22px;
  }
}
</style>