$(document).ajaxSend(function (e, xqr, op) {
    let token = localStorage.getItem("token");//不存在时返回null
    xqr.setRequestHeader("token", token);
});

/**
 * 登出
 */
function logout() {
    $.ajax({
        url: 'global/AutoLogin',
        method: 'POST',
        success: function () {
            localStorage.removeItem("token");//移除token
        }
    });
    location.assign("login.html");
}

/**
 * 使用模态框进行通知
 * @param msg 通知信息
 * @param level 三个等级: success , warning , danger .默认 info
 * @param showTime 显示时间
 */
function notice(msg, level, showTime) {
    let grade;
    switch (level) {
        case "success":
            grade = "text-success";
            break;
        case "warning":
            grade = "text-warning";
            break;
        case "danger":
            grade = "text-danger";
            break;
        default:
            grade = "text-info";
            break;
    }
    $('#Msg').addClass(grade).text(msg);
    $('#noticeModal').modal('show');//显示模态框

    if (showTime === undefined) {
        showTime = 2000;
    }

    setTimeout(function () {
        $('#noticeModal').modal('hide');
    }, showTime);
}

/**
 * 服务器无响应(断网)
 */
function errorInf() {
    console.log("网络错误!");
}