package cn.com.tarotframework.utils;

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

    public static void main(String[] args) {
        String a=System.getProperty("os.name");
        System.out.println(a);
    }

}
