$(function () {
    /**
     * 初始化页面设置 & 全局变量
     */
    let allDeps ="1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26";


    let menuId = null;
    let meetingId = localStorage.getItem("ManageMeeting-meetingId")

    console.log("编辑会议meetingId"+meetingId)
    $("#formMeetingId").val(meetingId);





    /**
     *  弹出根据群组添加文件上传人 模块框
     */
    $("#addFileUpBtnByGroup").click(function () {
        $("#AttendMeetingByGroupModel").modal('show');
    })


    /**
     * 根据群组添加文件上传人
     */
    $("#addFileUpUserByGroup").click(function () {
        let groupId = $("#groupId").val();
        if(groupId === null){
            layer.msg("请先选择群组")
        }else {
            $.ajax({
                type:'get',
                url : '/groupUser/getUpLoadUsersByGroup',
                data: {
                    "groupId" : groupId
                },
                success : function (e) {
                    if(e.code == 200){
                        $("#uploadUsersText").val($("#uploadUsersText").val()+e.data);
                        $("#AttendMeetingByGroupModel").modal('hide');
                    }else {
                        layer.msg("获取用户信息失败")
                    }
                }
            })
        }
    })



    $("#changeNameModel").modal('hide');
    $("#addSameLevelNode").modal('hide');
    $("#addNextLevelNode").modal('hide');
    $("#addFileUpUserModel").modal('hide');
    $("#editMenuShow").hide();
    initTreeMenu()

    // 日期选择器
    $('#startDate').datetimepicker({
        format: 'YYYY-MM-DD hh:mm:ss',
        locale: 'zh-CN'
    });
    $('#endDate').datetimepicker({
        format: 'YYYY-MM-DD hh:mm:ss',
        locale: moment.locale('zh-cn')
    })

    /**
     *  开启会议 关闭会议
     */

    // 关闭会议
    $("#closeMeeting").click(function () {
        $.ajax({
            type : 'get',
            url : "/meeting/closeMeeting",
            data : {
                "id" : meetingId
            },
            success : function (e) {
                if(e.data){
                    layer.msg("会议已关闭")
                }else {
                    layer.msg("会议关闭失败，及时联系管理员")
                }
            }
        })
    })
    // 开启会议
    $("#openMeeting").click(function () {
        $.ajax({
            type : 'get',
            url : "/meeting/openMeeting",
            data : {
                "id" : meetingId
            },
            success : function (e) {
                if(e.data){
                    layer.msg("会议已开启")
                }else {
                    layer.msg("会议开启失败，及时联系管理员")
                }
            }
        })
    })

    // 选择文件上传人
    $("#choseAttendUserInfo").click(function () {
        layer.open({
            type: 2,
            content: 'https://www.baidu.com/'
        });
    })


    /**
     * 编辑会议表单详情
     */

    // 获取会议表单详情
    function initMeeting() {
        $.ajax({
            type : 'get',
            url : "/meeting/getOneById",
            async:false,
            data : {
                "id" : meetingId
            },
            success : function (e) {
                if(e.code === 200){
                    // 表单默认值
                    $("#name").val((e.data).name);
                    $("#address").val((e.data).address);
                    // 单选按钮
                    if((e.data).attendPassword == "N"){
                        $("#radioCheckNo1").prop("checked",true)
                    }else {
                        $("#radioCheckYes1").prop("checked",true)
                    }
                    $("#endDate").val((e.data).endData);
                    $("#startDate").val((e.data).startDate);

                    // 下拉列表


                    $("#type").val((e.data).type);
                    $("#type  option[value="+(e.data).type+"] ").prop("selected",true)

                    $("#templateId").val((e.data).type);
                    $("#templateId  option[value="+(e.data).templateId+"] ").prop("selected",true)

                }else {
                    layer.msg("会议详情获取失败")
                }
            }
        })
    }
    initMeeting()

    // 重置会议
    $("#recover").click(function () {
        initMeeting();
    })

    // 将用户信息转换格式  （牟欢）117042
    function changeUserInfoAndName(str){
        let arr = str.split("所在部门")
        return ("("+arr[0].substring(6,12)+")"+arr[0].substring(20));
    }

    /**
     * 树形目录
     */


    // 获取树形目录数据
    function getTemplateMenuData() {
        let treeData = null;
        $.ajax({
            type : 'get',
            url: '/meetingMenu/getMeetingMenu',
            data : {
                "meetingId" : meetingId
            },
            // 此处获取值必须是同步获取，否则menu数据值可能为NULL
            async : false,
            success :function (e) {
                if(e.code == 200){
                    treeData =e.data;
                }else{
                    layer.msg("获取目录数据失败[错误信息："+e.toString()+"]")
                }
            }
        })
        return treeData;
    }

    // 初始化radio
    function initRadio() {
        $("#showCheckList").hide();
        $("#showFormList").hide();
        $("#showDepList").hide()
        $("#checkUserText").val("");
        $("#selectForm").val("");
    }




    // 初始化树形目录
    function initTreeMenu(){
        // 初始化目录
        $('#templateMenu').treeview({
            data: getTemplateMenuData(),
            levels: 5,
        });

        // 初始化点击事件
        $("#templateMenu").on('nodeSelected',function (event,data) {
            $("#p_path").text("当前位置: / "+data.text);
            menuId=data.id;
            let menu = null;

            // 通过ID 获取节点全部信息
            $.ajax({
                type : 'get',
                url: '/meetingMenu/getOne',
                data : {
                    "id" : menuId
                },
                // 此处获取值必须是同步获取，否则menu数据值可能为NULL
                async : false,
                success :function (e) {
                    if(e.code == 200){
                        menu =e.data;
                    }else{
                        console.log("获取节点信息失败")
                    }
                }
            })

            // 文件上传人
            let fileUpUsers = menu.fileUpJobNumbers;
            if(fileUpUsers === "0"){
                $("#uploadUsersText").val("暂无文件上传人")
            }else {
                $("#uploadUsersText").val(fileUpUsers)
            }
            initRadio();

            // 是否需要审批  审批节点内容
            let checkText = menu.checkList;
            if(checkText === "[Y]"){
                $("#radioCheckNo").prop("checked",true);
            }else {
                $("#radioCheckYes").prop("checked",true);
                $("#showCheckList").show();
                if(checkText === "[Y]"){
                    $("#checkUserText").val("")
                }else {
                    $("#checkUserText").val(checkText)
                }

            }

            // // 选择参会部门
            // let checkDep = menu.depIds;
            // console.log("checkDep :"+checkDep)
            // if(checkDep === allDeps){
            //     console.log("radioDepNo")
            //     $("#radioDepNo").prop("checked",true);
            // }else {
            //     console.log("radioDepNo")
            //     $("#radioDepYes").prop("checked",true);
            //     $("#showDepList").show();
            // }



            // 是否需要表单
            let formText = menu.formId;

            if(formText === 0){
                $("#radioFormNo").prop("checked",true);
            }else {
                $("#radioFormYes").prop("checked",true);
                $("#showFormList").show();
                console.log(formText)
                // 下拉列表选中默认值
                $("#selectForm  option[value="+formText+"] ").prop("selected",true)
            }
            $("#editMenuShow").show();
        })
    }
    /**
     *  编辑节点
     */

    // 重命名会议目录结构 ：
    $("#changeName").click(function () {
        $("#changeNameModel").modal('show');
    })
    $("#submitChangeNodeName").click(function () {
        let text =document.getElementById("changeNodeName").value;
        if(text == ""){
            layer.msg("节点名称不能为空")
        }else {
            $.ajax({
                type : 'get',
                url: '/meetingMenu/changeName',
                data : {
                    "id" : menuId,
                    "text" :text
                },
                // 此处获取值必须是同步获取，否则menu数据值可能为NULL
                async : false,
                success :function (e) {
                    if(e.data){
                        initTreeMenu();
                        $("#changeNodeName").val("");
                        layer.msg("重命名成功")
                    }else{
                        layer.msg("重命名失败")
                    }
                }
            })
        }
    })

    // 删除节点
    $("#delNode").click(function () {
        layer.confirm('该节点和节点下的子节点都将被删除，确认删除?',function (index) {
            $.ajax({
                type: 'POST',
                data: {
                    "id":menuId
                },
                url: "meetingMenu/del",
                success:function (e) {
                    if(e.code == 200){
                        initTreeMenu();
                        layer.msg("删除成功")
                    }else {
                        layer.msg("删除失败,请重试或联系管理员");
                    }
                }
            })
            layer.close(index);
        })
    })

    // 添加同级节点
    $("#addSameLevelNode").click(function () {
        $("#addSameLevelNodeModel").modal('show');
    })
    $("#submitSameLevelName").click(function() {
        let text =document.getElementById("sameLevelNodeName").value;
        if(text === ""){
            layer.msg("节点名称不能为空")
        }else {
            $.ajax({
                type : 'get',
                url: '/meetingMenu/addSameNode',
                data : {
                    "id" : menuId,
                    "text" :text,
                    "meetingId" : meetingId   //默认创建的模板
                },
                // 此处获取值必须是同步获取，否则menu数据值可能为NULL
                async : false,
                success :function (e) {
                    if(e.data){
                        initTreeMenu();
                        $("#sameLevelNodeName").val("");
                        layer.msg("添加成功")
                    }else{
                        layer.msg("添加失败")
                    }
                }
            })
        }
    })

    // 添加子级节点
    $("#addNextLevelNode").click(function () {
        $("#addNextLevelNodeModel").modal('show');
    })
    $("#submitNextLevelNodeName").click(function() {
        let text =document.getElementById("nextLevelNodeName").value;
        if(text === ""){
            layer.msg("节点名称不能为空")
        }else {
            $.ajax({
                type : 'get',
                url: '/meetingMenu/addNextNode',
                data : {
                    "id" : menuId,
                    "text" :text,
                    "meetingId" : meetingId   //默认创建的模板
                },
                // 此处获取值必须是同步获取，否则menu数据值可能为NULL
                async : false,
                success :function (e) {
                    if(e.data){
                        initTreeMenu();
                        $("#nextLevelNodeName").val("");
                        layer.msg("添加成功")
                    }else{
                        layer.msg("添加失败")
                    }
                }
            })
        }
    })

    /**
     *   表单编辑
     */

    // 是否需要审批
    $("input[name='radioCheck']").change(function(){
        let checkData = $("input[name='radioCheck']:checked").val();
        if(checkData == "checkYes"){
            $("#showCheckList").show();
        }else{
            $("#showCheckList").hide();
        }
    })

    // // 是否选择部门
    // $("input[name='radioDep']").change(function(){
    //     let checkData = $("input[name='radioDep']:checked").val();
    //     if(checkData === "depAll"){
    //         $("#showDepList").hide();
    //     }else{
    //         $("#showDepList").show();
    //     }
    // })

    // 是否需要表头
    $("input[name='radioForm']").change(function(){
        let checkData = $("input[name='radioForm']:checked").val();
        if(checkData == "formYes"){
            $("#showFormList").show();
        }else{
            $("#showFormList").hide();
        }
    })

    // 添加文件上传人

    $("#fileUpUserBtn").click(function () {
        $("#addFileUpUserModel").modal('show');
    })
    $("#jobNumberSearch").click(function () {
        let jobNumber =document.getElementById("SearchJobNumber").value;
        if(jobNumber == ""){
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
                        $("#textAreaUserInfoText").val(e.data)
                        $("#showUserInfo").show();
                    }else {
                        layer.msg(e.msg)
                    }
                }
            })
        }
    })


    $("#btnAdd").click(function () {
        let userInfo = $("#textAreaUserInfoText").val();
        $("#uploadUsersText").val((document.getElementById("uploadUsersText").value+","+changeUserInfoAndName(userInfo)).replace(/\s*/g,""))
        $("#addFileUpUserModel").modal('hide');
        // 重置查询内容
        $("#SearchJobNumber").val("")
        $("#textAreaUserInfoText").val("")
    })

    // 添加审批人

    $("#checkUserTextBtn").click(function () {
        $("#addUserCheckModel").modal('show');
    })
    $("#checkSearchBtn").click(function(){
        let jobNumber =document.getElementById("SearchCheckJobNumber").value;
        if(jobNumber == ""){
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
                        $("#textAreaCheckUserInfoText").val(e.data)
                        $("#showUserInfoCheck").show();
                    }else {
                        layer.msg(e.msg)
                    }
                }
            })
        }
    })

    $("#btnAddCheck").click(function () {
        let userInfo = $("#textAreaCheckUserInfoText").val();
        let text = document.getElementById("checkUserText").value;
        if(text ===""){
            $("#checkUserText").val(changeUserInfoAndName(userInfo))
        }else {
            $("#checkUserText").val((document.getElementById("checkUserText").value+"-->"+changeUserInfoAndName(userInfo)).replace(/\s*/g,""))
        }
        $("#addUserCheckModel").modal('hide');
        // 重置查询内容
        $("#SearchCheckJobNumber").val("")
        $("#textAreaCheckUserInfoText").val("")
    })


    // 提交表单
    $("#saveMenu").click(function () {

        let formId = 0;
        let checkList = "[Y]";
        let fileUpJobNumbers;
        let formData = $("input[name='radioForm']:checked").val();
        if(formData === "formNo"){
            formId = 0;
        }else {
            formId = $("#selectForm").val();
        }
        let checkData = $("input[name='radioCheck']:checked").val();
        if(checkData === "checkNo"){
            checkList = "[Y]";
        }else {
            checkList = $("#checkUserText").val();
        }
        fileUpJobNumbers = $("#uploadUsersText").val();


       /* // 部门信息
        let depIds = "";
        let depData = $("input[name='radioDep']:checked").val();
        if(depData === "depAll"){
            depIds = allDeps;
        }else {
            depIds = $("#selectDep").val().toString();
        }
        console.log("depIds:"+depIds)*/



        $.ajax({
            type : 'post',
            url: '/meetingMenu/editMenu',
            data : {
                "id" : menuId,
                "fileUpJobNumbers" :fileUpJobNumbers,
                "formId":formId,
                "checkList":checkList,
                "depIds" : "0"
            },
            // 此处获取值必须是同步获取，否则menu数据值可能为NULL
            async : false,
            success :function (e) {
                if(e.data){
                    layer.msg("保存成功")
                }else{
                    layer.msg("保存失败")
                }
            }
        })
    })

    /**
     *  表单预览
     */
    $("#formReview").click(function () {
        let formId = $("#selectForm").val();
        if(formId == null){
            layer.msg("请选中表单后再预览")
        }else{

            autoInitForm("initFormJs",getReviewFormData(formId))
            // 显示出预览表单
            $("#formReviewModel").modal('show');
        }
    })

    /**
     *  生成表单的校验
     */
    /**
     *  表单校验
     */
    $("#sub").click(function () {
        autoInitValidator("initForm");
    })


    /**
     * 参会人员列表
     */

    // 获取 表格数据

    function getAttendMeetingData(){
        let  tableData = null;
        $.ajax({
            url : '/meetingAttend/getGroupUserList',
            type : 'get',
            async : false,
            data : {
                "meetingId":meetingId
            },
            success : function (e) {
                if(e.code == 200){
                    tableData = e.data;
                }else {
                    console.log("返回异常，错误信息 ："+e)
                }
            }
        })
        return tableData;
    }
    function initMeetingAttendTable(){
        $("#MeetingAttendTable").bootstrapTable('destroy')
        // 初始化表格
        $("#MeetingAttendTable").bootstrapTable({ // 对应table标签的id
            data: getAttendMeetingData(),
            cache: false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
            striped: true, //表格显示条纹，默认为false
            toolbar: '#MeetingAttendTableToolbar', //工具按钮用哪个容器
            search: true,
            searchAlign: "right",
            showRefresh: true,  //显示刷新按钮
            columns: [
                {
                    width:  20,
                    checkbox: true,
                    align: "center"
                },
                {
                    width:  500,
                    field: 'jobNumber',    //与返回json 字符串中的内容进行匹配
                    title: '工号',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width:  500,
                    field: 'userName',    //与返回json 字符串中的内容进行匹配
                    title: '姓名',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width:  500,
                    field: 'departmentName',    //与返回json 字符串中的内容进行匹配
                    title: '部门',
                    align: 'center',
                    valign: 'middle'
                }
            ],getSelections: function () {
            }
        })
        $("#MeetingAttendTable").bootstrapTable('hideLoading')
    }
    initMeetingAttendTable();


    /**
     *  根据工号添加用户
     */

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

    // 根据群组工号添加
    $("#addByGroup").click(function () {
        $("#AttendMeetingByGroupModel1").modal('show')
    })

    // 根据群组添加参会人
    $("#addAttendMeetingByGroup").click(function () {
        let groupId = $("#groupId1").val();
        if(groupId === null){
            layer.msg("请先选择群组")
        }else {
            $.ajax({
                type:'post',
                url : '/meetingAttend/addByGroup',
                data: {
                    "groupId" : groupId,
                    "meetingId":meetingId
                },
                success : function (e) {
                    if(e.code == 200){
                        $("#AttendMeetingByGroupModel1").modal('hide')
                        initMeetingAttendTable();
                    }else {
                        layer.msg(e.msg)
                    }
                }
            })
        }
    })

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

    /**
     *  发送钉钉通知
     */
    $("#sendMessage").click(function () {
        let rows = $("#MeetingAttendTable").bootstrapTable('getSelections');
        if (rows.length === 0) {
            // 没有选中内容
            layer.alert("选择接受钉钉消息的人");
            return;
        } else{
            $("#sendMessageModel").modal("show");
        }
    })
    $("#sendBtn").click(function () {
        let rows = $("#MeetingAttendTable").bootstrapTable('getSelections');
        let sendText = $("#messageText").val();
        if(sendText === ""){
            layer.msg("发送的钉钉消息不能为空")
        }else {
            layer.confirm('确认发送钉钉消息?',function (index) {
                let ids = new Array(); // 声明一个数组
                $(rows).each(function() { // 通过获得别选中的来进行遍历
                    ids.push(this.jobNumber); // cid为获得到的整条数据中的一列
                });
                $.ajax({
                    type: 'POST',
                    data:JSON.stringify({"jobNumberList":ids,"text":sendText}),
                    url: "ding/sendMessages1",
                    contentType : 'application/json; charset=UTF-8',
                    dataType: 'json',
                    success:function (e) {
                        if(e.code === 200){
                            layer.msg("钉钉消息发送成功")
                        }else {
                            layer.msg("钉钉消息发送失败");
                        }
                        // 改模弹窗
                        $("#sendMessageModel").modal("hide");
                    }
                })
                layer.close(index);
            })
        }
    })


    /**
     *  获取通知部门
     */
    $("#departmentBtn").click(function () {
        $.ajax({
            url : "/meeting/getAttendDepartment",
            data : {
                "meetingId" : meetingId
            },
            type : "get",
            success: function (e) {
                if(e.code === 200){
                    $("#attendDepartmentText").val(e.data);
                    $("#AttendDepartmentModel").modal('show')
                }
            }
        })

    })


    /**
     *  领导管理
     */


    $("#userName").text(localStorage.getItem("userName"))
    // 初始化隐藏添加表单
    $("#addDepartmentModel").modal('hide')

    // 显示添加表单
    $("#addDepartment").click(function () {
        $("#addDepartmentModel").modal('show')
    })



    // 获取 表格数据

    function getData(){
        let  tableData = null;
        $.ajax({
            url : '/meetingLeader/getList',
            type : 'get',
            data : {
                "meetingId" :meetingId
            },
            async : false,
            success : function (e) {
                if(e.code == 200){
                    tableData = e.data;
                }else {
                    console.log("返回异常，错误信息 ："+e)
                }
            }
        })
        return tableData;
    }
    function initTable(){
        $("#DepartmentTable").bootstrapTable('destroy')
        // 初始化表格
        $("#DepartmentTable").bootstrapTable({ // 对应table标签的id
            data: getData(),
            cache: false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
            striped: true, //表格显示条纹，默认为false
            toolbar: '#DepartmentTableToolbar', //工具按钮用哪个容器
            search: true,
            searchAlign: "right",
            showRefresh: true,  //显示刷新按钮
            columns: [
              /*  {
                    width:  20,
                    checkbox: true,
                    align: "center"
                },*/
                {
                    width:  500,
                    field: 'leaderJobNumber',    //与返回json 字符串中的内容进行匹配
                    title: '领导工号',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width:  500,
                    field: 'name',    //与返回json 字符串中的内容进行匹配
                    title: '领导姓名',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width:  500,
                    field: 'departmentName',    //与返回json 字符串中的内容进行匹配
                    title: '所在部门部门',
                    align: 'center',
                    valign: 'middle'
                },{
                    width: 500,
                    title: "操作",
                    align: 'center',
                    valign: 'middle',
                    events: 'event',
                    formatter: addFunction,   //表格最上方函数
                }
            ],getSelections: function () {
            }
        })
        $("#DepartmentTable").bootstrapTable('hideLoading')
    }

    // 表格中的操作
    function addFunction(value,row,index){
        return ['<div class="btn-group btn-group-sm">\n' +
        '  <button id="btn-del-leader" type="button" class="btn btn-danger">删除</button>\n' +
        '</div>'].join()
    }

    // 编辑操作
    window.event = {
        'click #btn-del-leader' : function (e,value,row,index){
            let id=row.id;
            layer.confirm('确认删除?',function (index) {
                $.ajax({
                    type: 'POST',
                    data: {
                        "meetingId" : meetingId,
                        "id" : id
                    },
                    url: "meetingLeader/delById",
                    success:function (e) {
                        if(e.code == 200){
                            initTable();
                            layer.msg("领导删除成功")
                        }else {
                            layer.msg("领导删除失败,请重试或联系管理员");
                        }
                    }
                })
            })
        }
    }
    initTable();

    // 批量删除
    $("#delDepartment").click(function () {
        let rows = $("#DepartmentTable").bootstrapTable('getSelections');
        if (rows.length == 0) {
            // 没有选中内容
            $("#editFile").modal('hide');
            layer.alert(" 选择要删除的Ip! ");
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
                    url: "meetingLeader/del?meetingId="+meetingId,
                    contentType : 'application/json; charset=UTF-8',
                    dataType: 'json',
                    success:function (e) {
                        if(e.code == 200){
                            initTable();
                            layer.msg("领导删除成功")
                        }else {
                            layer.msg("领导删除失败,请重试或联系管理员");
                        }
                    }
                })
                layer.close(index);
            })
        }
    })

    // 重置参会领导
    $("#reset").click(function () {
        layer.confirm('确认重置参会领导?',function (index) {
            $.ajax({
                type : 'post',
                url : '/meetingLeader/reset',
                async: false,
                data : {
                    "meetingId" :meetingId
                },
                success : function (e) {
                    if(e.data){
                        initTable();
                        layer.msg("重置成功")
                    }
                }
            })
        })
    })



    /**
     * 表单验证，并通过js 提交表单
     */
    /*let form =  $("#addLeaderForm");
    function initValidator(){
        form.bootstrapValidator({
            submitButtons: '#up',
            message:'无效的值',
            //成功和失败的图标
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields:{
                jobNumber:{
                    validators: {
                        notEmpty: {
                            message: '工号不能为空'
                        }
                    }
                },
                ip:{
                    validators: {
                        notEmpty: {
                            message: 'Ip不能为空'
                        }
                    }
                }
            }
        });
    }
    initValidator();*/

    $("#submitBtn").click(function () {
        // 进行表单验证
        /* let bv = form.data('bootstrapValidator')
         bv.validate();
         if(bv.isValid()){*/
        let leaderJobNumber = $("#leaderJobNumber").val();
        let name = $("#name").val();
        let selectDep = $("#selectDep1").val();


        $.ajax({
            type : 'post',
            url : '/meetingLeader/add',
            async: false,
            data : {
                "leaderJobNumber": leaderJobNumber,
                "name" : name,
                "departmentName" : selectDep,
                "meetingId" :meetingId
            },
            success : function (e) {
                $("#leaderJobNumber").val(null);
                $("#name").val(null);
                $("#selectDep").val(null);
                if(e.data){
                    $("#addDepartmentModel").modal('hide')
                    initTable();

                }else {

                    // 移除上次校验结果
                    //form.data('bootstrapValidator').destroy();
                    //initValidator();
                }
            }
        })
    })



})