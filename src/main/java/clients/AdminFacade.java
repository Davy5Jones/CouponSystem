package clients;

import Helper.SystemException;
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

        if (companiesDBDAO.isExistByNameOrEmail(company.getEmail(),company.getName())){
            throw new SystemException("Email or name already in use");
        }
        companyLock.lock();
        try {
            companiesDBDAO.add(company);

        }finally {
            companyLock.unlock();
        }
    }
    public void updateCompany(Company company) throws SQLException, SystemException {
        Company lastCompany = companiesDBDAO.getOne(company.getId());
        if (!lastCompany.getName().equals(company.getName())){
            throw new SystemException("cannot update a company's name");
        }
        if (!lastCompany.getEmail().equals(company.getEmail())&&companiesDBDAO.isExistByNameOrEmail(company.getEmail(),"")){
            throw new SystemException("Email already in use");
        }
        companyLock.lock();
        try {
            companiesDBDAO.update(company,company.getId());
        }finally {
            companyLock.unlock();
        }

    }
    public void deleteCompany(int id) throws SQLException, SystemException {
        Company company = getOneCompany(id);
        couponLock.lock();
        try {
            for (Coupon coupon : company.getCoupons()) {
                couponsDBDAO.deleteFromVS(coupon.getId());
            }
            couponsDBDAO.deleteCompanyCoupons(id);
        }finally {
            couponLock.lock();
        }
        companyLock.lock();
        try {
            companiesDBDAO.drop(id);
        }finally {
            companyLock.unlock();
        }
    }

    public List<Company> getAllCompanies() throws SQLException {
        return companiesDBDAO.getAll();
    }
    public Company getOneCompany(int id) throws SQLException, SystemException {
        return companiesDBDAO.getOne(id);
    }
    public void addCustomer(Customer customer) throws SystemException, SQLException {
        customerLock.lock();
        try {
            if (customersDBDAO.existsByEmail(customer.getEmail())) throw new SystemException("email already in use!");
            customersDBDAO.add(customer);
        }finally {
            customerLock.unlock();
        }
    }
    public void updateCustomer(Customer customer, int id) throws SystemException, SQLException {
        customerLock.lock();
        try {
            if (!customersDBDAO.getOne(id).getEmail().equals(customer.getEmail())) {
                if (customersDBDAO.existsByEmail(customer.getEmail()))
                    throw new SystemException("email already in use!");
            }
            customersDBDAO.update(customer, id);
        }finally {
            customerLock.unlock();
        }
    }

    public void deleteCustomer(int id) throws SQLException {
        customerLock.lock();
        try {
            customersDBDAO.deleteFromVS(id);
            customersDBDAO.drop(id);
        }finally {
            customerLock.unlock();
        }
    }

    public List<Customer> getAllCustomers() throws SQLException {
        return customersDBDAO.getAll();
    }


    public Customer getCustomer(int id) throws SQLException {
        return customersDBDAO.getOne(id);
    }

}
