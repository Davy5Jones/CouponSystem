package Helper;

import java.sql.Date;

public class Utils {
    private static final Utils utils = new Utils();

    public Utils getUtils() {
        return utils;
    }

    private Utils() {
    }

    public static boolean dateValid(Date startDate, Date endDate) {
        return endDate.before(new java.util.Date(System.currentTimeMillis())) || startDate.before(endDate);
    }

}
