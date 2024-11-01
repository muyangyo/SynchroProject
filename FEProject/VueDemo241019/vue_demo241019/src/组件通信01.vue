<script setup>
import {ref} from 'vue';
import MyGoods from './components/MyGoods.vue';

// 商品列表
const goodsList = ref([
  {
    id: '123',
    name: '称心如意手摇咖啡磨豆机咖啡豆研磨机',
    price: 289,
    picture: 'https://yanxuan-item.nosdn.127.net/84a59ff9c58a77032564e61f716846d6.jpg'
  }
]);

/**
 * 减少商品价格
 * @param {number} index - 商品在列表中的索引
 * @param {number} dePrice - 每次减少的价格
 */
const subPrice = (index, dePrice) => {
  if (index >= 0 && index < goodsList.value.length) {
    goodsList.value[index].price -= dePrice;
  }
};
</script>

<template>
  <div className="list">
    <MyGoods
        v-for="(item, index) in goodsList"
        :key="item.id"

        :idx="index"
        :imgUrl="item.picture"
        :title="item.name"
        :price="item.price"

        @dePrice="subPrice"
    />
  </div>
</template>

<style lang="scss" scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.list {
  width: 1000px;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
}
</style>