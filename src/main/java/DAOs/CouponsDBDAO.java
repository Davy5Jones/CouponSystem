package DAOs;

import Helper.SystemException;
import Helper.SystemExceptionEnum;
import Helper.Utils;
import beans.Coupon;
import db.ConvertUtils;
import db.JDBCUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CouponsDBDAO implements CouponsDAO {

    private static final String CREATE_COUPON_QUERY = "INSERT INTO `java-151-cs1`.`coupons` (`COMPANY_ID`, `CATEGORY_ID`, `TITLE`, " +
            "`DESCRIPTION`, `START_DATE`, `END_DATE`, `AMOUNT`, `price`, `image`) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_COUPON_QUERY = "UPDATE `java-151-cs1`.`coupons` SET " +
            " `CATEGORY_ID` = ?, `TITLE` = ?, `DESCRIPTION` = ?, `START_DATE` = ?, `END_DATE` = ?, `AMOUNT` = ?, `price` = ?, `image` = ? WHERE (`id` = ?);";
    private static final String DROP_COUPON_QUERY = "DELETE FROM `java-151-cs1`.`coupons` WHERE (`id` = ?);\n";
    private static final String FIND_ONE_COUPON_QUERY = "SELECT * FROM `java-151-cs1`.coupons where id = ?;";
    private static final String GET_ALL_COUPON_QUERY = "SELECT * FROM `java-151-cs1`.coupons;";
    private static final String ADD_COUPON_PURCHASE_QUERY = "INSERT INTO " +
            "`java-151-cs1`.`customers_vs_coupons` (`CUSTOMER_ID`, `COUPON_ID`) VALUES (?, ?);";
    private static final String DELETE_COUPON_PURCHASE_QUERY = "DELETE FROM `java-151-cs1`.`customers_vs_coupons` WHERE (`CUSTOMER_ID` = ?) and (`COUPON_ID` = ?);";
    private static final String DELETE_FROM_VS_QUERY = "DELETE FROM `java-151-cs1`.`customers_vs_coupons` WHERE  (`COUPON_ID` = ?);";
    private static final String DELETE_COMPANY_COUPONS_QUERY = "DELETE FROM `java-151-cs1`.`coupons` WHERE (`COMPANY_ID` = ?);";
    private static final String IS_EXISTS_BY_COMPANY_TITLE = "SELECT count(1) as present FROM `java-151-cs1`.coupons  where exists(select id where `COMPANY_ID` = ? and `TITLE` = ?)";
    private static final int MINIMUM_COUPON_AMOUNT = 1;

    @Override
    public void addCouponPurchase(int customerID, int couponID) throws SQLException {
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, customerID);
        map.put(2, couponID);
        JDBCUtils.runQuery(ADD_COUPON_PURCHASE_QUERY, map);
    }

    @Override
    public void deleteCouponPurchase(int customerID, int couponID) throws SQLException {
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, customerID);
        map.put(2, couponID);
        JDBCUtils.runQuery(DELETE_COUPON_PURCHASE_QUERY, map);

    }


    @Override
    public void add(Coupon coupon) throws SQLException, SystemException {
        if (coupon.getAmount() < MINIMUM_COUPON_AMOUNT)
            throw new SystemException(SystemExceptionEnum.INVALID_AMOUNT.getMessage());
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, coupon.getCompanyID());
        map.put(2, coupon.getCategory().ordinal() + 1);
        map.put(3, coupon.getTitle());
        map.put(4, coupon.getDescription());
        map.put(5, coupon.getStartDate());
        map.put(6, coupon.getEndDate());
        map.put(7, coupon.getAmount());
        map.put(8, coupon.getPrice());
        map.put(9, coupon.getImage());
        JDBCUtils.runQuery(CREATE_COUPON_QUERY, map);

    }

    @Override
    public void update(Coupon coupon, Integer id) throws SQLException, SystemException {
        if (coupon.getAmount() < MINIMUM_COUPON_AMOUNT)
            throw new SystemException(SystemExceptionEnum.INVALID_AMOUNT.getMessage());
        if (Utils.dateValid(coupon.getStartDate(), coupon.getEndDate()))
            throw new SystemException(SystemExceptionEnum.INVALID_DATE.getMessage());
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, coupon.getCategory().ordinal() + 1);
        map.put(2, coupon.getTitle());
        map.put(3, coupon.getDescription());
        map.put(4, coupon.getStartDate());
        map.put(5, coupon.getEndDate());
        map.put(6, coupon.getAmount());
        map.put(7, coupon.getPrice());
        map.put(8, coupon.getImage());
        map.put(9, id);
        JDBCUtils.runQuery(UPDATE_COUPON_QUERY, map);

    }

    @Override
    public void drop(Integer id) throws SQLException {
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, id);
        JDBCUtils.runQuery(DROP_COUPON_QUERY, map);
    }

    @Override
    public List<Coupon> getAll() throws SQLException {
        List<Coupon> allCoupons = new ArrayList<>();
        List<Map<String, Object>> list = (List<Map<String, Object>>) JDBCUtils.runQueryWithResult(GET_ALL_COUPON_QUERY);
        for (Map<String, Object> map : list) {
            allCoupons.add(ConvertUtils.objectToCoupon(map));
        }
        return allCoupons;
    }

    @Override
    public Coupon getOne(Integer id) throws SQLException, SystemException {
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, id);
        Map<String, Object> mapper = (Map<String, Object>) JDBCUtils.runQueryWithResult(FIND_ONE_COUPON_QUERY, map).get(0);
        if (mapper.isEmpty()) throw new SystemException(SystemExceptionEnum.COUPON_NOT_FOUND.getMessage());
        return ConvertUtils.objectToCoupon(mapper);
    }

    @Override
    public void deleteFromVS(int CouponID) throws SQLException {
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, CouponID);
        JDBCUtils.runQuery(DELETE_FROM_VS_QUERY, map);
    }

    @Override
    public void deleteCompanyCoupons(int companyID) throws SQLException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, companyID);
        JDBCUtils.runQuery(DELETE_COMPANY_COUPONS_QUERY, params);
    }

    @Override
    public boolean couponExistsByCompanyAndTitle(Coupon coupon) throws SQLException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, coupon.getCompanyID());
        params.put(2, coupon.getTitle());
        return ConvertUtils.objectToBool((Map<String, Object>) JDBCUtils.runQueryWithResult(IS_EXISTS_BY_COMPANY_TITLE, params).get(0));
    }


}
