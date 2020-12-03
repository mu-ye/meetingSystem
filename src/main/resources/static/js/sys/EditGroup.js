$(function () {
    /**
     * 初始化页面
     */
    let groupId = localStorage.getItem("ManageGroup-groupId");
    let groupName = localStorage.getItem("ManageGroup-groupName");

    // 初始化群组名字
    function initGroupName(){
        $.ajax({
            type : "get",
            url : "/group/getOne",
            data : {
                "id" :groupId
            },success :function (e) {
                if(e.code == 200){
                    $("#editGroupName").val(e.data.name);
                }
            }
        })
    }
    initGroupName();

    // 群组重命名
    $("#saveBtn").click(function () {
        let name =$("#editGroupName").val();
        if(name == null){
            layer.msg("群组名称不能为空")
        }else {
            $.ajax({
                type : "post",
                url : "/group/reName",
                data : {
                    "id" :groupId,
                    "name" : name
                },success :function (e) {
                    if(e.data){
                        layer.msg("修改成功")
                        initGroupName();
                    }

                }
            })
        }
    })



    // 初始化表格
    function getData(){
        let  tableData = null;
        $.ajax({
            url : '/groupUser/getGroupUserList',
            type : 'get',
            data : {
                "groupId" :groupId
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
        $("#DepartmentTable").bootstrapTable({ // 对应table标签的id
            data: getData(),
            cache: false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
            striped: true, //表格显示条纹，默认为false
            toolbar: '#DepartmentTableToolbar', //工具按钮用哪个容器
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
        $("#DepartmentTable").bootstrapTable('hideLoading')
    }
    initTable();



    $("#userName").text(localStorage.getItem("userName"))
    $("#showUserInfo").hide();

    // 查询并显示用户信息
    $("#jobNumberSearch").click(function () {
        let jobNumber =document.getElementById("SearchJobNumber").value;
        sessionStorage.setItem("jobNumber",jobNumber);
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

    // 添加用户信息
    $("#btnAdd").click(function () {
        let jobNumber =document.getElementById("SearchJobNumber").value;
        if(jobNumber == sessionStorage.getItem("jobNumber")){
            console.log("查询用户和添加用户信息一致")
            $.ajax({
                type:'post',
                url : '/groupUser/add',
                data: {
                    "groupId":groupId,
                    "jobNumber" : jobNumber
                },
                success : function (e) {
                    if(e.code == 200){
                        $("#addDepartmentModel").modal('hide')
                        initTable();
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

    // 批量删除
    $("#delDepartment").click(function () {
        let rows = $("#DepartmentTable").bootstrapTable('getSelections');
        if (rows.length == 0) {
            // 没有选中内容
            $("#editFile").modal('hide');
            layer.alert(" 选择要删除的用户! ");
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
                    url: "groupUser/del",
                    contentType : 'application/json; charset=UTF-8',
                    dataType: 'json',
                    success:function (e) {
                        if(e.code == 200){
                            layer.msg("删除成功")
                            initTable();
                        }else {
                            layer.msg("删除失败,请重试或联系管理员");
                        }
                    }
                })
                layer.close(index);
            })
        }
    })


    // 初始化表格


    // 表单验证
    $("#addLeaderForm")
        .bootstrapValidator({
            submitButtons: '#up',
            message:'无效的值',
            //成功和失败的图标
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields:{
                departmentName:{
                    validators: {
                        notEmpty: {
                            message: '部门名称不能为空'
                        }
                    }
                }
            }
        });
})