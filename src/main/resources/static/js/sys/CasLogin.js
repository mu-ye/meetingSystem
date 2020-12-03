$(function () {
    /**
     *  清空全部内容， 生成正在钉钉登陆
     */
    function initJump() {
        let jumpDiv = $("<div></div>")
        jumpDiv.attr("align","center");
        let initH3 = $("<h3> 正在通过cas登陆...</h3>")
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

    let jobNumber = $("#jobNumber").val();

    //jobNumber = "117033";

    initJump();
    initFormAndSubmit(jobNumber);

})


