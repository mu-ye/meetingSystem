package com.demo.demo.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.demo.sys.entity.Meeting;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 牟欢
 * @since 2020-06-04
 */
public interface IMeetingService extends IService<Meeting> {
    /**
     * 返回自己创建的还未 开始的会议列表
     * @return
     */
    public List<Meeting> iGetMeetingsToDo();

    /**
     * 返回自己创建的历史会议
     * @return
     */
    public List<Meeting> iGetMeetingsHistory();


}
