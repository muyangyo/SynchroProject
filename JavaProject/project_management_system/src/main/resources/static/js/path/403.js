getUserInfo();

/**
 * 申请权限按钮
 * */
$('#applicationReasonSubmit').click(function () {
    // 获取用户输入的申请理由
    let applicationReason = $('#applicationReason').val();

    $.ajax({
        type: 'POST',
        url: 'your-backend-url',
        data: {applicationReason: applicationReason},
        success: function (body) {
            notice("申请发送成功!", "success");
        },
        error: function (body) {

        }
    });
});