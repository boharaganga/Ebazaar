
package business.externalinterfaces;
import java.util.List;

import business.RuleException;
import middleware.DatabaseException;
import middleware.EBazaarException;

public interface ICustomerSubsystem {
    public void initializeCustomer(String id) throws DatabaseException;
    public IAddress createAddress(String street, String city, String state, String zip);
    public void saveNewAddress(IAddress addr) throws DatabaseException;
    public List<IAddress> getAllAddresses() throws DatabaseException;
    public List<IOrder> getOrderHistory();
    public IAddress runAddressRules(IAddress addr) throws RuleException, EBazaarException; 
    public ICustomerProfile getCustomerProfile();

}
