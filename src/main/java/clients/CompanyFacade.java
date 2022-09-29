package clients;

import Helper.SystemException;
import Helper.SystemExceptionEnum;
import Helper.Utils;
import beans.Category;
import beans.Company;
import beans.Coupon;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyFacade extends ClientFacade {
    private int companyID;


    @Override
    public boolean login(String email, String password) throws SQLException, SystemException {
        if (!companiesDBDAO.isExists(email, password))
            throw new SystemException(SystemExceptionEnum.EMAIL_OR_PASSWORD_WRONG.getMessage());
        companyID = companiesDBDAO.getIDByEmail(email);
        return true;
    }

    public void addCoupon(Coupon coupon) throws SQLException, SystemException {
        coupon.setCompanyID(companyID);
        if (coupon.getEndDate().before(new Date(System.currentTimeMillis())))
            throw new SystemException(SystemExceptionEnum.INVALID_DATE.getMessage());
        if (coupon.getStartDate().after(coupon.getEndDate()))
            throw new SystemException("invalid end Date, end date is before start date");

        if (couponsDBDAO.couponExistsByCompanyAndTitle(coupon)) {
            throw new SystemException("title already in use in the company!");
        }
        couponsDBDAO.add(coupon);

    }

    public void updateCoupon(Coupon coupon) throws SQLException, SystemException {
        if (Utils.dateValid(coupon.getStartDate(), coupon.getEndDate()))
            throw new SystemException(SystemExceptionEnum.INVALID_DATE.getMessage());


        if (coupon.getCompanyID() != companyID) {
            throw new SystemException(SystemExceptionEnum.CANNOT_UPDATE_ID.getMessage());
        }
        couponsDBDAO.update(coupon, coupon.getId());

    }

    public void deleteCoupon(int id) throws SQLException, SystemException {
        if (couponsDBDAO.getOne(id).getCompanyID() != companyID) {
            throw new SystemException(SystemExceptionEnum.CANNOT_DELETE_ANOTHER.getMessage());
        }
        couponsDBDAO.deleteFromVS(id);
        couponsDBDAO.drop(id);
    }

    public List<Coupon> companyCoupons() throws SQLException {
        return companiesDBDAO.couponListGenerator(companyID);
    }

    public List<Coupon> companyCouponsByCategory(Category category) throws SQLException {
        List<Coupon> couponsByCategory = new ArrayList<>();
        companiesDBDAO.couponListGenerator(companyID).forEach(coupon -> {
            if (coupon.getCategory().equals(category)) couponsByCategory.add(coupon);

        });
        return couponsByCategory;
    }

    public List<Coupon> companyCouponsByPrice(double price) throws SQLException {
        List<Coupon> couponsByPrice = new ArrayList<>();
        companiesDBDAO.couponListGenerator(companyID).forEach(coupon -> {
            if (coupon.getPrice() <= price) couponsByPrice.add(coupon);

        });
        return couponsByPrice;
    }

    public Company companyDetails() throws SQLException, SystemException {
        return companiesDBDAO.getOne(companyID);
    }
}
