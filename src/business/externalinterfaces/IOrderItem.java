
package business.externalinterfaces;


public interface IOrderItem {
    public String getLineitemid();
    
    public String getProductid();
    public String getOrderid();
    public String getQuantity();
    public String getTotalPrice();

}
