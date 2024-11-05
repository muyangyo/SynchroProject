// 导入定义仓库的函数
import {defineStore} from 'pinia'
import {ref, computed} from 'vue'

// 定义仓库
// defineStore(仓库名称, 函数或对象)

export const useStockStore = defineStore('stock', () => {
    // 提供共享数据
    const stock = ref(20);

    // 计算 stock 的两倍值： 上 computed 计算属性了
    const doubleStock = computed(() => {
        return stock.value * 2;
    })

    // 修改共享数据

    // 新增
    function addStock() {
        stock.value++;
    }

    // 减少
    function subStock() {
        stock.value--;
    }

    // 返回共享数据和修改函数
    return {
        stock,
        doubleStock,
        addStock,
        subStock
    }
})