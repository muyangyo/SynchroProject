<script setup>
// 节点 的点击事件
const handleNodeClick = (data) => {
  // data： 当前节点对象
  console.log(data)
}

const data = [
  {
    name: 'Level one 1',
    childs: [
      {
        name: 'Level two 1-1',
        childs: [
          {
            name: 'Level three 1-1-1'
          }
        ]
      }
    ]
  },
  {
    name: 'Level one 2',
    childs: [
      {
        name: 'Level two 2-1',
        childs: [
          {
            name: 'Level three 2-1-1'
          }
        ]
      },
      {
        name: 'Level two 2-2',
        childs: [
          {
            name: 'Level three 2-2-1'
          }
        ]
      }
    ]
  },
  {
    name: 'Level one 3',
    childs: [
      {
        name: 'Level two 3-1',
        childs: [
          {
            name: 'Level three 3-1-1'
          }
        ]
      },
      {
        name: 'Level two 3-2',
        childs: [
          {
            name: 'Level three 3-2-1'
          }
        ]
      }
    ]
  }
]

const defaultProps = {
  children: 'childs',
  label: 'name'
}
</script>
<template>
  <!--
    el-tree: 树形组件
      style: 行内样式
      data: 数据源(树形数组)， 通过 children 嵌套体现层级关系的
      props: 指明渲染的属性名, 默认渲染的是 label 和 children；
             如果后台返回的树形数据不叫 label 和 children，那么
            可以通过 props 进行配置
      show-checkbox: 是否可选择
      accordion: 是否开启手风琴效果
      @node-click: 节点的点击事件
  -->

  <el-tree
      style="max-width: 600px"
      show-checkbox
      accordion
      :data="data"
      :props="defaultProps"
      @node-click="handleNodeClick"/>
</template>