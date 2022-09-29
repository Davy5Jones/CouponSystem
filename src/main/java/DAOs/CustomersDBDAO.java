package DAOs;

import Helper.QueryBuilder;
import Helper.SystemException;
import Helper.SystemExceptionEnum;
import beans.Coupon;
import beans.Customer;
import db.ConnectionPool;
import db.ConvertUtils;
import db.JDBCUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomersDBDAO implements CustomersDAO {
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String ADD_QUERY = QueryBuilder.insertInto("`java-151-cs1`.`customers`", new String[]{"`FIRST_NAME`,", "LAST_NAME", "EMAIL", "Password"}).values(new String[]{"?", "?", "?", "?"}).getQuery();
    private static final String IS_EXISTS_QUERY = QueryBuilder.select().count(1).as("present").from("`java-151-cs1`.`customers`").where("", "", "").exists("where exists(SELECT id WHERE EMAIL = ?  AND PASSWORD=? )").getQuery();
    private static final String UPDATE_CUSTOMER_QUERY = "UPDATE `java-151-cs1`.`customers` SET `FIRST_NAME` = ?, `LAST_NAME` = ?, `EMAIL` = ?, `Password` = ? WHERE (`id` = ?);";
    private static final String DELETE_CUSTOMER_QUERY = "DELETE FROM `java-151-cs1`.`customers` WHERE (`id` = ?);";
    private static final String GET_ALL_CUSTOMERS_QUERY = "SELECT * FROM `java-151-cs1`.`customers`;";
    private static final String GET_ONE_CUSTOMER_QUERY = "SELECT * FROM `java-151-cs1`.`customers` where `id` = ?;";
    private static final String GET_COUPON_FROM_VS_QUERY = "SELECT * FROM `java-151-cs1`.`coupons` where exists(select * from `java-151-cs1`.`customers_vs_coupons` where `CUSTOMER_ID`=? and `COUPON_ID` = `java-151-cs1`.`coupons`.`id` );";
    private static final String DELETE_FROM_VS_QUERY = "DELETE FROM `java-151-cs1`.`customers_vs_coupons` WHERE  (`CUSTOMER_ID` = ?);";

    private static final String CUSTOMER_ID_BY_EMAIL_QUERY = "SELECT id FROM `java-151-cs1`.`customers` where Email = ? limit 1;";

    private static final String EXISTS_BY_EMAIL = "SELECT count(1) as present FROM `java-151-cs1`.`customers`  where exists(SELECT id WHERE EMAIL = ?)";

    @Override
    public boolean isExists(String email, String password) throws SQLException {
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, email);
        map.put(2, password);
        return ConvertUtils.objectToBool((Map<String, Object>) JDBCUtils.runQueryWithResult(IS_EXISTS_QUERY, map).get(0));

    }

    @Override
    public void add(Customer customer) throws SQLException {
        Map<Integer, Object> map = customerToMap(customer);
        JDBCUtils.runQuery(ADD_QUERY, map);

    }

    @Override
    public void update(Customer customer, Integer id) throws SQLException {
        Map<Integer, Object> map = customerToMap(customer);
        map.put(5, id);
        JDBCUtils.runQuery(UPDATE_CUSTOMER_QUERY, map);

    }

    @Override
    public void drop(Integer id) throws SQLException {
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, id);
        JDBCUtils.runQuery(DELETE_CUSTOMER_QUERY, map);
    }

    @Override
    public List<Customer> getAll() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        List<Map<String, Object>> relist = (List<Map<String, Object>>) JDBCUtils.runQueryWithResult(GET_ALL_CUSTOMERS_QUERY);
        for (Map<String, Object> mapper : relist) {
            customers.add(ConvertUtils.objectToCustomer(mapper, couponListGenerator((Integer) mapper.get("id"))));

        }


        return customers;
    }

    @Override
    public Customer getOne(Integer id) throws SQLException, SystemException {
        Map<Integer, Object> mapper = new HashMap<>();
        mapper.put(1, id);
        List<Map<String, Object>> customer = (List<Map<String, Object>>) JDBCUtils.runQueryWithResult(GET_ONE_CUSTOMER_QUERY, mapper);
        if (customer.isEmpty()) throw new SystemException(SystemExceptionEnum.CUSTOMER_NOT_FOUND.getMessage());
        return ConvertUtils.objectToCustomer(customer.get(0), couponListGenerator(id));
    }

    @Override
    public boolean existsByEmail(String email) throws SQLException {
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, email);
        return ConvertUtils.objectToBool((Map<String, Object>) JDBCUtils.runQueryWithResult(EXISTS_BY_EMAIL, map).get(0));
    }

    @Override
    public void deleteFromVS(int customerID) throws SQLException {
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, customerID);
        JDBCUtils.runQuery(DELETE_FROM_VS_QUERY, map);
    }


    private Map<Integer, Object> customerToMap(Customer customer) {
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, customer.getFirstName());
        map.put(2, customer.getLastName());
        map.put(3, customer.getEmail());
        map.put(4, customer.getPassword());

        return map;
    }

    @Override
    public List<Coupon> couponListGenerator(int id) throws SQLException {
        List<Coupon> couponList = new ArrayList<>();
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, id);
        List<Map<String, Object>> list = (List<Map<String, Object>>) JDBCUtils.runQueryWithResult(GET_COUPON_FROM_VS_QUERY, map);
        for (Map<String, Object> mapper : list) {
            couponList.add(ConvertUtils.objectToCoupon(mapper));
        }
        return couponList;

    }


    @Override
    public int getIDByEmail(String email) throws SQLException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, email);
        return (int) ((Map<String, Object>) JDBCUtils.runQueryWithResult(CUSTOMER_ID_BY_EMAIL_QUERY, params).get(0)).get("id");
    }
}
