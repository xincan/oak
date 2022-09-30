package cn.com.tarotframework.utils;

import cn.com.tarotframework.server.oak.dto.Holiday;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HolidayUtil {

    private static Map<String, Holiday> getHolidays(int year) {
        String url = "http://timor.tech/api/holiday/year/" + year;
        OkHttpClient client = new OkHttpClient();
        Response response;
        String rsa = null;
        Request request = new Request.Builder().url(url).get()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            response = client.newCall(request).execute();
            rsa = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject result = JSON.parseObject(rsa);

        Map<String, Holiday> res = new HashMap<>();

        assert result != null;
        JSONObject holidayMap = result.getJSONObject("holiday");

        holidayMap.entrySet().forEach( a -> {
            JSONObject holiday = JSON.parseObject(a.getValue().toString());
            res.put(a.getKey(), Holiday.builder().date(holiday.getString("date")).wage(holiday.getInteger("wage")).build());
        });
        return res;
    }

    /**
     * 获取节日日期
     */
    public static Map<String, List<String>> overHolidays(String year){

        Map<String, List<String>> over = new HashMap<>();

        Map<String, Holiday> holiday = getHolidays(Integer.parseInt(year));
        Arrays.stream("01-,02-,03-,04-,05-,06-,07-,08-,09-,10-,11-,12-".split(",")).forEach( m -> {
            String bb = (year + m).substring(0,(year + m).length() -1);
            List<String> hol = holiday.entrySet().stream()
                    .filter( hd -> hd.getKey().contains(m) && hd.getValue().getWage() != 1)
                    .map( hd -> hd.getValue().getDate()).collect(Collectors.toList());
            if(hol.size()>0 ) over.put(bb, hol);
        });
        return over;
    }

    /**
     * 补班日期
     */
    public static Map<String, List<String>> overWorkDays(String year){

        Map<String, List<String>> over = new HashMap<>();
        Map<String, Holiday> holiday = getHolidays(Integer.parseInt(year));
        Arrays.stream("01-,02-,03-,04-,05-,06-,07-,08-,09-,10-,11-,12-".split(",")).forEach( m -> {
            String bb = (year + m).substring(0,(year + m).length() -1);
            List<String> hol = holiday.entrySet().stream()
                    .filter( hd -> hd.getKey().contains(m) && hd.getValue().getWage() == 1)
                    .map( hd -> hd.getValue().getDate()).collect(Collectors.toList());
            if(hol.size()>0 ) over.put(bb, hol);
        });
        return over;
    }



    public static void main(String[] args) {
//        System.out.println(getJjr(2022, 4));
//        System.out.println(getMonthWekDay(2022, 4));
//        System.out.println(JJR(2022, 12));

//      getHolidays(2022);

        overHolidays("2022");

        overWorkDays("2022");

    }

}
