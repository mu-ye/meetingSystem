$(function () {
    /**
     *  页面初始化设置
     */

    // 日期选择器
    $('#startDate').datetimepicker({
        format: 'YYYY-MM-DD HH:mm:ss',
        locale: 'zh-CN'
    });
    $('#endDate').datetimepicker({
        format: 'YYYY-MM-DD HH:mm:ss',
        locale: moment.locale('zh-cn')
    })

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
            url : '/meeting/getHistoryList',
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
        '  <button id="btn-download" type="button" class="btn btn-info">文件下载</button>\n' +
        '</div>'].join()
    }

    // 编辑操作
    window.event = {
        'click #btn-download' : function (e,value,row,index){
            let id=row.id;
            //console.log("HistoryMeeting-meetingId"+id)
            localStorage.setItem("HistoryMeeting-meetingId",id)
            window.location.href="index/downLoadFiles";
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
            toolbar: '#TemplateTableToolbar', //工具按钮用哪个容器
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
                    width:  300,
                    field: 'type',    //与返回json 字符串中的内容进行匹配
                    title: '会议类别',
                    align: 'center',
                    valign: 'middle'


                },{
                    width: 500,
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

    // 批量删除
    $("#delMeeting").click(function () {
        let rows = $("#meetingTable").bootstrapTable('getSelections');
        if (rows.length == 0) {
            // 没有选中内容
            layer.alert(" 选择要删除的会议! ");
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
                    url: "meeting/del",
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

    // 表单验证
    $("#newMeeting")
        .bootstrapValidator({
            message:'无效的值',
            //成功和失败的图标
            feedbackIcons: {
                /*valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'*/
            },
            fields:{
                name:{
                    validators: {
                        notEmpty: {
                            message: '会议名不为空'
                        }
                    }
                },
                address: {
                    validators: {
                        notEmpty: {
                            message: '会议地点不能为空'
                        }
                    }
                },
                startDate1: {
                    validators: {
                        notEmpty: {
                            message: '开会时间不能为空'
                        }
                    }
                },
                endDate1: {
                    validators: {
                        notEmpty: {
                            message: '文件上传截至时间不能为空'
                        }
                    }
                },
                attendPassword: {
                    validators: {
                        notEmpty: {
                            message: '不能为空'
                        }
                    }
                },
                type: {
                    validators: {
                        notEmpty: {
                            message: '会议类别不能为空'
                        }
                    }
                },
                templateId: {
                    validators: {
                        notEmpty: {
                            message: '模板不能为空'
                        }
                    }
                }
            }
        });
})