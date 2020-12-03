package com.demo.demo.sys.controller;

import com.demo.demo.core.common.SystemCommon;
import com.demo.demo.core.ret.RetResponse;
import com.demo.demo.core.ret.RetResult;
import com.demo.demo.core.unit.SendMessage;
import com.demo.demo.core.unit.StrUtil;
import com.demo.demo.core.unit.UserInfoUtil;
import com.demo.demo.sys.entity.requestbody.DingDingEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 牟欢
 * @Classname DingDingController
 * @Description TODO
 * @Date 2020-07-07 17:35
 */
@Controller
@Slf4j
@RequestMapping("/ding")
public class DingDingController {

    /**
     * 如果是钉钉环境，返回钉钉的验证信息
     * @return
     */
    @ResponseBody
    @GetMapping("/corporation")
    public RetResult<String> getDingDingCorporation() {
        log.info("判断为钉钉环境，返回进行授权");
        return RetResponse.makeOKRsp(SystemCommon.CORP_ID);
    }

    /**
     * 返回用户工号
     *
     * @param code 钉钉的授权码
     * @return
     */
    @ResponseBody
    @PostMapping("/getJobNumberByCode")
    public RetResult<String> getDingDingCorporation(String code) {
        String jobNumber = UserInfoUtil.getEmployeeByCodeFromServer(code);
        log.info("钉钉登陆获取的员工工号为"+jobNumber);
        return RetResponse.makeOKRsp(jobNumber);
    }

    /**
     * 发送钉钉消息
     *
     * @param userLists
     * @param text
     * @return
     */
    @ResponseBody
    @PostMapping("/sendMessages")
    public RetResult<String> sendMessages(String userLists,String text) {
        log.info(userLists);
        log.info(text);
        List<String> jobNumbers = StrUtil.getJobNumberList(userLists,";",":");
        for (String jobNumber : jobNumbers){
            SendMessage.send(jobNumber,text);
            log.info(jobNumber+"消息发送成功");
        }
       return  RetResponse.makeOKRsp("消息发送成功");
    }


    /**
     * 发送钉钉消息
     *
     * @param entity
     * @return
     */
    @ResponseBody
    @PostMapping("/sendMessages1")
    public RetResult<String> sendMessages1(@RequestBody DingDingEntity entity) {

        for(String jobNumber :entity.getJobNumberList()){
            SendMessage.send(jobNumber,entity.getText());
            log.info(jobNumber+"消息发送成功");
        }
        return  RetResponse.makeOKRsp("消息发送成功");
    }
}
