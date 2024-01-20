$(document).ajaxSend(function (e, xqr, op) {
    var token = localStorage.getItem("token");
    xqr.setRequestHeader("token", token);
});

function logout() {
    localStorage.removeItem("token");
    location.href = "blog_login.html";
}