package com.demo.demo.sys.service.impl;

import com.demo.demo.sys.entity.FreeIp;
import com.demo.demo.sys.mapper.FreeIpMapper;
import com.demo.demo.sys.result.RetIp;
import com.demo.demo.sys.service.IFreeIpService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 牟欢
 * @since 2020-05-27
 */
@Service
public class FreeIpServiceImpl extends ServiceImpl<FreeIpMapper, FreeIp> implements IFreeIpService {

    @Autowired
    FreeIpMapper freeIpMapper;
    @Override
    public List<RetIp> iGetIpList() {
        return freeIpMapper.iSelectIpList();
    }
}
