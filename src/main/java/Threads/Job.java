package Threads;

import DAOs.CouponsDAO;
import DAOs.CouponsDBDAO;
import clients.ClientFacade;

import java.sql.Date;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Job implements Runnable{
    private final static Job JOB = new Job();
    private final long MILLIS = TimeUnit.HOURS.toMillis(12);
    CouponsDAO couponsDAO = new CouponsDBDAO();
    private final ReentrantLock lock = ClientFacade.getCouponLock();
    private boolean quit = false;

    private Job() {
    }

    public static Job getJob() {
        return JOB;
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        while (!quit){
            try {
                lock.lock();
                couponsDAO.getAll().forEach(coupon -> {
                    if (coupon.getEndDate().before(new Date(System.currentTimeMillis()))){
                try {
                            couponsDAO.drop(coupon.getId());
                            couponsDAO.deleteFromVS(coupon.getId());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);

                        }
                    }
                });
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }finally {
                lock.unlock();
            }
            try {
                Thread.sleep(MILLIS);
            } catch (InterruptedException ignored) {
            }

        }
    }

    public void quit(){
        quit=true;
    }
}
