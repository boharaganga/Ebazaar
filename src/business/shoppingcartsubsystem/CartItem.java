
package business.shoppingcartsubsystem;

import business.externalinterfaces.ICartItem;


public class CartItem implements ICartItem {
    String cartid;
    String productid;
    String lineitemid;
    String quantity;
    String totalprice;
    //this is true if this cart item is data that has come from
    //database
    boolean alreadySaved;
    public CartItem(String cartid, 
                    String productid, 
                    String lineitemid, 
                    String quantity, 
                    String totalprice,
                    boolean alreadySaved){
        this.cartid = cartid;
        this.productid= productid;
        this.lineitemid = lineitemid;
        this.quantity = quantity;
        this.totalprice =totalprice;
        this.alreadySaved = alreadySaved;
    }
    public String toString(){
        StringBuffer buf = new StringBuffer();
        buf.append("cartid = <"+cartid+">,");
        buf.append("productid = <"+productid+">,");
        buf.append("lineitemid = <"+lineitemid+">,");
        buf.append("quantity = <"+quantity+">,");
        buf.append("totalprice = <"+totalprice+">");
        buf.append("alreadySaved = <"+alreadySaved+">");
        return buf.toString();
    }
	public boolean isAlreadySaved() {
		return alreadySaved;
	}
	public String getCartid() {
		return cartid;
	}
	public String getLineitemid() {
		return lineitemid;
	}
	public String getProductid() {
		return productid;
	}
	public String getQuantity() {
		return quantity;
	}
	public String getTotalprice() {
		return totalprice;
	}
}
