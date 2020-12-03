$(function () {
    /**
     * 初始化页面设置 & 全局变量
     */

    //$("#editTemplateShow").hide();
    $("#editMenuShow").hide();
    $("#changeNameModel").modal('hide');
    $("#addSameLevelNode").modal('hide');
    $("#addNextLevelNode").modal('hide');
    $("#showCheckList").hide();
    $("#showFormList").hide();
    $("#addFileUpUserModel").modal('hide');
    $("#showUserInfoCheck").hide();

    let allDeps ="1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26";
    $("#showDepList").hide();



    let menuId = null;
    let templateId = null;
    // 将用户信息转换格式  （牟欢）117042
    function changeUserInfoAndName(str){
        let arr = str.split("所在部门")
        return ("("+arr[0].substring(6,12)+")"+arr[0].substring(20));
    }

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





    /**
     *  新建模板
     */

    $("#newTemplateBtn").click(function () {
        let templateName = document.getElementById("newTemplateText").value;
        if(templateName == ""){
            layer.msg("模板名不能为空")
        }else {
            $.ajax({
                type : 'post',
                url : '/template/add',
                data : {
                    "name":templateName
                },
                success:function (e) {
                    if(e.code == 200){
                        layer.msg("模板创建成功");
                        sessionStorage.setItem("templateId",JSON.parse(e.data).templateId);
                        sessionStorage.setItem("menuId",JSON.parse(e.data).menuId);
                        // 创建成功后 创建按钮失效
                        $("#newTemplateBtn").attr("disabled",true)
                        $("#newTemplateText").attr("disabled",true)
                        templateId =sessionStorage.getItem("templateId");
                        menuId = sessionStorage.getItem("menuId");
                        initTreeMenu("menuId"+menuId);
                        $("#editTemplateShow").show();
                    }else{
                        layer.msg(e.msg)
                    }
                }
            })
        }
    })

    /**
     * 树形目录
     */


    // 获取树形目录数据
    function getTemplateMenuData() {
        let treeData = null;
        $.ajax({
            type : 'get',
            url: '/templateMenu/getTemplateMenu',
            data : {
                "templateId" : templateId
            },
            // 此处获取值必须是同步获取，否则menu数据值可能为NULL
            async : false,
            success :function (e) {
                if(e.code == 200){
                    treeData =e.data;
                }else{
                    layer.msg("获取目录数据失败[错误信息："+e+"]")
                }
            }
        })
        return treeData;
    }

    // 初始化radio
    function initRadio() {
        $("#showCheckList").hide();
        $("#showFormList").hide();
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
                url: '/templateMenu/getOne',
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

            // 是否需要部门

            // 根据后台传入数据 初始化
            // let checkDep = menu.depIds;
            // console.log("checkDep :"+checkDep)
            // if(checkDep === allDeps){
            //     console.log("radioDepNo")
            //     $("#radioDepNo").prop("checked",true);
            //     $("#showDepList").hide();
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
                url: '/templateMenu/changeName',
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
                url: "templateMenu/del",
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
                url: '/templateMenu/addSameNode',
                data : {
                    "id" : menuId,
                    "text" :text,
                    "templateId" : templateId   //默认创建的模板
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
                url: '/templateMenu/addNextNode',
                data : {
                    "id" : menuId,
                    "text" :text,
                    "templateId" : templateId   //默认创建的模板
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

    // 是否需要表头
    $("input[name='radioForm']").change(function(){
        let checkData = $("input[name='radioForm']:checked").val();
        if(checkData == "formYes"){
            $("#showFormList").show();
        }else{
            $("#showFormList").hide();
        }
    })

    //  切换状态
    $("input[name='radioDep']").change(function(){
        let checkData = $("input[name='radioDep']:checked").val();
        if(checkData === "depAll"){
            $("#showDepList").hide();
        }else{
            $("#showDepList").show();
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
        $("#uploadUsersText").val((document.getElementById("uploadUsersText").value+" , "+changeUserInfoAndName(userInfo)).replace(/\s*/g,""))
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
            $("#checkUserText").val((document.getElementById("checkUserText").value+" --> "+changeUserInfoAndName(userInfo)).replace(/\s*/g,""))
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
            checkList = '[Y]';
        }else {
            checkList = $("#checkUserText").val();
        }
        fileUpJobNumbers = $("#uploadUsersText").val();


        // 部门信息
        // let depIds = "";
        // let depData = $("input[name='radioDep']:checked").val();
        // if(depData === "depAll"){
        //     depIds = allDeps;
        // }else {
        //     depIds = $("#selectDep").val().toString();
        // }
        // console.log("depIds:"+depIds)

        $.ajax({
            type : 'post',
            url: '/templateMenu/editMenu',
            data : {
                "id" : menuId,
                "fileUpJobNumbers" :fileUpJobNumbers,
                "formId":formId,
                "checkList":checkList,
                "depIds":"0"
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

    // 表单预览
    $("#formReview").click(function () {
        let formId = $("#selectForm").val();
        if(formId == null){
            layer.msg("请选中表单后再预览")
        }else{
            // 根据表格ID 获取json 数组
            //let jsonArr = getJsonArr(formId);
            autoInitForm("initFormJs",getReviewFormData(formId))
            // 显示出预览表单
            $("#formReviewModel").modal('show');
        }
    })

    /**
     *  表单校验
     */
    $("#sub").click(function () {
        autoInitValidator("initForm");
    })

})