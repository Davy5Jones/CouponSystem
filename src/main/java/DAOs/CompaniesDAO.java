package DAOs;

import beans.Company;
import beans.Coupon;

import java.sql.SQLException;
import java.util.List;

public interface CompaniesDAO extends DAO<Company,Integer> {

    boolean isExists(String email, String password) throws SQLException;

    List<Coupon> couponListGenerator(Integer id) throws SQLException;
    boolean isExistByNameOrEmail(String email,String name) throws SQLException;

    int getIDByEmail(String email) throws SQLException;
}