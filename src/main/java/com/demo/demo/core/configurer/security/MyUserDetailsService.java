package com.demo.demo.core.configurer.security;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.demo.sys.entity.UserRole;
import com.demo.demo.sys.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

;
/**
 * @ClassName MyUserDetailsService
 * @Description TODO
 * @Author muhuan
 * @Version 1.0
 * @Date 2019/11/25 09:53
 */
@Service
@Slf4j
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    UserInfoServiceImpl userInfoService;
    @Autowired
    UserRoleServiceImpl userRoleService;
    @Autowired
    RoleServiceImpl roleService;
    @Autowired
    FreeIpServiceImpl freeIpService;
    @Autowired
    MeetingMenuServiceImpl meetingMenuService;

    @Override
    public UserDetails loadUserByUsername(String jobNumber) throws UsernameNotFoundException {
        //jobNumber = "117033";
        Collection<GrantedAuthority> authorities = new ArrayList<>();
       /* log.info("=============开始进行登陆校验=================");
        log.info("用户工号"+jobNumber);

        //  根据IP免登
        //  此时传进来的jobNumber 为用户登陆IP
        if (IpUtil.ipCheck(jobNumber)){
            log.info(jobNumber+"是Ip");
            QueryWrapper<FreeIp> freeIpQueryWrapper = new QueryWrapper<>();
            freeIpQueryWrapper.eq("ip",freeIpQueryWrapper);
            FreeIp freeIp = freeIpService.getOne(freeIpQueryWrapper);
            if (freeIp == null){
                log.info("此IP不可免登");
            }else {
                log.info("根据IP获取用户工号"+jobNumber);
                jobNumber = freeIp.getJobNumber();
            }
        }else {
            log.info(jobNumber+"不是Ip");
            if(UserInfoUtil.getEmployeeByIdFromServer(jobNumber) == null){
                // 查询到没有此员工
                log.info("用户不存在");
            }else {
                // 用户默认都有 会议参会 权限
                // authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

                // 判断用户是否有 会议创建者权限
                QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
                userRoleQueryWrapper.eq("job_number",jobNumber);
                UserRole userRole = userRoleService.getOne(userRoleQueryWrapper);
                if(userRole != null){
                    authorities.add(new SimpleGrantedAuthority("ROLE_MEETING_CREAT"));
                }
                // 判断用户是否有会议审批权限
                if(!meetingMenuService.iGetCheckRole(jobNumber).isEmpty()){
                    authorities.add(new SimpleGrantedAuthority("ROLE_CHECK"));
                }
                // 判断用户是否有文件上传权限
                if(!meetingMenuService.iGetMeetingListUpload(jobNumber).isEmpty()){
                    authorities.add(new SimpleGrantedAuthority("ROLE_UPLOAD"));
                }
            }
        }*/
        // 判断用户是否有 会议创建者权限
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("job_number",jobNumber);
        UserRole userRole = userRoleService.getOne(userRoleQueryWrapper);
        if(userRole != null){
            authorities.add(new SimpleGrantedAuthority("ROLE_MEETING_CREAT"));
        }
        // 判断用户是否有会议审批权限
        if(!meetingMenuService.iGetCheckRole(jobNumber).isEmpty()){
            authorities.add(new SimpleGrantedAuthority("ROLE_CHECK"));
        }
        // 判断用户是否有文件上传权限
        if(!meetingMenuService.iGetMeetingListUpload(jobNumber).isEmpty()){
            authorities.add(new SimpleGrantedAuthority("ROLE_UPLOAD"));
        }
        log.info("用户拥有的权限："+authorities.toString());
        return new User(jobNumber, jobNumber, authorities);
    }
}
