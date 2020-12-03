package com.demo.demo.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.demo.sys.entity.Meeting;
import com.demo.demo.sys.entity.MeetingMenu;
import com.demo.demo.sys.entity.treeNode.RootNode;
import com.demo.demo.sys.mapper.MeetingMenuMapper;
import com.demo.demo.sys.service.IMeetingMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 牟欢
 * @since 2020-05-26
 */
@Service
public class MeetingMenuServiceImpl extends ServiceImpl<MeetingMenuMapper, MeetingMenu> implements IMeetingMenuService {

    @Autowired
    MeetingMenuMapper meetingMenuMapper;
    
    @Override
    public List<RootNode> iSelectMeetingMenuListByTemplateId(int meetingId, int parentId) {
        List<RootNode> resultRootNodeList = new ArrayList<>();
        QueryWrapper<MeetingMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("meeting_id",meetingId)
                .eq("parent_id",parentId);
        List<MeetingMenu> templateMenuList = meetingMenuMapper.selectList(queryWrapper);
        if(templateMenuList.isEmpty()){
            return null;
        }else{
            for(MeetingMenu meetingMenu : templateMenuList){
                List<RootNode> rootNodeList = iSelectMeetingMenuListByTemplateId(meetingId,meetingMenu.getId());
                RootNode rootNode = new RootNode();
                if(rootNodeList != null) {

                    rootNode.setId(meetingMenu.getId());
                    rootNode.setText(meetingMenu.getText());
                    rootNode.setIsLeaf(false);
                    rootNode.setNodes(rootNodeList);
                }else {
                    rootNode.setId(meetingMenu.getId());
                    rootNode.setText(meetingMenu.getText());
                    rootNode.setIsLeaf(true);
                    rootNode.setNodes(null);
                }
                resultRootNodeList.add(rootNode);

            }
            return resultRootNodeList;
        }
    }

    @Override
    public List<RootNode> iSelectMeetingMenuListUpload(int meetingId, int parentId) {
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        List<RootNode> resultRootNodeList = new ArrayList<>();
        QueryWrapper<MeetingMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("meeting_id",meetingId)
                .eq("parent_id",parentId);
        List<MeetingMenu> templateMenuList = meetingMenuMapper.selectList(queryWrapper);
        if(templateMenuList.isEmpty()){
            return null;
        }else{
            for(MeetingMenu meetingMenu : templateMenuList){
                List<RootNode> rootNodeList = iSelectMeetingMenuListUpload(meetingId,meetingMenu.getId());
                RootNode rootNode = new RootNode();
                if(rootNodeList != null) {
                    rootNode.setId(meetingMenu.getId());
                    rootNode.setText(meetingMenu.getText());
                    rootNode.setIsLeaf(false);
                    rootNode.setNodes(rootNodeList);

                    rootNode.setIsShow(false);
                }else {
                    rootNode.setId(meetingMenu.getId());
                    rootNode.setText(meetingMenu.getText());
                    meetingMenu.getFileUpJobNumbers();
                    rootNode.setIsLeaf(true);
                    rootNode.setNodes(null);
                }
                // 判断是否可以上传文件  true: 可以上传文件  false: 不可以上传文件
                rootNode.setIsShow(meetingMenu.getFileUpJobNumbers().contains(jobNumber));
                resultRootNodeList.add(rootNode);

            }
            return resultRootNodeList;
        }
    }

    @Override
    public List<RootNode> iSelectAllMeetingMenuListUpload(int meetingId, int parentId) {
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        List<RootNode> resultRootNodeList = new ArrayList<>();
        QueryWrapper<MeetingMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("meeting_id",meetingId)
                .eq("parent_id",parentId);
        List<MeetingMenu> templateMenuList = meetingMenuMapper.selectList(queryWrapper);
        if(templateMenuList.isEmpty()){
            return null;
        }else{
            for(MeetingMenu meetingMenu : templateMenuList){
                List<RootNode> rootNodeList = iSelectMeetingMenuListUpload(meetingId,meetingMenu.getId());
                RootNode rootNode = new RootNode();
                if(rootNodeList != null) {
                    rootNode.setId(meetingMenu.getId());
                    rootNode.setText(meetingMenu.getText());
                    rootNode.setIsLeaf(false);
                    rootNode.setNodes(rootNodeList);

                    rootNode.setIsShow(false);
                }else {
                    rootNode.setId(meetingMenu.getId());
                    rootNode.setText(meetingMenu.getText());
                    meetingMenu.getFileUpJobNumbers();
                    rootNode.setIsLeaf(true);
                    rootNode.setNodes(null);
                }
                // 判断是否可以上传文件  true: 可以上传文件  false: 不可以上传文件
                rootNode.setIsShow(true);
                resultRootNodeList.add(rootNode);

            }
            return resultRootNodeList;
        }
    }


    @Override
    public List<Integer> iGetCheckRole(String jobNumber) {
        LocalTime initLocalTime = LocalTime.of(0,0,0);
        LocalDateTime nowDate = LocalDateTime.of(LocalDate.now(),initLocalTime);
        return meetingMenuMapper.iSelectCheckRole(jobNumber,nowDate);
    }

    @Override
    public List<Meeting> iGetMeetingListUpload(String jobNumber) {
        LocalTime initLocalTime = LocalTime.of(0,0,0);
        LocalDateTime nowDate = LocalDateTime.of(LocalDate.now(),initLocalTime);
        return meetingMenuMapper.iSelectMeetingListUpLoad(jobNumber,nowDate);
    }
}
