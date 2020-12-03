package com.demo.demo.sys.mapper;

import com.demo.demo.sys.entity.Meeting;
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
 * @since 2020-06-04
 */
public interface MeetingMapper extends BaseMapper<Meeting> {
    /**
     * 查找 自己创建的会议开始时间大于等于今天的会议
     *
     * @param jobNumber 会议创建者 工号
     * @param nowDate 当前日期
     * @return
     */
    public List<Meeting> iSelectMeetingsToDo(@Param("jobNumber") String jobNumber, @Param("nowDate") LocalDateTime nowDate);

    /**
     * fasg
     *
     * @param jobNumber
     * @param nowDate
     * @return
     */
    public List<Meeting> iSelectMeetingsHistory(@Param("jobNumber") String jobNumber, @Param("nowDate") LocalDateTime nowDate);

}
