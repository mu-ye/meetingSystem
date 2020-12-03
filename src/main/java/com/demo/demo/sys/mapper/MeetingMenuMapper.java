package com.demo.demo.sys.mapper;

import com.demo.demo.sys.entity.Meeting;
import com.demo.demo.sys.entity.MeetingMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 牟欢
 * @since 2020-05-26
 */
public interface MeetingMenuMapper extends BaseMapper<MeetingMenu> {

    /**
     *  用户是否有审核权限
     *
     * @param jobNumber
     * @param nowLocalDateTime
     * @return
     */
    public List<Integer> iSelectCheckRole(@Param("jobNumber")String jobNumber,@Param("nowLocalDateTime") LocalDateTime nowLocalDateTime);

    /**
     * 允许我上传的文件列表
     *
     * @param jobNumber 用户工号
     * @param nowLocalDateTime 当前日期
     * @return
     */
    public List<Meeting> iSelectMeetingListUpLoad(@Param("jobNumber")String jobNumber, @Param("nowLocalDateTime") LocalDateTime nowLocalDateTime);

}
