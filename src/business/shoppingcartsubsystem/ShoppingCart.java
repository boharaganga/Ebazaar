package business.shoppingcartsubsystem;

import java.util.LinkedList;
import java.util.List;

import business.externalinterfaces.IAddress;
import business.externalinterfaces.ICartItem;
import business.externalinterfaces.ICreditCard;
import business.externalinterfaces.IShoppingCart;

public class ShoppingCart implements IShoppingCart {

	private String customerId;
	private List<ICartItem> cartItems;
	private IAddress shippingAddress;
	private IAddress billingAddress;
	private ICreditCard paymentInfo;

	public ShoppingCart(List<ICartItem> cartItems) {
		if (cartItems == null)
			this.cartItems = new LinkedList<ICartItem>();
		else
			this.cartItems = cartItems;
	}
	
	public ShoppingCart(String customerId, List<ICartItem> cartItems) {
		this(cartItems);
		this.customerId = customerId;
	}
	
	public void add(ICartItem cartItem) {
		this.cartItems.add(cartItem);
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public List<ICartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<ICartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public IAddress getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(IAddress shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public IAddress getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(IAddress billingAddress) {
		this.billingAddress = billingAddress;
	}

	public ICreditCard getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(ICreditCard creditCard) {
		this.paymentInfo = creditCard;
	}
	
	

}
