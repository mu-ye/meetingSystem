$(function () {
     $("#userName").text(localStorage.getItem("userName"))

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
            layer.alert(" 选择要删除的部门! ");
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
                    url: "dep/del",
                    contentType : 'application/json; charset=UTF-8',
                    dataType: 'json',
                    success:function (e) {
                        if(e.code == 200){
                            layer.msg("部门删除成功")
                            $("#DepartmentTable").bootstrapTable("refresh")
                        }else {
                            layer.msg("部门删除失败,请重试或联系管理员");
                        }
                    }
                })
                layer.close(index);
            })
        }
    })

    // 初始化表格
    $("#DepartmentTable").bootstrapTable({ // 对应table标签的id
        url: "dep/getLists",
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
                field: 'departmentName',    //与返回json 字符串中的内容进行匹配
                title: '部门名称',
                align: 'center',
                valign: 'middle'
            }
        ],getSelections: function () {
        }
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