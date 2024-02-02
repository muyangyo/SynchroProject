getUserInfo();

/**
 * 获取所有用户的信息(小于用户权限的)
 */
function getAllUserInfo() {
    $.ajax({
        url: '/update',
        type: 'get',
        success: function (body) {
            let userId = '2';
            let username = '123123';
            let password = '******';
            let nickname = 'demo';
            let authority = '管理员';
            let userState = 'checked';

            // 构造 HTML 内容
            let htmlContent = '<!--这是一个用户的数据-->\n' +
                '<tr>\n' +
                '<!--以id作为行标识符-->\n' +
                '<td><input type="checkbox" value="" class="checkboxItem" id="checkBox_' + userId + '"></td>\n' +
                '<td id="userId_' + userId + '">' + userId + '</td>\n' +
                '<td id="username_' + userId + '">' + username + '</td>\n' +
                '<td id="password_' + userId + '">' + password + '</td>\n' +
                '<td id="nickname_' + userId + '">' + nickname + '</td>\n' +
                '<td id="authority_' + userId + '">' + authority + '</td>\n' +
                '<td><input type="checkbox" class="state" id="userState_' + userId + '" ' + userState + '/></td>\n' +
                '<td>\n' +
                '  <button id="editBtn_' + userId + '" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#userInfo" onclick="handleEditClickOnline(event)">编辑</button>\n' +
                '  <button id="deleteBtn_' + userId + '" class="btn btn-danger btn-sm" onclick="handleDeleteClickOnline(event)">删除</button>\n' +
                '</td>\n' +
                '</tr>';

            // 将构造的 HTML 内容插入到表格中（这里表格的 ID 为 "userTable"）
            $('#userTable').append(htmlContent);
        },
        error: function () {
            errorInf();
        }
    });
}

getAllUserInfo();

/**
 * 初始化滑动开关(必须在getAllUserInfo方法后面,要不然没办法渲染到)
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
            //触发事件的用户id
            let userId = splitWithUnderline(event.target.id);
        } else {

        }
    }
});

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
 * 新增用户按钮点击事件处理函数
 */
function handleAddUserClick() {
    modalSubmitBtnClick();
}

/**
 * 点击修改提交按钮
 */
function modalSubmitBtnClick() {
    $('#modalSubmitBtn').off('click.submitData').on('click.submitData', function (e) {
        e.preventDefault(); // 阻止表单原有的提交功能

        let formData = {
            username: $('#username_0').val(),
            password: $('#password_0').val(),
            nickname: $('#nickname_0').val(),
            authority: $('#authority_0').val()
        };

        console.log(formData);
        $.ajax({
            url: '/update',
            type: 'POST',
            data: formData,
            success: function (body) {

            },
            error: function () {
                errorInf();
            }
        });
    });
}

/**
 * 编辑 上部 按钮的点击事件处理函数
 */
function handleEditClick() {
    let editLineId;
    checkboxes.forEach(checkbox => {
        if (checkbox.checked) {
            //获取要修改的列id
            editLineId = splitWithUnderline(checkbox.id);
        }
    });
    console.log(editLineId);

    //请求对应用户信息
    $.ajax({
        url: 'url',
        method: 'POST',
        data: {"editLineId": editLineId},
        success: function (body) {
            //设置用户信息
            $('#username_0').val();
            $('#password_0').val();
            $('#nickname_0').val();
            let character = "admin";// 假设后端传过来的是user
            $('#authority_0 option[value="' + character + '"]').prop('selected', true);
        },
        error: function (body) {
            errorInf();
        }
    });

    modalSubmitBtnClick();
}

/**
 * 编辑 行 按钮的点击事件处理函数
 */
function handleEditClickOnline(event) {
    //获取要修改的列id

    let editLineId = splitWithUnderline(event.target.id);
    console.log(editLineId);


    //请求对应用户信息
    $.ajax({
        url: 'url',
        method: 'POST',
        data: {"editLineId": editLineId},
        success: function (body) {
            //设置用户信息
            $('#username_0').val();
            $('#password_0').val();
            $('#nickname_0').val();
            let character = "admin";// 假设后端传过来的是user
            $('#authority_0 option[value="' + character + '"]').prop('selected', true);
        },
        error: function (body) {
            errorInf();
        }
    });

    modalSubmitBtnClick();
}

/**
 *删除 总 按钮的点击事件处理函数
 */
function handleDeleteClick() {
    //获取要删除的序号组
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

/**
 * 删除 行 按钮的点击事件处理函数
 */
function handleDeleteClickOnline(event) {
    //获取要删除的列id
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
