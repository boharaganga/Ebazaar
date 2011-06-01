
package application;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import middleware.DatabaseException;
import middleware.EBazaarException;
import business.DbClassLogin;
import business.Login;
import business.SessionContext;
import business.customersubsystem.CustomerSubsystemFacade;
import business.externalinterfaces.*;
import application.gui.LoginWindow;
import application.gui.ParentWindow;

public class LoginControl {
    private LoginWindow loginWindow;
    private Controller controller;
    
    ParentWindow currWindow;
    Window parentWindow;
    public LoginControl(ParentWindow currWindow, Window parentWindow){
        this.currWindow = currWindow;
        this.parentWindow = parentWindow;
    }
    public LoginControl(ParentWindow currWindow, Window parentWindow, Controller controller){
        this(currWindow,parentWindow);
        this.controller=controller;
    }    
    
    public void startLogin() {
        loginWindow = new LoginWindow(this);
        parentWindow.setVisible(false);
        loginWindow.setVisible(true);
    }
    
    private void loadCustomer(String custId) throws DatabaseException{
        ICustomerSubsystem customer = new CustomerSubsystemFacade();
        customer.initializeCustomer(custId);
        SessionContext context = SessionContext.INSTANCE;
        context.add(CustomerConstants.LOGGED_IN, Boolean.TRUE);
        context.add(CustomerConstants.CUSTOMER, customer); 
    }
    
    private void authenticate(String id, String pwd) {
        try {
            //authenticate
            Login login = new Login(id,pwd);
            DbClassLogin dbClass = new DbClassLogin(login);
            boolean authenticated = dbClass.authenticate();
        
            //if authenticated, load customer subsystem
            if(authenticated){
                loadCustomer(id);
                JOptionPane.showMessageDialog(loginWindow,                                                    
                        "Login successful",
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);                
        	    
            }
            else {
                throw new UserException("Either id or password is incorrect.");
 
            }
        }
        catch(EBazaarException e){
            JOptionPane.showMessageDialog(loginWindow,                                                    
                        "Error: "+e.getMessage(),
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                
            loginWindow.setVisible(true);
        }
    }
    //////// event handling code
    
    public SubmitListener getSubmitListener(LoginWindow w) {
        return new SubmitListener();
    }
    public CancelListener getCancelListener(LoginWindow w) {
        return new CancelListener();
    }
    
	class SubmitListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            loginWindow.setVisible(false);
        	String id = loginWindow.getCustId();
        	String pwd = loginWindow.getPassword();
       	    authenticate(id,pwd);
       	    loginWindow.dispose();
       	    if(controller != null){
       	    	Boolean loggedIn = (Boolean)SessionContext.INSTANCE.get(CustomerConstants.LOGGED_IN);
       	    	if(loggedIn==Boolean.TRUE) controller.doUpdate();
       	    	else parentWindow.setVisible(true);
       	    }
       	    else {
       	    	Boolean loggedIn = (Boolean)SessionContext.INSTANCE.get(CustomerConstants.LOGGED_IN);
       	    	if(loggedIn==Boolean.TRUE) currWindow.setVisible(true);
       	    	else parentWindow.setVisible(true);
       	    	
       	    }   	    
        }
	}
	class CancelListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        	
        	if(parentWindow != null) {
        		parentWindow.setVisible(true);
        	}
        	loginWindow.dispose();

        }
	}
    

}


