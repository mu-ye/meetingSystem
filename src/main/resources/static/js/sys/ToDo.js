$(function () {
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
    function getData() {
        let tableData = null;
        $.ajax({
            url: '/meetingFile/getToCheckFiles',
            type: 'get',
            async: false,
            success: function (e) {
                if (e.code == 200) {
                    tableData = e.data;
                } else {
                    console.log("返回异常，错误信息 ：" + e)
                }
            }
        })
        return tableData;
    }

    // 表格中的操作
    function addFunction(value, row, index) {
        return ['<div class="btn-group btn-group-sm">\n' +
        '  <button id="btn-text-todo" type="button" class="btn btn-default">内容预览</button>\n' +
        '  <button id="btn-review-todo" type="button" class="btn btn-info">文件预览</button>\n' +
        '  <button id="btn-pass-todo" type="button" class="btn btn-default">通过</button>\n' +
        '  <button id="btn-no-todo" type="button" class="btn btn-warning">拒绝</button>\n' +
        '</div>'].join()
    }

    // 编辑操作
    window.event = {
        // 审核通过
        'click #btn-pass-todo': function (e, value, row, index) {
            let id = row.id;
            layer.confirm('确认审核通过?', function (index) {
                $.ajax({
                    type: 'get',
                    url: '/meetingFile/checkPass',
                    data: {
                        "id": id
                    },
                    async: false,
                    success: function (e) {
                        if (e.data) {
                            layer.msg("文件已通过")
                        } else {
                            layer.msg("操作失败，请及时联系管理员")
                        }
                    }
                })
                layer.close(index);
                initTable();
                initTableChecked();
                // 浏览器刷新
                window.location.reload()
            })
        },
        // 审核未通过
        'click #btn-no-todo': function (e, value, row, index) {
            fileId = row.id;
            $("#refuseModel").modal('show')
        },
        // 表格内容预览
        'click #btn-text-todo': function (e, value, row, index) {
            let initFormData = getReviewTableData(row.id);
            if (initFormData === null) {
                layer.msg("文件没有预览内容")
            } else {
                initShowTable("tableId", initFormData)
                $("#formTextReviewModel").modal('show')
            }
        },
        // 表格文件预览
        'click #btn-review-todo': function (e, value, row, index) {
            let id = row.id;
            $.ajax({
                type: 'GET',
                data: {},
                url: 'meetingFile/getURL?id=' + id,
                success: function (e) {

                    let entity = JSON.parse(e);
                    if (entity.path != null) {
                        //window.location.href=entity.path;
                        // 打开新页面
                        console.log("开始要跳转了----")
                        window.open(entity.path)
                    } else {
                        layer.msg("正在转换中，请稍后再试");
                    }
                },
                error: function (e) {
                    layer.msg("操作失败，请刷新后重试");
                }
            })
            return true;
        }

    }

    // 初始化表格
    function initTable() {
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
                /* {
                     width:  20,
                     checkbox: true,
                     align: "center"
                 },*/
                {
                    width: 500,
                    field: 'fileName',
                    title: '文件名称',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width: 500,
                    field: 'name',
                    title: '所在会议',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width: 500,
                    field: 'uploadUserName',
                    title: '文件上传人姓名',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width: 500,
                    field: 'uploadJobNumber',
                    title: '文件上传者工号',
                    align: 'center',
                    valign: 'middle',
                    formatter: function (value, row, index) {
                        if (value === "N") {
                            return "不支持会议密码"
                        } else {
                            return value;
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
            ], getSelections: function () {
            }
        })
        $("#meetingTable").bootstrapTable('hideLoading')
    }

    initTable();


    /**
     * 已审核 文件列表
     */
    // 获取 表格数据
    function getCheckedData() {
        let tableData = null;
        $.ajax({
            url: '/meetingFile/getCheckedFiles',
            type: 'get',
            async: false,
            success: function (e) {
                if (e.code == 200) {
                    tableData = e.data;
                } else {
                    console.log("返回异常，错误信息 ：" + e)
                }
            }
        })
        return tableData;
    }

    // 表格中的操作
    function addCheckedFunction(value, row, index) {
        return ['<div class="btn-group btn-group-sm">\n' +
        '  <button id="btn-text-done" type="button" class="btn btn-default">内容预览</button>\n' +
        '  <button id="btn-review-done" type="button" class="btn btn-info">文件预览</button>\n' +
        '</div>'].join()
    }

    window.event1 = {
        // 表格内容预览
        'click #btn-text-done': function (e, value, row, index) {
            let initFormData = getReviewTableData(row.id);
            if (initFormData === null) {
                layer.msg("文件没有预览内容")
            } else {
                initShowTable("tableId", initFormData)
                $("#formTextReviewModel").modal('show')
            }
        },
        // 表格文件预览
        'click #btn-review-done': function (e, value, row, index) {
            let id = row.id;
            $.ajax({
                type: 'GET',
                data: {},
                url: 'meetingFile/getURL?id=' + id,
                success: function (e) {
                    // 浏览器刷新
                    // window.location.reload()

                    let entity = JSON.parse(e);
                    if (entity.path != null) {
                        //window.location.href=entity.path;
                        // 打开新页面
                        console.log("开始要跳转了----")
                        window.open(entity.path)
                    } else {
                        layer.msg("正在转换中，请稍后再试");
                    }
                },
                error: function (e) {
                    layer.msg("操作失败，请刷新后重试");
                }
            })
            return true;
        }

    }

    // 初始化表格
    function initTableChecked() {
        $("#checkedTable").bootstrapTable('destroy')
        // 初始化表格
        $("#checkedTable").bootstrapTable({ // 对应table标签的id
            data: getCheckedData(),
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
                    width: 500,
                    field: 'fileName',
                    title: '文件名称',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width: 500,
                    field: 'name',
                    title: '所在会议',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width: 500,
                    field: 'uploadUserName',
                    title: '文件上传人姓名',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width: 500,
                    field: 'uploadJobNumber',
                    title: '文件上传者工号',
                    align: 'center',
                    valign: 'middle',
                    formatter: function (value, row, index) {
                        if (value === "N") {
                            return "不支持会议密码"
                        } else {
                            return value;
                        }
                    }
                }, {
                    width: 500,
                    field: 'checkList',
                    title: '审批流程',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    width: 500,
                    field: 'pass',    //与返回json 字符串中的内容进行匹配
                    title: '审核状态',
                    align: 'center',
                    valign: 'middle',
                    formatter: function (value, row, index) {
                        switch (value) {
                            case 0:
                                return '<b><span style="color: blue">待审核</span></b>'
                            case 1:
                                //value=row.refuseReason
                                return ' <b><span style="color: red" id="passNo" >' + '审核未通过(理由：' + row.refuseReason + ')' + '</span></b>';
                            case 2:
                                return ' <b><span style="color: green">通过</span></b>';

                        }
                    }
                }, {
                    width: 500,
                    title: "操作",
                    align: 'center',
                    valign: 'middle',
                    events: 'event1',
                    formatter: addCheckedFunction,   //表格最上方函数
                }
            ], getSelections: function () {
            }
        })
        $("#checkedTable").bootstrapTable('hideLoading')
    }

    initTableChecked();

    /**
     *  文件拒绝提交
     */
    $("#submitBtn").click(function () {
        if ($("#reason").val() === "") {
            layer.msg("拒绝理由不能为空")
        } else {
            $.ajax({
                url: '/meetingFile/refuse',
                type: 'get',
                async: false,
                data: {
                    "id": fileId,
                    "refuseReason": $("#reason").val()
                },
                success: function (e) {

                    if (e.code == 200) {
                        // 浏览器刷新
                        window.location.reload()
                        $("#refuseModel").modal('hide')
                        // 刷新两张表格
                        initTableChecked();
                        initTable();
                        layer.msg("操作成功")
                    } else {
                        console.log("操作失败，请重试或及时联系管理员")
                    }
                }
            })
        }
    })

})