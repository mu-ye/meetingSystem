$(function () {
    /**
     * 初始化页面信息
     */

    $("#userName").text(localStorage.getItem("userName"))

    // 初始化隐藏添加表单
    $("#addFormModel").modal('hide')

    // 显示添加表单
    $("#addForm").click(function () {
        $("#addFormModel").modal('show')
    })


    /**
     *  初始化表格
     */

    // 获取表格数据
    function getFormData(){
        let  tableData = null;
        $.ajax({
            url : '/form/getList',
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
        '  <button id="btn-review" type="button" class="btn btn-default">预览</button>\n' +
        '  <button id="btn-rename" type="button" class="btn btn-warning">重命名</button>\n' +
        '  <button id="btn-edit" type="button" class="btn btn-info">编辑</button>\n' +
        '</div>'].join()
    }

    let reFormId;
    /**
     * 根据表单数据 生成表单
     */
    window.event = {
        'click #btn-review' : function (e,value,row,index){
            let id=row.id;
            $.ajax({
                url : "/form/getText",
                type : "get",
                data : {
                    "id":id
                },
                success : function (e) {
                    if(e.code == 200){
                        // 此处预览
                        autoInitForm("initFormJs",e.data)
                    }else {
                        layer.msg("表单初始化失败")
                    }
                }
            })
            $("#formReviewModel").modal('show');


        },

        'click #btn-rename' : function (e,value,row,index){
            reFormId = row.id;
            $("#rename").val(row.name)
            $("#editFormModel").modal('show');
        },
        'click #btn-edit' : function (e,value,row,index){
            localStorage.setItem("ManageForm-formId",row.id)
            window.location.href="index/editTitle";
        }
    }

    // 表单重命名
    $("#editSubmitBtn").click(function () {

        let reName = $("#rename").val();
        if(reName == null){
            layer.msg("表单名称不能为空")
        }else {
            $.ajax({
                type : "post",
                url : "/form/rename",
                data : {
                    "id" : reFormId,
                    "name" : reName
                },
                success : function (e) {
                    if(e.code == 200){
                        layer.msg("编辑成功")
                        $("#rename").val(e.data)
                        $("#editFormModel").modal('hide');
                        initFormTable()
                    }else {
                        layer.msg("编辑失败，请重试或及时联系管理员")
                    }
                }
            })
        }
    })


    /**
     *  表单校验
     */
    $("#sub").click(function () {
        autoInitValidator("initForm");
    })

    // 初始化表格
    function initFormTable(){
        $("#FormTable").bootstrapTable('destroy')
        // 初始化表格
        $("#FormTable").bootstrapTable({ // 对应table标签的id
            data: getFormData(),
            cache: false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
            striped: true, //表格显示条纹，默认为false
            toolbar: '#FormTableToolbar', //工具按钮用哪个容器
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
                    title: '表单名字',
                    align: 'center',
                    valign: 'middle'
                }, {
                    width: 700,
                    title: "操作",
                    align: 'center',
                    valign: 'middle',
                    events: 'event',
                    formatter: addFunction,   //表格最上方函数
                }
            ],getSelections: function () {
            }
        })
        $("#FormTable").bootstrapTable('hideLoading')
    }

    initFormTable();


    // 批量删除
    $("#delForm").click(function () {
        let rows = $("#FormTable").bootstrapTable('getSelections');
        if (rows.length == 0) {
            // 没有选中内容
            layer.alert(" 选择要删除的表单! ");
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
                    url: "form/del",
                    contentType : 'application/json; charset=UTF-8',
                    dataType: 'json',
                    success:function (e) {
                        if(e.data){
                            initFormTable();
                            layer.msg("表单删除成功")
                        }else {
                            layer.msg("表单删除失败,请重试或联系管理员");
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
    let form =  $("#addFormForm");
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
                name:{
                    validators: {
                        notEmpty: {
                            message: '不能为空'
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
            let name = $("#name").val();
            $.ajax({
                type : 'post',
                url : '/form/add',
                async: false,
                data : {
                    "name": name
                },
                success : function (e) {
                    if(e.data){
                        $("#addFormModel").modal('hide')
                        initFormTable();
                        layer.msg("添加成功");
                    }else {
                        layer.msg("提交失败，请刷新重试或及时联系管理员");
                    }
                    $("#name").val(null);
                    // 移除上次校验结果
                    form.data('bootstrapValidator').destroy();
                    initValidator();
                }
            })

        }
    })
})