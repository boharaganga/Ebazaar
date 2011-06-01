/*
 * Created on Mar 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package business.externalinterfaces;

import java.util.List;

import middleware.DatabaseException;
import business.Quantity;
import business.util.TwoKeyHashMap;

public interface IProductSubsystem {
	//public TwoKeyHashMap<String,String,IProductFromDb> getProductTable() throws DatabaseException;
	//public void readQuantityAvailable(String prodName, Quantity quantity) throws DatabaseException;
	/** retrieves a twokey hashmap that consists of all products keyed on both name and id */
	public TwoKeyHashMap<String,String,IProductFromDb> getProductTable() throws DatabaseException;


	/** same as getProductTable but forces a database read */
	public TwoKeyHashMap<String,String,IProductFromDb> refreshProductTable() throws DatabaseException;

     
	/** retrieves list of catalog names; if this value is already in memory, it is returned from memory */
	public List<String[]> getCatalogNames() throws DatabaseException;

     
	/** retrieves list getCatalogNames, but forces a database read */
	public List<String[]> refreshCatalogNames() throws DatabaseException;
     
     
	/** gets a list of products from the database, based on category type; if list is already in memory then
	 * database read is avoided */
	public List<IProductFromDb> getProductList(String catType) throws DatabaseException;
    
     
	/** like getProductList, but forces a database read */
	public List<IProductFromDb> refreshProductList(String catType) throws DatabaseException;

     
	/** convenience method to obtain product id for a given product name */
	public String getProductIdFromName(String prodName) throws DatabaseException;

     
	/** reads the product object from the database using the product name using a database hit*/
	public IProductFromDb getProduct(String prodName) throws DatabaseException;
	
	
	/** reads the product from the productid, using a database hit */
	public IProductFromDb getProductFromId(String prodId) throws DatabaseException;
	
	
	
	/** saves newly created catalog */
	public void saveNewCatalogName(String name) throws DatabaseException;

     
	/** creates an IProductFromGui when user creates a product */
	public IProductFromGui createProduct(String name, String date, String numAvail, String unitPrice);

     
	/** saves a new product obtained from user input */
	public void saveNewProduct(IProductFromGui product, String catalogType) throws DatabaseException;


}
