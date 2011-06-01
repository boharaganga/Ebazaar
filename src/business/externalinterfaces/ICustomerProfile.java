
package business.externalinterfaces;


public interface ICustomerProfile {
    public String getFirstName();
    public String getLastName();
    public String getCustId();
    public void setFirstName(String fn);
    public void setLastName(String ln);
    public void setCustId(String id);
}
