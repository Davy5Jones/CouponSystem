package clients;

import Helper.SystemException;
import Helper.SystemExceptionEnum;
import beans.Company;
import beans.Coupon;
import beans.Customer;

import java.sql.SQLException;
import java.util.List;

public class AdminFacade extends ClientFacade {
    @Override
    public boolean login(String email, String password) {
        return (email.equals("admin@admin.com") && password.equals("admin"));
    }

    public void addCompany(Company company) throws SystemException, SQLException {

        if (companiesDBDAO.isExistByNameOrEmail(company.getEmail(), company.getName())) {
            throw new SystemException(SystemExceptionEnum.EMAIL_ALREADY_USED.getMessage());
        }

        companiesDBDAO.add(company);


    }

    public void updateCompany(Company company) throws SQLException, SystemException {
        Company lastCompany = companiesDBDAO.getOne(company.getId());
        if (!lastCompany.getName().equals(company.getName())) {
            throw new SystemException(SystemExceptionEnum.CANNOT_UPDATE_COMPANY_NAME.getMessage());
        }
        if (!lastCompany.getEmail().equals(company.getEmail()) && companiesDBDAO.isExistByNameOrEmail(company.getEmail(), "")) {
            throw new SystemException(SystemExceptionEnum.EMAIL_ALREADY_USED.getMessage());
        }

        companiesDBDAO.update(company, company.getId());


    }

    public void deleteCompany(int id) throws SQLException, SystemException {
        Company company = getOneCompany(id);

        for (Coupon coupon : company.getCoupons()) {
            couponsDBDAO.deleteFromVS(coupon.getId());
        }
        couponsDBDAO.deleteCompanyCoupons(id);

        companiesDBDAO.drop(id);

    }

    public List<Company> getAllCompanies() throws SQLException {
        return companiesDBDAO.getAll();
    }

    public Company getOneCompany(int id) throws SQLException, SystemException {
        return companiesDBDAO.getOne(id);
    }

    public void addCustomer(Customer customer) throws SystemException, SQLException {

        if (customersDBDAO.existsByEmail(customer.getEmail()))
            throw new SystemException(SystemExceptionEnum.EMAIL_ALREADY_USED.getMessage());
        customersDBDAO.add(customer);

    }

    public void updateCustomer(Customer customer, int id) throws SystemException, SQLException {

        if (!customersDBDAO.getOne(id).getEmail().equals(customer.getEmail()) && customersDBDAO.existsByEmail(customer.getEmail())) {
            throw new SystemException(SystemExceptionEnum.EMAIL_ALREADY_USED.getMessage());
        }
        customersDBDAO.update(customer, id);
    }


    public void deleteCustomer(int id) throws SQLException {
        customersDBDAO.deleteFromVS(id);
        customersDBDAO.drop(id);

    }

    public List<Customer> getAllCustomers() throws SQLException {
        return customersDBDAO.getAll();
    }


    public Customer getCustomer(int id) throws SQLException, SystemException {
        return customersDBDAO.getOne(id);
    }

}
