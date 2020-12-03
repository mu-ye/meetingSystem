package com.demo.demo.core.unit;

import com.demo.demo.core.common.SystemCommon;
import com.demo.demo.sys.domain.Employee;
import com.demo.demo.sys.exception.EmployeeNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author 牟欢
 * @Classname UserInfoUtil
 * @Description TODO
 * @Date 2020-05-21 16:35
 */
@Slf4j
public class UserInfoUtil {

    /**
     * 根据职工工号从服务器获取职工信息
     *
     * @param userId 职工工号
     * @return 职工信息
     */
    public static Employee getEmployeeByIdFromServer(String userId) {
        long timeStamp = System.currentTimeMillis() / 1000;
        String signature = DigestUtils.sha1Hex(SystemCommon.APP_ID_DD + SystemCommon.APP_WD_DD + timeStamp).toUpperCase();
        String url = "http://192.168.138.122:8011/MicroApp/ws_microapp.asmx/GetUserDataFormGhV3?appid=" + SystemCommon.APP_ID_DD
                + "&timeStamp=" + timeStamp + "&gh=" + userId + "&signature=" + signature;
        String result = HttpUtils.getEncoded(url);
        log.info("服务器的职工信息（新接口）：{}", result);
        if (result == null) {
            log.error("服务器的职工信息（新接口）：结果为空");
            throw new EmployeeNotFoundException();
        }
        try {
            // {"errcode":"0","errmsg":"成功","data":{"gh":"","username":"","dept":"","line":"","gw":"","gwlx":""}}
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode resultNode = objectMapper.readValue(result, JsonNode.class);
            if (resultNode.get("errcode").asInt() == 0) {
                JsonNode data = resultNode.get("data");
                String jobNumber = data.get("gh").asText();
                String userName = data.get("username").asText();
                String department = data.get("dept").asText();
                return new Employee(jobNumber, userName, department);
            } else {
                log.error("服务器的职工信息（新接口）：查询失败 [tilInfo.java 返回null]");
                log.error("U");
                return  null;
                //throw new EmployeeNotFoundException();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("服务器的职工信息（新接口）：解析失败");
            throw new EmployeeNotFoundException();
        }
    }


    /**
     * 根据钉钉免登码从服务器获取职工信息
     *
     * @param code 钉钉免登码
     * @return 职工信息
     */
    public static Employee getEmployeeByCodeFromServer1(String code) {
        String url = "http://192.168.138.122:8023/WebUserinfo?mm=qazwsx&userid=000035&code=" + code;
        String result = HttpUtils.get(url);
        log.info("钉钉免登返回结果：{}", result);
        if (result == null) {
            log.error("钉钉免登返回结果：结果为空");
            throw new EmployeeNotFoundException();
        }
        try {
            // {"errcode":"0","userid":"","username":"","mobile":""}
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode resultNode = objectMapper.readValue(result, JsonNode.class);
            // 钉钉免登成功，获取职工工号
            if (resultNode.get("errcode").asInt() == 0) {
                String id = resultNode.get("userid").asText();
                return getEmployeeByIdFromServer(id);
            } else {
                log.error("钉钉免登返回结果：查询失败");
                throw new EmployeeNotFoundException();
            }
        } catch (JsonProcessingException e) {
            log.error("钉钉免登返回结果：解析失败");
            throw new EmployeeNotFoundException();
        }
    }

    public static String getEmployeeByCodeFromServer(String code) {
        long timeStamp = System.currentTimeMillis() / 1000;
        String signature = DigestUtils.sha1Hex(SystemCommon.APP_ID_DD + SystemCommon.APP_WD_DD + timeStamp).toUpperCase();
        String url = "http://192.168.138.122:8011/MicroApp/ws_microapp.asmx/GetUserDataV3?appid=" + SystemCommon.APP_ID_DD
                + "&timeStamp=" + timeStamp + "&code=" + code +"&signature=" + signature;
        String result = HttpUtils.getEncoded(url);
        log.info("code返回用户信息："+result);
        if (result == null) {
            log.error("钉钉免登返回结果：结果为空");
            throw new EmployeeNotFoundException();
        }
        try {
            // {"errcode":"0","userid":"","username":"","mobile":""}
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode resultNode = objectMapper.readValue(result, JsonNode.class);
            log.info("resultNode"+resultNode.toString());

            // 钉钉免登成功，获取职工工号
            if (resultNode.get("errcode").asInt() == 0) {
                return resultNode.get("data").get("gh").asText();
            } else {
                log.error("钉钉免登返回结果：查询失败");
                throw new EmployeeNotFoundException();
            }
        } catch (JsonProcessingException e) {
            log.error("钉钉免登返回结果：解析失败");
            throw new EmployeeNotFoundException();
        }
    }
}
