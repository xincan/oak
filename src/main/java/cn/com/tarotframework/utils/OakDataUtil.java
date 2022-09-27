package cn.com.tarotframework.utils;

import cn.com.tarotframework.server.oak.dto.ExcelData;
import cn.com.tarotframework.server.oak.dto.ProjectHour;
import cn.com.tarotframework.server.oak.dto.ProjectHourDetail;
import cn.com.tarotframework.server.oak.dto.User;
import cn.com.tarotframework.server.oak.po.SysProject;
import cn.com.tarotframework.server.oak.po.SysProjectUser;
import cn.com.tarotframework.server.oak.po.SysUser;
import com.alibaba.fastjson2.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


public class OakDataUtil {

    public static final String EXCEL_URL = "D:\\hatech-hour\\2021-汇总.xlsx";

    public static List<ExcelData> getExcelData() {
        Map<String, List<ExcelData>> excel = EasyExcelUtil.readExcelByData(EXCEL_URL, ExcelData.class);
        // 将excel表格数据转换成集合
        List<ExcelData> dataList = new ArrayList<>();
        excel.forEach((key, value) ->
                value.forEach(excelData -> {
                    JSONObject project = ConvertUtil.getProjectName(excelData.getNum());
                    excelData.setProjectNum(project.getString(ConvertUtil.PROJECT_NUM));
                    excelData.setProjectName(project.getString(ConvertUtil.PROJECT_NAME));
                    dataList.add(excelData);
                })
        );
        return dataList;
    }

    /**
     * 获取所有项目，组装SysProject实体
     */
    public static List<SysProject> getProjects(String year) {
        return new ArrayList<>(getExcelData()
                .parallelStream()
                .collect(Collectors.groupingBy(ExcelData::getProjectName, HashMap::new, Collectors.collectingAndThen(Collectors.toList(), ed ->
                        SysProject.builder()
                                .projectName(ed.get(0).getProjectName()).projectCode(ed.get(0).getProjectNum())
                                .projectManager(1).enable(1).projectStatus("a")
                                .startDate(LocalDate.parse(year + "-01-01", DateTimeFormatter.ofPattern("yyyy-M-dd"))).remark("无").createBy("admin")
                                .createTime(LocalDateTime.parse(year + "-01-01 12:12:12", DateTimeFormatter.ofPattern("yyyy-M-dd HH:mm:ss")))
                                .duration(ed.stream().mapToDouble(ExcelData::getHour).sum()).build()
                ))).values());
    }

    /**
     * 获取用户数据，组装SysUser，SysProjectUser实体
     */
    public static List<SysUser> getUsers(String year) {
        LocalDateTime time = LocalDateTime.parse(year + "-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-M-dd HH:mm:ss"));
        List<ExcelData> excelDataList = getExcelData();
        List<SysUser> users = excelDataList
                .stream()
                .filter(DistinctUtil.distinctByKey(ExcelData::getName)).collect(Collectors.toList())
                .stream()
                .map(su -> {
                    String userName = PinyinUtil.getPinyin(su.getName());
                    return SysUser.builder()
                            .userName(userName).nickName(su.getName())
                            .userType("00").email(userName + "@hatech.com.cn").sex("2")
                            .avatar("/profile/avatar/2022/09/23/89f54f83-35be-4952-becb-fca07944865e.jpeg")
                            .password(SecurityUtils.encryptPassword("123456"))
                            .status("0").delFlag("0").createBy("admin").createTime(time).updateBy("admin").updateTime(time).remark("管理员")
                            .deptId(100L).departmentName(su.getTwoDepart()).sysUserRoleId(108L).sysUserPostId(13L).build();
                }).collect(Collectors.toList());

        Map<String, List<ExcelData>> projects = excelDataList.stream().filter(a -> a.getName().equals("王涛")).collect(Collectors.groupingBy(ExcelData::getName));

        Map<String, List<ExcelData>> up = new HashMap<>();
        projects.forEach((key, value) -> {
            List<ExcelData> list = value.stream()
                    .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ExcelData::getProjectName))), ArrayList::new));
            up.put(key, list);
        });

        users.forEach(user -> {
            List<SysProjectUser> lists = new ArrayList<>();
            up.entrySet().stream().filter( p -> user.getNickName().equals(p.getKey())).forEach( p ->
                p.getValue().forEach( v -> {
                    SysProjectUser projectUser = SysProjectUser.builder()
                        .projectName(v.getProjectName()).userId(user.getUserId()).status(1).everyday(1).createUser(1L)
                        .createTime(time).build();
                    lists.add(projectUser);
                })
            );
            user.setProjectUserList(lists);
        });


//        users.forEach(a -> a.getProjectUserList().forEach(System.out::println));
//        System.out.println(users.size());
        return users;
    }

    /**
     * 清洗、补位，每人，每年，所在项目上花的工时，进行插入
     */
    public static List<User> getProjectHours(String year) {

        // 按照月份分组，获取，每月工作日时间 总共261 天
        Map<String, List<String>> monthDay = DateUtil.getToDayListGroup(year);

        // 获取文件数据转换成单向对象集合
        List<ExcelData> dataList = getExcelData();

        // 根据用户去重，获取所有人员
        List<User> userLists = dataList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ExcelData::getName))), ArrayList::new))
                .stream()
                .map(ed -> User.builder().month(ed.getMonth()).userName(ed.getName()).build())
                .collect(Collectors.toList());

        // 根据，用户，项目，月，分组，求每个项目总工时（每月每人会有多个项目），然后转换成单项 list 集合
        List<ProjectHourDetail> projectHourDetails = dataList.stream().collect(Collectors.groupingBy(
                b -> b.getName() + "@" + b.getProjectName() + "@" + b.getMonth(), Collectors.summarizingDouble(ExcelData::getHour)
        )).entrySet().stream().map(value ->
                ProjectHourDetail.builder()
                        .userName(value.getKey().split("@")[0])
                        .projectName(value.getKey().split("@")[1]).month(value.getKey().split("@")[2]).projectStatus("a")
                        .everyDay(1).count((int) value.getValue().getCount()).sum(value.getValue().getSum())
                        .build()
        ).collect(Collectors.toList());

