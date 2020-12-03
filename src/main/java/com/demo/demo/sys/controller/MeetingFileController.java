package com.demo.demo.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.demo.core.common.SystemCommon;
import com.demo.demo.core.ret.RetResponse;
import com.demo.demo.core.ret.RetResult;
import com.demo.demo.core.ret.RetTileText;
import com.demo.demo.core.unit.*;
import com.demo.demo.sys.domain.Employee;
import com.demo.demo.sys.entity.MeetingFile;
import com.demo.demo.sys.entity.MeetingMenu;
import com.demo.demo.sys.entity.Title;
import com.demo.demo.sys.result.RetDownloadFile;
import com.demo.demo.sys.result.RetFiles;
import com.demo.demo.sys.result.RetToCheckFile;
import com.demo.demo.sys.service.impl.MeetingFileServiceImpl;
import com.demo.demo.sys.service.impl.MeetingMenuServiceImpl;
import com.demo.demo.sys.service.impl.TitleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 牟欢
 * @since 2020-05-26
 */
@Controller
@RequestMapping("/meetingFile")
@Slf4j
public class MeetingFileController {
    @Autowired
    MeetingFileServiceImpl meetingFileService;
    @Autowired
    MeetingMenuServiceImpl meetingMenuService;
    @Autowired
    TitleServiceImpl titleService;
    /**
     * 中文正则
     */
    private static String REGEX_CHINESE = "[\u4e00-\u9fa5]";

    /**
     * 根据Id 获取文件的预览地址
     *
     * @param id 上传文件的ID
     * @return
     * @throws IOException
     */
    @RequestMapping("/getURL")
    @ResponseBody
    public String getUrl(Integer id) throws IOException {
        String jobNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee employee = UserInfoUtil.getEmployeeByIdFromServer(jobNumber);
        // 水印内容 : 工号 + 姓名
        String waterMark = "";
        if(employee == null){
            waterMark = jobNumber;
        }else {
            waterMark = jobNumber + " " +employee.getUserName();
        }
        log.info("------------通过post方式获取文件预览地址--------------------");
        // 服务器中文件的名字
        String fileName=meetingFileService.getById(id).getUrl();
        // 服务器中文件存储的绝对地址
        String localPath= SystemCommon.FILE_SAVE_PATH+fileName;
        String downloadUrl= SystemCommon.DOWNLOAD_BASE_URL+fileName;
        return WaterMarkUtil.getUrlPost(localPath,downloadUrl,waterMark,fileName);
    }

