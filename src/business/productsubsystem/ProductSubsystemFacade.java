/*
 * Created on Mar 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package business.productsubsystem;

import java.util.List;

import business.DbClassQuantity;
import business.Quantity;
import business.externalinterfaces.IProductFromDb;
import business.externalinterfaces.IProductFromGui;
import business.externalinterfaces.IProductSubsystem;
import business.util.TwoKeyHashMap;
import middleware.DatabaseException;

public class ProductSubsystemFacade implements IProductSubsystem {

	public TwoKeyHashMap<String, String, IProductFromDb> getProductTable()
			throws DatabaseException {
		DbClassProduct dbClass = new DbClassProduct();
		return dbClass.readProductTable();

	}

	/* reads quantity avail and stores in the Quantity argument */
	public void readQuantityAvailable(String prodName, Quantity quantity)
			throws DatabaseException {
		DbClassQuantity dbclass = new DbClassQuantity();
		dbclass.setQuantity(quantity);
		dbclass.readQuantityAvail(prodName);

	}

	/** creates an IProductFromGui when user creates a product */
	public IProductFromGui createProduct(String productName,
			String mfgDate, String quantity, String unitPrice) {
		Product p = new Product(productName, mfgDate, quantity, unitPrice);
		return p;
	}

	// note: don't invoke rules for quantity from here

	public void saveNewProduct(IProductFromGui product, String catalogType)
			throws DatabaseException {

		DbClassProduct dbClass = new DbClassProduct();
		dbClass.saveProductData(product, catalogType);

	}

	public void saveNewCatalogName(String name) throws DatabaseException {
		DbClassProduct dbClass = new DbClassProduct();
		dbClass.buildSaveCatalogQuery(name);
	}

	public IProductFromDb getProductFromId(String prodId) throws DatabaseException {
		return (new DbClassProduct().getProductFromId(prodId));
	}

	public IProductFromDb getProductFromName(String prodName) throws DatabaseException{
		return (new DbClassProduct().getProductFromString(prodName));
	}
	
	public String getProductIdFromName(String prodName) throws DatabaseException{
		return (new DbClassProduct().getProductIdFromName(prodName));
	}
	
	public IProductFromDb getProduct(String prodName) throws DatabaseException {
		return (new DbClassProduct().getProductFromString(prodName));
	}
	
	public List<IProductFromDb> getProductList(String catType) throws DatabaseException {
		return (new DbClassProduct().getProductList(catType));
	}
	
	/** like getProductList, but forces a database read */
	public List<IProductFromDb> refreshProductList(String catType) throws DatabaseException {
		return (new DbClassProduct().refreshProductList(catType));
	}
	
	public List<String[]> refreshCatalogNames() throws DatabaseException{
		return(new DbClassProduct().refreshCatalogNames());
	}
	
	public List<String[]> getCatalogNames() throws DatabaseException {
		return (new DbClassProduct().getCatalogNames());
	}
	
	public TwoKeyHashMap<String,String,IProductFromDb> refreshProductTable() throws DatabaseException{
		return (new DbClassProduct().refreshProductTable());
	}
}