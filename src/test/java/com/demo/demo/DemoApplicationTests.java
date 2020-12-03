package com.demo.demo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.demo.core.common.SystemCommon;
import com.demo.demo.core.ret.RetTitle;
import com.demo.demo.core.unit.*;
import com.demo.demo.sys.domain.Employee;
import com.demo.demo.sys.entity.MeetingMenu;
import com.demo.demo.sys.entity.SelectNode;
import com.demo.demo.sys.entity.TemplateMenu;
import com.demo.demo.sys.entity.UserInfo;
import com.demo.demo.sys.service.impl.*;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.UnsupportedEncodingException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
@RunWith(SpringRunner.class)
//由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。
@WebAppConfiguration
@Slf4j
class DemoApplicationTests {
   @Autowired
   UserInfoServiceImpl userInfoService;
   @Autowired
    UserRoleServiceImpl userRoleService;
   @Autowired
   DepartmentServiceImpl departmentService;
   @Autowired
    TemplateMenuServiceImpl templateMenuService;
   @Autowired
    MeetingMenuServiceImpl meetingMenuService;
   @Autowired
   TitleServiceImpl titleService;


    @Test
    public void randomTest(){
        log.info(RandomString.getRandomString(5));
    }

    @Test
    public void select1(){
        String str = "测试1@@1##测试2@@0##";
        String splitOne="##";
        String splitTwo="@@";
        log.info("被分割的str"+str);
        List<SelectNode> selectNodeList = new ArrayList<>();
        String[] strArr = str.split(splitOne);
        for(int i=0;i<strArr.length;i++){
            log.info("strArr[i]"+strArr[i]);
            String index = strArr[i];
            String[] item = index.split(splitTwo);
            if(item.length == 2){
                SelectNode selectNode = new SelectNode();
                selectNode.setTableTitle(item[0]);
                String item1 = item[1];
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
    }



    /**
     *  删除 mybatis 自动生成的代码
     */
    @Test
    public void delFilesMybatisPlusBySQLName(){
        MyBatisPlusUtil.del("meeting_leader");
    }
    @Test
    public void a(){
        List<RetTitle> titleList = titleService.iGetFormTitlesByFormIdGroupByAutoIndex(1);
        for (RetTitle retTitle : titleList){
            log.info(retTitle.getWidth().toString());
        }

    }




    /**
     *  发送钉钉消息
     */
    @Test
    public void send(){
       SendMessage.send("117042","下班了");
    }






    @Test
    public void ss() {
       System.out.println("招标采购会议===2020-08-10T10:17:56.416".split("===")[0]);
   }
    @Test
    public void delFilesMybatisPlus(){

        // 获取当前日期

//        LocalTime initLocalTime = LocalTime.of(0,0,0);
//        LocalDateTime nowDate = LocalDateTime.of(LocalDate.now(),initLocalTime);
//        System.out.println(nowDate);

        // 正则表达式字符串匹配

//        Pattern pattern = Pattern.compile("117042");
//        Matcher matcher = pattern.matcher("117042(木换）,117044(欢欢)");
//        Boolean show = matcher.matches();
//        System.out.println(show);

        // 判断字符串中是否含有子串

//        String str="117042(木换）,117044(欢欢)";
//        Boolean status = str.contains("117042");
//        System.out.println(status.toString());

        // 去除空格
        String str1 = "(117042)牟欢";
        str1 = str1.replace(" ","");
        System.out.println(str1);
        // 去除字符串中所有中文
        Pattern pat = Pattern.compile("[\\u4e00-\\u9fa5]");
        Matcher mat = pat.matcher(str1);
               str1 = mat.replaceAll("");
        str1 = "[Y]"+str1.replace("-->","[N]")+"[Y]";
        System.out.println(str1);




    }


    @Test
    public void LocalDateTimeTest(){
        //从Java 8 开始 提供了新的时间API
        /**
         *  按照标准的ISO-8601格式
         *
         *  日期 ：  yyyy-MM-dd
         *  时间：  HH:mm:ss
         *  带毫秒时间： HH:mm:ss:SSS
         *  日期时间： yyyy-MM-dd'T'HH:mm:ss
         *  带毫秒的日期时间： yyyy-MM-dd'T'HH:mm:ss:SSS
         *
         */
            // LocalDateTime , LocalDate, LocalTime

        //  1. 通过 .now() 获取当前 日期
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        System.out.println(localDateTime);
        System.out.println(localDate);
        System.out.println(localTime);

        // 2. toLocalDate()  toLocalTime()
        // 这种方法可以保证  LocalDateTime的秒和localTime一致
        localDate = localDateTime.toLocalDate();
        localTime = localDateTime.toLocalTime();
        System.out.println(localDate);
        System.out.println(localTime);

        //  3. 通过 of() 方法来创建指定日期和时间
        localDateTime = LocalDateTime.of(2020,6,8,14,35,55);
        localDate = LocalDate.of(2020,6,23);
        localTime = LocalTime.of(14,46,33);
        System.out.println(localDateTime);
        System.out.println(localDate);
        System.out.println(localTime);

        // 4. parse() 从string 解析成 时间

        localDateTime = localDateTime.parse("2222-12-12T13:24:55");
        localDate = LocalDate.parse("1111-11-11");
        localTime = LocalTime.parse("20:33:33");
        System.out.println(localDateTime);
        System.out.println(localDate);
        System.out.println(localTime);

        // DateTimeFormatter  格式转换

        // 将ISO-8601格式转换为自定义格式
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        System.out.println(dateTimeFormatter.format(LocalDateTime.now()));
        // 从其他格式 转换为 ISO-8601 格式
        localDateTime = LocalDateTime.parse("2020/06/08 15:25:50",dateTimeFormatter);
        System.out.println(localDateTime);

        // 对日期进行简单的加减操作 plusXXXs() minusXXXs()
        localDateTime = localDateTime.plusMonths(8);
        System.out.println("加上8个月后日期："+localDateTime);
        localDateTime = localDateTime.minusDays(37);
        System.out.println("减去37天后日期："+localDateTime);

        // withXXX() 调整 年月日时分秒
        System.out.println(localDateTime.withYear(2088));

        // 复杂操作   TemporalAdjusters ： 日期调节器

        // 本月第一天0:00时刻:
        LocalDateTime firstDay = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        System.out.println(firstDay);

        // 本月最后1天:
        LocalDate lastDay = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        System.out.println(lastDay);

        // 下月第1天:
        LocalDate nextMonthFirstDay = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
        System.out.println(nextMonthFirstDay);

        // 本月第1个周一:
        LocalDate firstWeekday = LocalDate.now().with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        System.out.println(firstWeekday);

        // isBefore（）  isAfter()
        LocalDateTime ldt = LocalDateTime.of(2020,6,8,17,2,2);
        System.out.println(LocalDateTime.now().isBefore(ldt));
        System.out.println(LocalDateTime.now().isAfter(ldt));

        // Duration Period
        LocalDateTime start = LocalDateTime.of(2019, 11, 19, 8, 15, 0);
        LocalDateTime end = LocalDateTime.of(2020, 1, 9, 19, 25, 30);
        Duration d = Duration.between(start, end);
        System.out.println(d); // PT1235H10M30S
        Period p = LocalDate.of(2019, 11, 19).until(LocalDate.of(2020, 1, 9));
        System.out.println(p); // P1M21D


    }





    @Test
    public void test11(){
        QueryWrapper<MeetingMenu> meetingMenuQueryWrapper = new QueryWrapper<>();
        meetingMenuQueryWrapper.eq("meeting_id",3);
        log.info(meetingMenuService.list(meetingMenuQueryWrapper).toString());
    }
    @Test
    public void test12(){
        QueryWrapper<TemplateMenu> templateMenuQueryWrapper = new QueryWrapper<>();
        templateMenuQueryWrapper.eq("template_id", 4)
                .eq("parent_id", 0);
        TemplateMenu templateMenu1 = templateMenuService.getOne(templateMenuQueryWrapper);
        log.info(templateMenu1.toString());
    }



    @Test
    public void java(){
        // 获得项目根路径的绝对路径
        log.info(System.getProperty("user.dir") );
        // 获得类路径和包路径
        log.info(System.getProperty("java.class.path"));
    }



    @Test
    public void msg(){
        String id = "117042";
        String content = "下班还去学习吗";

        long timeStamp = System.currentTimeMillis() / 1000;
        String signature = DigestUtils.sha1Hex(SystemCommon.APP_ID_DD + SystemCommon.APP_WD_DD + timeStamp).toUpperCase();
        String url = "http://192.168.138.122:8011/MicroApp/ws_microapp.asmx/SendSms_ddV3?appid=" + SystemCommon.APP_ID_DD
                + "&timeStamp=" + timeStamp + "&gh=" + id + "&smsdata=" + content + "&signature=" + signature;
        String result = HttpUtils.getEncoded(url);
        log.info("钉钉通知：" + url + " -> " + result);
    }



    // 通过工号查询一个
   @Test
   public void testUserInfoByJobNumber(){
      /* String jobNumber = "112818";
       //String result = HttpClientGetUserInfo.getUserInfo(jobNumber);
       String result = UserInfoUtil.get(SystemCommon.BASE_USER_INFO_URL+jobNumber);
       if(result != null){
           log.info("用户信息 : " + result);
           JSONObject jsonUserInfo = JSONObject.fromObject(result);
           UserInfo userInfo = new UserInfo();
           userInfo.setAlive(1);
           userInfo.setUserName(jsonUserInfo.get("name").toString());
           userInfo.setJobNumber(jsonUserInfo.get("id").toString());
           userInfo.setPhone(jsonUserInfo.get("phone").toString());
           String departmentName = jsonUserInfo.get("department").toString();
           log.info(departmentName);
           QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
           queryWrapper.eq("department_name",departmentName);
           Department department = departmentService.getOne(queryWrapper);
           if(department != null){
               userInfo.setDepId(department.getId());
           }else {
               userInfo.setDepId(-1);
           }
           // 判断用户是否存在
           QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
           userInfoQueryWrapper.eq("job_number",jobNumber);
           if(userInfoService.getOne(userInfoQueryWrapper) == null){
               userInfoService.save(userInfo);
               log.info("用户"+jobNumber+"存入数据库成功");
           }else {
               log.info("用户已在数据库中");
           }
       }*/
   }




    // 添加全体员工的组织架构
    @Test
    public void testSplit1() throws UnsupportedEncodingException {
       /* for(int i = 101234 ; i <= 101300; i++){
            String jobNumber = String.valueOf(i);
            //String result = HttpClientGetUserInfo.getUserInfo(jobNumber);
            String result = UserInfoUtil.get(SystemCommon.BASE_USER_INFO_URL+jobNumber);
            if(result != null){
                log.info("用户信息 : " + result);
                JSONObject jsonUserInfo = JSONObject.fromObject(result);
                UserInfo userInfo = new UserInfo();
                userInfo.setAlive(1);
                userInfo.setUserName(jsonUserInfo.get("name").toString());
                userInfo.setJobNumber(jsonUserInfo.get("id").toString());
                userInfo.setPhone(jsonUserInfo.get("phone").toString());
                String departmentName = jsonUserInfo.get("department").toString();

                // 和本地数据库部门表中名称保持一致
                if(departmentName == "总经理室"){
                    departmentName ="总经理办公室";
                }

                log.info(departmentName);
                QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("department_name",departmentName);
                Department department = departmentService.getOne(queryWrapper);
                if(department != null){
                    userInfo.setDepId(department.getId());
                }else {
                    userInfo.setDepId(-1);
                }
                // 判断用户是否存在
                QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
                userInfoQueryWrapper.eq("job_number",jobNumber);
                if(userInfoService.getOne(userInfoQueryWrapper) == null){
                    userInfoService.save(userInfo);
                    log.info("用户"+i+"存入数据库成功");
                }else {
                    log.info("用户已在数据库中");
                }
            }
        }*/
    }

    @Test
    public void testChangeDepartment() throws UnsupportedEncodingException {
       QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
       queryWrapper.eq("dep_id",-1);
       List<UserInfo> userInfoList = userInfoService.list(queryWrapper);
       log.info(userInfoList.get(0).getJobNumber());
      /* for (UserInfo  userInfo : userInfoList){
           String result = UserInfoUtil.get(SystemCommon.BASE_USER_INFO_URL+userInfo.getJobNumber());
           JSONObject jsonUserInfo = JSONObject.fromObject(result);
           String departmentName = jsonUserInfo.get("department").toString();
           log.info("转换前部门 :"+departmentName);
           if(departmentName.equals("总经理室")){
               departmentName = "总经理办公室";
           }
           log.info("转换后部门 :"+departmentName);
           QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper<>();
           departmentQueryWrapper.eq("department_name",departmentName);
           Department department = departmentService.getOne(departmentQueryWrapper);
           if(department != null){
               userInfo.setDepId(department.getId());
           }else {
               userInfo.setDepId(-1);
           }
           log.info("部门ID:"+userInfo.getDepId());
           userInfoService.saveOrUpdate(userInfo);
           log.info(userInfo.getJobNumber() +"数据库更新完成");

       }*/
    }




    @Test
    public void testSplit(){
        String src = "117042@000000";
        if(src == null){
            log.info("用户列表不能为空");
        }else {
            String [] arr = src.split("@");
            List<UserInfo> userInfoList = new ArrayList<>();
            for(int i = 0; i< arr.length;i++){
                // 根据工号获取用户信息
                QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
                userInfoQueryWrapper.eq("job_number",arr[i]);
                userInfoList.add(userInfoService.getOne(userInfoQueryWrapper));
            }
        }


        log.info("------------------复选框选中人的工号全部添加进去------------");
        StringBuilder builder = new StringBuilder();
        builder.append("117042");
        builder.append("@");
        builder.append("000000");
        String res = builder.toString();
        log.info("res:"+res);
    }



   @Test
   public void getSignature() throws UnsupportedEncodingException {
       Signature signature = new Signature();
       Map<String,String> myMap=new HashMap<>();
       String systemSignature=signature.getSignature(myMap, SystemCommon.APP_ID,SystemCommon.APP_SECRET);
       log.info("result APP_ID： " + SystemCommon.APP_ID);
       log.info("result APP_SECRET：  "+systemSignature);
   }

    @Test
    public void getSignatureOther() throws UnsupportedEncodingException {
        String appid="47a6281efd764a695320732b88eec4c3";
        String appsignatrue ="2ab499fb6f99483a6a8d915a9db031e8";
        Signature signature = new Signature();
        Map<String,String> myMap=new HashMap<>();
        String systemSignature=signature.getSignature(myMap, appid,appsignatrue);
        log.info("result APP_ID： " + appid);
        log.info("result APP_SECRET：  "+systemSignature);
    }


    @Test
    public void test1() throws UnsupportedEncodingException {
         Employee employee = UserInfoUtil.getEmployeeByIdFromServer("117042");
         log.info(employee.toString());
    }


}