    /**
     *  上传文件
     * @param file
     * @param menuId
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadFile")
    public String insertNtTableOne(@RequestParam("file") MultipartFile[] file,@RequestParam("menuId") String menuId) throws IOException {
        //获取 文件上传者 姓名 和工号
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        MeetingFile meetingFile = new MeetingFile();
        meetingFile.setMeetingMenuId(Integer.parseInt(menuId));
        Employee employee = UserInfoUtil.getEmployeeByIdFromServer(jobNumber);
        String userName;
        if(employee!=null){
            userName = employee.getUserName();
        }else {
            userName = "用户不存在";
        }
        meetingFile.setUploadUserName(userName);
        meetingFile.setUploadJobNumber(jobNumber);
        MeetingMenu meetingMenu = meetingMenuService.getById(menuId);
        String checkList = meetingMenu.getCheckList();
        log.info("checkList---------------"+checkList);
        // checkList == "[Y]"成立 不需要审核； 不成立 需要审核
        if(checkList.equals("[Y]")){
            meetingFile.setCheckList("[Y]");
            // 设置默认审核成功
            meetingFile.setPass(2);
            log.info("设置文件无需审核成功------------------");
        }else {
            log.info("sql审核列表"+checkList);
            checkList.replace(" ","");
            Pattern pattern = Pattern.compile(REGEX_CHINESE);
            checkList ="[Y]"+pattern.matcher(checkList).replaceAll("").replace("-->","[N]")+"[N]";
            log.info("审核过程："+checkList);
            meetingFile.setCheckList(checkList.replace(" ",""));

            meetingFile.setPass(0);
        }




        // 可以查看的部门
        meetingFile.setDepIds("1");
        meetingFile.setRefuseReason("");
        // 默认FormId为没有表单
        meetingFile.setFormText("");
        meetingFile.setAlive(0);


        // 设置当前审核节点
        meetingFile.setNowNode("");
        meetingFile.setRemark(LocalDateTime.now().toString()+userName+"("+jobNumber+")"+"上传文件");
        for(int i=0; i<file.length;i++){
            if (file[i].isEmpty()) {
                log.info("文件上传失败");
                return "UploadFile";
                // return "redirect:/meetingFile/uploadFile";
            } else {
                log.info("文件名称"+file[i].getOriginalFilename());
                meetingFile.setFileName(file[i].getOriginalFilename());
                String sqlFileName= MyFileTransformation.fileNameTransformationAddDateFileName(file[i].getOriginalFilename());
                log.info("存入数据库中的URL="+sqlFileName);
                meetingFile.setUrl(sqlFileName);

                //新建文件夹
                File file1 = new File(SystemCommon.FILE_SAVE_PATH);
                file1.mkdirs();
                //保存文件
                File dest = new File(SystemCommon.FILE_SAVE_PATH+sqlFileName);
                try {
                    //根据文件路径保存文件
                    file[i].transferTo(dest);
                    //将文件信息保存到数据库
                    meetingFileService.save(meetingFile);
                    log.info("上传成功");
                } catch (IOException e) {
                    log.info(e.toString(), e);
                }
            }
        }
        return "UploadFile";
        //return "redirect:/meetingFile/uploadFile";
    }

    /**
     * 删除文件
     *
     * @param id 文件Id
     * @return
     */
    @PostMapping("/del")
    @ResponseBody
    public RetResult<Boolean> del(Integer id){
        MeetingFile meetingFile = meetingFileService.getById(id);
        // 切换文件状态1： 已删除
        meetingFile.setAlive(1);
        Boolean resultFlag = meetingFileService.saveOrUpdate(meetingFile);
        if(resultFlag){
            return RetResponse.makeOKRsp(true);
        }else {
            return RetResponse.makeErrRsp("删除失败");
        }
    }

    /**
     * 恢复已删除文件
     *
     * @param id 文件Id
     * @return
     */
    @PostMapping("/recover")
    @ResponseBody
    public RetResult<Boolean> recover(Integer id){
        MeetingFile meetingFile = meetingFileService.getById(id);
        // 切换文件状态1： 未删除
        meetingFile.setAlive(0);
        Boolean resultFlag = meetingFileService.saveOrUpdate(meetingFile);
        if(resultFlag){
            return RetResponse.makeOKRsp(true);
        }else {
            return RetResponse.makeErrRsp("删除失败");
        }
    }

