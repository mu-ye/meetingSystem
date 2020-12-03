package com.demo.demo.core.unit;

import com.demo.demo.sys.entity.SelectNode;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 牟欢
 * @Classname StrUtil
 * @Description TODO
 * @Date 2020-06-24 16:04
 */
@Slf4j
public class StrUtil {
    public static String  toSelect(String str,String splitOne,String splitTwo){
        List<SelectNode> selectNodeList = new ArrayList<>();
        String[] strArr = str.split(splitOne);
        for(int i=0;i<strArr.length;i++){
            String index = strArr[i];
            String[] item = index.split(splitTwo);
            if(item.length == 2){
                SelectNode selectNode = new SelectNode();
                selectNode.setTableTitle(item[0]);
                if(item[1].equals("1") || item[1].equals("0")){
                    selectNode.setTableText(item[1]);
                }else {
                    selectNode.setTableText("0");
                }
                selectNodeList.add(selectNode);
            }else {
                SelectNode selectNode = new SelectNode();
                selectNode.setTableTitle(index);
                selectNode.setTableText("0");
                selectNodeList.add(selectNode);
            }
        }
        Gson gson = new Gson();
        log.info( gson.toJson(selectNodeList));
        return  gson.toJson(selectNodeList);
    }


    public static List<String> getJobNumberList(String str,String splitOne,String splitTwo){
        List<String> jobNumberList = new ArrayList<>();
        String[] strArr = str.split(splitOne);
        for(int i=0;i<strArr.length;i++){
            String index = strArr[i];
            System.out.println(index);
            String[] item = index.split(splitTwo);

            if(item.length == 2){
                 jobNumberList.add(item[0]);
            }else {
                 log.info("转换异常");
            }
        }
        log.info(jobNumberList.toString());
        return jobNumberList;
    }

    public static void main(String[] args) {
        List<String> jobNumbers = StrUtil.getJobNumberList("117042:牟欢;117042:牟欢;",";",":");
        for (String jobNumber : jobNumbers){
            SendMessage.send(jobNumber,"下班了");
        }
    }




}
