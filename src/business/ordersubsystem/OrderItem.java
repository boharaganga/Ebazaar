package business.ordersubsystem;

import business.externalinterfaces.IOrderItem;

class OrderItem implements IOrderItem {
	String lineitemid;
	String productid;
	String orderid;
	String quantity;
	String totalPrice;

	OrderItem(String lineitemid, String productid, String orderid,
			String quantity, String totalPrice) {
		this.lineitemid = lineitemid;
		this.productid = productid;
		this.orderid = orderid;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("lineitemid: <" + lineitemid + ">,");
		buf.append("productid: <" + productid + ">,");
		buf.append("orderid: <" + orderid + ">,");
		buf.append("quantity: <" + quantity + ">,");
		buf.append("totalPrice: <" + totalPrice + ">");
		return buf.toString();
	}

	public String getLineitemid() {
		return lineitemid;
	}

	public String getProductid() {
		return productid;
	}

	public String getOrderid() {
		return orderid;
	}

	public String getQuantity() {
		return quantity;
	}

	public String getTotalPrice() {
		return totalPrice;
	}
}
