package com.demo.demo.sys.controller;


import com.demo.demo.core.ret.RetResponse;
import com.demo.demo.core.ret.RetResult;
import com.demo.demo.core.unit.UserInfoUtil;
import com.demo.demo.sys.domain.Employee;
import com.demo.demo.sys.entity.MeetingMenu;
import com.demo.demo.sys.entity.treeNode.RootNode;
import com.demo.demo.sys.service.impl.MeetingMenuServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 牟欢
 * @since 2020-05-26
 */
@Controller
@Slf4j
@RequestMapping("/meetingMenu")
public class MeetingMenuController {
    @Autowired
    MeetingMenuServiceImpl meetingMenuService;
    /**
     *  获取 会议目录
     *
     * @param meetingId 模板ID
     * @return  模板目录结构数据
     */
    @GetMapping("/getMeetingMenu")
    @ResponseBody
    public RetResult<List<RootNode>> iGetMeetingMenuListByTemplateId(int meetingId){
        return RetResponse.makeOKRsp(meetingMenuService.iSelectMeetingMenuListByTemplateId(meetingId,0));
    }

    /**
     *  获取 会议目录
     *
     * @param meetingId 模板ID
     * @return  模板目录结构数据
     */
    @GetMapping("/getMeetingMenuUpload")
    @ResponseBody
    public RetResult<List<RootNode>> getMeetingMenuUpload(int meetingId){
        return RetResponse.makeOKRsp(meetingMenuService.iSelectMeetingMenuListUpload(meetingId,0));
    }

    /**
     *  获取全部会议目录
     *
     * @param meetingId 模板ID
     * @return  模板目录结构数据
     */
    @GetMapping("/getAllMeetingMenuUpload")
    @ResponseBody
    public RetResult<List<RootNode>> getAllMeetingMenuUpload(int meetingId){
        return RetResponse.makeOKRsp(meetingMenuService.iSelectAllMeetingMenuListUpload(meetingId,0));
    }

    /**
     * 根据ID 获取节点信息
     *
     * @param id 节点ID
     * @return 节点的全部信息
     */
    @GetMapping("/getOne")
    @ResponseBody
    public RetResult<MeetingMenu>  getOne(Integer id){
        return RetResponse.makeOKRsp(meetingMenuService.getById(id));
    }

    /**
     * 重命名
     *
     * @param text 节点新名字
     * @param id 节点ID
     * @return 重命名失败  true: 重命名成功
     */
    @GetMapping("/changeName")
    @ResponseBody
    public RetResult<Boolean>  changeName(String text,Integer id){
        MeetingMenu meetingMenu = meetingMenuService.getById(id);
        meetingMenu.setText(text);
        meetingMenu.setAlive(0);
        return RetResponse.makeOKRsp(meetingMenuService.saveOrUpdate(meetingMenu));
    }

    /**
     * 删除一个节点
     *
     * @param id 删除节点ID
     * @return false : 删除失败  true: 删除成功
     */
    @PostMapping("/del")
    @ResponseBody
    public RetResult<Boolean>  del(Integer id){
        return RetResponse.makeOKRsp(meetingMenuService.removeById(id));
    }

    /**
     * 添加同级节点
     *
     * @param text 同级节点名字
     * @param id 参照节点的ID, 根据ID找到parent_id,作为同级节点的parent_id
     * @param meetingId 节点所在模板ID
     * @return 添加失败  true: 添加成功
     */
    @GetMapping("/addSameNode")
    @ResponseBody
    public RetResult<Boolean>  addSameNode(String text,Integer id,Integer meetingId){
        RetResult retResult = new RetResult();
        MeetingMenu meetingMenu = meetingMenuService.getById(id);
        MeetingMenu meetingMenuNew = new MeetingMenu();
        meetingMenuNew.setText(text);
        meetingMenuNew.setParentId(meetingMenu.getParentId());
        meetingMenuNew.setMeetingId(meetingId);
        meetingMenuNew.setCheckList("[Y]");
        // 根据工号获取 用户信息
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        Employee employee = UserInfoUtil.getEmployeeByIdFromServer(jobNumber);
        if(employee!=null){
            meetingMenuNew.setFileUpJobNumbers("("+jobNumber+")"+employee.getUserName());
        }else {
            meetingMenuNew.setFileUpJobNumbers("0");
        }
        meetingMenuNew.setAttendJobNumbers("0");
        meetingMenuNew.setFormId(0);
        meetingMenuNew.setAlive(0);
        meetingMenuNew.setDepIds("0");
        return RetResponse.makeOKRsp(meetingMenuService.save(meetingMenuNew));
    }

    /**
     * 添加子级节点
     *
     * @param text 子级节点名称
     * @param id 参照节点的ID, 将参照节点的ID作为子级节点的parent_id
     * @param meetingId
     * @return false : 添加失败  true: 添加成功
     */
    @GetMapping("/addNextNode")
    @ResponseBody
    public RetResult<Boolean>  addNextNode(String text,Integer id,Integer meetingId){
        MeetingMenu meetingMenu = meetingMenuService.getById(id);
        MeetingMenu meetingMenuNew = new MeetingMenu();
        meetingMenuNew.setText(text);
        meetingMenuNew.setParentId(id);
        meetingMenuNew.setAlive(0);
        meetingMenuNew.setMeetingId(meetingId);
        meetingMenuNew.setCheckList("[Y]");
        // 根据工号获取 用户信息
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        Employee employee = UserInfoUtil.getEmployeeByIdFromServer(jobNumber);
        if(employee!=null){
            meetingMenuNew.setFileUpJobNumbers("("+jobNumber+")"+employee.getUserName());
        }else {
            meetingMenuNew.setFileUpJobNumbers("0");
        }
        meetingMenuNew.setAttendJobNumbers("0");
        meetingMenuNew.setFormId(0);
        meetingMenuNew.setDepIds("0");
        return RetResponse.makeOKRsp(meetingMenuService.save(meetingMenuNew));
    }

    /**
     * 编辑目录节点
     *
     * @param id 节点ID
     * @param fileUpJobNumbers 文件上传人列表： 默认只有会议创建者
     * @param formId 是否需要表头 0： 不需要表头  1： 根据表头ID
     * @param checkList 是否需要审批 0： 无需审批 1： 审批列表
     * @return false : 编辑失败  true: 编辑成功
     */
    @PostMapping("/editMenu")
    @ResponseBody
    public RetResult<Boolean>  editMenu(Integer id, String fileUpJobNumbers, Integer formId,String checkList,String depIds){
        log.info("depIds"+depIds);
        MeetingMenu meetingMenu = meetingMenuService.getById(id);
        meetingMenu.setFormId(formId);
        meetingMenu.setFileUpJobNumbers(fileUpJobNumbers);
        meetingMenu.setCheckList(checkList);
        // 设置部门
        meetingMenu.setDepIds(depIds);
        return RetResponse.makeOKRsp(meetingMenuService.saveOrUpdate(meetingMenu));
    }


}
