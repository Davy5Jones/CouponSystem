package clients;

import DAOs.CompaniesDBDAO;
import DAOs.CouponsDBDAO;
import DAOs.CustomersDBDAO;
import Helper.SystemException;

import java.sql.SQLException;

public abstract class ClientFacade {


    protected CompaniesDBDAO companiesDBDAO = new CompaniesDBDAO();
    protected CouponsDBDAO couponsDBDAO = new CouponsDBDAO();
    protected CustomersDBDAO customersDBDAO = new CustomersDBDAO();


    public abstract boolean login(String email, String password) throws SQLException, SystemException;
}
