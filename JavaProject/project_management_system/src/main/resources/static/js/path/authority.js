/**
 * 初始化滑动开关
 */
$(".state").bootstrapSwitch({
    onText: "启用",      // 设置ON文本
    offText: "停用",    // 设置OFF文本
    onColor: "success",// 设置ON文本颜色     (info/success/warning/danger/primary)
    offColor: "warning",  // 设置OFF文本颜色        (info/success/warning/danger/primary)
    size: "mini",    // 设置控件大小,从小到大  (mini/small/normal/large)
    // 当开关状态改变时触发
    onSwitchChange: function (event, state) {
        if (state == true) {

        } else {

        }
    }
});
/**
 这个是设置模态框的账号
 */
let username = document.querySelector("#username");
console.dir(username);
username.value = "账号";

// 获取元素引用
const checkAll = document.getElementById('checkAll');
const editBtn = document.getElementById('editBtn');
const deleteBtn = document.getElementById('deleteBtn');
const checkboxes = document.querySelectorAll('.checkboxItem'); // 获取所有多选框元素

/**
 多选框的全选和取消全选的实现
 */

// 全选或反选功能函数
function toggleCheckboxes() {
    checkboxes.forEach(checkbox => {
        checkbox.checked = checkAll.checked; // 如果全选框被选中，则选中所有多选框；否则取消选中所有多选框。
    });
    updateButtonStates(); // 更新按钮状态
}

// 更新按钮状态函数（根据选中的多选框数量）
function updateButtonStates() {
    let checkedCount = 0; // 计数选中的多选框数量
    checkboxes.forEach(checkbox => {
        if (checkbox.checked) {
            checkedCount++; // 如果多选框被选中，则增加计数器。
        }
    });
    editBtn.disabled = checkedCount !== 1; // 如果选中的多选框数量不是1，则禁用编辑按钮。
    deleteBtn.disabled = checkedCount === 0; // 如果没有选中的多选框，则禁用删除按钮。


    if (checkedCount < checkboxes.length) {
        checkAll.checked = false;//如果没有全部选中,则取消全选
    } else {
        checkAll.checked = true;//当全部选中时,则选中全选按钮
    }
}

// 为全选框添加事件监听器以实现全选和反选功能
checkAll.addEventListener('change', toggleCheckboxes);
// 为每个多选框添加事件监听器以更新按钮状态
checkboxes.forEach(checkbox => {
    checkbox.addEventListener('change', updateButtonStates); // 当任何一个多选框状态改变时，更新按钮状态。
});

/**
 * 按钮功能
 */

// 编辑 上部 按钮的点击事件处理函数
function handleEditClick() {
    checkboxes.forEach(checkbox => {
        if (checkbox.checked) {
            //发送ajax请求
            let editLineId = splitWithUnderline(checkbox.id);
            console.log(editLineId);

            $.ajax({
                url: 'url',
                method: 'POST',
                data: {"editLineId": editLineId},
                success: function (body) {

                },
                error: function (body) {

                }
            });
        }
    });
}

// 编辑 行 按钮的点击事件处理函数
function handleEditClickOnline(event) {
    //发送ajax请求
    let editLineId = splitWithUnderline(event.target.id);
    console.log(editLineId);


    $.ajax({
        url: 'url',
        method: 'POST',
        data: {"editLineId": editLineId},
        success: function (body) {

        },
        error: function (body) {

        }
    });
}

// 删除 总 按钮的点击事件处理函数
function handleDeleteClick() {
    let arr = [];
    checkboxes.forEach(checkbox => {
        if (checkbox.checked) {
            let editLineId = splitWithUnderline(checkbox.id);
            arr.push(editLineId);
        }
    });

    let data = JSON.stringify(arr);//后端通过List获取
    console.log(data);

    $.ajax({
        url: '/api/data',
        type: 'POST',
        data: data,
        contentType: 'application/json',
        success: function (response) {

        },
        error: function (error) {

        }
    });
}

// 删除 行 按钮的点击事件处理函数
function handleDeleteClickOnline(event) {
    //发送ajax请求
    let editLineId = splitWithUnderline(event.target.id);
    console.log(editLineId);

    $.ajax({
        url: 'url',
        method: 'POST',
        data: {"editLineId": editLineId},
        success: function (body) {

        },
        error: function (body) {

        }
    });
}
