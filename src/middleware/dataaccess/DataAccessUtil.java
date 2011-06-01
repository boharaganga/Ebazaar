
package middleware.dataaccess;

import java.sql.Connection;
import java.sql.ResultSet;
import middleware.DatabaseException;
import middleware.DbConfigProperties;
import middleware.externalinterfaces.DbConfigKey;

/**
 * @author pcorazza
 * @since Nov 10, 2004
 * Class Description:
 * 
 * 
 */
public class DataAccessUtil {
	static DbConfigProperties props = new DbConfigProperties();
	static String productsDburl = props.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());
	static String accountsDburl = props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
	public static ResultSet runQuery(String dbUrl, String query) throws DatabaseException {
        System.out.println("query = "+query);
        SimpleConnectionPool pool = getPool();
        Connection con = pool.getConnection(dbUrl);
        ResultSet rs = SimpleConnectionPool.doQuery(con,query);
        pool.returnToPool(con,dbUrl);
        return rs;
        
    }
	private static SimpleConnectionPool getPool() throws DatabaseException {
		return SimpleConnectionPool.getInstance(
	            props.getProperty(DbConfigKey.DB_USER.getVal()), 
	            props.getProperty(DbConfigKey.DB_PASSWORD.getVal()), 
	            props.getProperty(DbConfigKey.DRIVER.getVal()),
	            Integer.parseInt(props.getProperty(DbConfigKey.MAX_CONNECTIONS.getVal())));
	    
	}
    public static void runUpdate(String dbUrl, String query) throws DatabaseException {
        SimpleConnectionPool pool = getPool();        	
        Connection con = pool.getConnection(dbUrl);
        SimpleConnectionPool.doUpdate(con,query);  
        pool.returnToPool(con,dbUrl);
        
        
        
       
    }
    
    /// utilities for getting next id in various tables
    final static String custQuery =  "SELECT DISTINCT custid "+
	   								"FROM Customer ";
    final static String addressQuery =  "SELECT DISTINCT addressid "+
		"FROM altshipaddress ";
    final static String shopCartQuery  = "SELECT DISTINCT cartid "+
	   									"FROM ShopCartTbl ";
    final static String cartItemQuery =  "SELECT DISTINCT cartitemid "+
											"FROM ShopCartItem ";
    final static String orderQuery =  "SELECT DISTINCT orderid "+
											"FROM Ord ";
    final static String orderItemQuery =  "SELECT DISTINCT orderitemid "+
    								"FROM OrderItem ";
    
   	final static String productIdQuery =  
   	    "SELECT DISTINCT productid "+
   	    "FROM Product ";	
   	
   	final static String catalogIdQuery =  
   	    "SELECT DISTINCT catalogid "+
   	    "FROM CatalogType ";
    
   	private static String getNextId(String dburl, String theQuery, String idname) throws DatabaseException {
        SimpleConnectionPool pool = getPool();
        Connection con = pool.getConnection(dburl);
        ResultSet rs = SimpleConnectionPool.doQuery(con, theQuery);  
        pool.returnToPool(con,dburl);       		   
        Integer nextVal = null;
        Integer maxVal = new Integer(0);
        try {
            while(rs.next()){
                nextVal = new Integer(rs.getString(idname));
                if(nextVal.compareTo(maxVal) > 0){
                    maxVal = nextVal;
                }
            }
            
        }
        catch(Exception e){
            //do nothing
        }
        return succ(maxVal).toString();
   		
   	}
   	
    public static String getNextProductId(String theQuery, String idname) throws DatabaseException {
    	return getNextId(productsDburl, theQuery,idname);
    }	

 
    public static String getNextAvailCatalogId() throws DatabaseException {
    	return getNextProductId(catalogIdQuery, "catalogid" );
    }
    public static String getNextAvailProductId() throws DatabaseException {
    	    return getNextProductId(productIdQuery,"productid");
    }	

    
    public static String getNextAvailAccountId(String theQuery, String idname) throws DatabaseException {
    	DbConfigProperties props = new DbConfigProperties();	
    	String dburl = props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
    	return getNextId(dburl, theQuery, idname);
    }
    public static String getNextAvailCartItemId() throws DatabaseException {
        return getNextAvailAccountId(cartItemQuery,"cartitemid");
    }
    public static String getNextAvailOrderId() throws DatabaseException{
        return getNextAvailAccountId(orderQuery,"orderid");
    }  
    public static String getNextAvailOrderItemId() throws DatabaseException{
        return getNextAvailAccountId(orderItemQuery,"orderitemid");
    }     
    public static String getNextAvailCustId() throws DatabaseException{
        return getNextAvailAccountId(custQuery,"custid");
    }
    public static String getNextAvailAddressId() throws DatabaseException{
        return getNextAvailAccountId(addressQuery,"addressid");
    }
    public static String getNextAvailShopCartId() throws DatabaseException{
        return getNextAvailAccountId(shopCartQuery,"cartid");
        
    }
    public static void main(String[] args){
        try {
        System.out.println(getNextAvailCustId());
        System.out.println(getNextAvailShopCartId());
        System.out.println(getNextAvailCartItemId());
        System.out.println(getNextAvailOrderId());
        System.out.println(getNextAvailOrderItemId());
        System.out.println(getNextAvailProductId());
        }
        catch(DatabaseException e){}
    }
    
    public static Integer succ(Integer integer){
        return new Integer(integer.intValue()+1);
    }
    
    public static String succ(String integerString){
        Integer successor = succ(new Integer(integerString));
        return successor.toString();
    }
    /* Private constructor ensures the class is not instantiated from outside */
    private DataAccessUtil(){}
     
}
