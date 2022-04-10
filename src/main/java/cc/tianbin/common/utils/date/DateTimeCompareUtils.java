package cc.tianbin.common.utils.date;

/**
 * Created by nibnait on 2022/04/09
 */
public class DateTimeCompareUtils {

    public static long compare(long timeA, long timeB) {
        return DateTimeConvertUtils.toMilliTimeStamp(timeA) - DateTimeConvertUtils.toMilliTimeStamp(timeB);
    }

}