    /**
     *  上传文件
     * @param file
     * @param menuId
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadFileJs")
    @ResponseBody
    public String uploadFileJs(@RequestParam("file") MultipartFile[] file,@RequestParam("menuId") String menuId,@RequestParam("text")String text,@RequestParam("depIds") String depIds) throws IOException {

        log.info(text);
        log.info(menuId);
        log.info(file.toString());
        log.info(depIds);

        //获取 文件上传者 姓名 和工号
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        MeetingFile meetingFile = new MeetingFile();
        meetingFile.setMeetingMenuId(Integer.parseInt(menuId));
        Employee employee = UserInfoUtil.getEmployeeByIdFromServer(jobNumber);
        String userName;
        if(employee!=null){
            userName = employee.getUserName();
        }else {
            userName = "用户不存在";
        }
        meetingFile.setUploadUserName(userName);
        meetingFile.setUploadJobNumber(jobNumber);
        MeetingMenu meetingMenu = meetingMenuService.getById(menuId);
        String checkList = meetingMenu.getCheckList();
        log.info("checkList---------------"+checkList);
        // checkList == "[Y]"成立 不需要审核； 不成立 需要审核
        if(checkList.equals("[Y]")){
            meetingFile.setCheckList("[Y]");
            // 设置默认审核成功
            meetingFile.setPass(2);
            log.info("设置文件无需审核成功------------------");
        }else {
            log.info("sql审核列表"+checkList);
            checkList.replace(" ","");
            Pattern pattern = Pattern.compile(REGEX_CHINESE);
            checkList ="[Y]"+pattern.matcher(checkList).replaceAll("").replace("-->","[N]")+"[N]";
            log.info("审核过程："+checkList);
            meetingFile.setCheckList(checkList.replace(" ",""));

            meetingFile.setPass(0);
        }


        // 可以查看的部门 默认 招标采购 财务部 企管 总经办 总经理室 监督办
        depIds+=",1,4,5,6,16";
        meetingFile.setDepIds(depIds);
        meetingFile.setRefuseReason("");
        // 默认FormId为没有表单
        meetingFile.setFormText("");
        meetingFile.setAlive(0);


        // 设置当前审核节点
        meetingFile.setNowNode("");
        meetingFile.setRemark(LocalDateTime.now().toString()+userName+"("+jobNumber+")"+"上传文件");
        for(int i=0; i<file.length;i++){
            log.info("开始进入文件循环");
            if (file[i].isEmpty()) {
                log.info("文件上传失败");
                return "UploadFile";
                // return "redirect:/meetingFile/uploadFile";
            } else {
                log.info("文件名称"+file[i].getOriginalFilename());
                meetingFile.setFileName(file[i].getOriginalFilename());
                String sqlFileName= MyFileTransformation.fileNameTransformationAddDateFileName(file[i].getOriginalFilename());
                log.info("存入数据库中的URL="+sqlFileName);
                meetingFile.setUrl(sqlFileName);
                // 设置表单文件内容
                meetingFile.setFormText(text);
                // 设置表单值
                meetingFile.setFormId(meetingMenu.getFormId());

                //新建文件夹
                File file1 = new File(SystemCommon.FILE_SAVE_PATH);
                file1.mkdirs();
                //保存文件
                File dest = new File(SystemCommon.FILE_SAVE_PATH+sqlFileName);
                try {
                    //根据文件路径保存文件
                    file[i].transferTo(dest);
                    //将文件信息保存到数据库
                    meetingFileService.save(meetingFile);
                    log.info("上传成功");
                } catch (IOException e) {
                    log.info(e.toString(), e);
                }
            }
        }
        log.info("=====================================结束====================================");
        return "UploadFile";
        //return "redirect:/meetingFile/uploadFile";
    }

    /**
     *  上传文件(无需审核)
     * @param file
     * @param menuId
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadFileNoCheck")
    @ResponseBody
    public String uploadFileNoCheck(@RequestParam("file") MultipartFile[] file,@RequestParam("menuId") String menuId,@RequestParam("text")String text,@RequestParam("depIds") String depIds) throws IOException {


        //获取 文件上传者 姓名 和工号
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        MeetingFile meetingFile = new MeetingFile();
        meetingFile.setMeetingMenuId(Integer.parseInt(menuId));
        Employee employee = UserInfoUtil.getEmployeeByIdFromServer(jobNumber);
        String userName;
        if(employee!=null){
            userName = employee.getUserName();
        }else {
            userName = "用户不存在";
        }
        meetingFile.setUploadUserName(userName);
        meetingFile.setUploadJobNumber(jobNumber);
        MeetingMenu meetingMenu = meetingMenuService.getById(menuId);
        String checkList = meetingMenu.getCheckList();
        log.info("checkList---------------"+checkList);
      /*  // checkList == "[Y]"成立 不需要审核； 不成立 需要审核
        if(checkList.equals("[Y]")){
            meetingFile.setCheckList("[Y]");
            // 设置默认审核成功
            meetingFile.setPass(2);
            log.info("设置文件无需审核成功------------------");
        }else {
            log.info("sql审核列表"+checkList);
            checkList.replace(" ","");
            Pattern pattern = Pattern.compile(REGEX_CHINESE);
            checkList ="[Y]"+pattern.matcher(checkList).replaceAll("").replace("-->","[N]")+"[N]";
            log.info("审核过程："+checkList);
            meetingFile.setCheckList(checkList.replace(" ",""));
            meetingFile.setPass(0);
        }*/
        // 设置文件通过  审核状态为 通过
        meetingFile.setPass(2);
        meetingFile.setCheckList("[Y]");

