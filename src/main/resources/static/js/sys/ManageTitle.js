$(function () {
    /**
     * 初始化页面信息
     */

    $("#userName").text(localStorage.getItem("userName"))
    let editTitleId;

    // 初始化隐藏添加表单
    $("#addTitleModel").modal('hide')

    // 显示添加表单
    $("#addTitle").click(function () {
        $("#addTitleModel").modal('show')
    })


    /**
     *  初始化表格
     */

    // 获取表格数据
    function getTitleData(){
        let  tableData = null;
        $.ajax({
            url : '/title/getList',
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
            editTitleId = row.id;
            $("#editTitleName").val(row.titleName)
            $("#editPrompt").val(row.prompt)
            $("#editTitleType").val(row.titleType)
            $("#editData").val(row.data)
            $("#editTitleModel").modal("show");
            }
    }

    // 初始化表格
    function initTitleTable(){
        $("#TitleTable").bootstrapTable('destroy')
        // 初始化表格
        $("#TitleTable").bootstrapTable({ // 对应table标签的id
            data: getTitleData(),
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
                    field: 'titleName',    //与返回json 字符串中的内容进行匹配
                    title: '表单名字',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width:  500,
                    field: 'prompt',    //与返回json 字符串中的内容进行匹配
                    title: '提示信息',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width:  500,
                    field: 'titleType',    //与返回json 字符串中的内容进行匹配
                    title: '数据类型',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width:  500,
                    field: 'data',    //与返回json 字符串中的内容进行匹配
                    title: '默认（选择）数据',
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
        $("#TitleTable").bootstrapTable('hideLoading')
    }

    initTitleTable();

    // 批量删除
    $("#delTitle").click(function () {
        let rows = $("#TitleTable").bootstrapTable('getSelections');
        if (rows.length == 0) {
            // 没有选中内容
            $("#editFile").modal('hide');
            layer.alert(" 选择要删除的表头! ");
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
                    url: "title/del",
                    contentType : 'application/json; charset=UTF-8',
                    dataType: 'json',
                    success:function (e) {
                        if(e.data){
                            initTitleTable();
                            layer.msg("表头删除成功")
                        }else {
                            layer.msg("表头删除失败,请重试或联系管理员");
                        }
                    }
                })
                layer.close(index);
            })
        }
    })


    /**
     *  统一的表单验证
     */
    function commonValidator(formId){
        let form = $('#'+formId+'');
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
                titleName:{
                    validators: {
                        notEmpty: {
                            message: '不能为空'
                        }
                    }
                },
                prompt:{
                    validators: {
                        notEmpty: {
                            message: '不能为空'
                        }
                    }
                },
                dataType:{
                    validators: {
                        notEmpty: {
                            message: '不能为空'
                        }
                    }
                },
                data:{
                    validators: {
                        notEmpty: {
                            message: '不能为空'
                        }
                    }
                }
            }
        });
        let bv = form.data('bootstrapValidator')
        bv.validate();
        return bv.isValid();

    }


    /**
     *  ajax提交  添加输入框
     */
    $("#submitBtn").click(function () {
        // 进行表单验证
        if(commonValidator("addInputTitleForm")){
            $.ajax({
                type : 'post',
                url : '/title/addTitle',
                async: false,
                data : {
                    "titleName": $("#titleName").val(),
                    "prompt" : $("#prompt").val(),
                    "titleType" : $("#titleType").val(),
                    "data" : $("#inputData").val()
                },
                success : function (e) {
                    if(e.data){
                        $("#addTitleModel").modal('hide')
                        initTitleTable();
                        layer.msg("添加成功");
                    }else {
                        layer.msg("提交失败，请刷新重试或及时联系管理员");
                    }
                    $("#titleName").val(null);
                    $("#prompt").val(null);
                    $("#titleType").val(null);
                    $("#inputData").val(null);
                    // 刷新浏览器
                    window.location.reload();
                }
            })

        }
    })


    /**
     * 编辑表头信息
     */
    $("#editBtn").click(function () {
        if(commonValidator("editInputTitleForm")){
            let data = $("#data").val();
            $.ajax({
                type : 'post',
                url : '/title/edit',
                "id" : editTitleId,
                async: false,
                data : {
                    "titleName": $("#editTitleName").val(),
                    "prompt" : $("#editPrompt").val(),
                    "titleType": $("#editTitleType").val(),
                    "data" :$("#editData").val(),
                    "dataType" : "-",
                    "id": editTitleId
                },
                success : function (e) {
                    if(e.data){
                        $("#editTitleModel").modal('hide')
                        initTitleTable();
                        layer.msg("编辑成功");
                    }else {
                        layer.msg("编辑失败，请刷新重试或及时联系管理员");
                    }
                    $("#titleName").val(null);
                    $("#prompt").val(null);
                    // 移除上次校验结果
                    editForm.data('bootstrapValidator').destroy();
                    initEditValidator();
                }
            })

        }
    })


    /**
     * 添加下拉列表
     */
    $("#addSelect").click(function () {
        $("#addSelectModel").modal('show')
    })


















})