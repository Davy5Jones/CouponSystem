package DAOs;

import beans.Coupon;

import java.sql.SQLException;

public interface CouponsDAO extends DAO<Coupon, Integer> {

    void addCouponPurchase(int customerID, int couponID) throws SQLException;

    void deleteCouponPurchase(int customerID, int couponID) throws SQLException;

    void deleteFromVS(int id) throws SQLException;

    void deleteCompanyCoupons(int companyID) throws SQLException;

    boolean couponExistsByCompanyAndTitle(Coupon coupon) throws SQLException;
}