//        projectHourDetails.stream().filter(p-> p.getUserName().equals("王涛")).forEach(System.out::println);


        // 获取用户下，按照项目分组获取，每个项目总工时
        userLists.forEach(user -> {
            // 获取用户下，按照项目分组获取，每个项目总工时
            List<ProjectHour> projectHours = projectHourDetails.stream()
                    .filter(pch -> user.getUserName().equals(pch.getUserName()))
                    .map(ph -> ProjectHour.builder()
                            .createTime(LocalDateTime.parse(year + "-01-01 18:00:00", DateTimeFormatter.ofPattern("yyyy-M-dd HH:mm:ss")))
                            .totalHour(ph.getSum())
                            .projectName(ph.getProjectName())
                            .fillDate(DateUtil.strToDay(year + "-01-01"))
                            .build()
                    )
                    .collect(Collectors.toList())
                    .stream().collect(Collectors.groupingBy(
                            ProjectHour::getProjectName, Collectors.summarizingDouble(ProjectHour::getTotalHour)
                    )).entrySet().stream().map(v -> ProjectHour.builder()
                            .fillDate(DateUtil.strToDay("2021-01-01")).createTime(LocalDateTime.parse("2021-01-01 18:00:00", DateTimeFormatter.ofPattern("yyyy-M-dd HH:mm:ss")))
                            .userName(user.getUserName()).totalHour(v.getValue().getSum()).projectName(v.getKey()).build()
                    ).collect(Collectors.toList());
            user.setProjectHours(projectHours);
        });

//        userLists.parallelStream().filter(p-> p.getUserName().equals("王涛")).forEach(a -> a.getProjectHours().forEach(System.out::println));

        // 拆分项目工时，按天计算
        userLists.forEach(
            user -> user.getProjectHours().stream()
                .filter(  ph -> user.getUserName().equals("王涛") )
                .filter(  ph -> user.getUserName().equals(ph.getUserName()) )
                .forEach(ph -> {
                    List<ProjectHourDetail> hours = new ArrayList<>();
                    projectHourDetails.stream().
                        filter(phd -> user.getUserName().equals(ph.getUserName()) && ph.getUserName().equals(phd.getUserName()) && ph.getProjectName().equals(phd.getProjectName()))
                        .collect(Collectors.toList()).forEach( w -> {
                            Double totalHour = w.getSum();
                            // 如果总工时小于8小时，将数据信息组装到当前月的最后一天
                            // 否则循环当前月所有工作日，根据工作日倒序插入相应工时
                            if (totalHour <= 8.00) {
                                ProjectHourDetail detail = ProjectHourDetail.builder()
                                    .userName(w.getUserName())
                                    .month(w.getMonth()).projectName(w.getProjectName()).userName(w.getUserName()).projectStatus("a").everyDay(1)
                                    .fillDate(DateUtil.strToDay(monthDay.get(w.getMonth()).get(monthDay.get(w.getMonth()).size() - 1)))
                                    .createTime(DateUtil.strToDateTime(monthDay.get(w.getMonth()).get(monthDay.get(w.getMonth()).size() - 1)))
                                    .daily(ph.getProjectName()).useHour(totalHour).sum(w.getSum()).build();
                                hours.add(detail);
                            } else {
                                int day = (int) Math.ceil(totalHour / 8.00);
                                int workDay = monthDay.get(w.getMonth()).size();

                                for (int i = workDay - 1; i >= 0; i--) {
                                    ProjectHourDetail detail = ProjectHourDetail.builder()
                                        .userName(w.getUserName())
                                        .month(w.getMonth()).projectName(w.getProjectName()).userName(w.getUserName())
                                        .projectStatus("a").everyDay(1).fillDate(DateUtil.strToDay(monthDay.get(w.getMonth()).get(i)))
                                        .createTime(DateUtil.strToDateTime(monthDay.get(w.getMonth()).get(i)))
                                        .daily(ph.getProjectName()).useHour(8.00).sum(w.getSum()).build();
                                    hours.add(detail);
                                }
                            }
                        });
                    ph.setProjectHourDetails(hours);

                })

        );


        // 获取王涛，所有项目，全年工作日，日报填写情况
//        userLists.stream().filter( a -> a.getUserName().equals("王涛")).forEach(a -> a.getProjectHours().forEach(b -> b.getProjectHourDetails().forEach(System.out::println)));

//        userLists.parallelStream().filter(a -> a.getUserName().equals("王涛")).forEach( a -> a.getProjectHours().forEach(System.out::println));


        return userLists;
    }

    public static void main(String[] args) {

//            getExcelData();

//         getProjects("2021").forEach(System.out::println);
//        System.out.println(getProjects("2021").size());

//        getUsers("2021");

        getProjectHours("2021");


    }

}
