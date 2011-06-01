package business.externalinterfaces;

import java.util.List;

/**
 * This interface should list all the features (in the form of public methods)
 * of the shopping cart that other parts of the application will need to access
 */
public interface IShoppingCart {

	void add(ICartItem cartItem);

	String getCustomerId();

	void setCustomerId(String customerId);

	List<ICartItem> getCartItems();

	void setCartItems(List<ICartItem> cartItems);

	IAddress getShippingAddress();

	void setShippingAddress(IAddress shippingAddress);

	IAddress getBillingAddress();

	void setBillingAddress(IAddress billingAddress);
	
	ICreditCard getPaymentInfo();
	
	void setPaymentInfo(ICreditCard paymentInfo);

}
