<script setup>
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting
} from '@element-plus/icons-vue'

const handleOpen = (key, keyPath) => {
  console.log(key, keyPath)
}
const handleClose = (key, keyPath) => {
  console.log(key, keyPath)
}
</script>

<template>
  <!--
    el-menu: 菜单组件
      active-text-color：菜单高亮时的文本颜色
      background-color: 菜单背景色
      class: 添加自定义类名，控制样式
      default-active: 默认高亮的菜单
      text-color: 文字默认颜色
      @open: 子菜单打开时触发
      @close: 子菜单关闭时触发
  -->
  <el-menu
      active-text-color="red"
      background-color="#545c64"
      class="el-menu-vertical-demo"
      default-active="4"
      text-color="#fff"
      @open="handleOpen"
      @close="handleClose">
    <!--
      el-sub-menu: 说明当前菜单下嵌套的有子菜单
    -->
    <el-sub-menu index="1">
      <!--
        通过 title 具名插槽进行自定义一级菜单的图标和名称
      -->
      <template #title>
        <el-icon>
          <location/>
        </el-icon>
        <span>Navigator One</span>
      </template>
      <!--
        el-menu-item-group: 将子菜单进行分组
      -->
      <el-menu-item-group title="Group One">
        <el-menu-item index="1-1">item one</el-menu-item>
        <el-menu-item index="1-2">item two</el-menu-item>
      </el-menu-item-group>
      <el-menu-item-group title="Group Two">
        <el-menu-item index="1-3">item three</el-menu-item>
      </el-menu-item-group>
      <!--
         子菜单里面嵌套子菜单，本质就是 el-sub-menu 嵌套 el-sub-menu
      -->
      <el-sub-menu index="1-4">
        <!-- #title 具名插槽自定义菜单名称 -->
        <template #title>item four</template>
        <!-- 子菜单 -->
        <el-menu-item index="1-4-1">item one</el-menu-item>
      </el-sub-menu>
    </el-sub-menu>

    <!--
      el-menu-item: 子菜单（下面没有嵌套子菜单）
    -->
    <el-menu-item index="2">
      <!-- 字体图标 -->
      <el-icon>
        <icon-menu/>
      </el-icon>
      <!-- 菜单名称 -->
      <span>用户管理</span>
    </el-menu-item>
    <el-menu-item index="3">
      <el-icon>
        <document/>
      </el-icon>
      <span>权限管理</span>
    </el-menu-item>
    <el-menu-item index="4">
      <el-icon>
        <setting/>
      </el-icon>
      <span>角色管理</span>
    </el-menu-item>
  </el-menu>
</template>

<style scoped>
.el-menu {
  width: 350px;
}
</style>