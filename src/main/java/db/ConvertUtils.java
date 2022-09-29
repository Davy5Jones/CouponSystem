package db;

import beans.Category;
import beans.Company;
import beans.Coupon;
import beans.Customer;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by kobis on 06 Sep, 2022
 */
public class ConvertUtils {

    public static List<?> toList(ResultSet resultSet) throws SQLException {
        //optionally change to <Map<String, Object> todo

        List<Map<String, Object>> list = new ArrayList<>();

        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        final int columnCount = resultSetMetaData.getColumnCount();

        while (resultSet.next()) {
            Object[] values = new Object[columnCount];
            Map<String, Object> keyAndValue = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                values[i - 1] = resultSet.getObject(i);
                keyAndValue.put(resultSetMetaData.getColumnName(i), values[i - 1]);
            }

            list.add(keyAndValue);


        }

        return list;
    }

    public static Customer objectToCustomer(Map<String, Object> map, List<Coupon> coupons) {
        int id = (int) map.get("id");
        String firstName = (String) map.get("FIRST_NAME");
        String lastName = (String) map.get("LAST_NAME");
        String email = (String) map.get("EMAIL");
        String password = (String) map.get("Password");

        return new Customer(id, firstName, lastName, email, password, coupons);
    }

    public static Coupon objectToCoupon(Map<String, Object> map) {
        int id = (int) map.get("id");
        int companyID = (int) map.get("COMPANY_ID");
        Category category = Category.values()[(int) map.get("CATEGORY_ID") - 1];
        String title = (String) map.get("TITLE");
        String description = (String) map.get("DESCRIPTION");
        Date startDate = (Date) map.get("START_DATE");
        Date endDate = (Date) map.get("END_DATE");
        int amount = (int) map.get("AMOUNT");
        double price = (double) map.get("price");
        String image = (String) map.get("image");
        return new Coupon(id, companyID, category, title, description, startDate, endDate, amount, price, image);
    }

    public static Company objectToCompany(Map<String, Object> map, List<Coupon> couponList) {
        int id = (int) map.get("id");
        String name = (String) map.get("Name");
        String email = (String) map.get("Email");
        String password = (String) map.get("Password");

        return new Company(id, name, email, password, couponList);
    }


    public static boolean objectToBool(Map<String, Object> map) {
        return ((long) map.get("present") == 1);
    }

    public static double objectToDouble(Map<String, Object> map) {
        return ((double) map.get("res"));
    }

}
