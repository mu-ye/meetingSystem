$(function () {
    /**
     *  页面初始化设置
     */


    // 获取用户信息
    $("#userName").text(localStorage.getItem("userName"))

    // 初始化隐藏添加表单
    $("#addMeetingModel").modal('hide')

    // 显示添加表单
    $("#addMeeting").click(function () {
        $("#addMeetingModel").modal('show')
    })


    /**
     * 表格
     */

    // 获取 表格数据
    function getData(){
        let  tableData = null;
        $.ajax({
            url : '/meeting/getMeetingListUpload',
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
            localStorage.setItem("MeetingUpload-meetingId",id)
            window.location.href="index/uploadMeeting";
        }
    }

    // 初始化表格
    function  initTable(){
        $("#meetingTable").bootstrapTable('destroy')
        // 初始化表格
        $("#meetingTable").bootstrapTable({ // 对应table标签的id
            data: getData(),
            cache: false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
            striped: true, //表格显示条纹，默认为false
            search: true,
            searchAlign: "right",
            showRefresh: true,  //显示刷新按钮
            columns: [
                {
                    width:  500,
                    field: 'name',    //与返回json 字符串中的内容进行匹配
                    title: '会议名称',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width:  500,
                    field: 'address',    //与返回json 字符串中的内容进行匹配
                    title: '会议地址',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width:  500,
                    field: 'startDate',    //与返回json 字符串中的内容进行匹配
                    title: '开会时间',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width:  500,
                    field: 'attendPassword',    //与返回json 字符串中的内容进行匹配
                    title: '会议密码',
                    align: 'center',
                    valign: 'middle',
                    formatter : function(value, row, index){
                        if(value === "N"){
                            return "不支持会议密码"
                        }else {
                            return  value;
                        }
                    }
                },
                {
                    width:  500,
                    field: 'type',    //与返回json 字符串中的内容进行匹配
                    title: '会议类别',
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
        $("#meetingTable").bootstrapTable('hideLoading')
    }

    initTable();

})