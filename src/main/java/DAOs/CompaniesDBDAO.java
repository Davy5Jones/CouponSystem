package DAOs;

import Helper.SystemException;
import beans.Company;
import beans.Coupon;
import db.ConnectionPool;
import db.ConvertUtils;
import db.JDBCUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompaniesDBDAO implements CompaniesDAO{
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String IS_EXISTS_QUERY ="SELECT count(1) as present FROM `java-151-cs1`.`companies`  " +
            "where exists(SELECT id WHERE EMAIL = ?  AND PASSWORD=? ) \n";
    private static final String ADD_COMPANY_QUERY ="INSERT INTO `java-151-cs1`.`companies` " +
            "(`Name`, `Email`, `Password`) VALUES (?,?,?);";
    private static final String UPDATE_COMPANY_QUERY ="UPDATE `java-151-cs1`.`companies` SET  `Email` = ?, `Password` = ? WHERE (`id` = ?);";
    private static final String DROP_COMPANY_QUERY ="DELETE FROM `java-151-cs1`.`companies` WHERE (`id` = ?);";
    private static final String GET_COUPONS_BY_COMPANY_QUERY = "SELECT * FROM `java-151-cs1`.coupons where COMPANY_ID = ?;";
    private static final String GET_ALL_COMPANIES_QUERY = "SELECT * FROM `java-151-cs1`.companies;";
    private static final String GET_COMPANY_INSTANCE_QUERY ="SELECT * FROM `java-151-cs1`.companies where id = ? ;";
    private static final String IS_EXISTS_BY_EMAIL_OR_NAME_QUERY ="SELECT count(1) as present FROM `java-151-cs1`.`companies`  where exists(SELECT id WHERE EMAIL =? ) or exists(select id where name =?)";

    private static final String GET_COUPONS_BY_COMPANY_CATEGORY_QUERY ="SELECT * FROM `java-151-cs1`.coupons where COMPANY_ID = ? and CATEGORY_ID = ?;";
    //optionally use sql for category coupon list todo
    private static final String ID_BY_EMAIL_QUERY = "SELECT id FROM `java-151-cs1`.companies where Email = ? limit 1;";
    @Override
    public boolean isExists(String email, String password) throws SQLException {
        Map<Integer, Object> map = new HashMap<>();
        map.put(1,email);
        map.put(2,password);
        try {
            return ConvertUtils.objectToBool((Map<String, Object>) JDBCUtils.runQueryWithResult(IS_EXISTS_QUERY, map).get(0));

        }catch (IndexOutOfBoundsException e){
            return false;
        }
    }
    @Override

    public void add(Company company) throws SQLException {
        Map<Integer,Object> map = new HashMap<>();
        map.put(1,company.getName());
        map.put(2,company.getEmail());
        map.put(3,company.getPassword());
        JDBCUtils.runQuery(ADD_COMPANY_QUERY,map);

    }

    @Override
    public void update(Company company,Integer id) throws SQLException, SystemException {
        if (!isExistByNameOrEmail("",company.getName())){
            throw new SystemException("no such company!");
        }
        Map<Integer,Object> map = new HashMap<>();
        map.put(1,company.getEmail());
        map.put(2,company.getPassword());
        map.put(3,id);
        JDBCUtils.runQuery(UPDATE_COMPANY_QUERY,map);


    }

    @Override
    public void drop(Integer id) throws SQLException {
        //todo check if exists!

        Map<Integer,Object> map = new HashMap<>();
        map.put(1,id);
        JDBCUtils.runQuery(DROP_COMPANY_QUERY,map);
    }

    @Override
    public List<Company> getAll() throws SQLException {
        List<Company> companies = new ArrayList<>();
        List<Map<String, Object>> relist = (List<Map<String, Object>>) JDBCUtils.runQueryWithResult(GET_ALL_COMPANIES_QUERY);
        for (Map<String, Object> mapper: relist) {
            companies.add(ConvertUtils.objectToCompany(mapper,couponListGenerator((Integer) mapper.get("id"))));

        }
        return companies;
    }

    @Override
    public List<Coupon> couponListGenerator(Integer id) throws SQLException {
        //todo check if exists!
        List<Coupon> couponList = new ArrayList<>();
        Map<Integer,Object> map = new HashMap<>();
        map.put(1,id);
        List<Map<String, Object>> list = (List<Map<String, Object>>) JDBCUtils.runQueryWithResult(GET_COUPONS_BY_COMPANY_QUERY,map);
        for (Map<String, Object> mapper: list) {
            couponList.add(ConvertUtils.objectToCoupon(mapper));
        }
        return couponList;
    }

    @Override
    public Company getOne(Integer id) throws SQLException, SystemException {
        Map<Integer,Object> mapper = new HashMap<>();
        mapper.put(1,id);
        List<Map<String, Object>> company = (List<Map<String, Object>>) JDBCUtils.runQueryWithResult(GET_COMPANY_INSTANCE_QUERY,mapper);
        if (company.isEmpty()) throw new SystemException("no such company exists");
        return ConvertUtils.objectToCompany(company.get(0),couponListGenerator(id));
    }

    @Override
    public boolean isExistByNameOrEmail(String email,String name) throws SQLException {
        Map<Integer,Object> map = new HashMap<>();
        map.put(1,email);
        map.put(2,name);
        return ConvertUtils.objectToBool((Map<String, Object>) JDBCUtils.runQueryWithResult(IS_EXISTS_BY_EMAIL_OR_NAME_QUERY,map).get(0));
    }
    @Override
    public int getIDByEmail(String email) throws SQLException {
        Map<Integer,Object> params = new HashMap<>();
        params.put(1,email);
        return (int) ((Map<String,Object>) JDBCUtils.runQueryWithResult(ID_BY_EMAIL_QUERY,params).get(0)).get("id");
    }


}
