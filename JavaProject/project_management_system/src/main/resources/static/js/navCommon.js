/**
 * 获取用户信息
 */
function getUserInfo() {
    $.ajax({
        url: '/accountModal',
        type: 'GET',
        success: function (body) {
            //设置欢迎信息
            let userChar = "超级管理员";
            let userName = "沐阳Yo";
            $('#welcomeInfo').text("您好!" + userChar + ": " + userName);

            // 设置img元素的src属性
            userHeadImg.attr('src', '新图片链接');

            $('#nickname').val('新的昵称');
            $('#username').val('新的用户名');
            $('#password').val('新的密码');

            let genderSelection = 'male'; // 假设后端传来的参数是 'male'

            // 根据后端参数设置单选框的选中状态
            if (genderSelection === 'male') {
                $('#male').prop('checked', true);
            } else if (genderSelection === 'secret') {
                $('#secret').prop('checked', true);
            } else if (genderSelection === 'female') {
                $('#female').prop('checked', true);
            }

        },
        error: function (body) {
            $('#nickname').val('加载中...');
            $('#username').val('加载中...');
            $('#password').val('加载中...');
        }
    });
}


/**
 * 图片上传
 */
let userHeadImg = $('#userHead');
if (userHeadImg != null) {
    userHeadImg.click(function () {
        $('#fileInput').click();  // 触发文件选择对话框
        // 绑定文件选择后的上传事件
        $('#fileInput').on('change', function () {
            let file = this.files[0]; // 获取选中的文件
            let formData = new FormData(); // 创建一个FormData对象
            formData.append('file', file); // 将文件添加到FormData对象中

            $.ajax({
                url: 'upload.php', // 你的上传处理页面的URL
                type: 'POST', // 使用POST方法上传数据
                data: formData, // 将FormData对象作为数据源发送
                processData: false, // 不处理数据，直接发送原始数据
                contentType: false, // 不设置内容类型，让浏览器自动处理
                success: function (bdoy) {
                    notice("修改成功!", "success", 1500);
                },
                error: function (body) {

                }
            });
        });
    });
}

/**
 * 用户信息修改
 */
let userInfoEdit = $('#userInfoEdit');
if (userInfoEdit !== null) {
    userInfoEdit.on('click', function () {
        let nickname = $('#nickname').val();
        let password = $('#password').val();
        let genderSelection = $('input[name="genderSelection"]:checked').val();

        $.ajax({
            url: '/your-backend-url',
            type: 'POST',
            data: {
                'nickname': nickname,
                'password': password,
                'gender': genderSelection
            },
            success: function (response) {
                notice("修改成功!", "success", 1500);
            },
            error: function (error) {

            }
        });

    });
}


/**
 * 搜索框功能
 */
let searchButton = $('#searchBtn');
if (searchButton !== null) {
    searchButton.click(function (e) {
        let input = $('#searchInfo');

        e.preventDefault(); // 阻止表单的默认提交行为
        let searchTerm = input.val(); // 获取输入框的内容
        let url = 'https://www.baidu.com/s?wd=' + encodeURIComponent(searchTerm); // 构造请求的URL
        location.assign(url);

        /*    //待启用功能
            $.ajax({
                url: url,
                type: 'GET',
                success: function (body) {

                },
                error: function (body) {

                }
            });*/
    });

}
