function logOff(){
    $.ajax({
        type: 'get',
        url: 'logOut',
        error: function (body) {
            console.log("正在跳转至登录页");
            location.assign('login.html');
        }
    });
}