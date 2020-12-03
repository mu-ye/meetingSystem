


$(function () {
    /**
     *  清空全部内容， 生成正在钉钉登陆
     */
    function initJump(message) {
        let jumpDiv = $("<div></div>")
        jumpDiv.attr("align","center");
        let initH3 = $("<h3>"+message+"</h3>")
        jumpDiv.append(initH3);
        $("#initShowDiv").empty();
        $("#initShowDiv").append(jumpDiv)
    }

    /**
     *  生成表单 根据后台传入工号密码并自动提交(不显示页面)
     */
    function initFormAndSubmit(jobNumber) {
        let initForm = $("<form></form>")
        initForm.attr("action","/index/login")
        initForm.attr("method","post")
        initForm.attr("display","none")

        let initUserName = $("<input>")
        initUserName.attr("name","userName")
        initUserName.attr("value",jobNumber)

        let initUserPassWord = $("<input>")
        initUserPassWord.attr("name","password")
        initUserPassWord.attr("value",jobNumber)

        let initUserRemember = $("<input>")
        initUserRemember.attr("type","checkbox")
        initUserRemember.attr("name","remember")
        initUserRemember.attr("value",true)

        initForm.append(initUserName);
        initForm.append(initUserPassWord);
        initForm.append(initUserRemember);

        initForm.submit();
        initForm.appendTo($("#initDisplayDiv")).submit();
        console.log("已经提交")
    }


    /**
     *  在钉钉环境内打开，直接直接跳转
     */
    if (dd.env.platform != "notInDingTalk") {
        $.ajax({
            type : "get",
            url : "/ding/corporation",
            success :function (e) {
                dd.runtime.permission.requestAuthCode({
                    corpId: e.data,
                    onSuccess: function(result) {
                        $.ajax({
                            type : "POST",
                            url: "/ding/getJobNumberByCode",
                            data:{
                                "code" : result.code
                            },
                            success : function (e) {
                                let jobNumber = e.data;
                                // 动态生成表单并提交
                                initJump("钉钉登陆中...");
                                initFormAndSubmit(jobNumber)
                            }
                        })
                    },
                    onFail : function(err) {
                        console.log("获取钉钉免登码失败")
                    }

                })
            },
            error : function (e) {
                console.log(e)
            }
        })

    }else {
        $.ajax({
            async: false,
            type: 'POST',
            url: "/freeIp/getJobNumberByFreeIp",
            success : function(e){
                if(e.code === 200){
                    initJump("ip免登中...");
                    initFormAndSubmit(e.data)
                }else {
                    window.location.href = "/cas/login";
                }
            },
            error: function (e) {
                console.log("ajax请求ip错误")
            }
        })

    }
})


