package business.productsubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.border.EmptyBorder;

import middleware.DatabaseException;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.externalinterfaces.DbConfigKey;
import middleware.externalinterfaces.IDataAccessSubsystem;
import middleware.externalinterfaces.IDbClass;
import business.externalinterfaces.IProductFromDb;
import business.externalinterfaces.IProductFromGui;
import business.util.TwoKeyHashMap;

class DbClassProduct implements IDbClass {
	private IDataAccessSubsystem dataAccess;
	/**
	 * the productTable matches product ID with Product object. It is static so
	 * that requests for "read product" based on product ID can be handled
	 * without extra db hits
	 */
	private static TwoKeyHashMap<String, String, IProductFromDb> productTable;
	private List<IProductFromDb> productList = new ArrayList<IProductFromDb>();
	private List<String[]> catalogNames = new ArrayList<String[]>();

	private String queryType;
	private String query;

	private final String LOAD_PROD_TABLE = "LoadProdTable";
	private final String LOAD_PROD_FROM_ID = "LoadProdIds";
	private final String LOAD_PROD_FROM_NAME = "LoadProdFromName";
	private final String LOAD_PROD_ID_FROM_NAME = "LoadProdIdFromName";
	private final String GET_PROD_LIST = "LoadProdList";
	private final String SAVE_PROD_TABLE = "SaveProdTable";
	private final String SAVE_CAT_TABLE = "SaveCatTable";
	private final String GET_CAT_NAMES = "GetCatNames";
	private final String GET_CAT_ID_FROM_NAME = "GetIdFromName";
	private final String DELETE_PRODUCT = "DeleteProduct";
	private final String DELETE_PRODUCT_WITH_CATALOGID = "DeleteProductWithCatalogId";
	private final String DELETE_CATALOG = "DeleteCatalog";
	private final String dELETE_CATALOG_NAME = "DeleteCatName";
	private int productId;
	private String catalogId;
	private String catalogType;

	// Product related variables
	private String name;
	private String quantity;
	private String price;
	private String date;
	private String desc;
	private int catid;

	// Catalog related variables
	private String catalogName;
	private String productName;
	private String productNameToSearch;

	IProductFromDb productFromDb;
	private String prodectIdFromName;

	public static TwoKeyHashMap<String, String, IProductFromDb> getProductTable() {
		return productTable;
	}

	/**
	 * We are using this class also for saving, adding and deleting the Catalogs
	 * other than products Generally this is not a good Idea and we could have
	 * Created another class "DbClassCatalog.java" in order to perform the
	 * database operations with Catalogs
	 */
	public void buildQuery() {
		if (queryType.equals(LOAD_PROD_TABLE)) {
			buildProdTableQuery();
		}

		if (queryType.equals(LOAD_PROD_FROM_ID)) {
			buildProdFromIdQuery();
		}

		if (queryType.equals(LOAD_PROD_FROM_NAME)) {
			buildProdFromNameQuery();
		}

		if (queryType.equals(LOAD_PROD_ID_FROM_NAME)) {
			buildProdIdFromNameQuery();
		}

		if (queryType.equals(SAVE_PROD_TABLE)) {
			buildSaveProductQuery();
		}

		if (queryType.equals(SAVE_CAT_TABLE)) {
			buildSaveCatalogQuery();
		}

		if (queryType.equals(GET_PROD_LIST)) {
			buildGetProdListQuery();
		}

		if (queryType.equals(GET_CAT_NAMES)) {
			buildGetCatNamesQuery();
		}
		if (queryType.equals(GET_CAT_ID_FROM_NAME)) {
			buildGetCatIdFromNameQuery();
		}
		
		if (queryType.equals(DELETE_PRODUCT)) {
			buildDeleteProductQuery();
		}
		
		if (queryType.equals(DELETE_CATALOG)) {
			buildDeleteCatalogQuery();
		}

		if(queryType.equals(DELETE_PRODUCT_WITH_CATALOGID)) {
			buildDeleteProductWithCatalogIdQuery();
		}
	}
	
    private void buildDeleteProductWithCatalogIdQuery() {
		query = "DELETE  from Product WHERE catalogid = '"+catalogId+ "'";	
	}

