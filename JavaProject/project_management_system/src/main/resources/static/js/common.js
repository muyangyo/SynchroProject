$(document).ajaxSend(function (e, xqr, op) {
    let token = localStorage.getItem("token");//不存在时返回null
    xqr.setRequestHeader("token", token);
});