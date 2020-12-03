package com.demo.demo.sys.mapper;

import com.demo.demo.sys.entity.FreeIp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.demo.sys.result.RetIp;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 牟欢
 * @since 2020-05-27
 */

// 注意： 自定义的 mapper 函数名不能和 MYBATIS-PLUS 中封装的 mapper名称一样，否则会出现一些 返回值类型不匹配的问题
// 为解决这个问题： 在 mapper 函数前 加上 “i“ eg : ISelectList 避免和系统自定义的 SelectList 冲突，覆盖默认的SelectList方法。


public interface FreeIpMapper extends BaseMapper<FreeIp> {
    List<RetIp> iSelectIpList();

}
