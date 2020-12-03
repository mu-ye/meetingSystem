package com.demo.demo.core.unit;

import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @author 牟欢
 * @Classname MyBatisPlusUtil
 * @Description TODO
 * @Date 2020-06-08 10:32
 */
@Slf4j
public class MyBatisPlusUtil {

    public static void main(String[] args) {
        MyBatisPlusUtil.del("meeting_leader");
    }



    /**
     * 根据 数据库表名字 删除自动生成文件
     * @param baseName 数据库表名字
     */
    public static void del(String baseName){
        // 将数据库中的 下划线 格式转为 大驼峰
        baseName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, baseName);
        // 获取项目的根目录 eg: G:\a_workspace\javaWorkspace\no_paper_meeting
        String rootPath = System.getProperty("user.dir");
        // src 目录(根据项目获取路径)
        String srcPath = "\\src\\main\\java\\com\\demo\\demo\\sys\\";
        // resources (根据项目获取路径)
        String resPath = "\\src\\main\\resources\\mapper\\";

        // 删除文件

        File controllerFile = new File(rootPath+srcPath+"controller\\"+baseName+"Controller.java");
        if(controllerFile.delete()){
            log.info(controllerFile.getName() + "--删除成功");
        }else {
            log.info(controllerFile.getName() + "--删除失败");
        }

        File entityFile = new File(rootPath+srcPath+"entity\\"+baseName+".java");
        if(entityFile.delete()){
            log.info(entityFile.getName() + "--删除成功");
        }else {
            log.info(entityFile.getName() + "--删除失败");
        }

        File mapperFile = new File(rootPath+srcPath+"mapper\\"+baseName+"Mapper.java");
        if(mapperFile.delete()){
            log.info(mapperFile.getName() + "--删除成功");
        }else {
            log.info(mapperFile.getName() + "--删除失败");
        }

        File mapperXmlFile = new File(rootPath+resPath+baseName+"Mapper.xml");
        if(mapperXmlFile.delete()){
            log.info(mapperXmlFile.getName()+ "--删除成功");
        }else {
            log.info(mapperXmlFile.getName()+ "--删除失败");
        }

        File serviceFile = new File(rootPath+srcPath+"service\\I"+baseName+"Service.java");
        if(serviceFile.delete()){
            log.info(serviceFile.getName()+ "--删除成功");
        }else {
            log.info(serviceFile.getName()+ "--删除失败");
        }
        File implServiceFile = new File(rootPath+srcPath+"service\\impl\\"+baseName+"ServiceImpl.java");
        if(implServiceFile.delete()){
            log.info(implServiceFile.getName()+ "--删除成功");
        }else {
            log.info(implServiceFile.getName()+ "--删除失败");
        }
    }
}
