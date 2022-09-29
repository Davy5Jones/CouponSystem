package clients;

import Helper.SystemException;
import Helper.SystemExceptionEnum;
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
        if (!customersDBDAO.isExists(email, password))
            throw new SystemException(SystemExceptionEnum.EMAIL_OR_PASSWORD_WRONG.getMessage());
        customerID = customersDBDAO.getIDByEmail(email);
        return true;
    }

    public void customerPurchaseCoupon(int id) throws SQLException, SystemException {
        Coupon coupon1 = couponsDBDAO.getOne(id);

        if (coupon1.getAmount() == 0) throw new SystemException(SystemExceptionEnum.COUPON_OUT_OF_STOCK.getMessage());
        if (customersDBDAO.getOne(customerID).getCoupons().contains(coupon1))
            throw new SystemException(SystemExceptionEnum.ALREADY_OWNS.getMessage());
        if (coupon1.getEndDate().before(new Date(System.currentTimeMillis())))
            throw new SystemException(SystemExceptionEnum.EXPIRED_COUPONS.getMessage());
        couponsDBDAO.addCouponPurchase(customerID, id);
        coupon1.setAmount(coupon1.getAmount() - 1);
        couponsDBDAO.update(coupon1, id);

    }

    public List<Coupon> customerCoupons() throws SQLException, SystemException {
        return customersDBDAO.getOne(customerID).getCoupons();
    }

    public List<Coupon> customerCouponsByCategory(Category category) throws SQLException, SystemException {
        List<Coupon> couponsByCategory = new ArrayList<>();
        customersDBDAO.getOne(customerID).getCoupons().forEach(coupon -> {
            if (coupon.getCategory().equals(category)) couponsByCategory.add(coupon);
        });
        return couponsByCategory;
    }

    public List<Coupon> customerCouponsByPrice(double price) throws SQLException, SystemException {
        List<Coupon> couponsByPrice = new ArrayList<>();
        customersDBDAO.getOne(customerID).getCoupons().forEach(coupon -> {
            if (coupon.getPrice() <= price) couponsByPrice.add(coupon);
        });
        return couponsByPrice;
    }

    public Customer customerDetails() throws SQLException, SystemException {
        return customersDBDAO.getOne(customerID);
    }
}
