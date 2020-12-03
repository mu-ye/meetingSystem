/**
 *  动态生成 日期选择器(精确到时分秒) 模板
 *
 * @param {Object} title 名称      eg: 用户姓名
 * @param {Object} prompt   提示信息  eg: 请输入用户姓名
 * @param {Object} name  input 的 id 和 name值
 */
function initDateTime(title,prompt,name){
    let  initDiv = $("<div class='form-group'>")
    let  initTable = $("<label>"+title+"</label>")
    let  initInput = $("<input type='text' placeholder=' "+prompt+"'>")
    initInput.attr("id",name)
    // 为每个生成的控件 设置class为njdt_auto_init_dateTime;根据njdt_auto_init_dateTime 来设置日期选择器
    initInput.attr("class","form-control njdt_auto_init_dateTime datetimepicker-input")
    initInput.attr("name",name)
    // 添加表单验证
    initInput.attr("data-bv-notempty","true")
    initInput.attr("data-bv-notempty-message","不能为空")
    // 添加时间选择器
    initInput.attr("data-toggle","datetimepicker")
    initInput.attr("data-target","#datetimepicker")

    initDiv.append(initTable)
    initDiv.append(initInput)
    return initDiv;
}

/**
 *  动态生成 input 模板
 *
 * @param {Object} title 名称      eg: 用户姓名
 * @param {Object} prompt   提示信息  eg: 请输入用户姓名
 * @param {Object} name  input 的 id 和 name值
 */
function initInput(title,prompt,name){
    let  initDiv = $("<div class='form-group'>")
    let  initTable = $("<label>"+title+"</label>")
    let  initInput = $("<input type='text' placeholder=' "+prompt+"'>")
    initInput.attr("id",name)
    // 为每个生成的控件 设置class为initElement;根据initElement 来便利表单中的值
    initInput.attr("class","form-control njdt_auto_init")
    initInput.attr("name",name)
    // 添加表单验证
    initInput.attr("data-bv-notempty","true")
    initInput.attr("data-bv-notempty-message","不能为空")

    initDiv.append(initTable)
    initDiv.append(initInput)
    return initDiv;
}

/**
 * 动态生成下拉列表
 *
 * @param {Object} title  table 中的提示信息 eg: 请选择你的老婆
 * @param {Object} prompt   下拉列表的提示信息 eg: 请选择你的老婆
 * @param {Object} name  下拉列表的 id 和 name
 * @param {Object} selectArr 下拉列表的选项内容  text: 选项的内容   selected : 1: 默认选中 0: 默认未选中
 */
function initGroupSelect(title,prompt,name,selectArr){
    let jsonArray= $.parseJSON(selectArr);
    let  initDiv = $("<div class='form-group'><div>")
    let  initTable = $("<label>"+title+"</label>")
    let  initSelect = $("<select type='text'> </select>")
    initSelect.attr("id",name)
    initSelect.attr("name",name)
    initSelect.attr("class","province form-control selectpicker njdt_auto_init")
    initSelect.attr("multiple","multiple")
    initSelect.attr("data-live-search","true")
    initSelect.attr("title",prompt)
    // 添加表单验证
    initSelect.attr("data-bv-notempty","true")
    initSelect.attr("data-bv-notempty-message","不能为空")

    // 添加下拉列表
    for(let i =0;i<jsonArray.length;i++){
        let initOption = $("<option>"+jsonArray[i].tableTitle+"</option>")

        initOption.attr("value",jsonArray[i].tableTitle)
        if(jsonArray[i].tableText == "1"){
            initOption.prop("selected","selected")
        }
        initSelect.append(initOption)
    }
    initDiv.append(initTable)
    initDiv.append(initSelect)
    return initDiv;
}


/**
 * 动态生成form
 *
 * @param {Object} addElementId  动态生成的form 要挂载的地方
 * @param {Object} jsonFormData  jsonFormData 后台传进来的参数
 */
