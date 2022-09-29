package Managment;

import DAOs.CompaniesDAO;
import DAOs.CompaniesDBDAO;
import DAOs.CouponsDBDAO;
import DAOs.CustomersDBDAO;
import Helper.SystemException;
import Threads.Job;
import beans.Category;
import beans.Company;
import beans.Coupon;
import beans.Customer;
import clients.AdminFacade;
import clients.ClientFacade;
import clients.CompanyFacade;
import clients.CustomerFacade;
import db.DatabaseManager;
import db.JDBCUtils;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Test {
    private static LoginManager loginManager = LoginManager.getLoginManager();
    private static Thread task = new Thread(Job.getJob());
    public static void testAll() {


        try {
            DatabaseManager.dropAndCreateStrategy();
            task.start();
            AdminFacade adminFacade = (AdminFacade) loginManager.Login("admin@admin.com","admin",ClientType.ADMINISTRATOR);

            adminFacade.addCustomer(new Customer("Ido","Shal","ido@gmail","1234"));
            adminFacade.addCustomer(new Customer("Guy","Lo","guy@gmail","333"));
            adminFacade.addCustomer(new Customer("Niv","Erich","Niv@gmail","222"));
            adminFacade.addCustomer(new Customer("Barbara", "Santa","santaBarbara@gov","gov123"));
            adminFacade.addCustomer(new Customer("Kobi","shasha","Ks@gmail","147"));
            adminFacade.addCompany(new Company("john bryce","john@gmail","john123"));
            adminFacade.addCompany(new Company("Matrix","Matrix@gmail","mat123"));
            adminFacade.addCompany(new Company("Oracle","Oracle@gmail","Ora123"));

            CompanyFacade companyFacade1 = (CompanyFacade) loginManager.Login("john@gmail","john123",ClientType.COMPANY);

            companyFacade1.addCoupon(new Coupon(Category.ELECTRICITY,"course discount","java course discount of 10%!",new Date(System.currentTimeMillis()),Date.valueOf(LocalDate.ofYearDay(2024,2)),100,10.5,"temp"));
            companyFacade1.addCoupon(new Coupon(Category.VACATION,"vacation discount","vacation discount of 10%! for students!",new Date(System.currentTimeMillis()),Date.valueOf(LocalDate.ofYearDay(2023,2)),15,23.5,"temp"));
            companyFacade1.addCoupon(new Coupon(Category.RESTAURANT,"sql discount","Oracle sql discount for companies",new Date(System.currentTimeMillis()),Date.valueOf(LocalDate.ofYearDay(2023,1)),50_000,5.5,"temp"));

            CompanyFacade companyFacade2 = (CompanyFacade) loginManager.Login("Matrix@gmail","mat123",ClientType.COMPANY);
            companyFacade2.addCoupon(new Coupon(Category.VACATION,"Santa Barbara home discount","25% off houses in Santa Barbara",new Date(System.currentTimeMillis()),Date.valueOf(LocalDate.ofYearDay(2031,200)),10,10000,"temp"));
            companyFacade2.addCoupon(new Coupon(Category.ELECTRICITY,"AI Corporations","The new AI for 10% off",Date.valueOf(LocalDate.ofYearDay(2022,1)),Date.valueOf(LocalDate.ofYearDay(2026,155)),50,1000,"temp"));
            CompanyFacade companyFacade3 = (CompanyFacade) loginManager.Login("Oracle@gmail","Ora123",ClientType.COMPANY);
            companyFacade3.addCoupon(new Coupon(Category.FOOD,"Grain discount","Ukrainian wheat for 15% off",Date.valueOf(LocalDate.ofYearDay(2021,200)),Date.valueOf(LocalDate.ofYearDay(2024,25)),10_000,10,"temp"));
            companyFacade3.addCoupon(new Coupon(Category.ELECTRICITY,"microchip discount","Taiwanese chips for 15% off",Date.valueOf(LocalDate.ofYearDay(2020,55)),Date.valueOf(LocalDate.ofYearDay(2025,54)),1000,100,"temp"));

            CustomerFacade customerFacade1 = (CustomerFacade) loginManager.Login("ido@gmail","1234",ClientType.CUSTOMER);
            CustomerFacade customerFacade2 = (CustomerFacade) loginManager.Login("guy@gmail","333",ClientType.CUSTOMER);
            CustomerFacade customerFacade3 = (CustomerFacade) loginManager.Login("santaBarbara@gov","gov123",ClientType.CUSTOMER);

            customerFacade1.customerPurchaseCoupon(1);
            customerFacade1.customerPurchaseCoupon(3);
            customerFacade2.customerPurchaseCoupon(2);
            customerFacade2.customerPurchaseCoupon(4);
            customerFacade3.customerPurchaseCoupon(5);
            customerFacade3.customerPurchaseCoupon(7);

            Coupon coupon =companyFacade1.companyCoupons().get(0);
            coupon.setImage("123");
            ;


        }catch (SystemException | SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            Job.getJob().quit();
            task.interrupt();
        }
    }
}
