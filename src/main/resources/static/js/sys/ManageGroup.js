$(function () {
    /**
     * 初始化表格
     */

    // 表格操作
    function addFunction(value,row,index){
        return ['<div class="btn-group btn-group-sm">\n' +
        '  <button id="btn-group-edit" type="button" class="btn btn-default">编辑</button>\n' +
        '</div>'].join()
    }

    //操作内容
    window.event = {
        'click #btn-group-edit' : function (e,value,row,index){
            localStorage.setItem("ManageGroup-groupId",row.id)
            localStorage.setItem("ManageGroup-groupName",row.name)
            window.location.href="index/editGroup";
        }
    }

    // 获取表格数据
    function getGroupData(){
        let  tableData = null;
        $.ajax({
            url : '/group/getList',
            type : 'get',
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

    // 初始化表格
    function initGroupTable(){
        $("#GroupTable").bootstrapTable('destroy')
        $("#GroupTable").bootstrapTable({
            data: getGroupData(),
            cache: false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
            striped: true, //表格显示条纹，默认为false
            toolbar: '#GroupTableToolbar', //工具按钮用哪个容器
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
                    field: 'name',    //与返回json 字符串中的内容进行匹配
                    title: '群组名称',
                    align: 'center',
                    valign: 'middle'
                },{
                    width: 50,
                    title: "操作",
                    align: 'center',
                    valign: 'middle',
                    events: 'event',
                    formatter: addFunction,   //表格最上方函数
                }

            ],getSelections: function () {
            }
        })
        $("#GroupTable").bootstrapTable('hideLoading')
    }
    initGroupTable();

    // 点击弹出 新建群组
    $("#addGroup").click(function () {
        $("#addGroupModel").modal('show')
    })

    // 点击创建群组
    $("#creatGroup").click(function () {
        let groupName = document.getElementById("groupName").value;
        if(groupName == ""){
            layer.msg("群组名不为空")
        }else {
            $.ajax({
                type : 'post',
                url : '/group/add',
                data: {
                    "groupName" : groupName
                },
                success:function (e) {
                    if(e.code == 200){
                        layer.msg(e.data);
                        $("#addGroupModel").modal('hide')
                        initGroupTable();
                    }else {
                        layer.msg(e.msg);
                    }
                }
            })
        }
    })


    // 批量删除
    $("#delGroup").click(function () {
        let rows = $("#GroupTable").bootstrapTable('getSelections');
        if (rows.length == 0) {
            // 没有选中内容
            $("#editFile").modal('hide');
            layer.alert(" 选择要删除的群组! ");
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
                    url: "group/del",
                    contentType : 'application/json; charset=UTF-8',
                    dataType: 'json',
                    success:function (e) {
                        if(e.code == 200){
                            layer.msg("删除成功")
                            initGroupTable();
                        }else {
                            layer.msg("删除失败,请重试或联系管理员");
                        }
                    }
                })
                layer.close(index);
            })
        }
    })














    $("#addGroup2").click(function(e){
        let jobNumber =document.getElementById("SearchJobNumber").value;
        if(jobNumber == sessionStorage.getItem("jobNumber")){
            console.log("查询用户和添加用户信息一致")
            $.ajax({
                type:'post',
                url : '/userRole/add',
                data: {
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



    // 初始化隐藏添加表单
    $("#addDepartmentModel").modal('hide')

    // 显示添加表单
    $("#addDepartment").click(function () {
        $("#addDepartmentModel").modal('show')
    })






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