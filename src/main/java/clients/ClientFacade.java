package clients;

import DAOs.CompaniesDBDAO;
import DAOs.CouponsDBDAO;
import DAOs.CustomersDBDAO;
import Helper.SystemException;

import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

public abstract class ClientFacade {


    protected static ReentrantLock customerLock =new ReentrantLock(true);
    protected static ReentrantLock couponLock =new ReentrantLock(true);
    protected static ReentrantLock companyLock =new ReentrantLock(true);
    protected CompaniesDBDAO companiesDBDAO = new CompaniesDBDAO();
    protected CouponsDBDAO couponsDBDAO = new CouponsDBDAO();
    protected CustomersDBDAO customersDBDAO = new CustomersDBDAO();

    public static ReentrantLock getCustomerLock() {
        return customerLock;
    }

    public static ReentrantLock getCouponLock() {
        return couponLock;
    }

    public static ReentrantLock getCompanyLock() {
        return companyLock;
    }

    public abstract boolean login(String email, String password) throws SQLException, SystemException;
}
