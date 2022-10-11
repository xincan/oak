package cn.com.tarotframework.utils;

import cn.com.tarotframework.server.oak.dto.ExcelData;
import cn.com.tarotframework.server.oak.dto.ProjectHour;
import cn.com.tarotframework.server.oak.dto.ProjectHourDetail;
import cn.com.tarotframework.server.oak.dto.User;
import cn.com.tarotframework.server.oak.po.SysProject;
import cn.com.tarotframework.server.oak.po.SysProjectUser;
import cn.com.tarotframework.server.oak.po.SysUser;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class OakDataUtil {

    public static List<ExcelData> getExcelData(String url) {
        Map<String, List<ExcelData>> excel = EasyExcelUtil.readExcelByData(url, ExcelData.class);
        // 将excel表格数据转换成集合
        List<ExcelData> dataList = new ArrayList<>();
        excel.forEach((key, value) -> dataList.addAll(value));
        return dataList;
    }

    static int index = 0;
    /**
     * 获取所有项目，组装SysProject实体
     */
    public static List<SysProject> getProjects(List<ExcelData> excelDataLists, String year) {
        return new ArrayList<>(excelDataLists.stream()
                .collect(Collectors.groupingBy(ExcelData::getProjectName, HashMap::new, Collectors.collectingAndThen(Collectors.toList(), ed ->
                  SysProject.builder()
                        .projectName(ed.get(0).getProjectName())
                        .projectCode(StringUtils.isEmpty(ed.get(0).getProjectNum()) ? ConvertUtil.getProjectNum(ed.get(0).getProjectName()): ed.get(0).getProjectNum())
                        .projectManager(1).enable(1).projectStatus("a")
                        .startDate(LocalDate.parse(year + "-01-01", DateTimeFormatter.ofPattern("yyyy-M-dd"))).remark("无").createBy("admin")
                        .createTime(LocalDateTime.parse(year + "-01-01 12:12:12", DateTimeFormatter.ofPattern("yyyy-M-dd HH:mm:ss")))
                        .duration(ed.stream().mapToDouble(ExcelData::getHour).sum()).build()
                ))).values());
    }

    /**
     * 获取用户数据，组装SysUser，SysProjectUser实体
     */
    public static List<SysUser> getUsers(List<ExcelData> excelDataLists, String year) {

        LocalDateTime time = LocalDateTime.parse(year + "-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-M-dd HH:mm:ss"));
        String password = SecurityUtils.encryptPassword("123456");

        // 提取所有用户，根据用户去重
        List<SysUser> users = excelDataLists
                .parallelStream()
                .filter(DistinctUtil.distinctByKey(ExcelData::getName)).collect(Collectors.toList())
                .parallelStream()
                .map(su -> {
                    String userName = PinyinUtil.getPinyin(su.getName());
                    return SysUser.builder()
                            .userName(userName).nickName(su.getName())
                            .userType("00").email(userName + "@hatech.com.cn").sex("2")
                            .avatar("/profile/avatar/2022/09/23/89f54f83-35be-4952-becb-fca07944865e.jpeg")
                            .password(password)
                            .status("0").delFlag("0").createBy("admin").createTime(time).updateBy("admin").updateTime(time).remark("管理员")
                            .deptId(100L).departmentName(su.getTwoDepart()).sysUserRoleId(108L).sysUserPostId(13L).build();
                }).collect(Collectors.toList());

        // 根据用户分组，提取用户对应的项目
        Map<String, List<ExcelData>> up = new HashMap<>();

        Map<String, List<ExcelData>> projects = excelDataLists.stream().collect(Collectors.groupingBy(ExcelData::getName));
        // 遍历去重，提取，该用户对应的所有项目
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
        return users;
    }

    /**
     * 清洗、补位，每人，每年，所在项目上花的工时，进行插入
     */
    public static List<User> getProjectHours(List<ExcelData> excelDataLists, String year) {


        // 根据用户去重，获取所有人员
        List<User> userLists = excelDataLists.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ExcelData::getName))), ArrayList::new))
                .stream()
                .map(ed -> User.builder().month(ed.getMonth()).userName(ed.getName()).build())
                .collect(Collectors.toList());

        //
        userLists.forEach( user -> {

            // 当天详情数据
            List<ProjectHourDetail> projectHourDetailListTotal = new ArrayList<>();
            excelDataLists.stream().filter( excelData -> user.getUserName().equals(excelData.getName())).forEach( excelData -> {
                projectHourDetailListTotal.add(ProjectHourDetail.builder()
                    .userName(user.getUserName())
                    .projectName(excelData.getProjectName())
                    .fillDate(DateUtil.strToDay(excelData.getMonth()))
                    .useHour(excelData.getHour())
                    .createTime(DateUtil.strToDateTime(excelData.getMonth()))
                    .projectStatus("a").everyDay(1)
                    .daily(excelData.getProjectName())
                    .build());
            });

            // 当天总数居 对应 mh_user_hour
            List<ProjectHour> projectHourList = projectHourDetailListTotal.stream().collect(Collectors.groupingBy(
                    ph -> ph.getUserName() + "@" + ph.getFillDate(), Collectors.summarizingDouble(ProjectHourDetail::getUseHour)
            )).entrySet().stream().map(value ->
                    projectHourDetailListTotal.stream().filter(el -> el.getFillDate().equals(DateUtil.strToDay(value.getKey().split("@")[1]))).map(el ->
                        ProjectHour.builder()
                            .userName(el.getUserName())
                            .projectName(el.getProjectName())
                            .totalHour(BigDecimal.valueOf(value.getValue().getSum()))
                            .createTime(DateUtil.strToDateTime(el.getFillDate().toString()))
                            .fillDate(DateUtil.strToDay(el.getFillDate().toString()))
                            .build()
                ).collect(Collectors.toList()).get(0)
            ).collect(Collectors.toList());

            // 当天数据详情 对应mh_user_detail
            projectHourList.forEach( ph -> {
                List<ProjectHourDetail> lists = new ArrayList<>();
                projectHourDetailListTotal.stream()
                        .filter(phd -> ph.getFillDate().equals(phd.getFillDate()))
                        .forEach(lists::add);
                ph.setProjectHourDetails(lists);
            });
            user.setProjectHours(projectHourList);
        });
        return userLists;
    }

    public static void main(String[] args) {

        List<ExcelData> lists = getExcelData("D:\\hatech-hour\\2022-08-测试数据.xlsx");
        lists.forEach(System.out::println);
        System.out.println(lists.size());

//         getProjects(lists, "2022").forEach(System.out::println);
//        System.out.println(getProjects(lists, "2022").size());

//        getUsers(lists, "2022").forEach(System.out::println);
//        System.out.println(getUsers(lists, "2022").size());

//        getProjectHours(lists, "2022").forEach(System.out::println);
//        System.out.println(getProjectHours(lists, "2022").size());


    }

}
