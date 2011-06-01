
package business.customersubsystem;


import business.externalinterfaces.IAddress;


/**
 * @author Peter
 * @since Nov 4, 2004
 * Class Description:
 * 
 * 
 */
class Address implements IAddress{
    Address() {}
    Address(String street, String city, String state, String zip){
        this.street1 = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }
    Address(String street1, String street2, String city, String state, String zip){
        this.street1 = street1;
        this.street2 = street2;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }    
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String zip;
    /**
     * @return Returns the city.
     */
    public String getCity() {
        return city;
    }
    /**
     * @param city The city to set.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return Returns the state.
     */
    public String getState() {
        return state;
    }
    /**
     * @param state The state to set.
     */
    public void setState(String state) {
        this.state = state;
    }
    /**
     * @return Returns the street1.
     */
    public String getStreet1() {
        return street1;
    }
    /**
     * @param street1 The street1 to set.
     */
    public void setStreet1(String street1) {
        this.street1 = street1;
    }
    /**
     * @return Returns the street2.
     */
    public String getStreet2() {
        return street2;
    }
    /**
     * @param street2 The street2 to set.
     */
    public void setStreet2(String street2) {
        this.street2 = street2;
    }
    /**
     * @return Returns the zip.
     */
    public String getZip() {
        return zip;
    }
    /**
     * @param zip The zip to set.
     */
    public void setZip(String zip) {
        this.zip = zip;
    }
    
    public String toString() {
        String n = System.getProperty("line.separator");
        StringBuffer sb = new StringBuffer();
        sb.append("Street1: "+street1+n);
        sb.append("Street2: "+street2+n);
        sb.append("City: "+city+n);
        sb.append("State: "+state+n);
        sb.append("Zip: "+zip+n);
        return sb.toString();
    }
}
