package cn.com.tarotframework.utils;

import com.alibaba.fastjson2.JSONObject;
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
    private static final String FIX_NUM_WTBGS = "WTBGS000000";
    private static final String FIX_NUM_PXXX = "PXXX000000";
    private static final String FIX_NUM_XJ = "XJ00000000";
    private static final String FIX_NUM_KXL = "KXL0000000";
    private static final String FIX_NUM_KB = "KB00000000";
    private static final String FIX_NUM_CL = "CL00000000";
    private static final String FIX_NUM_ZWXX = "ZWXX000000";
    private static final String FIX_NUM_BMNBPX = "BMNBPX0000";
    private static final String FIX_NUM_GSPX = "GSPX000000";


    public static String getProjectNum(String str) {
        JSONObject json = new JSONObject();
        if(str.equals("培训及学习类")){
            return ConvertUtil.FIX_NUM_PXXX;
        }

        if(str.equals("自我学习")){
            return ConvertUtil.FIX_NUM_ZWXX;
        }

        if(str.equals("部门内部培训")){
            return ConvertUtil.FIX_NUM_BMNBPX;
        }

        if(str.equals("公司培训")){
            return ConvertUtil.FIX_NUM_GSPX;
        }

        if(str.equals("休假类")){
            return ConvertUtil.FIX_NUM_XJ;
        }

        if(str.equals("材料支持类")){
            return ConvertUtil.FIX_NUM_CL;
        }

        if(str.equals("无")){
            return ConvertUtil.FIX_NUM_WU;
        }

        if(str.equals("空闲类")){
            return ConvertUtil.FIX_NUM_KXL;
        }

        if(str.equals("空白")){
            return ConvertUtil.FIX_NUM_KB;
        }

        if(str.equals("未填写")){
            return ConvertUtil.FIX_NUM_WTX;
        }

        if(str.equals("未填报工时")){
            return ConvertUtil.FIX_NUM_WTBGS;
        }

        return setNum();
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
