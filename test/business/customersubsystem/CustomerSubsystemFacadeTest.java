package business.customersubsystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import middleware.DatabaseException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import business.externalinterfaces.IOrder;

public class CustomerSubsystemFacadeTest {

	private CustomerSubsystemFacade facade;
	
	@Before
	public void setUp() throws Exception {
		facade = new CustomerSubsystemFacade();
		facade.initializeCustomer("1");
	}

	@After
	public void tearDown() throws Exception {
		facade = null;
	}

	@Test
	public void testGetOrderHistory() {
		List<IOrder> orders = facade.getOrderHistory();
		assertEquals(1, orders.size());
	}

	@Test
	public void testGetCustomerProfile() {
		CustomerProfile customer=facade.getCustomerProfile();
		assertEquals("1", customer.getCustId());
		assertEquals("John", customer.getFirstName());
		assertEquals("Smith", customer.getLastName());
	}

}
