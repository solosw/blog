function registerSumbit() {
    var username = $('#username').val();
    var password = $('#password').val();
    var em = $('#email').val();
    var checkpwd = $('#checkpwdinput').val();
    var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(em)) {
        alert("输入正确的邮箱格式");
        return;
    }
    if (username.trim() === '') {
        alert("输入用户名");
        return;
    }
    if (password.trim() === '') {
        alert('输入密码');
        return;
    }
    // 创建用户对象
    var user = {
        username: username,
        password: password,
        email: em,
        checkpwd: checkpwd
    };

    // 发送注册请求
    $.ajax({
        url: '/register',  // 替换为你的后端注册请求路径
        type: 'POST',  // 请求类型
        dataType: 'text',  // 响应数据类型
        contentType: 'application/json',  // 请求内容类型
        data: JSON.stringify(user),  // 将用户对象转换为JSON字符串
        success: function (response) {
            // 注册成功处理逻辑
            var inf = JSON.parse(response);
            
            if (inf.status == true) {
                alert('注册成功');
                window.location.href = "index.html";
            }else
            {
                alert('注册失败');
            }
        },
        error: function (xhr, status, error) {
            // 注册失败处理逻辑
            //console.error(error);
            alert('注册失败'+'服务器错误'+error);
        }
    });
}


function sendCheckPwd() {
    var em = $('#email').val();
    var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(em)) {
        alert("输入正确的邮箱格式");
        return;
    }
    $.ajax({
        url: '/checkpwd',  // 替换为你的后端注册请求路径
        type: 'POST',  // 请求类型 
        data: { email: em }, // 将用户对象转换为JSON字符串
        success: function (response) {
            
            var seconds = 60; // 剩余秒数
            $("#checkpwdbutton").prop("disabled", true);
            var countdown = setInterval(function () {
                seconds--;
                document.getElementById("checkpwdbutton").innerText = "剩余时间: " + seconds + "秒";
                if (seconds <= 0) {
                    // 倒计时结束后启用按钮
                    clearInterval(countdown);
                    $("#checkpwdbutton").prop("disabled", false);
                    document.getElementById("checkpwdbutton").innerText = "发送验证码";
                }
            }, 1000);
            //alert(666);
            if(response==true)
            {
                alert('发送成功');
            }else
            {
                alert('发送失败');
            }
        },
        error: function (xhr, status, error) {
            // 注册失败处理逻辑
           
            alert('发送失败'+'服务器错误'+error);
        }
    });
}