<script setup>
import {reactive} from 'vue'

// do not use same name with ref
const form = reactive({
  name: '',
  region: '',
  date1: '',
  date2: '',
  delivery: false,
  type: [],
  resource: '',
  desc: ''
})

const onSubmit = () => {
  console.log('submit!')
}
</script>

<template>
  <!--
    el-form: 表单最外层组件
      model: 绑定表单的数据源（一个用来收集表单数据，另一个就是用来做校验）
      label-width: 表单域标签的宽度
      style: 行内样式
  -->
  <el-form
      :model="form"
      label-width="auto"
      style="max-width: 600px">
    <!--
      el-form-item: 用来包裹表单控件，比如用来包裹输入框、下拉列表、复选框、按钮等
        label: 表单控件的名称
    -->
    <el-form-item label="Activity name">
      <!--
        输入框组件：
           v-model 用来收集输入框的值，实现双向数据绑定
           placeholder: 提示文本
      -->
      <el-input
          v-model="form.name"
          placeholder="请输入活动名称"/>
    </el-form-item>
    <el-form-item label="Activity zone">
      <!--
         el-select + el-option： 下拉列表组件
           v-model： 收集下拉列表的值
           placeholder: 提示文本

      -->
      <el-select
          v-model="form.region"
          placeholder="please select your zone">
        <!--
          el-option: 选项
             label: 给用户展示的文本
             value: 为v-model提供收集的值
        -->
        <el-option
            label="Zone one"
            value="shanghai"/>
        <el-option
            label="Zone two"
            value="beijing"/>
      </el-select>
    </el-form-item>
    <el-form-item label="Activity time">
      <!--
        el-col: 列组件，一行分为24列
           span: 指明占据的份额，比如 11 就代表占了 11/24
      -->
      <el-col :span="11">
        <!--
          el-date-picker: 日期选择器
            v-model: 收集选择的日期
            type: 指明选择器的类型，date表示可以选择日期
            placeholder: 占位提示文本
            style: 行内样式
        -->
        <el-date-picker
            v-model="form.date1"
            type="date"
            placeholder="Pick a date"
            style="width: 100%"/>
      </el-col>
      <el-col
          :span="2"
          class="text-center">
        <!-- 中间的分隔线 -->
        <span class="text-gray-500">-</span>
      </el-col>
      <el-col :span="11">
        <!--
           el-time-picker: 时间选择器
             v-model: 收集选择的时间
        -->
        <el-time-picker
            v-model="form.date2"
            placeholder="Pick a time"
            style="width: 100%"/>
      </el-col>
    </el-form-item>
    <el-form-item label="Instant delivery">
      <!--
        el-switch: 开关组件，只有两种状态，打开或关闭
         v-model: 收集开关的状态
      -->
      <el-switch v-model="form.delivery"/>
    </el-form-item>
    <el-form-item label="Activity type">
      <!--
        el-checkbox-group: 将多选框进行分组，用组来管理多个复选框
          v-model: 收集多个复选框的值
      -->
      <el-checkbox-group v-model="form.type">
        <!--
           el-checkbox: 一个复选框组件
             value: 提供给v-model收集值用的
             name: 多选框的名字
        -->
        <el-checkbox
            value="Online activities"
            name="type">
          Online activities
        </el-checkbox>
        <el-checkbox
            value="Promotion activities"
            name="type">
          Promotion activities
        </el-checkbox>
        <el-checkbox
            value="Offline activities"
            name="type">
          Offline activities
        </el-checkbox>
        <el-checkbox
            value="Simple brand exposure"
            name="type">
          Simple brand exposure
        </el-checkbox>
      </el-checkbox-group>
    </el-form-item>
    <el-form-item label="Resources">
      <!--
        el-radio-group: 将单选框进行分组，用来管理一组单选框
          v-model: 收集选中的单选框的值
      -->
      <el-radio-group v-model="form.resource">
        <!--
          el-radio: 单选框组件
             value: 提供给v-model收集值用的
        -->
        <el-radio value="Sponsor">Sponsor</el-radio>
        <el-radio value="Venue">Venue</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="Activity form">
      <!--
         el-input: 输入框组件
           v-model: 收集输入框的值
           type: 默认值是text文本框，还有可选值比如: password/textarea
      -->
      <el-input
          v-model="form.desc"
          type="textarea"/>
    </el-form-item>
    <el-form-item>
      <!--
        el-button: 按钮组件
          type: 按钮的类型，不同的类型对应不同的颜色
          @click: 点击事件
      -->
      <el-button
          type="primary"
          @click="onSubmit"
      >Create
      </el-button
      >
      <!-- 没有添加 type 属性，就是默认按钮 -->
      <el-button>Cancel</el-button>
    </el-form-item>
  </el-form>
</template>