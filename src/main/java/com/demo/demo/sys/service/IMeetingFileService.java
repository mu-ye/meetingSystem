package com.demo.demo.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.demo.sys.entity.MeetingFile;
import com.demo.demo.sys.result.RetDownloadFile;
import com.demo.demo.sys.result.RetFiles;
import com.demo.demo.sys.result.RetToCheckFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 牟欢
 * @since 2020-06-10
 */
public interface IMeetingFileService extends IService<MeetingFile> {
    /**
     * 获取待审核的文件列表
     * @return
     */
    public List<RetToCheckFile> iGetToCheckFiles();

    /**
     * 根据menuId 获取无需审核的文件
     *
     * @param menuId 目录ID
     * @return
     */
    public List<RetFiles> iGetNoCheckFiles(Integer menuId);

    /**
     * 获取已审核的文件列表
     * @return
     */
    public List<RetToCheckFile> iGetToCheckedFiles();

    /**
     * 根据会议ID 获取会议全部文件
     *
     * @param meetingId 会议Id
     * @return
     */
    public List<RetFiles> iGetMeetingFilesByMeetingId(Integer meetingId);

    /**
     * 根据Id获取全部参会部门
     *
     * @param meetingId 会议Id
     * @return
     */
    public List<String> iGetAttendDepartment(Integer meetingId);

    /**
     * 根据目录id 获取所有通过的会议
     *
     * @param menuId
     * @return
     */
    public List<RetDownloadFile> iGetDownloadFiles(Integer menuId);


}
