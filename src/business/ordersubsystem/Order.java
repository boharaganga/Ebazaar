/*
 * @author Rajesh
 */
package business.ordersubsystem;

import java.util.List;

import business.externalinterfaces.IAddress;
import business.externalinterfaces.ICreditCard;
import business.externalinterfaces.IOrder;
import business.externalinterfaces.IOrderItem;

class Order implements IOrder {
	private String orderId;
	private String orderDate;
	private String totalPrice;
	private IAddress billingAddress;
	private IAddress shippingAddress;
	private ICreditCard paymentInfo;
	private List<IOrderItem> orderItems;

	Order(String orderId, String orderDate, String totalPrice) {

		this.orderId = orderId;
		this.orderDate = orderDate;
		this.totalPrice = totalPrice;
	}

	public String getOrderId() {
		return orderId;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public IAddress getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(IAddress billingAddress) {
		this.billingAddress = billingAddress;
	}

	public IAddress getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(IAddress shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public ICreditCard getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(ICreditCard paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	public List<IOrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<IOrderItem> orderItems) {
		this.orderItems = orderItems;
	}

}
