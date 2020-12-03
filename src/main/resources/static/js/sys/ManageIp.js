$(function () {
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
            url : '/freeIp/getList',
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
                },
                {
                    width:  500,
                    field: 'ip',    //与返回json 字符串中的内容进行匹配
                    title: '免登IP',
                    align: 'center',
                    valign: 'middle'
                }
            ],getSelections: function () {
            }
        })
        $("#DepartmentTable").bootstrapTable('hideLoading')
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
                    url: "freeIp/del",
                    contentType : 'application/json; charset=UTF-8',
                    dataType: 'json',
                    success:function (e) {
                        if(e.code == 200){
                            initTable();
                            layer.msg("Ip删除成功")
                        }else {
                            layer.msg("Ip删除失败,请重试或联系管理员");
                        }
                    }
                })
                layer.close(index);
            })
        }
    })


    /**
     * 表单验证，并通过js 提交表单
     */
    let form =  $("#addLeaderForm");
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
    initValidator();
    $("#submitBtn").click(function () {
        // 进行表单验证
        let bv = form.data('bootstrapValidator')
        bv.validate();
        if(bv.isValid()){
            let ip = $("#ip").val();
            let jobNumber = $("#jobNumber").val();
            $.ajax({
                type : 'post',
                url : '/freeIp/add',
                async: false,
                data : {
                    "ip": ip,
                    "jobNumber" : jobNumber
                },
                success : function (e) {
                    if(e.code == 200){
                        $("#addDepartmentModel").modal('hide')
                        initTable();
                        layer.msg(e.data);
                    }else {
                        layer.msg(e.msg);
                        $("#ip").val(null);
                        $("#jobNumber").val(null);
                        // 移除上次校验结果
                        form.data('bootstrapValidator').destroy();
                        initValidator();
                    }
                }
            })

        }
    })
})