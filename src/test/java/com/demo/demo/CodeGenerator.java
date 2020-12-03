package com.demo.demo;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class CodeGenerator {
    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("牟欢");
        gc.setOpen(false);
        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();

        // 正式环境数据源配置
        dsc.setUrl("jdbc:mysql://192.168.138.37:3300/no_paper_meeting?serverTimezone=CTT&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("MHADMIN");
        dsc.setPassword("dz123456");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(scanner("模块名"));
        pc.setParent("com.demo.demo");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" +
                        tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
        //strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
        strategy.setSuperEntityColumns("id");
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}

//-----------------------------------------生成文件---------------------------------------------------------------------

// 修改57行数据库名称。
// sys
// department,form,form_title,meeting,meeting_file,meeting_menu,meeting_role,meeting_type,role,template,template_menu,title,user_info,user_role,user_group,user_group_job_number,free_ip


// 一 、生成文件目录
//       1. src/demo下生成sys文件夹，该文件夹下包括 controller、entity、mapper、service（service层接口）、service\impl(service层实现)
//       2. resources下生成 mapper文件夹，包含生成的所有mapper.xml

// 二、 启动 CodeGenerator的主函数， 输入内容依此为：                                                     sys
//       1. 统一的文件夹名  sys
//       2. 输入 数据库中表格的名称，可以输入多个，中间用逗号分隔。

// 三、 注意事项
//       1. 数据库表不要带有sys, 如果数据库中表格名为 user_info 会在[sys]文件夹下生成 userInfo


//---------------------------------------生成以后要注意的问题---------------------------------------------------------------------------

// 一、开启MP生效的配置
//      1. 在主配置文件中开启注解@MapperScan("com.demo.demo.sys.mapper")
// 二、修改生成的代码
//      -> entity
//          主键自增id修改为
//              @TableId(value="id",type= IdType.AUTO)
//              private Integer id;

//      -> 时间格式转换
//            @DateTimeFormat(pattern = "yyyy-MM-dd 'T' hh:mm:ss")
//            @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd 'T' hh:mm:ss")
//      -> Controller
//            1. @RestController ==> @Controller
//            2. @RequestMapping("") 里面的映射内容修改成自己的 eg: @RequestMapping("/sys/department") ==>@RequestMapping("/department")




