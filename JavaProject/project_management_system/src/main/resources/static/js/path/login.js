$(document).ready(function () {

    /**
     *  检查是否有Token存在,存在则进行自动登录尝试
     */
    if (localStorage.getItem('token')) {
        // 如果Token存在，则自动发送登录请求
        $.ajax({
            url: 'global/AutoLogin',
            method: 'POST',
            success: function (body) {
                // 处理登录成功的逻辑
                location.assign("index.html");
            },
            error: function (body) {
                // 处理登录失败的逻辑
                console.log('登录失败:', body.error);
                $('#loginErrorMsg').text('token已过期，请手动登录');
                $('#loginModal').modal('show'); // 显示模态框
                setTimeout(function () {
                    $('#loginModal').modal('hide');
                }, 1500);
            }
        });
    }

    /**
     * 点击登录按钮触发
     */
    $('#loginBtn').click(function (e) {
        e.preventDefault();//阻止 表单原有的提交功能
        let username = $('#username').val();
        let password = $('#password').val();
        let rememberMe = false;

        let errorMsg = $('#errorMsg');
        errorMsg.hide();
        if (username === '' || password === '') {
            errorMsg.text('请填写账号和密码!').show();
            return;
        }

        //是否勾选记住我
        if ($('#rememberMe').is(':checked')) {
            rememberMe = true;
        }

        let data = {"username": username, "password": password, "rememberMe": rememberMe};
        console.log(data);

        $.ajax({
            url: 'global/login',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (body) {
                if (body) {
                    location.assign("index.html")
                } else {
                    errorMsg.text('账号或密码错误').show();
                    // 清空输入框
                    $('#username').val('');
                    $('#password').val('');
                }
            },
            error: function () {
                errorMsg.text('登录失败,请检查网络是否正常!').show();
            }
        });
    });

});

