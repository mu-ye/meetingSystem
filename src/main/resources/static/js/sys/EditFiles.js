$(function () {
    let meetingId = localStorage.getItem("EditFiles-meetingId");
    console.log("meetingId"+meetingId)


    /**
     *  页面初始化设置
     */

    // 获取用户信息
    $("#userName").text(localStorage.getItem("userName"))


    let fileId;

    /**
     * 待审核 文件列表
     */

    // 获取 表格数据
    function getData(){
        let  tableData = null;
        $.ajax({
            url : '/meetingFile/getAllFilesByMeetingId',
            type : 'get',
            data: {
                "meetingId": meetingId
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

    // 表格中的操作
    function addFunction(value,row,index){
        if(row.alive == 0){
            return ['<div class="btn-group btn-group-sm">\n' +
            '  <button id="btn-text-todo" type="button" class="btn btn-warning">表单预览</button>\n' +
            '  <button id="btn-review-todo" type="button" class="btn btn-info">文件预览</button>\n' +
            '  <button id="btn-review-del" type="button" class="btn btn-danger">删除文件</button>\n' +
            '</div>'].join()
        }else {
            return ['<div class="btn-group btn-group-sm">\n' +
            '  <button id="btn-text-todo" type="button" class="btn btn-warning">表单预览</button>\n' +
            '  <button id="btn-review-todo" type="button" class="btn btn-info">文件预览</button>\n' +
            '  <button id="btn-review-recovery" type="button" class="btn btn-default">恢复文件</button>\n' +
            '</div>'].join()
        }

    }

    // 编辑操作
    window.event = {
        // 删除文件
        'click #btn-review-del' : function (e,value,row,index){
            let id=row.id;
            layer.confirm('确认删除?',function (index) {
                $.ajax({
                    type:'post',
                    url:'/meetingFile/del',
                    data:{
                        "id" :id
                    },
                    async:false,
                    success:function (e) {
                        if(e.data){
                            layer.msg("文件删除成功")
                        }else {
                            layer.msg("操作失败，请及时联系管理员")
                        }
                    }
                })
                initTable();
            })
        },
        // 审核未通过
        'click #btn-review-recovery' : function (e,value,row,index){
            let id=row.id;
            layer.confirm('确认恢复文件?',function (index) {
                $.ajax({
                    type:'post',
                    url:'/meetingFile/recover',
                    data:{
                        "id" :id
                    },
                    async:false,
                    success:function (e) {
                        if(e.data){
                            layer.msg("文件已恢复")
                        }else {
                            layer.msg("操作失败，请及时联系管理员")
                        }
                    }
                })
                initTable();
            })
        },
        // 表格内容预览
        'click #btn-text-todo' : function (e,value,row,index){
            initShowTable("tableId",getReviewTableData(row.id))
            $("#formTextReviewModel").modal('show')
        },
        // 表格文件预览
        'click #btn-review-todo' : function (e,value,row,index){
            let id=row.id;
            $.ajax({
                type:'GET',
                data:{
                },
                url: 'meetingFile/getURL?id='+id,
                success:function(e){

                    let entity=JSON.parse(e);
                    if (entity.path!=null) {
                        //window.location.href=entity.path;
                        // 打开新页面
                        console.log("开始要跳转了----")
                        window.open(entity.path)
                    }else{
                        layer.msg("正在转换中，请稍后再试");
                    }
                },
                error:function(e){
                    layer.msg("操作失败，请刷新后重试");
                }
            })
            return true;
        }

    }

    // 初始化表格
    function  initTable(){
        $("#fileTable").bootstrapTable('destroy')
        // 初始化表格
        $("#fileTable").bootstrapTable({ // 对应table标签的id
            data: getData(),
            cache: false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
            striped: true, //表格显示条纹，默认为false
            search: true,
            searchAlign: "right",
            showRefresh: true,  //显示刷新按钮
            columns: [
                /* {
                     width:  20,
                     checkbox: true,
                     align: "center"
                 },*/
                {
                    width:  500,
                    field: 'fileName',
                    title: '文件名称',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width:  500,
                    field: 'text',
                    title: '所在目录',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width:  500,
                    field: 'uploadUserName',
                    title: '文件上传人姓名',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width:  500,
                    field: 'uploadJobNumber',
                    title: '文件上传者工号',
                    align: 'center',
                    valign: 'middle',
                },
                {
                    width:  500,
                    field: 'alive',
                    title: '文件状态',
                    align: 'center',
                    valign: 'middle',
                    formatter: function (value, row, index) {
                        console.log(value)
                        if(value == 0){
                            return '<b><span style="color: green">未删除</span></b>';
                        }else {
                            return '<b><span style="color: red">已删除</span></b>';
                        }
                    }
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
        $("#fileTable").bootstrapTable('hideLoading')
    }
    initTable();


    /**
     *  无审核文件上传
     */
    $("#editMenuShow").hide();
    let menuId;

    // 获取树形目录数据
    function getTemplateMenuData() {
        let treeData = null;
        $.ajax({
            type : 'get',
            url: '/meetingMenu/getAllMeetingMenuUpload',
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

    // 初始化树形目录
    function initTreeMenu(){
        // 初始化目录
        $('#upLoadTree').treeview({
            data: getTemplateMenuData(),
            levels: 5,
        });

        // 初始化点击事件
        $("#upLoadTree").on('nodeSelected',function (event,data) {
            $("#editMenuShow").show();
            menuId = data.id;
            // 设置表单提交的 menuId
            $("#menuId").val(data.id)
            // 初始化上传文件
            initNoCheckFileTable();

        })
    }
    initTreeMenu()


    /**
     * 文件上产
     */
    let arrJson = null; // 后面获取获取表单数据需要使用
    $("#addFile").click(function () {
        arrJson = getReviewFormDataByMenuId(menuId);
        autoInitForm("initFormJs",arrJson)
        // 显示文件上传
        $("#addFileModel").modal('show');
    })

    /**
     *  表格全部文件
     */

    // 获取 表格数据
    function getNoCheckFilesData(){
        let  tableData = null;
        $.ajax({
            url : '/meetingFile/getNoCheckFiles',
            type : 'get',
            async : false,
            data : {
                menuId : menuId
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
    function addNoCheckFileFunction(value,row,index){
        if(row.alive == 0){
            return ['<div class="btn-group btn-group-sm">\n' +
            '  <button id="btn-nocheck-form-review" type="button" class="btn btn-warning">表单预览</button>\n' +
            '  <button id="btn-nocheck-file-review" type="button" class="btn btn-info">文件预览</button>\n' +
            '  <button id="btn-nocheck-del" type="button" class="btn btn-danger">删除文件</button>\n' +
            '</div>'].join()
        }else {
            return ['<div class="btn-group btn-group-sm">\n' +
            '  <button id="btn-nocheck-form-review" type="button" class="btn btn-warning">表单预览</button>\n' +
            '  <button id="btn-nocheck-file-review" type="button" class="btn btn-info">文件预览</button>\n' +
            '  <button id="btn-nocheck-recovery" type="button" class="btn btn-default">恢复文件</button>\n' +
            '</div>'].join()
        }
    }
    // 编辑操作
    window.eventNoCheckFile = {
        // 删除文件
        'click #btn-nocheck-del' : function (e,value,row,index){
            let id=row.id;
            layer.confirm('确认删除?',function (index) {
                $.ajax({
                    type:'post',
                    url:'/meetingFile/del',
                    data:{
                        "id" :id
                    },
                    async:false,
                    success:function (e) {
                        if(e.data){
                            layer.msg("文件删除成功")
                        }else {
                            layer.msg("操作失败，请及时联系管理员")
                        }
                    }
                })
                initNoCheckFileTable();
            })
        },
        // 文件恢复
        'click #btn-nocheck-recovery' : function (e,value,row,index){
            let id=row.id;
            layer.confirm('确认恢复文件?',function (index) {
                $.ajax({
                    type:'post',
                    url:'/meetingFile/recover',
                    data:{
                        "id" :id
                    },
                    async:false,
                    success:function (e) {
                        if(e.data){
                            layer.msg("文件已恢复")
                        }else {
                            layer.msg("操作失败，请及时联系管理员")
                        }
                    }
                })
                initNoCheckFileTable();
            })
        },
        // 表格内容预览
        'click #btn-nocheck-form-review' : function (e,value,row,index){
            initShowTable("tableId",getReviewTableData(row.id))
            $("#formTextReviewModel").modal('show')
        },
        // 表格文件预览
        'click #btn-nocheck-file-review' : function (e,value,row,index){
            let id=row.id;
            $.ajax({
                type:'GET',
                data:{
                },
                url: 'meetingFile/getURL?id='+id,
                success:function(e){

                    let entity=JSON.parse(e);
                    if (entity.path!=null) {
                        //window.location.href=entity.path;
                        // 打开新页面
                        console.log("开始要跳转了----")
                        window.open(entity.path)
                    }else{
                        layer.msg("正在转换中，请稍后再试");
                    }
                },
                error:function(e){
                    layer.msg("操作失败，请刷新后重试");
                }
            })
            return true;
        }

    }
    // 初始化表格
    function  initNoCheckFileTable(){
        $("#uploadFilesTable").bootstrapTable('destroy')
        // 初始化表格
        $("#uploadFilesTable").bootstrapTable({ // 对应table标签的id
            data: getNoCheckFilesData(),
            cache: false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
            striped: true, //表格显示条纹，默认为false
            search: true,
            searchAlign: "right",
            toolbar: '#NoCheckFileTableToolbar', //工具按钮用哪个容器
            showRefresh: true,  //显示刷新按钮
            columns: [
                /* {
                     width:  20,
                     checkbox: true,
                     align: "center"
                 },*/
                {
                    width:  500,
                    field: 'fileName',
                    title: '文件名称',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width:  500,
                    field: 'uploadUserName',
                    title: '文件上传人姓名',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width:  500,
                    field: 'uploadJobNumber',
                    title: '文件上传者工号',
                    align: 'center',
                    valign: 'middle',
                },{
                    width:  500,
                    field: 'alive',
                    title: '文件状态',
                    align: 'center',
                    valign: 'middle',
                    formatter: function (value, row, index) {
                        console.log(value)
                        if(value == 0){
                            return '<b><span style="color: green">未删除</span></b>';
                        }else {
                            return '<b><span style="color: red">已删除</span></b>';
                        }
                    }
                }, {
                    width: 700,
                    title: "操作",
                    align: 'center',
                    valign: 'middle',
                    events: 'eventNoCheckFile',
                    formatter: addNoCheckFileFunction,   //表格最上方函数
                }
            ],getSelections: function () {
            }
        })
        $("#uploadFilesTable").bootstrapTable('hideLoading')
    }
    initNoCheckFileTable();

    /**
     *  表单提交
     */
    $("#formSubmit").click(function () {
        if(autoInitValidator("initForm")){
            //表单验证通过后
            //此处设置参数信息
            let formData = new FormData();//必须是new FormData后台才能接收到
            formData.append("menuId", $("#menuId").val());
            // 暂时不允许多文件上传
            formData.append("file", $("#file")[0].files[0]);
            formData.append("text", getFormData(arrJson));
            // 添加可见的部门列表
            formData.append("depIds",$("#selectDep").val().toString())

            $.ajax({
                url: "/meetingFile/uploadFileNoCheck",
                data: formData,
                type: 'post',
                datatype: "json",
                contentType: false,//必须false才会自动加上正确的Content-Type
                processData: false,//必须false才会避开jQuery对 formdata 的默认处理，XMLHttpRequest会对 formdata 进行正确的处理
                success: function (e) {
                    $("#addFileModel").modal('hide')
                    initNoCheckFileTable();
                }
            });
        }

    })
})