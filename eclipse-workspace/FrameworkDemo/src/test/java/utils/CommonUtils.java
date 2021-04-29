package utils;

public class CommonUtils {

    public static void waitForSeconds(int seconds){
        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