	private void buildDeleteCatalogQuery() {
		query = "DELETE from CatalogType WHERE catalogname = '"+catalogName+ "'";
	}

	private void buildDeleteProductQuery() {
		query = "DELETE FROM Product WHERE productname = '"+ productName + "'"; 
	}

	private void buildGetCatIdFromNameQuery(){
    	query = "SELECT * FROM CatalogType WHERE catalogname = '"+catalogName+"'";
    }
	
	private void buildGetCatNamesQuery() {
		query = "SELECT * FROM CatalogType";
	}

	private void buildGetProdListQuery() {
		query = "SELECT * FROM CatalogType as c INNER JOIN  Product as p ON c.catalogid =  p.catalogid WHERE catalogname='"
				+ catalogType + "'";
	}

	private void buildSaveCatalogQuery() {
		// TODO Auto-generated method stub
		query = "INSERT INTO CatalogType values('','" + catalogName + "')";
	}

	private void buildSaveProductQuery() {
		// TODO Auto-generated method stub
		query = "INSERT INTO Product values(''," + catid + ",'" + name + "',"
				+ quantity + "," + price + ",'" + date + "','" + desc + "')";

	}

	private void buildProdFromNameQuery() {
		query = "SELECT * from Product where productname = '" + this.productName+ "'";
	}

	private void buildProdFromIdQuery() {
		query = "SELECT * from Product where productid = " + this.productId;
	}

	private void buildProdIdFromNameQuery() {
		query = "SELECT * from Product where productname = '"
				+ this.productNameToSearch + "'";
	}
	
	
	
	public void deleteProductFromCatalogId(String catalogId) throws DatabaseException{
		queryType = DELETE_PRODUCT_WITH_CATALOGID;
		dataAccess = DataAccessSubsystemFacade.INSTANCE;
		this.catalogId = catalogId;
		dataAccess.delete(this);
	}
	
    /**
     * 
     * @param catalogName
     * @return
     */
	public boolean deleteCatalog(String catalogName) throws DatabaseException {
		
		dataAccess = DataAccessSubsystemFacade.INSTANCE;
		this.catalogName = catalogName;
		
		// get all the products related with catalogs and delete first
		getCatalogIdFromCatalogName(catalogName);
		
		// Also delete all the products with the catalog ID
		deleteProductFromCatalogId(this.catalogId);
		deleteCatalogFromName(this.catalogName);
		return true;
	}

	
	private void deleteCatalogFromName(String catalogName) throws DatabaseException {
		queryType = DELETE_CATALOG;
		this.catalogName = catalogName;
		dataAccess = DataAccessSubsystemFacade.INSTANCE;
		dataAccess.delete(this);
		
	}

