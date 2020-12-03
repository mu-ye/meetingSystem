package com.demo.demo.sys.service;

import com.demo.demo.sys.entity.UserLeader;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 牟欢
 * @since 2020-07-15
 */
public interface IUserLeaderService extends IService<UserLeader> {
    /**
     * 根据工号查找 用户
     *
     * @param jobNumber
     * @return
     */
    UserLeader getUserLeaderListByJobNumber(String jobNumber);

}
