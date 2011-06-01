package business.shoppingcartsubsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import middleware.DatabaseException;
import middleware.dataaccess.DataAccessSubsystemFacade;
import business.externalinterfaces.IAddress;
import business.externalinterfaces.ICartItem;
import business.externalinterfaces.ICreditCard;
import business.externalinterfaces.ICustomerProfile;
import business.externalinterfaces.IProductFromDb;
import business.externalinterfaces.IProductSubsystem;
import business.externalinterfaces.IShoppingCart;
import business.externalinterfaces.IShoppingCartSubsystem;
import business.productsubsystem.ProductSubsystemFacade;
import business.util.TwoKeyHashMap;

public enum ShoppingCartSubsystemFacade implements IShoppingCartSubsystem {
	// This makes ShoppingCartSubsystemFacade a singleton (see Effective Java
	// 2nd ed.). This approach ensures that the current state of the shopping
	// cart is globally accessible. This is important to do because
	// at certain times (i.e. after login) this subsystem is under the
	// control of Customer Subsystem, and at other times (i.e. during browse and
	// select) it is not.
	INSTANCE;
	ShoppingCart liveCart;
	ShoppingCart savedCart;
	String shopCartId;
	ICustomerProfile customerProfile;
	Logger log = Logger.getLogger(this.getClass().getPackage().getName());
		

	// interface methods
	public void setCustomerProfile(ICustomerProfile customerProfile) {
		this.customerProfile = customerProfile;
	}

	// supporting methods

	String getShoppingCartId() throws DatabaseException {
		// implement -- read database; pass in customerProfile
		// so that the sql query can extract custId in order to
		// find this customer's shopping cart id
		// finished
return new DbClassShoppingCart().getShoppingCartId(customerProfile
				.getCustId());
		// return "1";

	}

	/**
	 * 
	 * @param shopCartId
	 * @return
	 * @throws DatabaseException
	 */
	List<ICartItem> getCartItems(String shopCartId) throws DatabaseException {
		DbClassShoppingCart sc = new DbClassShoppingCart();
		return sc.getSavedCartItems(shopCartId);
	}

	private ShoppingCartSubsystemFacade() {
		liveCart = new ShoppingCart(new ArrayList<ICartItem>());
		savedCart = new ShoppingCart(new ArrayList<ICartItem>());
	}

	@Override
	public IShoppingCartSubsystem retrieveSavedCart(ICustomerProfile custProfile)
			throws DatabaseException {
		setCustomerProfile(custProfile);
		String val = getShoppingCartId();
		if (val != null) {
			shopCartId = val;
			log.info("cart id: " + shopCartId);
			List<ICartItem> items = getCartItems(shopCartId);
			log.info("list of items: " + items);
			savedCart = new ShoppingCart(items);
		}
		return this;
	}

	@Override
	public void addCartItem(String itemName, String quantity, String totalPrice)
			throws DatabaseException {
		IProductSubsystem pss = new ProductSubsystemFacade();
		TwoKeyHashMap<String, String, IProductFromDb> hash = pss
				.getProductTable();
		IProductFromDb pFromDb = hash.getValWithSecondKey(itemName);
		String productId = pFromDb.getProductId();
		ICartItem cartItem = new CartItem(shopCartId, productId, "", quantity,
				totalPrice, false);
		liveCart.add(cartItem);
	}

	@Override
	public List<ICartItem> getLiveCartItems() {
		return liveCart.getCartItems();
	}

	@Override
	public void setShippingAddress(IAddress addr) {
		liveCart.setShippingAddress(addr);

	}

	@Override
	public void setBillingAddress(IAddress addr) {
		liveCart.setBillingAddress(addr);
	}

	@Override
	public void setPaymentInfo(ICreditCard cc) {
		throw new UnsupportedOperationException(
				"Credit Card payment system not yet implemented.");
	}

	@Override
	public IShoppingCart getLiveCart() {
		return liveCart;
	}

	@Override
	public void makeSavedCartLive() {
		liveCart = savedCart;

	}

	@Override
	public void saveLiveCart() throws DatabaseException {
		DbClassShoppingCart sc = new DbClassShoppingCart(liveCart);
		// TODO save sc
	}
	
}
