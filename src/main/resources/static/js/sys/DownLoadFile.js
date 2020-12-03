$(function () {

    let meetingId = localStorage.getItem("HistoryMeeting-meetingId");
    // 获取用户信息
    $("#userName").text(localStorage.getItem("userName"))

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


    // 获取 表格数据
    function getNoCheckFilesData(){
        let  tableData = null;
        $.ajax({
            url : '/meetingFile/getHistoryFiles',
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

            return ['<div class="btn-group btn-group-sm">\n' +
            '  <a href='+row.url+'>下载</a>\n' +
            '</div>'].join()

    }
    // 编辑操作
    window.eventNoCheckFile = {
       /* 'click #btn-download' : function (e,value,row,index){
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
        },*/
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


})