$(function () {
    /**
     * 初始化页面设置 & 全局变量
     */
    let menuId = null;
    let fileId = null;
    $("#editMenuShow").hide();
    let meetingId = localStorage.getItem("MeetingUpload-meetingId")

    /**
     *  通过ajax 获取会议文件的截至时间，并显示
     */
    function getEndTimeAndShow(){
        $.ajax({
            type : "POST",
            url : "/meeting/getEndTime",
            data : {
                "meetingId" : meetingId
            },
            async : false,
            success : function (e) {
                console.log(e)
                if(e.code == 200){
                    console.log(e.data)
                    $("#endTimeShow").text("文件上传截至时间 ："+e.data)
                }
            }
        })
    }
    getEndTimeAndShow();


    /**
     *  通过 meetingId 获取是否可以上传文件
     */
    function getFileUpEndTimeByMeetingId(){
        let resultFlag = false;
        $.ajax({
            type : "POST",
            url : "/meeting/getCanFile",
            data : {
                "meetingId" : meetingId
            },
            async : false,
            success : function (e) {
                if(e.data){
                    resultFlag = true;
                }else {
                    resultFlag = false;
                }
            }
        })
        return resultFlag;
    }







    /**
     * 树形目录
     */

    // 获取树形目录数据
    function getTemplateMenuData() {
        let treeData = null;
        $.ajax({
            type : 'get',
            url: '/meetingMenu/getMeetingMenuUpload',
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
        $('#templateMenu').treeview({
            data: getTemplateMenuData(),
            levels: 5,
        });

        // 初始化点击事件
        $("#templateMenu").on('nodeSelected',function (event,data) {
            let show = data.isShow;
            menuId = data.id;
            $("#menuId").val(data.id)
            if(show){
                initTable();
                // 判断是否可以山川文件
                if(getFileUpEndTimeByMeetingId()){
                    console.log("可以上传文件")
                    $("#addFile").attr("style","display: block")
                }else {
                    console.log("不可以上传文件")
                    $("#addFile").attr("style","display: none")
                }


                $("#editMenuShow").show();
            }else {
                $("#editMenuShow").hide();
            }
        })
    }
    initTreeMenu()


    // 表格中的操作
    function addFunction(value,row,index){
        return ['<div class="btn-group btn-group-sm">\n' +
        '  <button id="textReview" type="button" class="btn btn-default">内容预览</button>\n' +
        '  <button id="fileReview" type="button" class="btn btn-info">文件预览</button>\n' +
        '</div>'].join()
    }

    // 编辑操作
    window.event = {

        'click #fileReview' : function (e,value,row,index){
            let id=row.id
            getReviewUrlTimer(id)
            return true;
        },
        // 表格内容预览
        'click #textReview' : function (e,value,row,index){
            let initFormData = getReviewTableData(row.id);
            if(initFormData === null){
                layer.msg("文件没有预览内容")
            }else {
                initShowTable("tableId",initFormData)
                $("#formTextReviewModel").modal('show')
            }
        },

    }

    /**
     *  定时任务 请求文件预览
     */
    let number = 0;
    function getReviewUrlTimer(fileId) {
        let timer = setInterval(function(){
            $.ajax({
                type:'GET',
                data:{
                    "id" : fileId
                },
                url: "/meetingFile/getURL",
                success:function(e){
                    let entity=JSON.parse(e);
                    console.log("entity"+entity)
                    if (entity.path!=null) {
                        // 关闭定时任务
                        clearInterval(timer);
                        window.open(entity.path)
                    }
                },
                error:function(e){
                    console.log("转换失败，请及时联系管理员");
                    clearInterval(timer);
                }

            })
            number +=1;
            if(number > 5){
                console.log(number)
                number = 0;
                clearInterval(timer);
            }

        }, 500);
    }

    $("#formTextReviewModel").modal('hide')




    function getData(menuId){
        let  tableData = null;
        $.ajax({
            url : '/meetingFile/getFilesByJobNumber',
            type : 'get',
            async : false,
            data: {
                "menuId":menuId
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
    function initTable(){
        $("#fileTable").bootstrapTable('destroy')
        // 初始化表格
        $("#fileTable").bootstrapTable({ // 对应table标签的id
            data: getData(menuId),
            cache: false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
            striped: true, //表格显示条纹，默认为false
            toolbar: '#FileTableToolbar', //工具按钮用哪个容器
            search: true,
            searchAlign: "right",
            showRefresh: true,  //显示刷新按钮
            columns: [
                {
                    width:  500,
                    field: 'fileName',    //与返回json 字符串中的内容进行匹配
                    title: '文件名称',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width:  500,
                    field: 'checkList',    //与返回json 字符串中的内容进行匹配
                    title: '审批流程',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width:  200,
                    field: 'pass',    //与返回json 字符串中的内容进行匹配
                    title: '审核状态',
                    align: 'center',
                    valign: 'middle',
                    formatter :function (value,row,index) {
                        switch (value) {
                            case 0:
                             return '<b><span style="color: blue">待审核</span></b>'
                            case 1:
                                //value=row.refuseReason
                                return ' <b><span style="color: red" id="passNo" >'+'审核未通过(理由：'+row.refuseReason+')'+'</span></b>';
                            case 2:
                                return ' <b><span style="color: green">通过</span></b>';

                        }
                    }
                },
                {
                    width:  200,
                    title: '操作',
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
     * 文件上传
     */
    let arrJson = null; // 后面获取获取表单数据需要使用
    $("#addFile").click(function () {
        arrJson = getReviewFormDataByMenuId(menuId);
        autoInitForm("initFormJs",arrJson)
        // 显示文件上传
        $("#addFileModel").modal('show');
    })


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
                url: "/meetingFile/uploadFileJs",
                data: formData,
                type: 'post',
                datatype: "json",
                contentType: false,//必须false才会自动加上正确的Content-Type
                processData: false,//必须false才会避开jQuery对 formdata 的默认处理，XMLHttpRequest会对 formdata 进行正确的处理
                success: function (e) {
                    window.location.reload();
                    $("#addFileModel").modal('hide')
                    initTable();
                    $("#editMenuShow").show();
                }
            });
        }

    })





})