        // 可以查看的部门
        meetingFile.setDepIds(depIds);
        meetingFile.setRefuseReason("");
        // 默认FormId为没有表单
        meetingFile.setFormText("");
        meetingFile.setAlive(0);


        // 设置当前审核节点
        meetingFile.setNowNode("");
        meetingFile.setRemark(LocalDateTime.now().toString()+userName+"("+jobNumber+")"+"上传文件");
        for(int i=0; i<file.length;i++){
            log.info("开始进入文件循环");
            if (file[i].isEmpty()) {
                log.info("文件上传失败");
                return "UploadFile";
                // return "redirect:/meetingFile/uploadFile";
            } else {
                log.info("文件名称"+file[i].getOriginalFilename());
                meetingFile.setFileName(file[i].getOriginalFilename());
                String sqlFileName= MyFileTransformation.fileNameTransformationAddDateFileName(file[i].getOriginalFilename());
                log.info("存入数据库中的URL="+sqlFileName);
                meetingFile.setUrl(sqlFileName);
                // 设置表单文件内容
                meetingFile.setFormText(text);
                // 设置表单值
                meetingFile.setFormId(meetingMenu.getFormId());

                //新建文件夹
                File file1 = new File(SystemCommon.FILE_SAVE_PATH);
                file1.mkdirs();
                //保存文件
                File dest = new File(SystemCommon.FILE_SAVE_PATH+sqlFileName);
                try {
                    //根据文件路径保存文件
                    file[i].transferTo(dest);
                    //将文件信息保存到数据库
                    meetingFileService.save(meetingFile);
                    log.info("上传成功");
                } catch (IOException e) {
                    log.info(e.toString(), e);
                }
            }
        }
        log.info("=====================================结束====================================");
        return "UploadFile";
        //return "redirect:/meetingFile/uploadFile";
    }

    /**
     *  获取用户上传的文件
     * @param menuId
     * @return
     */
    @GetMapping("/getFilesByJobNumber")
    @ResponseBody
    public RetResult<List<MeetingFile>> getFilesByJobNumber(Integer menuId){
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        QueryWrapper<MeetingFile> meetingFileQueryWrapper = new QueryWrapper<>();
        meetingFileQueryWrapper.eq("upload_job_number",jobNumber)
                .eq("meeting_menu_id",menuId);
        List<MeetingFile> meetingFiles = meetingFileService.list(meetingFileQueryWrapper);
        // 返回审批节点时需要转换
        for(MeetingFile meetingFile : meetingFiles){
            meetingFile.setCheckList(CheckChangeUtil.getApprovalProcess(meetingFile.getCheckList()));
        }
        return RetResponse.makeOKRsp(meetingFiles);
    }

    /**
     *  获取用户上传的文件
     * @param menuId
     * @return
     */
    @GetMapping("/getFilesToDo")
    @ResponseBody
    public RetResult<List<MeetingFile>> getFilesToDo(Integer menuId){
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        QueryWrapper<MeetingFile> meetingFileQueryWrapper = new QueryWrapper<>();
        meetingFileQueryWrapper.eq("upload_job_number",jobNumber)
                .eq("meeting_menu_id",menuId);
        List<MeetingFile> meetingFiles = meetingFileService.list(meetingFileQueryWrapper);
        return RetResponse.makeOKRsp(meetingFiles);
    }

    /**
     * 获取 待审核文件列表
     * @param menuId
     * @return
     */
    @GetMapping("/getNoCheckFiles")
    @ResponseBody
    public RetResult<List<RetFiles>> getNoCheckFiles(Integer menuId){
        return RetResponse.makeOKRsp(meetingFileService.iGetNoCheckFiles(menuId));
    }

    /**
     * 获取 待审核文件列表
     * @param menuId
     * @return
     */
    @GetMapping("/getToCheckFiles")
    @ResponseBody
    public RetResult<List<RetToCheckFile>> getToCheckFiles(Integer menuId){
        List<RetToCheckFile> meetingList = meetingFileService.iGetToCheckFiles();
        for(RetToCheckFile retToCheckFile : meetingList){
            // 截取会议名字 eg : 招标采购会议$$$当前时间 -》 截取为  招标采购会议
            retToCheckFile.setName(retToCheckFile.getName().split("===")[0]);
            retToCheckFile.setCheckList(CheckChangeUtil.getApprovalProcess(retToCheckFile.getCheckList()));
        }
        return RetResponse.makeOKRsp(meetingList);
    }

    /**
     * 获取 已审核文件列表
     * @param menuId
     * @return
     */
    @GetMapping("/getCheckedFiles")
    @ResponseBody
    public RetResult<List<RetToCheckFile>> getCheckedFiles(Integer menuId){
        List<RetToCheckFile> meetingList = meetingFileService.iGetToCheckedFiles();
        for(RetToCheckFile retToCheckFile : meetingList){
            // 截取会议名字 eg : 招标采购会议$$$当前时间 -》 截取为  招标采购会议
            retToCheckFile.setName(retToCheckFile.getName().split("===")[0]);
            retToCheckFile.setCheckList(CheckChangeUtil.getApprovalProcess(retToCheckFile.getCheckList()));
        }
        return RetResponse.makeOKRsp(meetingList);
    }

    /**
     * 根据会议ID 获取会议全部文件
     *
     * @param meetingId  会议Id
     * @return
     */
    @GetMapping("/getAllFilesByMeetingId")
    @ResponseBody
    public RetResult<List<RetFiles>> getAllFilesByMeetingId(Integer meetingId){
        return RetResponse.makeOKRsp(meetingFileService.iGetMeetingFilesByMeetingId(meetingId));
    }

    /**
     * 文件审核通过
     *
     * @param id 文件ID
     * @return
     */
    @GetMapping("/checkPass")
    @ResponseBody
    public RetResult<Boolean> checkPass(Integer id){
        log.info(id.toString());
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        MeetingFile meetingFile = meetingFileService.getById(id);
        String checkList = meetingFile.getCheckList();
        meetingFile.setCheckList(checkList.replace("[Y]("+jobNumber+")[N]","[Y]("+jobNumber+")[Y]"));
        // 添加操作记录  时间 - 姓名 -工号 -操作内容
        Employee employee = UserInfoUtil.getEmployeeByIdFromServer(jobNumber);
        String userName;
        if(employee!=null){
            userName = employee.getUserName();
        }else {
            userName = "用户不存在";
        }
        meetingFile.setRemark(LocalDateTime.now().toString()+userName+"("+jobNumber+")"+"文件审核通过");
        // 修改文件审核后的状态
        Boolean flag = meetingFileService.saveOrUpdate(meetingFile);

        // 判断是否全部审核完成，全部审核完毕 设置 pass = 2, 没有全部完成  pass = 0
        MeetingFile meetingFile1 = meetingFileService.getById(id);
        log.info("审核状态："+meetingFile1.getCheckList());
        if(meetingFile1.getCheckList().endsWith("[Y]")){
            meetingFile1.setPass(2);
            meetingFileService.saveOrUpdate(meetingFile1);
            // 发送钉钉消息，文件审核全部通过
            String sendText = "您上传的文件[ "+meetingFile.getFileName()+" ] 审核通过。";
            SendMessage.send(meetingFile.getUploadJobNumber(),sendText);
            log.info("审核全部完成");
        }else{
            log.info("本次审核完成，文件未全部审核完成");
        }
        return RetResponse.makeOKRsp(flag);
    }

    /**
     * 文件审核驳回
     *
     * @param refuseReason 驳回理由
     * @param id 文件ID
     * @return
     */
    @GetMapping("/refuse")
    @ResponseBody
    public RetResult<Boolean> checkPass(String refuseReason,Integer id){
        MeetingFile meetingFile = meetingFileService.getById(id);
        meetingFile.setRefuseReason(refuseReason);
        // 文件没有通过
        meetingFile.setPass(1);
        // 表示已经审批过了
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();

        // 文件审核失败时 pass =1 审核列表状态 不改变
        //String checkList = meetingFile.getCheckList();
        //meetingFile.setCheckList(checkList.replace("[Y]("+jobNumber+")[N]","[Y]("+jobNumber+")[N]"));


        // 添加操作记录  时间 - 姓名 -工号 -操作内容
        Employee employee = UserInfoUtil.getEmployeeByIdFromServer(jobNumber);
        String userName;
        if(employee!=null){
            userName = employee.getUserName();
        }else {
            userName = "用户不存在";
        }
        meetingFile.setRemark(LocalDateTime.now().toString()+userName+"("+jobNumber+")"+"文件审核驳回");
        // 文件驳回时给 文件创建者发送钉钉消息（文件***被驳回，驳回理由）
        String sendText = "您上传的文件[ "+meetingFile.getFileName()+" ] 被"+userName+"驳回，驳回理由： "+refuseReason+"请确认错误后重新上传文件。";
        SendMessage.send(meetingFile.getUploadJobNumber(),sendText);
        return RetResponse.makeOKRsp( meetingFileService.saveOrUpdate(meetingFile));
    }


    /**
     * 获取文件预览信息
     *
     * @param id 文件Id
     * @return
     */
    @GetMapping("/getFormTexts")
    @ResponseBody
    public RetResult<List<RetTileText>> getFormTexts(Integer id){
        List<RetTileText> retTileTextList = new ArrayList<>();
        // 根据ID 获取表单内容 (去除所有空格)
        MeetingFile meetingFile = meetingFileService.getById(id);
        if(meetingFile.getFormId() == 0){
            return RetResponse.makeOKRsp(null);
        }else {
            String formText = meetingFile.getFormText().replace(" ","");
            String[]  textArr = formText.split("@@@");
            // 根据表单Id获取全部会议列表
            List<Title> titleList = titleService.iGetTextByFormId(meetingFile.getFormId());
            Integer size = 0;
            if(titleList.size() <= textArr.length){
                size = titleList.size();
            }else {
                size = textArr.length;
            }
            for(int i = 0; i< size;i++){
                String titleName = titleList.get(i).getTitleName();
                String text = textArr[i];
                RetTileText retTileText = new RetTileText();
                retTileText.setTitle(titleName);
                retTileText.setText(text);
                retTileTextList.add(retTileText);
            }
            return RetResponse.makeOKRsp(retTileTextList);
        }
    }

    /**
     * 获取历史文件
     *
     * @param menuId 目录
     * @return
     */
    @GetMapping("/getHistoryFiles")
    @ResponseBody
    public RetResult<List<RetDownloadFile>> getHistoryFiles(Integer menuId){
        List<RetDownloadFile > downloadFiles = meetingFileService.iGetDownloadFiles(menuId);
        for (RetDownloadFile retDownloadFile : downloadFiles){
            retDownloadFile.setUrl( SystemCommon.DOWNLOAD_BASE_URL+retDownloadFile.getUrl());
        }
        return RetResponse.makeOKRsp(downloadFiles);
    }



}
