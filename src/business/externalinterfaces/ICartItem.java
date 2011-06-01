package business.externalinterfaces;

public interface ICartItem {
	public boolean isAlreadySaved();
	public String getCartid();
	public String getLineitemid();
	public String getProductid();
	public String getQuantity();
	public String getTotalprice();
}
