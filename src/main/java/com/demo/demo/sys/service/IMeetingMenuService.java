package com.demo.demo.sys.service;

import com.demo.demo.sys.entity.Meeting;
import com.demo.demo.sys.entity.MeetingMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.demo.sys.entity.treeNode.RootNode;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 牟欢
 * @since 2020-05-26
 */
public interface IMeetingMenuService extends IService<MeetingMenu> {
    /**
     * 获取模板目录结构(新建模板  编辑模板)
     * @param meetingId 模板ID
     * @param parentId 模板的开始位置
     * @return
     */
    public List<RootNode> iSelectMeetingMenuListByTemplateId(int meetingId, int parentId);

    /**
     * 获取模板目录结构(新建模板  编辑模板)
     * @param meetingId 模板ID
     * @param parentId 模板的开始位置
     * @return
     */
    public List<RootNode> iSelectMeetingMenuListUpload(int meetingId, int parentId);


    /**
     * 获取全部文件上传目录
     * @param meetingId 模板ID
     * @param parentId 模板的开始位置
     * @return
     */
    public List<RootNode> iSelectAllMeetingMenuListUpload(int meetingId, int parentId);

    /**
     * 登陆用户是否有待审核权限
     *
     * @return
     */
    public List<Integer> iGetCheckRole(String jobNumber);

    /**
     * 获取 用户需要上传的会议列表
     *
     * @return
     */
    public List<Meeting> iGetMeetingListUpload(String jobNumber);

}
