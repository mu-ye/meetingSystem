package com.demo.demo.sys.service;

import com.demo.demo.sys.entity.FreeIp;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.demo.sys.result.RetIp;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 牟欢
 * @since 2020-05-27
 */
public interface IFreeIpService extends IService<FreeIp> {
    /**
     * 获取全部免登IP
     * @return
     */
    List<RetIp> iGetIpList();
}
