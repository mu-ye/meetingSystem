$(function () {

    // 根据群组添加参会人员
    $("#addByGroup").click(function () {
        $("#AttendMeetingByGroupModel").modal('show')
    })

    // 根据群组添加参会人
    $("#addAttendMeetingByGroup").click(function () {
        let groupId = $("#groupId").val();
        if(groupId === null){
            layer.msg("请先选择群组")
        }else {
            $.ajax({
                type:'get',
                url : '/groupUser/getGroupUserString',
                data: {
                    "groupId" : groupId
                },
                success : function (e) {
                    if(e.code == 200){
                        layer.msg(e.data)
                        $("#userList").val(e.data)
                    }else {
                        layer.msg("获取用户信息失败")
                    }
                }
            })
        }
    })

    // 发送钉钉消息
    $("#btnSend").click(function () {
        if($("#userList").val()== ""){
            layer.msg("通知用户列表不能为空")
        }else {
            if($("#sendMessage").val() == ""){
                layer.msg("消息内容不能为空")
            }else {
                $.ajax({
                    url : "/ding/sendMessages",
                    type : "post",
                    data : {
                        "userLists":$("#userList").val(),
                        "text":$("#sendMessage").val()
                    },
                    success : function (e) {
                        if(e.code == 200){
                            layer.msg("钉钉消息发送成功")
                        }
                    }
                })
            }
        }
    })



    // 根据用户工号添加
    $("#addByJobNumber").click(function () {
        $("#AttendMeetingByJobNumberModel").modal('show')
    })

    // 查询并显示用户信息
    $("#AttendMeetingJobNumberSearch").click(function () {
        let jobNumber =document.getElementById("AttendMeetingJobNumber").value;
        sessionStorage.setItem("attendJobNumber",jobNumber);
        if(jobNumber === ""){
            layer.msg("查询内容不能为空")
        }else {
            $.ajax({
                type: "post",
                url : "/userInfo/getUserInfo",
                data: {
                    "jobNumber" : jobNumber
                },
                success:function (e) {
                    if(e.code == 200){
                        $("#attendTextAreaUserInfoText").val(e.data)
                        $("#attendShowUserInfo").show();
                    }else {
                        layer.msg(e.msg)
                    }
                }
            })
        }
    })

    // 添加用户信息
    $("#attendAddBtn").click(function () {
        let jobNumber = document.getElementById("AttendMeetingJobNumber").value;
        if(jobNumber == sessionStorage.getItem("attendJobNumber")){
            console.log("查询用户和添加用户信息一致")
            $.ajax({
                type:'post',
                url : '/meetingAttend/add',
                data: {
                    "jobNumber" : jobNumber,
                    "meetingId":meetingId
                },
                success : function (e) {
                    if(e.code == 200){
                        $("#AttendMeetingByJobNumberModel").modal('hide')
                        initMeetingAttendTable();
                        layer.msg(e.data)
                    }else {
                        layer.msg(e.msg)
                    }
                }
            })
        }else {
            layer.msg("查询用户和添加用户信息不一致")
        }
    })

    // 初始化隐藏添加表单
    $("#addDepartmentModel").modal('hide')

    // 显示添加表单
    $("#addDepartment").click(function () {
        $("#addDepartmentModel").modal('show')
    })


    /**
     *  根据群组添加用户
     */









    // 批量删除
    $("#delAttendMeeting").click(function () {
        let rows = $("#MeetingAttendTable").bootstrapTable('getSelections');
        if (rows.length === 0) {
            layer.alert(" 选择要删除的人员! ");
            return;
        } else {
            layer.confirm('确认删除?',function (index) {
                let ids = new Array(); // 声明一个数组
                $(rows).each(function() { // 通过获得别选中的来进行遍历
                    ids.push(this.id); // cid为获得到的整条数据中的一列
                });
                $.ajax({
                    type: 'POST',
                    data: JSON.stringify(ids),
                    url: "meetingAttend/del",
                    contentType : 'application/json; charset=UTF-8',
                    dataType: 'json',
                    success:function (e) {
                        if(e.code == 200){
                            initMeetingAttendTable();
                            layer.msg("删除成功")
                        }else {
                            layer.msg("删除失败,请重试或联系管理员");
                        }
                    }
                })
                layer.close(index);
            })
        }
    })

})