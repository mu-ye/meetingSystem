package com.demo.demo.sys.controller;

import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 牟欢
 * @Classname CasController
 * @Description TODO
 * @Date 2020-07-09 9:18
 */
@Controller
@RequestMapping("/cas")
@Slf4j
public class CasController {
    @RequestMapping("/login")
    public String login(HttpServletRequest request, Model model){
        // 截取cas 登陆成功后的 用户工号
        AttributePrincipal principal = (AttributePrincipal)request.getUserPrincipal();
        String jobNumber = principal.getName();
        log.info("用户通过cas单点登陆，工号 ："+jobNumber);
        model.addAttribute("jobNumber",jobNumber);
        return "CasLogin";
    }
}
