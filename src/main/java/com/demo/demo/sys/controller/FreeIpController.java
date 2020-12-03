package com.demo.demo.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.demo.core.ret.RetResponse;
import com.demo.demo.core.ret.RetResult;
import com.demo.demo.core.unit.IpUtil;
import com.demo.demo.core.unit.UserInfoUtil;
import com.demo.demo.sys.domain.Employee;
import com.demo.demo.sys.entity.FreeIp;
import com.demo.demo.sys.entity.UserLeader;
import com.demo.demo.sys.result.RetIp;
import com.demo.demo.sys.service.impl.FreeIpServiceImpl;
import com.demo.demo.sys.service.impl.UserLeaderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 牟欢
 * @since 2020-05-27
 */
@RestController
@RequestMapping("/freeIp")
@Slf4j
public class FreeIpController {
    @Autowired
    FreeIpServiceImpl freeIpService;
    @Autowired
    UserLeaderServiceImpl userLeaderService;



    /**
     * 根据用户注册的Ip获取用户的工号
     *
     * @return
     */
    @PostMapping("/getJobNumberByFreeIp")
    @ResponseBody
    public RetResult<String> getJobNumberByFreeIp(HttpServletRequest request){
        // 获取访问的 IP
        String ip = IpUtil.getIpAddr(request);
        log.info("用户Ip: "+ip);
        if(!IpUtil.ipCheck(ip)) {
            log.info("Ip 格式错误");
            return RetResponse.makeErrRsp("ip格式不正确");
        }else {
            QueryWrapper<FreeIp> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ip",ip);
            FreeIp freeIp = freeIpService.getOne(queryWrapper);
            if(freeIp == null){
                log.info("Ip未注册");
                return RetResponse.makeErrRsp("Ip未注册");
            }else {
                return RetResponse.makeOKRsp(freeIp.getJobNumber());
            }
        }
    }

    /**
     * 返回免登 Ip 列表
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    public RetResult<List<RetIp>> getList(){
        List<RetIp> retIpList = new ArrayList<>();
        List<FreeIp>  freeIpList = freeIpService.list();
        for(FreeIp freeIp : freeIpList){
            RetIp retIp = new RetIp();
            retIp.setId(freeIp.getId());
            retIp.setIp(freeIp.getIp());
            retIp.setJobNumber(freeIp.getJobNumber());
            // 在 钉钉组织架构中找到 用户
            //String result = UserInfoUtil.get(SystemCommon.BASE_USER_INFO_URL+freeIp.getJobNumber());
            Employee result = UserInfoUtil.getEmployeeByIdFromServer(freeIp.getJobNumber());
            if( result== null){

                UserLeader userLeader = userLeaderService.getUserLeaderListByJobNumber(freeIp.getJobNumber());
                if(userLeader != null){
                    retIp.setDepartmentName(userLeader.getDepartmentName());
                    retIp.setUserName(userLeader.getName());
                }else {
                    retIp.setDepartmentName("-");
                    retIp.setUserName("用户不存在");
                }
            }else {
                retIp.setDepartmentName(result.getDepartment());
                retIp.setUserName(result.getUserName());
            }
            retIpList.add(retIp);
        }
        return RetResponse.makeOKRsp(retIpList);
    }

    /**
     *  批量删除
     * @param ids 要删除的id数组
     * @return
     */
    @PostMapping("/del")
    @ResponseBody
    public RetResult<Integer> del(@RequestBody List<Integer> ids){
        for(Integer id :ids){
            log.info("freeIpService.removeById(id)"+freeIpService.removeById(id));
        }
        return RetResponse.makeOKRsp(1);
    }

    /**
     *
     * @param jobNumber 工号
     * @param ip Ip
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public RetResult<String> add(@RequestParam("jobNumber") String jobNumber,@RequestParam("ip") String ip) throws UnsupportedEncodingException {
        if(!IpUtil.ipCheck(ip)) {
            log.info("Ip 格式不正确，请重新输入IP");
            return RetResponse.makeErrRsp("Ip 格式不正确，请重新输入IP");
        }else {
            //String result = UserInfoUtil.get(SystemCommon.BASE_USER_INFO_URL+jobNumber);
            Employee result = UserInfoUtil.getEmployeeByIdFromServer(jobNumber);
            if(result == null){
                log.info("工号不存在，请核对后重新数据工号");
                return RetResponse.makeErrRsp("工号不存在，请核对后重新数据工号");
            }else {
                // ip 是否已经注册
                QueryWrapper<FreeIp> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("ip",ip);
                if(freeIpService.getOne(queryWrapper)==null){
                    FreeIp freeIp = new FreeIp();
                    freeIp.setIp(ip);
                    freeIp.setJobNumber(jobNumber);
                    freeIp.setAlive(0);
                    freeIpService.save(freeIp);
                    log.info("添加IP成功");
                    return RetResponse.makeOKRsp("添加IP成功");
                }else {
                    log.info("IP 已添加");
                    return RetResponse.makeErrRsp("IP 已添加");
                }

            }
        }
    }

    /**
     * 判断IP是否为免登IP, 是： 返回免登IP 否： 返回 false
     * @param request
     * @return
     */
    @GetMapping("/getIp")
    @ResponseBody
    public String getIp(HttpServletRequest request){
        // 获取访问的 IP
        String ipAddress = IpUtil.getIpAddr(request);
        log.info("ipAddress :"+ipAddress);
        // 检查传入IP地址格式是否正确
        if(IpUtil.ipCheck(ipAddress)){
            // ip 是否为领导IP 是： 返回领导IP， 不是： 返回false
            QueryWrapper<FreeIp> freeIpQueryWrapper = new QueryWrapper<>();
            freeIpQueryWrapper.eq("ip",ipAddress);
            List<FreeIp> userIps = freeIpService.list(freeIpQueryWrapper);
            if(userIps.isEmpty()){
                log.info("Ip不为领导Ip");
                return "false";
            }else {
                log.info("领导IP: "+ipAddress);
                return ipAddress;
            }
        }else {
            log.info("Ip格式不正确");
            return "false";
        }
    }
}