	/**
	 * 
	 * @param productName
	 * @return
	 * @throws DatabaseException
	 */
	public boolean deleteProduct(String productName) throws DatabaseException{
		queryType = DELETE_PRODUCT;
		dataAccess = DataAccessSubsystemFacade.INSTANCE;
		this.productName = productName;
		dataAccess.delete(this);
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public List<String[]> getCatalogNames() throws DatabaseException {
		if (catalogNames.isEmpty()) {
			queryType = GET_CAT_NAMES;
			dataAccess = DataAccessSubsystemFacade.INSTANCE;
			dataAccess.read(this);
			return catalogNames;
		}
		return catalogNames;
	}

	/**
	 * 
	 * @return
	 * @throws DatabaseException
	 */
	public List<String[]> refreshCatalogNames() throws DatabaseException {
		queryType = GET_CAT_NAMES;
		dataAccess = DataAccessSubsystemFacade.INSTANCE;
		dataAccess.read(this);
		return catalogNames;
	}

	/**
	 * 
	 * @param catType
	 * @return
	 * @throws DatabaseException
	 */
	public List<IProductFromDb> refreshProductList(String catType)
			throws DatabaseException {
		queryType = GET_PROD_LIST;
		this.catalogType = catType;
		dataAccess = DataAccessSubsystemFacade.INSTANCE;
		dataAccess.read(this);
		return productList;
	}

	/**
	 * 
	 * @param catType
	 * @return
	 */
	public List<IProductFromDb> getProductList(String catType)
			throws DatabaseException {
		if ((productList.isEmpty())) {
			queryType = GET_PROD_LIST;
			this.catalogType = catType;
			dataAccess = DataAccessSubsystemFacade.INSTANCE;
			dataAccess.read(this);
			return productList;
		}
		return productList;
	}

	/**
	 * Saves the Catalog information to the database
	 * 
	 * @param name
	 */
	public void buildSaveCatalogQuery(String catalogName) {
		queryType = SAVE_CAT_TABLE;
		this.catalogName = catalogName;
		dataAccess = DataAccessSubsystemFacade.INSTANCE;
		try {
			dataAccess.save(this);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * Get Catalog ID from catalog Name
	 */
	public void getCatalogIdFromCatalogName(String catalogName){
		this.catalogName = catalogName;
		queryType = GET_CAT_ID_FROM_NAME;
		dataAccess = DataAccessSubsystemFacade.INSTANCE;
		try {
			dataAccess.read(this);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves the product information to the database
	 * 
	 * @param pro
	 * @param str
	 */
	public void saveProductData(IProductFromGui pro, String str) {
		this.name = pro.getProductName();
		this.quantity = pro.getQuantityAvail();
		this.price = pro.getUnitPrice();
		this.date = pro.getMfgDate();
		this.desc = pro.getDescription();
		getCatalogIdFromCatalogName(str);
		this.catid = Integer.parseInt(catalogId);
		queryType = SAVE_PROD_TABLE;

		dataAccess = DataAccessSubsystemFacade.INSTANCE;
		try {
			dataAccess.save(this);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param prodName
	 * @return
	 * @throws DatabaseException
	 */
	public String getProductIdFromName(String prodName)
			throws DatabaseException {
		this.queryType = LOAD_PROD_ID_FROM_NAME;
		this.productNameToSearch = prodName;
		DataAccessSubsystemFacade.INSTANCE.read(this);
		return prodectIdFromName;
	}

	/**
	 * 
	 * @param prodId
	 * @return
	 * @throws DatabaseException
	 */
	public IProductFromDb getProductFromId(String prodId)
			throws DatabaseException {
		this.queryType = LOAD_PROD_FROM_ID;
		this.productId = Integer.parseInt(prodId);
		DataAccessSubsystemFacade.INSTANCE.read(this);
		return productFromDb;
	}

	/**
	 * 
	 * @param prodName
	 * @return
	 * @throws DatabaseException
	 */
	public IProductFromDb getProductFromString(String prodName)
			throws DatabaseException {
		this.queryType = LOAD_PROD_FROM_NAME;
		this.productName = prodName;
		DataAccessSubsystemFacade.INSTANCE.read(this);
		return productFromDb;
	}

	private void buildProdTableQuery() {
		query = "SELECT * FROM Product";
	}

	public TwoKeyHashMap<String, String, IProductFromDb> readProductTable()
			throws DatabaseException {
		if (productTable != null) {
			return productTable.clone();
		}
		return refreshProductTable();
	}

	/**
	 * 
	 * @return
	 * @throws DatabaseException
	 */
	public TwoKeyHashMap<String, String, IProductFromDb> refreshProductTable()
			throws DatabaseException {
		queryType = LOAD_PROD_TABLE;
		dataAccess = DataAccessSubsystemFacade.INSTANCE;
		dataAccess.read(this);
		// return a clone since productTable must not be corrupted
		return productTable.clone();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * middleware.externalinterfaces.IDbClass#populateEntity(java.sql.ResultSet)
	 */
	public void populateEntity(ResultSet resultSet) throws DatabaseException {
		if (queryType.equals(LOAD_PROD_TABLE)) {
			populateProdTable(resultSet);
		} else if (queryType.equals(LOAD_PROD_FROM_ID)) {
			populateProdFromId(resultSet);
		} else if (queryType.equals(LOAD_PROD_FROM_NAME)) {
			populateProdFromName(resultSet);
		} else if (queryType.equals(LOAD_PROD_ID_FROM_NAME)) {
			populateProdIdFromName(resultSet);
		} else if (queryType.equals(GET_PROD_LIST)) {
			populateProdList(resultSet);
		} else if (queryType.equals(GET_CAT_NAMES)) {
			populateCatalogList(resultSet);
		} else if (queryType.equals(GET_CAT_ID_FROM_NAME)) {
			populateCatalogListFromID(resultSet);
		}

	}

	
	/**
	 * 
	 */
	private void populateCatalogListFromID(ResultSet rs) throws DatabaseException {
		try {

			while (rs.next()) {
				String[] catalogs = new String[2];
				catalogs[0] = rs.getString("catalogid");
				catalogs[1] = rs.getString("catalogname");
				catalogId = catalogs[0];
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	
	/**
	 * 
	 * @param rs
	 * @throws DatabaseException
	 */
	private void populateCatalogList(ResultSet rs) throws DatabaseException {
		catalogNames = new ArrayList<String[]>();
		try {

			while (rs.next()) {
				String[] catalogs = new String[2];
				catalogs[0] = rs.getString("catalogname");
				catalogs[1] = rs.getString("catalogid");
				catalogNames.add(catalogs);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	/**
	 * 
	 * @param rs
	 * @throws DatabaseException
	 */
	private void populateProdList(ResultSet rs) throws DatabaseException {
		productList = new ArrayList<IProductFromDb>();
		try {
			while (rs.next()) {
				String productid = rs.getString("productid");
				String catalogid = rs.getString("catalogid");
				String productname = rs.getString("productname");
				String totalquantity = rs.getString("totalquantity");
				String priceperunit = rs.getString("priceperunit");
				String mfgdate = rs.getString("mfgdate");
				String description = rs.getString("description");
				
				productList.add(new Product(productid, productname,
						totalquantity, priceperunit, mfgdate, catalogid,
						description));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}

	}

	private void populateProdIdFromName(ResultSet rs) throws DatabaseException {
		try {
			while (rs.next()) {
				prodectIdFromName = rs.getString("productid");
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}

	}

	private void populateProdFromName(ResultSet rs) throws DatabaseException {
		try {
			while (rs.next()) {
				String productid = rs.getString("productid");
				String catalogid = rs.getString("catalogid");
				String productname = rs.getString("productname");
				String totalquantity = rs.getString("totalquantity");
				String priceperunit = rs.getString("priceperunit");
				String mfgdate = rs.getString("mfgdate");
				String description = rs.getString("description");
				//System.out.println(productid);

				productFromDb = new Product(productid, productname,
						totalquantity, priceperunit, mfgdate, catalogid,
						description);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	/**
	 * 
	 * @param rs
	 * @throws DatabaseException
	 */
	private void populateProdFromId(ResultSet rs) throws DatabaseException {
		try {
			while (rs.next()) {
				String productid = rs.getString("productid");
				String catalogid = rs.getString("catalogid");
				String productname = rs.getString("productname");
				String totalquantity = rs.getString("totalquantity");
				String priceperunit = rs.getString("priceperunit");
				String mfgdate = rs.getString("mfgdate");
				String description = rs.getString("description");
				System.out.println(productid);

				productFromDb = new Product(productid, productname,
						totalquantity, priceperunit, mfgdate, catalogid,
						description);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DatabaseException(e);
		}

	}

	private void populateProdTable(ResultSet rs) throws DatabaseException {
		productTable = new TwoKeyHashMap<String, String, IProductFromDb>();
		// implement
		try {
			while (rs.next()) {
				String productid = rs.getString("productid");
				String catalogid = rs.getString("catalogid");
				String productname = rs.getString("productname");
				String totalquantity = rs.getString("totalquantity");
				String priceperunit = rs.getString("priceperunit");
				String mfgdate = rs.getString("mfgdate");
				String description = rs.getString("description");
				//System.out.println(productid);

				Product p = new Product(productid, productname, totalquantity,
						priceperunit, mfgdate, catalogid, description);
				productTable.put(productid, productname, p);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DatabaseException(e);
		}

	}

	public String getDbUrl() {
		DbConfigProperties props = new DbConfigProperties();
		return props.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());

	}

	public String getQuery() {

		return query;
	}

	

}