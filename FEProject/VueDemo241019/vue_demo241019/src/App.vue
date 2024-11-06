<template>
  <el-tabs type="border-card" class="demo-tabs" tab-position="right" v-model="activeTab">
    <el-tab-pane v-for="tab in tabs" :key="tab.name" :label="tab.label">
      <router-view></router-view>
    </el-tab-pane>
  </el-tabs>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const tabs = ref([])
const activeTab = ref('')

watch(route, (newRoute) => {
  const { path, meta } = newRoute
  const existingTab = tabs.value.find(tab => tab.name === path)

  if (!existingTab) {
    tabs.value.push({ name: path, label: meta.label })
  }

  activeTab.value = path
})

// Initialize with the current route
const currentRoute = route.path
tabs.value.push({ name: currentRoute, label: route.meta.label })
activeTab.value = currentRoute
</script>

<style>
.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}

.demo-tabs .custom-tabs-label .el-icon {
  vertical-align: middle;
}

.demo-tabs .custom-tabs-label span {
  vertical-align: middle;
  margin-left: 4px;
}
</style>