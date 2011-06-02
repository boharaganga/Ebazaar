package business.ordersubsystem;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import middleware.DatabaseException;
import middleware.dataaccess.DataAccessUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import application.LoginControl;
import application.gui.LoginWindow;
import business.customersubsystem.CustomerSubsystemFacade;
import business.externalinterfaces.ICustomerProfile;
import business.externalinterfaces.ICustomerSubsystem;
import business.externalinterfaces.IOrder;
import business.externalinterfaces.IOrderItem;

public class DbClassOrderTest {

	Order order;
	OrderItem item1;
	OrderItem item2;
	List<IOrderItem> orderItems;
	ICustomerSubsystem customerSs;
	ICustomerProfile customerProfile;

	@Before
	public void setUp() throws Exception {
		order = new Order("99", "06/02/2011", "100");
		customerSs = new CustomerSubsystemFacade();
		customerSs.initializeCustomer("1");
		customerProfile = customerSs.getCustomerProfile();
	}

	@After
	public void tearDown() throws Exception {
		order = null;
		customerSs = null;
		customerProfile = null;

	}

	@Test
	public final void testSubmitOrder() throws DatabaseException {
		order.setBillingAddress(customerSs.getDefaultBillAddress());
		order.setShippingAddress(customerSs.getDefaultShipAddress());
		order.setPaymentInfo(customerSs.getDefaultPaymentInfo());

		DbClassOrder dbClassOrder = new DbClassOrder(order, customerProfile);
		dbClassOrder.submitOrder();

		DbClassOrder ord = new DbClassOrder();
		IOrder retOrder = ord.getOrderData("99");
		String expected = retOrder.getOrderId();
		String actual = order.getOrderId();
		assertEquals(expected, actual);
	}

}