function autoInitForm(addElementId,jsonFormData){

    // 生成的表单添加到 id为 initDiv 的控件中
    let initDiv = $('#'+addElementId+'');
    // 每次初始化之前都要 清空里面内容
    initDiv.empty();
    for(let i in jsonFormData){
        // 根据 titleType 生成不同的控件
        let titleType = jsonFormData[i].titleType;
        // 生成输入框
        if(titleType === "text"){
            initDiv.append(initInput(jsonFormData[i].titleName,jsonFormData[i].prompt,jsonFormData[i].dataType))
        }
        if(titleType === "select"){
            initDiv.append(initGroupSelect(jsonFormData[i].titleName,jsonFormData[i].prompt,jsonFormData[i].dataType,jsonFormData[i].data))
        }
        if(titleType === "dateTime"){
            initDiv.append(initDateTime(jsonFormData[i].titleName,jsonFormData[i].prompt,jsonFormData[i].dataType))
        }
        if(titleType === "date"){
            alert("dateTime")
        }
        if(titleType === "radio"){
            alert("radio")
        }
    }
    $('.selectpicker').selectpicker('refresh');
    //render方法强制重新渲染引导程序
    $('.selectpicker').selectpicker('render');
}

/**
 * 表单验证
 *
 * @param {Object} formId 要验证的表单Id
 */
function autoInitValidator(formId){
    let form = $('#'+formId+'');
    //let form =  $("#initForm");
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
            file: {
                validators: {
                    file: {
                        extension: 'pdf',
                        type: 'application/pdf',
                        maxSize: 10*1024*1024,
                        message: '文件格式为pdf且不超过10M'
                    },
                    notEmpty:{
                        message :'文件不为空'
                    }

                }
            }
        }
    });
    let bv = $("#initForm").data('bootstrapValidator')
    bv.validate()
    return bv.isValid()
}

/**
 * 获取表单内容
 *
 * @param {Object} 传入初始化表单数组
 */
function getFormData(arr){
    let result = "";
    for(let i in arr){
        result += $('#'+arr[i].dataType+'').val();
        result += "@@@";
    }
    console.log(result)
    return result;
}




/**
 * 初始化 预览表格
 *
 * @param {Object} showElementId 显示的表格添加的位置
 * @param {Object} tableJsonData 表格中添加的位置
 */
function initShowTable(showElementId,tableJsonData){

    // 生成的表单添加到 id为 initDiv 的控件中
    let initDiv = $('#'+showElementId+'');
    initDiv.empty();
    let color = "colorOne";
    for(let i in tableJsonData){
        if(color === "colorOne"){
            let  iniTr = $("<tr class='titleOne' style='background-color: #B3D4DE;'><td>"+tableJsonData[i].title+"</td><td>"+tableJsonData[i].text+"</td></tr>")
            initDiv.append(iniTr)
            color = "colorTwo"
        }else{
            let  iniTr = $("<tr class='titleOne' style='background-color: #FFFFFF;'><td>"+tableJsonData[i].title+"</td><td>"+tableJsonData[i].text+"</td></tr>")
            initDiv.append(iniTr)
            color = "colorOne"
        }
    }
    return initDiv;
}

/**
 * 根据表单Id 获取表单预览数据
 *
 * @param formId  预览表单Id
 * @returns {*}
 */
function getReviewFormData(formId) {
    let result = null;
    $.ajax({
        url : "/form/getText",
        type : "get",
        data : {
            "id": formId
        },
        async: false,
        success : function (e) {
            if(e.code == 200){
                // 此处预览
                result = e.data;
            }else {
                console.log("id"+formId+"获取数据失败");
                result = null;
            }
        },error : function (e) {
            result = null;
            console.log("数据获取失败：错误信息"+e)
        }
    })
    return result;
}


/**
 *  获取上传文件的表格数据
 *
 * @param fileId 文件Id
 */
function getReviewTableData(fileId){
    let result;
    $.ajax({
        url : "/meetingFile/getFormTexts",
        type : "get",
        data : {
            "id" : fileId
        },
        async: false,
        success : function (e) {
            if(e.code === 200){
                if(e.data == ""){
                    // 文件没有表单 返回null
                    result = null
                }else {
                    result = e.data;
                }

            }else {
                console.log("id"+fileId+"获取数据失败");
                result = null;
            }
            console.log("数据获取失败：错误信息"+e)
        }
    })
    return result;
}

/**
 *  根据 目录Id 获取文件
 *
 * @param menuId 文件所在目录Id
 */
function getReviewFormDataByMenuId(menuId){
    let result;
    $.ajax({
        url : "/form/getTextByMenuId",
        type : "get",
        data : {
            "id" : menuId
        },
        async: false,

        success : function (e) {
            if(e.code === 200){
                // 此处预览
                result = e.data;
            }else {
                console.log("id"+menuId+"获取数据失败");
                result = null;
            }
        },error : function (e) {
            result = null;
            console.log("数据获取失败：错误信息"+e)
        }
    })
    return result;
}



