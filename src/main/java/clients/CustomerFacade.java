package clients;

import Helper.SystemException;
import beans.Category;
import beans.Coupon;
import beans.Customer;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CustomerFacade extends ClientFacade {

    private int customerID;



    @Override
    public boolean login(String email, String password) throws SQLException, SystemException {
        if (!customersDBDAO.isExists(email,password)) throw new SystemException("email or password are wrong!");
        customerID = customersDBDAO.getIDByEmail(email);
        return true;
    }

    public void customerPurchaseCoupon(int id) throws SQLException, SystemException {
        Coupon coupon1 =couponsDBDAO.getOne(id);

        if (coupon1.getAmount() == 0) throw new SystemException("coupon out of stock");
        if (customersDBDAO.getOne(customerID).getCoupons().contains(coupon1))
            throw new SystemException("customer already posses this coupon");
        if (coupon1.getEndDate().before(new Date(System.currentTimeMillis())))
            throw new SystemException("coupon has expired!");
        customerLock.lock();
        try {
            couponsDBDAO.addCouponPurchase(customerID, id);
            coupon1.setAmount(coupon1.getAmount() - 1);
            couponsDBDAO.update(coupon1, id);
        }finally {
            customerLock.unlock();
        }

    }
    public List<Coupon> customerCoupons() throws SQLException {
        return customersDBDAO.getOne(customerID).getCoupons();
    }
    public List<Coupon> customerCouponsByCategory(Category category) throws SQLException {
        List<Coupon> couponsByCategory = new ArrayList<>();
        customersDBDAO.getOne(customerID).getCoupons().forEach(coupon -> {
            if (coupon.getCategory().equals(category)) couponsByCategory.add(coupon);
        });
        return couponsByCategory;
    }

    public List<Coupon> customerCouponsByPrice(double price) throws SQLException {
        List<Coupon> couponsByPrice = new ArrayList<>();
        customersDBDAO.getOne(customerID).getCoupons().forEach(coupon -> {
            if (coupon.getPrice() <= price) couponsByPrice.add(coupon);
        });
        return couponsByPrice;
    }
    public Customer customerDetails() throws SQLException {
        return customersDBDAO.getOne(customerID);
    }
}
