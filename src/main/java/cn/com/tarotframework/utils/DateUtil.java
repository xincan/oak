package cn.com.tarotframework.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.util.comparator.Comparators;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 日期处理类
 */
@Slf4j
public class DateUtil {


    private static String dateToWeek(String datetime) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        Date date;
        try {
            date = format.parse(datetime);
            cal.setTime(date);
        } catch (ParseException e) {
            log.info("DateWeekUtils-->dateToWeek() 获取周错误:{}", e.getMessage());
        }
        //一周的第几天
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static Map<String, List<String>> getToDayListGroup(String year) {

        List<String> day = new ArrayList<>();
        try {
            //如果没有传入需要生成哪年的日历，则会自动生成明年的。如果传入哪年的就生成哪年的日历
            if (StringUtils.isEmpty(year)) {
                year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR) + 1);
            }
            //开始时间
            Calendar startTime = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            startTime.setTime(simpleDateFormat.parse(year + "-01-01"));
            //结束时间
            Calendar endTime = Calendar.getInstance();
            endTime.setTime(simpleDateFormat.parse(year + "-12-31"));
            while (startTime.compareTo(endTime) <= 0) {
                String week = dateToWeek(simpleDateFormat.format(startTime.getTime()));
                if (!"星期六".equals(week) && !"星期日".equals(week)) {
                    //result.put(simpleDateFormat.format(startTime.getTime()).trim(),week.trim());
                    day.add(simpleDateFormat.format(startTime.getTime()).trim());
                }
                //时间加一天
                startTime.set(Calendar.DATE, startTime.get(Calendar.DATE) + 1);
            }
        } catch (Exception e) {
            log.info("DateWeekUtils-->getToDay() 获取时间错误:{}", e.getMessage());
        }
        Map<String, List<String>> date = new HashMap<>();
        String finalYear = year;
        Arrays.stream("01,02,03,04,05,06,07,08,09,10,11,12".split(",")).forEach( m -> {
            List<String> lists = day.stream().filter(d -> m.equals(d.substring(5, 7))).collect(Collectors.toList());
            date.put(finalYear + m, lists);
        });
        // 去除节假日
        Map<String, List<String>> workday = date;
        HolidayUtil.overHolidays(year).entrySet().stream().filter(hd -> date.get(hd.getKey()) != null).forEach(hd -> {
            List<String> list02 = hd.getValue();
            List<String> list01 = date.get(hd.getKey());
            List<String> result = list01.stream().filter(word->!list02.contains(word)).collect(Collectors.toList());
            workday.put(hd.getKey(), result);
        });
        // 增加补班工作日
        HolidayUtil.overWorkDays(year).entrySet().stream().filter(hd -> workday.get(hd.getKey()) != null).forEach(hd -> {
            List<String> list02 = hd.getValue();
            List<String> list01 = workday.get(hd.getKey());
            list01.addAll(0, list02);
            List<String> list03 = list01.stream().sorted(Comparator.comparing(String::new)).collect(Collectors.toList());
            workday.put(hd.getKey(), list03);
        });
        return workday;
    }

    public static LocalDate strToDay(String str) {
        if(str.length()==6) {
            return LocalDate.parse(str.substring(4,6), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static LocalDateTime strToDateTime(String str) {
        return LocalDateTime.parse(str + " 18:00:00", DateTimeFormatter.ofPattern("yyyy-M-dd HH:mm:ss"));
    }

    public static void main(String[] args) {

        Map<String, List<String>> lists = getToDayListGroup("2022");
//        lists.entrySet().forEach( b -> System.out.println(b));
//        System.out.println(lists.size());

//        getToDayListGroup("2021").entrySet().parallelStream().filter(a -> a.getKey().equals("1月")).forEach( b-> System.out.println(b.getValue()));

//        getMonthLastDays("2021").forEach(System.out::println);

//        System.out.println(Math.ceil(7.0 / 8.00));


    }

}
