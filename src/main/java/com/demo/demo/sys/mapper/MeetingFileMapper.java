package com.demo.demo.sys.mapper;

import com.demo.demo.sys.entity.MeetingFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.demo.sys.result.RetDownloadFile;
import com.demo.demo.sys.result.RetFiles;
import com.demo.demo.sys.result.RetToCheckFile;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 * @author 牟欢
 * @since 2020-06-10
 */
public interface MeetingFileMapper extends BaseMapper<MeetingFile> {

    /**
     *  根据审核人员的工号 获取 要审核 的文件列表
     *
     * @param jobNumber 模糊查询 审查节点 [Y](+jobNumber+)[N] eg("[Y](117042)[n]")
     * @param nowDate 当天时间
     * @return
     */
    public List<RetToCheckFile> iSelectToCheckFiles(@Param("jobNumber") String jobNumber, @Param("nowDate") LocalDateTime nowDate);

    /**
     *  根据审核人员的工号 获取 要审核 的文件列表
     *
     * @param menuId 文件所在的menuID
     * @param jobNumber 用户工号
     * @return
     */
    public List<RetFiles> iSelectNoCheckFiles(@Param("jobNumber") String jobNumber,@Param("menuId") Integer menuId);

    /**
     *  根据审核人员的工号 获取 已审核 的文件列表
     *
     * @param jobNumber 模糊查询 审查节点 [Y](+jobNumber+)[N] eg("[Y](117042)[n]")
     * @param nowDate 当天时间
     * @return
     */
    public List<RetToCheckFile> iSelectToCheckedFiles(@Param("jobNumber") String jobNumber,@Param("nowDate") LocalDateTime nowDate);

    /**
     *  根据会议Id 获取全部上传文件
     *
     * @param meetingId 会议Id
     * @return
     */
    public List<RetFiles> iSelectMeetingFiles(@Param("meetingId") Integer meetingId);

    /**
     * 根据Id获取全部参会部门
     *
     * @param meetingId 会议Id
     * @return
     */
    public List<String> iSelectAttendDepartment(@Param("meetingId") Integer meetingId);

    /**
     * 根据目录id 获取所有通过的会议
     *
     * @param meetingId 目录ID
     * @return
     */
    public List<RetDownloadFile> iSelectDownloadFiles(@Param("meetingId") Integer meetingId);


}
