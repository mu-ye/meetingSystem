package com.demo.demo.core.unit;

import com.demo.demo.core.common.SystemCommon;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author 牟欢
 * @Classname SendMessage
 * @Description TODO
 * @Date 2020-06-05 16:02
 */
@Slf4j
public class SendMessage {
    /**
     * 发送钉钉通知
     *
     * @param id 发送人工号
     * @param content 发送钉钉内容
     * @return
     */
    public static String send(String id,String content){
        long timeStamp = System.currentTimeMillis() / 1000;
        String signature = DigestUtils.sha1Hex(SystemCommon.APP_ID_DD + SystemCommon.APP_WD_DD + timeStamp).toUpperCase();
        String url = "http://192.168.138.122:8011/MicroApp/ws_microapp.asmx/SendSms_ddV3?appid=" + SystemCommon.APP_ID_DD
                + "&timeStamp=" + timeStamp + "&gh=" + id + "&smsdata=" + content + "&signature=" + signature;
        String result = HttpUtils.getEncoded(url);
        log.info("钉钉通知：" + url + " -> " + result);
        return result;
    }
}
