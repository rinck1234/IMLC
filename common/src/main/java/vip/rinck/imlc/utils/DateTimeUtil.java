package vip.rinck.imlc.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 */
public class DateTimeUtil {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yy-MM-dd", Locale.ENGLISH);

    /**
     * 获取一个的时间字符串
     * @param date
     * @return 时间字符串
     */
    public static String getSampleDate(Date date){
        return FORMAT.format(date);
    }
}
