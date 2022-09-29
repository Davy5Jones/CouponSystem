package Managment;

import Helper.SystemException;
import clients.AdminFacade;
import clients.ClientFacade;
import clients.CompanyFacade;
import clients.CustomerFacade;

import java.sql.SQLException;

public class LoginManager {

    public static LoginManager loginManager = new LoginManager();

    private LoginManager() {
    }

    public static LoginManager getLoginManager() {
        return loginManager;
    }

    public ClientFacade Login(String email,String password,ClientType clientType) throws SystemException, SQLException {
        switch (clientType){
            case COMPANY:
                ClientFacade companyFacade = new CompanyFacade();
                return companyFacade.login(email,password) ? companyFacade : null;
            case CUSTOMER:
                ClientFacade customerFacade = new CustomerFacade();
                return customerFacade.login(email,password) ? customerFacade : null;
            case ADMINISTRATOR:
                ClientFacade adminFacade = new AdminFacade();
                return adminFacade.login(email,password) ? adminFacade : null;

        }
        return null;
    }
}
