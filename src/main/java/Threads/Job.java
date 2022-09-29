package Threads;

import DAOs.CouponsDAO;
import DAOs.CouponsDBDAO;
import beans.Coupon;

import java.sql.Date;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class Job implements Runnable {
    private final static Job JOB = new Job();
    private final long MILLIS = TimeUnit.HOURS.toMillis(12);
    CouponsDAO couponsDAO = new CouponsDBDAO();
    private boolean quit = false;

    private Job() {
    }

    public static Job getJob() {
        return JOB;
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        while (!quit) {
            try {
                for (Coupon coupon : couponsDAO.getAll()) {
                    if (coupon.getEndDate().before(new Date(System.currentTimeMillis()))) {
                        couponsDAO.drop(coupon.getId());
                        couponsDAO.deleteFromVS(coupon.getId());
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            try {
                Thread.sleep(MILLIS);
            } catch (InterruptedException ignored) {
            }

        }
    }

    public void quit() {
        quit = true;
    }
}
