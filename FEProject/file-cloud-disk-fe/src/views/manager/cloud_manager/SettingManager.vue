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

        <el-table-column label="路径" prop="path" min-width="180px" :show-overflow-tooltip="true" sortable>
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
        <el-table-column label="用户" prop="username" sortable></el-table-column>
        <el-table-column label="访问权限" sortable :sort-method="permissionSortMethod">
          <template #default="scope">
            {{ scope.row.permissions.map(p => permissionMap[p]).join(', ') }}
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="warning" @click="handleEditUser(scope.row)">修改</el-button>
            <el-button type="danger" @click="handleDeleteUser(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 用户管理弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="30%" :draggable="true">
      <el-form :model="currentUser" :rules="rules" ref="userForm" label-width="120px">
        <el-form-item label="用户" prop="username">
          <el-input v-model="currentUser.username" :disabled="isEdit"></el-input>
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
            <el-checkbox :value="'r'">读</el-checkbox>
            <el-checkbox :value="'w'">写</el-checkbox>
            <el-checkbox :value="'d'">删除</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmUserChangeAction">确定</el-button>
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
import {easyRequest, optionalRequest, RequestMethods} from "@/utils/RequestTool.js";
import isLocalhost from "@/utils/IsLocalHost.js";
import {rsaEncryptUtil} from "@/utils/RSAEncryptUtils.js";

// 共享目录列表数据
const directories = ref([]);

onMounted(() => {
  getShareFolderList();
  getUserList();
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
  if (isLocalhost()) {
    optionalRequest({method: RequestMethods.POST, url: "/shareFolderManager/addShareFolder", timeout: 0}).then(
        (response) => {
          if (response.data === true && response.statusCode === "SUCCESS") {
            ElMessage.success('添加成功');
            getShareFolderList(); // 更新目录列表
          } else {
            ElMessage.error(response.errMsg ? response.errMsg : '添加失败');
          }
        }
    );
  } else {
    // 弹出输入框让用户输入服务器上的绝对路径地址
    ElMessageBox.prompt('请输入服务器上的绝对路径地址', '添加目录', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPattern: /^(\/|\/?(?:[^\/\*?"<>|]+\/)*[^\/\*?"<>|]*)$/,
      inputErrorMessage: '路径格式不正确'
    }).then(({value}) => {
      if (value !== null && value.length > 0) {
        value = value.trim();
        easyRequest(RequestMethods.POST, "/shareFolderManager/addShareFolder", {path: value}, false, true).then(
            (response) => {
              if (response.data === true && response.statusCode === "SUCCESS") {
                ElMessage.success('添加成功');
                getShareFolderList(); // 更新目录列表
              } else {
                ElMessage.error(response.errMsg ? response.errMsg : '添加失败');
              }
            }
        );
      } else {
        ElMessage.error('路径不能为空!');
      }
    }).catch();
  }
};

const openFolder = (currentPath) => {
  if (isLocalhost()) {
    easyRequest(RequestMethods.POST, "/shareFolderManager/openFolder", {path: currentPath}, false, false).then(
        (response) => {
          if (response.data === true && response.statusCode === "SUCCESS") {
            ElMessage.success('打开成功');
          } else {
            ElMessage.error(response.errMsg ? response.errMsg : '调用系统资源文件管理器失败!');
          }
        }
    );
  } else {
    ElMessage.error('请在本地环境下操作!');
  }
};

const deleteDirectory = (currentPath) => {
  ElMessageBox.confirm('确定删除该目录: <br>' + currentPath, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    dangerouslyUseHTMLString: true,
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
    );
  });
};

// 用户管理操作
const dialogVisible = ref(false); // 用户管理弹窗
const isEdit = ref(false); // 是否为编辑模式
const currentUser = ref({}); // 当前用户
const users = ref([]); // 用户列表
const userForm = ref(null); // 用户表单


