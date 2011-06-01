package business.ordersubsystem;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;
import middleware.DatabaseException;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.dataaccess.DataAccessUtil;
import business.externalinterfaces.ICartItem;
import business.externalinterfaces.ICustomerProfile;
import business.externalinterfaces.IOrder;
import business.externalinterfaces.IOrderItem;
import business.externalinterfaces.IOrderSubsystem;
import business.externalinterfaces.IShoppingCart;

public class OrderSubsystemFacade implements IOrderSubsystem {

	Logger log = Logger.getLogger(this.getClass().getPackage().getName());
	ICustomerProfile customerProfile;

	public OrderSubsystemFacade(ICustomerProfile customerProfile) {
		this.customerProfile = customerProfile;
	}

	List<String> getAllOrderIds() throws DatabaseException {
		// finished implementing
		return new DbClassOrder().getAllOrderIds(customerProfile);

	}

	List<IOrderItem> getOrderItems(String orderId) throws DatabaseException {
		// need to implement -- finished
		return new DbClassOrder().getOrderItems(orderId);
	}

	IOrder getOrderData(String orderId) throws DatabaseException {
		// need to implement -- finished
		return new DbClassOrder().getOrderData(orderId);
	}

	@Override
	public List<IOrder> getOrderHistory() throws DatabaseException {
		// implement -- finished
		List<String> orderIds = getAllOrderIds();
		List<IOrder> orders = new ArrayList<IOrder>();
		for (String id : orderIds) {
			orders.add(new DbClassOrder().getOrderData(id));
		}
		return orders;

	}

	@Override
	public void submitOrder(IShoppingCart shopCart) throws DatabaseException {
		// finished implementing
		String orderId = DataAccessUtil.getNextAvailOrderId();
		List<IOrderItem> orderItems = new ArrayList<IOrderItem>();
		double totalPrice = 0;
		for (ICartItem cartItem : shopCart.getCartItems()) {
			orderItems.add(OrderUtil.createOrderItemFromCartItem(orderId,
					cartItem));
			totalPrice += Double.parseDouble(cartItem.getTotalprice());
		}
		Order order = new Order(orderId, new GregorianCalendar().getTime()
				.toString(), Double.toString(totalPrice));
		order.setBillingAddress(shopCart.getBillingAddress());
		order.setShippingAddress(shopCart.getShippingAddress());
		order.setPaymentInfo(shopCart.getPaymentInfo());
		order.setOrderItems(orderItems);
		new DbClassOrder(order, customerProfile).submitOrder();
		for (IOrderItem orderItem : order.getOrderItems()) {
			new DbClassOrderItem(orderItem).submitOrderItem();
		}
	}

	@Override
	public IOrderItem createOrderItem(String prodId, String orderId,
			String quantityReq, String totalPrice) throws DatabaseException {
		// implementation finished
		return new OrderItem(
				DataAccessUtil.getNextAvailOrderItemId(), prodId, orderId,
				quantityReq, totalPrice);
	}

}
