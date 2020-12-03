package com.demo.demo.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.demo.sys.entity.MeetingFile;
import com.demo.demo.sys.mapper.MeetingFileMapper;
import com.demo.demo.sys.result.RetDownloadFile;
import com.demo.demo.sys.result.RetFiles;
import com.demo.demo.sys.result.RetToCheckFile;
import com.demo.demo.sys.service.IMeetingFileService;
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
 * @since 2020-06-10
 */
@Service
public class MeetingFileServiceImpl extends ServiceImpl<MeetingFileMapper, MeetingFile> implements IMeetingFileService {

    @Autowired
    MeetingFileMapper meetingFileMapper;
    @Override
    public List<RetToCheckFile> iGetToCheckFiles(){
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        jobNumber="[Y]("+jobNumber+")[N]";
        LocalTime initLocalTime = LocalTime.of(0,0,0);
        LocalDateTime nowDate = LocalDateTime.of(LocalDate.now(),initLocalTime);
        return meetingFileMapper.iSelectToCheckFiles(jobNumber,nowDate);

    }

    @Override
    public List<RetFiles> iGetNoCheckFiles(Integer menuId) {
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        return meetingFileMapper.iSelectNoCheckFiles(jobNumber,menuId);
    }

    @Override
    public List<RetToCheckFile> iGetToCheckedFiles() {
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        jobNumber="[Y]("+jobNumber+")[Y]";
        LocalTime initLocalTime = LocalTime.of(0,0,0);
        LocalDateTime nowDate = LocalDateTime.of(LocalDate.now(),initLocalTime);
        return meetingFileMapper.iSelectToCheckedFiles(jobNumber,nowDate);
    }

    @Override
    public List<RetFiles> iGetMeetingFilesByMeetingId(Integer meetingId) {
        return meetingFileMapper.iSelectMeetingFiles(meetingId);
    }

    @Override
    public List<String> iGetAttendDepartment(Integer meetingId) {
        return meetingFileMapper.iSelectAttendDepartment(meetingId);
    }

    @Override
    public List<RetDownloadFile> iGetDownloadFiles(Integer menuId) {
        return meetingFileMapper.iSelectDownloadFiles(menuId);
    }
}
