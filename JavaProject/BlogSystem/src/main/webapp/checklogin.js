function checkLogin() {
    $.ajax({
        type: 'get',
        url: 'check',
        success: function (body) {
            console.log("成功验证登录!");
        },
        error: function (body) {
            console.log("正在跳转至登录页");
            location.assign('login.html');
        }
    });
}