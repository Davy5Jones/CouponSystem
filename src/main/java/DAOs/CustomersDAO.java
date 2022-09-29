package DAOs;

import beans.Coupon;
import beans.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomersDAO extends DAO<Customer, Integer> {

    boolean isExists(String email, String password) throws SQLException;

    boolean existsByEmail(String email) throws SQLException;

    void deleteFromVS(int customerID) throws SQLException;


    List<Coupon> couponListGenerator(int id) throws SQLException;

    int getIDByEmail(String email) throws SQLException;
}
