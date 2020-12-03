package com.demo.demo.sys.service.impl;

import com.demo.demo.sys.entity.Department;
import com.demo.demo.sys.mapper.DepartmentMapper;
import com.demo.demo.sys.service.IDepartmentService;
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
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

}
