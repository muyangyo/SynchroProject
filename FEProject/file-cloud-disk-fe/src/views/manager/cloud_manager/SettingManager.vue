<template>
  <!-- 内容区域 -->
  <div class="content-area">
    <!-- 共享目录列表 -->
    <div class="table-header">
      <h2>共享目录列表</h2>
      <el-button type="primary" @click="addShareDirectory">添加目录</el-button>
    </div>
    <div class="table-container">
      <el-table :data="directories" style="width: 100%" max-height="200px">

        <el-table-column label="路径" prop="path" min-width="180px">
          <template #default="scope">
            <span @click="openFolder(scope.row)" style="cursor: pointer;">{{ scope.row }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="danger" @click="deleteDirectory(scope.row)" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 用户管理 -->
    <div class="table-header">
      <h2>用户管理</h2>
      <el-button type="primary" @click="handleAddUser">添加用户</el-button>
    </div>
    <div class="table-container">
      <el-table :data="users" style="width: 100%" max-height="200px">
        <el-table-column label="用户" prop="username"></el-table-column>
        <el-table-column label="访问权限">
          <template #default="scope">
            {{ scope.row.permissions.join(', ') }}
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="warning" @click="handleEditUser(scope.row)">修改</el-button>
            <el-button type="danger" @click="handleDeleteUser(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 用户管理弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="30%" :draggable="true">
      <el-form :model="currentUser" :rules="rules" ref="userForm" label-width="120px">
        <el-form-item label="用户" prop="username">
          <el-input v-model="currentUser.username"></el-input>
        </el-form-item>
        <el-form-item :label="isEdit ? '新密码' : '密码'" prop="password">
          <el-input
              :type="passwordType"
              v-model="currentUser.password"
              @click.native="togglePasswordVisibility"
              show-password
          >
            <template #suffix>
              <el-icon
                  :icon="passwordType === 'password' ? 'View' : 'Hide'"
                  @click="togglePasswordVisibility"
                  style="cursor: pointer;"
              ></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="访问权限" prop="permissions">
          <el-checkbox-group v-model="currentUser.permissions">
            <el-checkbox label="读"></el-checkbox>
            <el-checkbox label="写"></el-checkbox>
            <el-checkbox label="删除"></el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmUserAction">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {onMounted, ref, watch} from 'vue';
import {
  ElMessage,
  ElMessageBox,
  ElTable,
  ElTableColumn,
  ElButton,
  ElDialog,
  ElForm,
  ElFormItem,
  ElInput,
  ElCheckboxGroup,
  ElCheckbox
} from 'element-plus';
import {easyRequest, RequestMethods} from "@/utils/RequestTool.js";

// 共享目录列表数据
const directories = ref([]);

// 用户管理数据
const users = ref([
  {username: 'user1', password: 'pass1', permissions: ['读', '写']},
  {username: 'user2', password: 'pass2', permissions: ['删除']},
  {username: 'user3', password: 'pass3', permissions: ['读']},
  {username: 'user4', password: 'pass4', permissions: ['写']},
  {username: 'user5', password: 'pass5', permissions: ['删除']},
]);

// 表单验证规则
const rules = ref({
  username: [
    {required: true, message: '请输入用户名', trigger: 'blur'},
    {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'},
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'},
  ],
  permissions: [
    {type: 'array', required: true, message: '请选择访问权限', trigger: 'change'},
  ],
});

onMounted(() => {
  getShareFolderList();
});

// 密码显示/隐藏
const passwordType = ref('password');
const togglePasswordVisibility = () => {
  passwordType.value = passwordType.value === 'password' ? 'text' : 'password';
};

const getShareFolderList = () => {
  easyRequest(RequestMethods.GET, "/shareFolderManager/getShareFolderList", "", false, false).then(
      (response) => {
        if (response.data && response.statusCode === "SUCCESS") {
          directories.value = response.data;
        } else {
          ElMessage.error(response.errMsg ? response.errMsg : '获取共享目录列表失败');
        }
      }
  );
};

// 添加共享目录
const addShareDirectory = () => {
  easyRequest(RequestMethods.POST, "/shareFolderManager/addShareFolder", "", false, false, 0).then(
      (response) => {
        if (response.data === true && response.statusCode === "SUCCESS") {
          ElMessage.success('添加成功');
          getShareFolderList(); // 更新目录列表
        } else {
          ElMessage.error(response.errMsg ? response.errMsg : '添加失败');
        }
      }
  );
};

const openFolder = (currentPath) => {
  console.warn('打开文件夹', currentPath);
  easyRequest(RequestMethods.POST, "/shareFolderManager/openFolder", {path: currentPath}, false, false).then(
      (response) => {
        if (response.data === true && response.statusCode === "SUCCESS") {
          ElMessage.success('打开成功');
        } else {
          ElMessage.error(response.errMsg ? response.errMsg : '调用系统资源文件管理器失败!');
        }
      }
  );
};

const deleteDirectory = (currentPath) => {
  ElMessageBox.confirm('确定删除该目录?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    easyRequest(RequestMethods.POST, "/shareFolderManager/deleteShareFolder", {path: currentPath}, false, false).then(
        (response) => {
          if (response.data === true && response.statusCode === "SUCCESS") {
            ElMessage.success('删除成功');
            getShareFolderList(); // 更新目录列表
          } else {
            ElMessage.error(response.errMsg ? response.errMsg : '删除失败');
          }
        }
    )
  });
};

// 用户管理操作
const dialogVisible = ref(false);
const isEdit = ref(false);
const currentUser = ref({});
const originalUser = ref({});
const userForm = ref(null);

const handleAddUser = () => {
  isEdit.value = false;
  currentUser.value = {username: '', password: '', permissions: []};
  dialogVisible.value = true;
};

const handleEditUser = (user) => {
  isEdit.value = true;
  currentUser.value = {...user};
  originalUser.value = {...user};
  dialogVisible.value = true;
};

const handleDeleteUser = (index) => {
  ElMessageBox.confirm('确定删除该用户?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    users.value.splice(index, 1);
    ElMessage.success('删除成功');
  });
};

const confirmUserAction = () => {
  userForm.value.validate((valid) => {
    if (valid) {
      if (isEdit.value) {
        const index = users.value.findIndex(u => u.username === originalUser.value.username);
        if (index !== -1) {
          users.value[index] = {...currentUser.value};
          ElMessage.success('修改成功');
        }
      } else {
        users.value.push({...currentUser.value});
        ElMessage.success('添加成功');
      }
      dialogVisible.value = false;
    } else {
      ElMessage.warning('请填写必填项并检查输入');
      return false;
    }
  });
};

const dialogTitle = ref('');
watch(isEdit, (newValue) => {
  dialogTitle.value = newValue ? '修改用户' : '添加用户';
}, {immediate: true});
</script>

<style lang="scss" scoped>
.content-area {
  .table-header {
    color: white;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-left: 40px;
    padding-right: 40px;
  }

  .table-container {
    color: white;
    max-height: 200px;
    overflow-y: auto;
    border-radius: 4px;
    margin-bottom: 20px;
    padding-left: 30px;
    padding-right: 30px;
  }

  .el-table {
    margin-top: 0;
    border-bottom: 2px solid #3d3d3d;
  }
}
</style>