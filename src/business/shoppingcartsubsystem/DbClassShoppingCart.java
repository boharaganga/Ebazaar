package business.shoppingcartsubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import middleware.DatabaseException;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.dataaccess.DataAccessUtil;
import middleware.externalinterfaces.DbConfigKey;
import middleware.externalinterfaces.IDataAccessSubsystem;
import middleware.externalinterfaces.IDbClass;
import business.externalinterfaces.IAddress;
import business.externalinterfaces.ICartItem;
import business.externalinterfaces.IShoppingCart;

public class DbClassShoppingCart implements IDbClass {
	IDataAccessSubsystem dataAccess;
	IShoppingCart cart;
	List<ICartItem> cartItemsList;
	String custId;
	String cartId;
	String query;
	final String GET_ID = "GetId";
	final String GET_SAVED_ITEMS = "GetSavedItems";
	final String SAVE_LIVE_CART = "SaveLiveCart";
	String queryType;

	public DbClassShoppingCart() {
	}

	public DbClassShoppingCart(IShoppingCart cart) {
		this.cart = cart;
	}

	public void buildQuery() {
		if (queryType.equals(GET_ID)) {
			buildGetIdQuery();
		} else if (queryType.equals(GET_SAVED_ITEMS)) {
			buildGetSavedItemsQuery();
		} else if (queryType.equals(SAVE_LIVE_CART)) {
			buildSaveLiveCartQuery();
		}

	}

	private void buildSaveLiveCartQuery() {
		// implement
		// for shipping address
		IAddress sadr = cart.getShippingAddress();

		String shipingAddress1 = null;
		String shppingAddress2 = null;
		String shippingCity = null;
		String shippingState = null;
		String shippingZip = null;
		shipingAddress1 = sadr.getStreet1();
		shppingAddress2 = sadr.getStreet2();
		shippingCity = sadr.getCity();
		shippingState = sadr.getState();
		shippingZip = sadr.getZip();

		// for billing address
		IAddress badr = cart.getBillingAddress();
		String billingAddress1 = null;
		String billingAddress2 = null;
		String billingCity = null;
		String billingState = null;
		String billingZip = null;
		billingAddress1 = badr.getStreet1();
		billingAddress2 = badr.getStreet2();
		billingCity = badr.getCity();
		billingState = badr.getState();
		billingZip = badr.getZip();

		// calculating prices
		double totalPriceAmount = 0;

		for (ICartItem cit : cart.getCartItems()) {
			String tp = cit.getTotalprice();
			double tpp = Double.parseDouble(tp);
			totalPriceAmount += tpp;
		}

		// }

		query = "INSERT INTO ShopCartTbl VALUES(" + cartId + "," + custId + ","
				+ shipingAddress1 + "," + shppingAddress2 + "," + shippingCity
				+ "," + shippingState + "," + shippingZip + ","
				+ billingAddress1 + "," + billingAddress2 + "," + billingCity
				+ "," + billingState + "," + billingZip + "," + null + ","
				+ null + "," + null + "," + null + "," + totalPriceAmount + ","
				+ 0.0 + "," + 0.0 + "," + totalPriceAmount + ") ";
	}

	private void buildGetIdQuery() {
		query = "SELECT shopcartid FROM ShopCartTbl WHERE custid = '" + custId
				+ "';";
	}

	private void buildGetSavedItemsQuery() {
		// implement
		query = "SELECT cartitemid, productid, quantity, totalprice "
				+ "FROM ShopCartItem " + "where shopcartid='" + cartId + "';";

	}

	public String getShoppingCartId(String custId) throws DatabaseException {
		this.custId = custId;
		// implement
		this.queryType = GET_ID;
		DataAccessSubsystemFacade.INSTANCE.read(this);
		return cartId;
	}

	public void saveCart() throws DatabaseException {
		// implement
		this.queryType = SAVE_LIVE_CART;
		DataAccessSubsystemFacade.INSTANCE.save(this);
	}

	public List<ICartItem> getSavedCartItems(String cartId)
			throws DatabaseException {
		this.cartId = cartId;
		queryType = GET_SAVED_ITEMS;
		// implement
		this.queryType = GET_SAVED_ITEMS;
		DataAccessSubsystemFacade.INSTANCE.read(this);
		return cartItemsList;
		// return new LinkedList<ICartItem>();

	}

	public void populateEntity(ResultSet resultSet) throws DatabaseException {
		if (queryType.equals(GET_ID)) {
			populateShopCartId(resultSet);
		} else if (queryType.equals(GET_SAVED_ITEMS)) {
			populateCartItemsList(resultSet);
		}

	}

	private void populateShopCartId(ResultSet rs) throws DatabaseException {
		// implement
		if (rs != null) {
			try {
				while (rs.next()) {
					cartId = rs.getString("shopcartid");
				}
			} catch (SQLException e) {
				throw new DatabaseException(e);
			}
		}

	}

	private void populateCartItemsList(ResultSet rs) throws DatabaseException {
		// implement
		cartItemsList = new LinkedList<ICartItem>();
		if (rs != null) {
			try {
				while (rs.next()) {
					String cartitemid = rs.getString("cartitemid");
					String productid = rs.getString("productid");
					String quantity = rs.getString("quantity");
					String totalPrice = rs.getString("totalprice");
					cartItemsList.add(new CartItem(cartitemid, productid, "12",
							quantity, totalPrice, true));
				}
			} catch (SQLException e) {
				throw new DatabaseException(e);
			}
		}

	}

	public String getDbUrl() {
		DbConfigProperties props = new DbConfigProperties();
		return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
	}

	public String getQuery() {
		return query;
	}

}
