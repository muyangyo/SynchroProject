//点击注册按钮触发
$('#registerBtn').click(function (e) {
    e.preventDefault();//阻止 表单原有的提交功能
    let username = $('#inputUsername').val();
    let mail = $('#inputMail').val();
    let nickName = $('#inputNickname').val();
    let password = $('#inputPassword').val();
    let rePassword = $('#inputREPassword').val();

    if (username === '' || mail === '' || nickName === '' || password === '') {
        notice("请填写完整信息!", "danger", 1500);
        return;
    }

    // 这个是复选框的选中判断
    let checkBox = $('#agreeCheckBox');
    if (checkBox.is(':not(:checked)')) {
        notice("请勾选用户协议", "danger");
        return;
    }


    let reDiv = $('#reInputDiv');
    if (password !== rePassword) {
        // 如果两次不一致则标红
        $('#reInputLab').text("两次输入不同!");
        reDiv.addClass("has-error");
        return;
    } else {
        $('#reInputLab').text("请再确定密码");
        reDiv.removeClass("has-error");
    }


    //正确则发送请求
    let data = {
        "username": username,
        "mail": mail,
        "nickName": nickName,
        "password": password
    };

    console.log(data);
    $.ajax({
        url: 'url',
        type: 'POST',
        data: data,
        success: function (body) {
            if (body) {
                location.assign("index.html")
            } else {

            }
        },
        error: function () {

        }
    });
});

