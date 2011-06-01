package customer.test.junit;

import java.util.List;

import middleware.DatabaseException;

import business.customersubsystem.CustomerSubsystemFacade;
import business.externalinterfaces.IOrder;
import junit.framework.TestCase;

public class CustomerSubsystemFacadeTest extends TestCase {
	
	public void testGetOrderHistory() throws DatabaseException {
		CustomerSubsystemFacade facade = new CustomerSubsystemFacade();
		facade.initializeCustomer("1");
		List<IOrder> orders = facade.getOrderHistory();
		assertEquals(0, orders.size());
	}
	
}