const getUserList = () => {
  easyRequest(RequestMethods.GET, "/userManager/getUserList", "", false, false).then(
      (response) => {
        if (response.data && response.statusCode === "SUCCESS") {
          users.value = response.data;
        } else {
          ElMessage.error(response.errMsg ? response.errMsg : '获取用户列表失败');
        }
      }
  );
};

const permissionMap = {
  r: '读',
  w: '写',
  d: '删除',
};

const permissionWeights = {
  r: 1,
  w: 2,
  d: 3,
};

const permissionSortMethod = (a, b) => {
  const aTotal = a.permissions.reduce((sum, perm) => sum + permissionWeights[perm], 0);
  const bTotal = b.permissions.reduce((sum, perm) => sum + permissionWeights[perm], 0);
  return bTotal - aTotal;
};

// 表单验证规则
const rules = ref({
  username: [
    {required: true, message: '请输入用户名', trigger: 'blur'},
    {min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur'},
  ],
  password: [
    {required: false, message: '请输入密码', trigger: 'blur'}, // 修改时密码非必填
    {min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur'},
  ],
  permissions: [
    {type: 'array'},
  ],
});

const handleAddUser = () => {
  isEdit.value = false;
  rules.value.password = [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur'},
  ];
  currentUser.value = {username: '', password: '', permissions: []};
  dialogVisible.value = true;
};

const handleEditUser = (user) => {
  isEdit.value = true;
  rules.value.password = [
    {required: false, message: '请输入密码', trigger: 'blur'},
    {min: 1, max: 32, message: '长度在 1 到 32 个字符', trigger: 'blur'},
  ];
  currentUser.value = {...user, password: ''}; // 修改时密码为空
  dialogVisible.value = true;
};

const confirmUserChangeAction = () => {
  userForm.value.validate((valid) => {
    if (valid) {
      easyRequest(RequestMethods.GET, '/userManager/getPublicKey', "").then((response) => {
        rsaEncryptUtil.setPublicKey(response.data); // 保存公钥

        const userData = {
          username: rsaEncryptUtil.encryptData(currentUser.value.username), // 加密用户名
          permissions: currentUser.value.permissions,
        };

        // 如果密码不为空，则添加密码字段
        if (currentUser.value.password) {
          userData.password = rsaEncryptUtil.encryptData(currentUser.value.password); // 加密密码
        }


        if (isEdit.value) {
          // 修改用户
          easyRequest(RequestMethods.POST, "/userManager/updateUser", userData, false, true).then(
              (response) => {
                if (response.data === true && response.statusCode === "SUCCESS") {
                  ElMessage.success('修改成功');
                  dialogVisible.value = false;
                  getUserList(); // 更新用户列表
                } else {
                  ElMessage.error(response.errMsg ? response.errMsg : '修改失败');
                }
              }
          );
        } else {
          // 添加用户
          easyRequest(RequestMethods.POST, "/userManager/addUser", userData, false, true).then(
              (response) => {
                if (response.data === true && response.statusCode === "SUCCESS") {
                  ElMessage.success('添加成功');
                  dialogVisible.value = false;
                  getUserList(); // 更新用户列表
                } else {
                  ElMessage.error(response.errMsg ? response.errMsg : '添加失败');
                }
              }
          );

        }
      })
    } else {
      ElMessage.warning('请填写必填项并输入合法数据!')
      return false;
    }
  });
};

const handleDeleteUser = (index, user) => {
  ElMessageBox.confirm('确定删除该用户: ' + user.username, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    easyRequest(RequestMethods.DELETE, `/userManager/deleteUser?username=${user.username}`, "", false, false).then(
        (response) => {
          if (response.data === true && response.statusCode === "SUCCESS") {
            ElMessage.success('删除成功');
            getUserList(); // 更新用户列表
          } else {
            ElMessage.error(response.errMsg ? response.errMsg : '删除失败');
          }
        }
    );
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