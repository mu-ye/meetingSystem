package com.demo.demo.sys.service.impl;

import com.demo.demo.sys.entity.Meeting;
import com.demo.demo.sys.mapper.MeetingMapper;
import com.demo.demo.sys.service.IMeetingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 牟欢
 * @since 2020-06-04
 */
@Service
public class MeetingServiceImpl extends ServiceImpl<MeetingMapper, Meeting> implements IMeetingService {

    @Autowired
    MeetingMapper meetingMapper;

    @Override
    public List<Meeting> iGetMeetingsToDo() {
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        LocalTime initLocalTime = LocalTime.of(0,0,0);
        LocalDateTime nowDate = LocalDateTime.of(LocalDate.now(),initLocalTime);
        return meetingMapper.iSelectMeetingsToDo(jobNumber,nowDate);
    }

    @Override
    public List<Meeting> iGetMeetingsHistory() {
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        LocalTime initLocalTime = LocalTime.of(0,0,0);
        LocalDateTime nowDate = LocalDateTime.of(LocalDate.now(),initLocalTime);
        return meetingMapper.iSelectMeetingsHistory(jobNumber,nowDate);
    }
}
