package com.demo.demo.core.unit;

/**
 *
 * @author 牟欢
 * @Classname CheckChangeUtil
 * @Description TODO
 * @Date 2020-09-03 9:22
 */
public class CheckChangeUtil {

    /**
     * 对于返回到前台的审批流程进行转换  N->待审批  Y->通过  （）中工号改成 对应姓名
     *
     * @param str  数据库中的审批流程
     * @return
     */
    public static String getApprovalProcess(String str){
        str = str.substring(3);
        str = str.replace("N","待审批");
        str = str.replace("Y","通过");
        str = str.replace("(","--> (");
        str = str.replace(")",")");
        str = str.replace("[","【 ");
        str = str.replace("]"," 】");
        str = str.substring(4);
        return str;
    }

    public static void main(String[] args) {
        String str = "[Y](117033)[Y](104508)[Y](101272)[Y]";
        System.out.println(getApprovalProcess(str));
    }
}
