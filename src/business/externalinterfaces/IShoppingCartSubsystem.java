package business.externalinterfaces;

import java.util.List;

import middleware.DatabaseException;

public interface IShoppingCartSubsystem {
	/**
	 * used during customer login to cache this customer's saved cart --
	 * performs a database access through data access subsystem
	 */
	
	public IShoppingCartSubsystem retrieveSavedCart(ICustomerProfile custProfile)
			throws DatabaseException;

	/**
	 * used when a user adds an item to shopping cart -- it creates a new cart
	 * item and adds to the list of cart items stored in the shopping cart
	 * subsystem's live cart
	 */
	public void addCartItem(String itemName, String quantity, String totalPrice)
			throws DatabaseException;

	/**
	 * used to display items currently in live shopping cart (for instance, when
	 * an item is added to cart and the CartItemsWindow is about to be
	 * displayed). the method returns the list of cart items currently stored in
	 * the live cart, sitting in the shopping cart subsystem facade
	 */
	public List<ICartItem> getLiveCartItems();

	/**
	 * accessor used by customer subsystem to store user's selected ship address
	 * during checkout; stores value in shop cart facade
	 */
	public void setShippingAddress(IAddress addr);

	/**
	 * accessor used by customer subsystem to store user's selected ship address
	 * during checkout; stores value in shop cart facade
	 */
	public void setBillingAddress(IAddress addr);

	/**
	 * accessor used by customer subsystem to store user's selected ship address
	 * during checkout; stores value in shop cart facade
	 */
	public void setPaymentInfo(ICreditCard cc);

	/**
	 * Used during order submission when order subsystem prepares an order from
	 * the shopping cart.
	 * 
	 * @return
	 */
	public IShoppingCart getLiveCart();

	/**
	 * used when user choose the option to 'retrieve saved cart' -- which
	 * requires that the customer's saved cart be stored in the live cart in the
	 * shopping cart subsystem facade
	 */
	public void makeSavedCartLive();

	public void saveLiveCart() throws DatabaseException;

	public void setCustomerProfile(ICustomerProfile customerProfile);
}