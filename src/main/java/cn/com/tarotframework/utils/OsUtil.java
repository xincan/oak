package cn.com.tarotframework.utils;

import java.math.BigDecimal;

public class OsUtil {

    public static String getOsInfo() {
        String os = System.getProperty("os.name");
        if(os != null && os.toLowerCase().startsWith("windows")){
            return "window";
        }else if (os != null && os.toLowerCase().startsWith("linux")) {
            return "linux";
        }
        return os;
    }

    public static String getOsPath() {
        String os = System.getProperty("os.name");
        if(os != null && os.toLowerCase().startsWith("windows")){
            return "c:";
        }
        return os;
    }

    public static void main(String[] args) {
        String a=System.getProperty("os.name");
        System.out.println(a);

        double c = 8.0/3;

        String b = String.format("%.1f", c);

        System.out.println(new BigDecimal(b));

    }

}
