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

@Slf4j
public class DateUtil {

    public static List<String> getToDayList(String year) {
        List<String> result = new ArrayList<>();
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
                    result.add(simpleDateFormat.format(startTime.getTime()).trim());
                }
                //时间加一天
                startTime.set(Calendar.DATE, startTime.get(Calendar.DATE) + 1);
            }
        } catch (Exception e) {
            log.info("DateWeekUtils-->getToDay() 获取时间错误:{}", e.getMessage());
        }
        return result;
    }

    public static Map<String, String> getToDay(String year) {
        Map<String, String> result = new LinkedHashMap<>();
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
                    result.put(simpleDateFormat.format(startTime.getTime()).trim(), week.trim());
                }
                //时间加一天
                startTime.set(Calendar.DATE, startTime.get(Calendar.DATE) + 1);
            }
        } catch (Exception e) {
            log.info("DateWeekUtils-->getToDay() 获取时间错误:{}", e.getMessage());
        }
        return result;
    }

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

    public static String date() {
        return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }

    public static List<String> getMonthLastDays(String year) {
        return Stream.of("1,2,3,4,5,6,7,8,9,10,11,12".split(",")).map(d ->
                LocalDate.of(Integer.parseInt(year), Integer.parseInt(d), 1).with(TemporalAdjusters.lastDayOfMonth()).toString()
        ).collect(Collectors.toList());
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

        Map<String, List<String>> month = new HashMap<>();
        String finalYear = year;
        Arrays.stream("01,02,03,04,05,06,07,08,09,10,11,12".split(",")).forEach(m -> {
            List<String> lists = day.stream().filter(d -> m.equals(d.substring(5, 7))).collect(Collectors.toList());
//            month.put(Integer.parseInt(m)+"月", lists);
            month.put(finalYear + m, lists);
        });

        return month;
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

//        List<String> lists = getToDayList("2021");
//        System.out.println(lists);
//        System.out.println(lists.size());

//        getToDayListGroup("2021").entrySet().parallelStream().filter(a -> a.getKey().equals("1月")).forEach( b-> System.out.println(b.getValue()));

//        getMonthLastDays("2021").forEach(System.out::println);

//        System.out.println(Math.ceil(7.0 / 8.00));

        System.out.println("202101".substring(4, 6));

    }

}
