package com.demo.demo.sys.service.impl;

import com.demo.demo.sys.entity.UserInfo;
import com.demo.demo.sys.mapper.UserInfoMapper;
import com.demo.demo.sys.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 牟欢
 * @since 2020-05-26
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
