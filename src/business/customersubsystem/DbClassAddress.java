
package business.customersubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import business.externalinterfaces.*;
import middleware.DatabaseException;
import middleware.DbConfigProperties;
import middleware.dataaccess.*;

import middleware.externalinterfaces.*;

/**
 * @author pcorazza
 * @since Nov 13, 2004
 * Class Description:
 * 
 * 
 */
class DbClassAddress implements IDbClass {
    ICustomerProfile customerProfile;
    private IAddress address;
    private List<IAddress> addressList;
    private Address defaultShipAddress;
    private Address defaultBillAddress;
    private String queryType;
    private String query;
    private IDataAccessSubsystem dataAccess;
    
    private final String SAVE = "Save";
    private final String READ = "Read";
    private final String READ_DEFAULT_BILL = "ReadDefaultBill";
    private final String READ_DEFAULT_SHIP = "ReadDefaultShip";
    private final String STREET="street";
    private final String CITY = "city";
    private final String STATE = "state";
    private final String ZIP = "zip";
 
    public void saveAddress(CustomerProfile customerProfile) throws DatabaseException {
        this.customerProfile = customerProfile;
        queryType = SAVE;
        dataAccess = DataAccessSubsystemFacade.INSTANCE;
        dataAccess.save(this);
    }
    
    public void buildQuery() throws DatabaseException {
        if(queryType.equals(SAVE)){
            buildSaveNewAddrQuery();
        }
        else if(queryType.equals(READ)){
            buildReadAllAddressesQuery();
        }
        else if(queryType.equals(READ_DEFAULT_BILL)){
            buildReadDefaultBillQuery();
        }
        else if(queryType.equals(READ_DEFAULT_SHIP)){
            buildReadDefaultShipQuery();
        }       
    }
    
    IAddress getAddress() {
        return address;
    }
    List<IAddress> getAddressList() {
        return addressList;
    }
    Address getDefaultShipAddress(){
        return this.defaultShipAddress;
    }
    Address getDefaultBillAddress() {
        return this.defaultBillAddress;
    }
    void readDefaultShipAddress(CustomerProfile customerProfile) throws DatabaseException {
        this.customerProfile = customerProfile;
        queryType=READ_DEFAULT_SHIP;
        dataAccess = DataAccessSubsystemFacade.INSTANCE;
        dataAccess.read(this);   
    }
    void readDefaultBillAddress(CustomerProfile customerProfile) throws DatabaseException {
        this.customerProfile = customerProfile;
        queryType=READ_DEFAULT_BILL;
        dataAccess = DataAccessSubsystemFacade.INSTANCE;
        dataAccess.read(this);   
    }    
    void readAllAddresses(CustomerProfile customerProfile) throws DatabaseException {
        this.customerProfile = customerProfile;
        queryType = READ;
        dataAccess = DataAccessSubsystemFacade.INSTANCE;
        dataAccess.read(this);    
        
    }
    
        
    void setAddress(IAddress addr){
        address = addr;
    }
    void buildSaveNewAddrQuery() throws DatabaseException {
    	String addressid = DataAccessUtil.getNextAvailAddressId();
        query = "INSERT into AltShipAddress "+
        		"(addressid, custid,street,city,state,zip) " +
        		"VALUES('"+addressid+"','" +customerProfile.getCustId()+"','"+
        				  address.getStreet1()+"','"+
        				  address.getCity()+"','"+
        				  address.getState()+"','"+
        				  address.getZip()+"')";
    }
    void buildReadAllAddressesQuery() {
        query = "SELECT * from AltShipAddress";
    }
    void buildReadDefaultBillQuery() {
        query = "SELECT billaddress1, billaddress2, billcity, billstate, billzipcode "+
                "FROM Customer "+
                "WHERE custid = '"+customerProfile.getCustId()+"'";
    }
    void buildReadDefaultShipQuery() {
        query = "SELECT shipaddress1, shipaddress2, shipcity, shipstate, shipzipcode "+
        "FROM Customer "+
        "WHERE custid = '"+customerProfile.getCustId()+"'";
    }
    
    public String getDbUrl() {
    	DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
        
    }
    
    public String getQuery() {
        return query;
        
    }
    
    void populateAddressList(ResultSet rs) throws DatabaseException {
        addressList = new LinkedList<IAddress>();
        if(rs != null){
            try {
                while(rs.next()) {
                    address = new Address();
                    String str = rs.getString(STREET);
                    address.setStreet1(str);
                    address.setCity(rs.getString(CITY));
                    address.setState(rs.getString(STATE));
                    address.setZip(rs.getString(ZIP));
                    addressList.add(address);
                }
                
            }
            catch(SQLException e){
                throw new DatabaseException(e);
            }
            
        }
        
    }
    
    void populateDefaultShipAddress(ResultSet rs) throws DatabaseException {
        try {
            if(rs.next()){
                defaultShipAddress = new Address(rs.getString("shipaddress1"),
                                                 rs.getString("shipaddress2"),
                                                 rs.getString("shipcity"),
                                                 rs.getString("shipstate"),
                                                 rs.getString("shipzipcode"));
            }
            
        }
        catch(SQLException e) {
            throw new DatabaseException(e);
        }
        
    }
    void populateDefaultBillAddress(ResultSet rs) throws DatabaseException {
        try {
            if(rs.next()){
                defaultBillAddress = new Address(rs.getString("billaddress1"),
                                                 rs.getString("billaddress2"),
                                                 rs.getString("billcity"),
                                                 rs.getString("billstate"),
                                                 rs.getString("billzipcode"));
            }
            
        }
        catch(SQLException e) {
            throw new DatabaseException(e);
        }
               
    }
    /* used only when we read from the database
     */
    public void populateEntity(ResultSet rs) throws DatabaseException {
        if(queryType.equals(READ)){
            populateAddressList(rs);
        }
        else if(queryType.equals(READ_DEFAULT_SHIP)){
            populateDefaultShipAddress(rs);
        }
        else if(queryType.equals(this.READ_DEFAULT_BILL)){
            populateDefaultBillAddress(rs);
        }
    }
    public static void main(String[] args){
        DbClassAddress dba = new DbClassAddress();
        try {
            dba.readAllAddresses(new CustomerProfile("1", "Joe","Smith"));
            System.out.println(dba.getAddressList());
        }
        catch(DatabaseException e){
            e.printStackTrace();
        }
    }
 
}
