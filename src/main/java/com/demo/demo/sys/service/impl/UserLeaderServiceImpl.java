package com.demo.demo.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.demo.sys.entity.UserLeader;
import com.demo.demo.sys.mapper.UserLeaderMapper;
import com.demo.demo.sys.service.IUserLeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 牟欢
 * @since 2020-07-15
 */
@Service
public class UserLeaderServiceImpl extends ServiceImpl<UserLeaderMapper, UserLeader> implements IUserLeaderService {
    @Autowired
    UserLeaderMapper userLeaderMapper;

    @Override
    public UserLeader getUserLeaderListByJobNumber(String jobNumber) {
        QueryWrapper<UserLeader> userLeaderQueryWrapper = new QueryWrapper<>();
        userLeaderQueryWrapper.eq("leader_job_number",jobNumber);
        List<UserLeader> userLeaderList = userLeaderMapper.selectList(userLeaderQueryWrapper);
        if(!userLeaderList.isEmpty()){
            // 返回第一个
            return userLeaderList.get(0);
        }else {
            return null;
        }
    }

}
