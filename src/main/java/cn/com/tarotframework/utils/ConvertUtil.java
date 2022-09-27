package cn.com.tarotframework.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * 数据转换类
 */
public class ConvertUtil {

    private static final String FIX_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static final String PROJECT_NUM = "projectNum";

    public static final String PROJECT_NAME = "projectName";

    private static final String FIX_NUM = "0123456789";

    private static final String FIX_NUM_WU = "WU000000000";

    private static final String FIX_NUM_WTX = "WTX00000000";

    private static final Object FIX_NUM_WTBGS = "WTBGS000000";

    private static final String FIX_NUM_PXXX = "PXXX000000";

    private static final String FIX_NUM_XJ = "XJ00000000";

    private static final Object FIX_NUM_KXL = "KXL0000000";

    private static final Object FIX_NUM_KB = "KB00000000";

    private static final Object FIX_NUM_CL = "CL00000000";
    private static final Object FIX_NUM_ZWXX = "ZWXX000000";
    private static final Object FIX_NUM_BMNBPX = "BMNBPX0000";
    private static final Object FIX_NUM_GSPX = "GSPX000000";



    /**
     * 去除字符串开头的字母数字
     */
    public static JSONObject getProjectName(String str) {

        JSONObject json = new JSONObject();

        if(str.equals("培训及学习类")){
            json.put(PROJECT_NUM, ConvertUtil.FIX_NUM_PXXX);
            json.put(PROJECT_NAME, str);
            return json;
        }

        if(str.equals("自我学习")){
            json.put(PROJECT_NUM, ConvertUtil.FIX_NUM_ZWXX);
            json.put(PROJECT_NAME, str);
            return json;
        }

        if(str.equals("部门内部培训")){
            json.put(PROJECT_NUM, ConvertUtil.FIX_NUM_BMNBPX);
            json.put(PROJECT_NAME, str);
            return json;
        }

        if(str.equals("公司培训")){
            json.put(PROJECT_NUM, ConvertUtil.FIX_NUM_GSPX);
            json.put(PROJECT_NAME, str);
            return json;
        }

        if(str.equals("休假类")){
            json.put(PROJECT_NUM, ConvertUtil.FIX_NUM_XJ);
            json.put(PROJECT_NAME, str);
            return json;
        }

        if(str.equals("材料支持类")){
            json.put(PROJECT_NUM, ConvertUtil.FIX_NUM_CL);
            json.put(PROJECT_NAME, str);
            return json;
        }

        if(str.equals("无")){
            json.put(PROJECT_NUM, ConvertUtil.FIX_NUM_WU);
            json.put(PROJECT_NAME, str);
            return json;
        }

        if(str.equals("空闲类")){
            json.put(PROJECT_NUM, ConvertUtil.FIX_NUM_KXL);
            json.put(PROJECT_NAME, str);
            return json;
        }

        if(str.equals("空白")){
            json.put(PROJECT_NUM, ConvertUtil.FIX_NUM_KB);
            json.put(PROJECT_NAME, str);
            return json;
        }

        if(str.equals("未填写")){
            json.put(PROJECT_NUM, ConvertUtil.FIX_NUM_WTX);
            json.put(PROJECT_NAME, str);
            return json;
        }

        if(str.equals("未填报工时")){
            json.put(PROJECT_NUM, ConvertUtil.FIX_NUM_WTBGS);
            json.put(PROJECT_NAME, str);
            return json;
        }

        String name = str;
        char[] b = name.toCharArray();
        int index = 0;
        StringBuilder number = new StringBuilder();

        for (int i = 0; i < b.length; i++){
            if(FIX_CHAR.contains(String.valueOf(b[i]))) {
                index = i;
                number.append(b[i]);
            } else {
                name = name.substring(index + 1, name.length());
                break;
            }
        }
        b =  name.toCharArray();
        for (int i = 0; i < b.length; i++){
            if(FIX_NUM.contains(String.valueOf(b[i]))) {
                index = index + i;
                number.append(b[i]);
            } else {
                break;
            }
        }

        String num = number.toString().trim();
        String pn = str.replace(num, "").trim();
        json.put(PROJECT_NUM, num.length() == 0 ? setNum() : num);
        json.put(PROJECT_NAME, pn.length() == 0 ? num : pn);
        return json;


    }

    private static String setNum() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 2; ++i) {
            int number = random.nextInt(FIX_CHAR.length());
            sb.append(FIX_CHAR.charAt(number));
        }
        for (int i = 0; i < 9; ++i) {
            int number = random.nextInt(FIX_NUM.length());
            sb.append(FIX_NUM.charAt(number));
        }
        return sb.toString().toUpperCase();
    }


    public static void main(String[] args) {

//        String a = "ZX201905018武汉农村商业银行股份有限公司FSS-IC-XY-001";
//        JSONObject object = getProjectName(a);

        //System.out.println(LocalDate.now());

        System.out.println(setNum());

    }


}
