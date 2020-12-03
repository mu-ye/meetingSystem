$(function () {
    /**
     * 初始化页面信息
     */
    let formId = localStorage.getItem("ManageForm-formId")

    $("#userName").text(localStorage.getItem("userName"))

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
            url : '/title/getEditFormTitleList',
            type : 'get',
            async : false,
            data : {
                "formId" :formId
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

    // 表格中的操作
    function addFunction(value,row,index){
        return ['<div class="btn-group btn-group-sm">\n' +
        '  <button id="btn-edit" type="button" class="btn btn-info">编辑</button>\n' +
        '</div>'].join()
    }

    window.event = {
        'click #btn-edit' : function (e,value,row,index){
            $("#titleIdInput").val(row.id)
            $("#editTitleModel").modal('show')
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
                    title: '表头类型',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width:  500,
                    field: 'data',    //与返回json 字符串中的内容进行匹配
                    title: '默认（选择）数据',
                    align: 'center',
                    valign: 'middle'
                }, {
                    width:  500,
                    field: 'autoIndex',    //与返回json 字符串中的内容进行匹配
                    title: '显示顺序（数值越大越靠前）',
                    align: 'center',
                    valign: 'middle'
                }, {
                    width:  500,
                    field: 'width',    //与返回json 字符串中的内容进行匹配
                    title: '宽度',
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
                    url: "/formTitle/del",
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
     * 编辑表单显示顺序
     */
    $("#editIndexBtn").click(function () {
        let autoIndex = $("#autoIndex").val();
        let width = $("#width").val();
        let titleId = $("#titleIdInput").val();
        let pattern = /^[0-9]*$/;
        if(pattern.test(autoIndex)){
            $.ajax({
                url : "/formTitle/editAutoIndex",
                data: {
                    "formTitleId" : titleId,
                    "autoIndex":autoIndex,
                    "width" :width
                },
                type : "post",
                success : function (e) {
                    if(e.data){
                        layer.msg("编辑成功");
                        $("#editTitleModel").modal('hide');
                        initTitleTable();
                    }else {
                        layer.msg("编辑失败,重试或及时联系管理员");
                    }
                }
            })

        }else {
            layer.msg("显示顺序只能树数字，请重新填写")
            $("#autoIndex").val("")
        }

        console.log(pattern.test(autoIndex));
    })

    /**
     *  显示添加表头弹窗
     */
    $("#addTitle").click(function () {
       // alert(1)
        $("#addTitleModel").modal('show');
    })

    /**
     *  添加表头
     */
    $("#addTitleBtn").click(function () {
        let titleIds = $("#selectForm").val().toString();
        if(titleIds == null){
            layer.msg("选择要添加的表头后再提交")
        }else {
            $.ajax({
                type : "post",
                url : "/formTitle/add",
                data: {
                    "titleIds" : titleIds,
                    "formId" : formId
                },success :function (e) {
                    if(e.code == 200){
                        layer.msg("添加成功")
                        initTitleTable();
                    }else {
                        layer.msg("添加失败，重试或及时联系管理员")
                    }
                    $("#addTitleModel").modal("hide")
                }

            })
        }
    })


    /**
     * 表单验证，并通过js 提交表单
     */
    let form =  $("#addInputTitleForm");
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
            let titleName = $("#titleName").val();
            let prompt = $("#prompt").val();
            $.ajax({
                type : 'post',
                url : '/title/addInput',
                async: false,
                data : {
                    "titleName": titleName,
                    "prompt" : prompt
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
                    // 移除上次校验结果
                    form.data('bootstrapValidator').destroy();
                    initValidator();
                }
            })

        }
    })


    /**
     *  表单预览
     */
    $("#review").click(function () {
            $.ajax({
                url : "/form/getText",
                type : "get",
                data : {
                    "id":formId
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
            // 显示出预览表单
            $("#formReviewModel").modal('show');

    })


})