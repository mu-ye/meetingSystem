$(function () {
    /**
     *  页面初始化设置
     */

    // 获取用户信息
    $("#userName").text(localStorage.getItem("userName"))

    // 初始化隐藏添加表单
    $("#addDepartmentModel").modal('hide')

    // 显示添加表单
    $("#addDepartment").click(function () {
        $("#addDepartmentModel").modal('show')
    })


    /**
     * 表格
     */

    // 获取 表格数据
    function getData(){
        let  tableData = null;
        $.ajax({
            url : '/template/getList',
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

    // 表格中的操作
    function addFunction(value,row,index){
        return ['<div class="btn-group btn-group-sm">\n' +
        '  <button id="btn-edit" type="button" class="btn btn-info">编辑</button>\n' +
        '</div>'].join()
    }

    // 编辑操作
    window.event = {
        'click #btn-edit' : function (e,value,row,index){
            let id=row.id;
            localStorage.setItem("ManageTemplate-templateId",id)
            window.location.href="index/editTemplate";
        }
    }

    // 初始化表格
    function  initTable(){
        $("#TemplateTable").bootstrapTable('destroy')
        // 初始化表格
        $("#TemplateTable").bootstrapTable({ // 对应table标签的id
            data: getData(),
            cache: false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
            striped: true, //表格显示条纹，默认为false
            toolbar: '#TemplateTableToolbar', //工具按钮用哪个容器
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
                    title: '模板名称',
                    align: 'center',
                    valign: 'middle'
                },{
                    width: 200,
                    title: "操作",
                    align: 'center',
                    valign: 'middle',
                    events: 'event',
                    formatter: addFunction,   //表格最上方函数
                }
            ],getSelections: function () {
            }
        })
        $("#TemplateTable").bootstrapTable('hideLoading')
    }

    initTable();

    // 批量删除
    $("#delDepartment").click(function () {
        let rows = $("#TemplateTable").bootstrapTable('getSelections');
        if (rows.length == 0) {
            // 没有选中内容
            $("#editFile").modal('hide');
            layer.alert(" 选择要删除的模板! ");
            return;
        } else {
            layer.confirm('确认删除?删除后模板数据全部丢失，不可恢复',function (index) {
                let ids = new Array(); // 声明一个数组
                $(rows).each(function() { // 通过获得别选中的来进行遍历
                    ids.push(this.id); // cid为获得到的整条数据中的一列
                });
                $.ajax({
                    type: 'POST',
                    data: JSON.stringify(ids),
                    url: "template/del",
                    contentType : 'application/json; charset=UTF-8',
                    dataType: 'json',
                    success:function (e) {
                        if(e.code == 200){
                            initTable();
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