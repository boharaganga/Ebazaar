package business.externalinterfaces;

import java.util.List;

public interface IOrder {

	String getOrderDate();

	String getOrderId();

	String getTotalPrice();

	IAddress getBillingAddress();
	
	void setBillingAddress(IAddress billingAddress);

	IAddress getShippingAddress();

	void setShippingAddress(IAddress shippingAddress);

	ICreditCard getPaymentInfo();

	void setPaymentInfo(ICreditCard paymentInfo);

	List<IOrderItem> getOrderItems();

	void setOrderItems(List<IOrderItem> orderItems);

}
