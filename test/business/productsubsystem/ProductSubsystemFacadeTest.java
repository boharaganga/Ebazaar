package business.productsubsystem;

import static org.junit.Assert.*;

import java.util.List;

import middleware.DatabaseException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import business.externalinterfaces.IProductFromDb;
import business.externalinterfaces.IProductFromGui;

public class ProductSubsystemFacadeTest {

	ProductSubsystemFacade psf;
	private String catalogType = "Books";
	IProductFromGui ipg;
	String catalogName = "phones";
	String proName = "testProduct";

	@Before
	public void setUp() throws Exception {
		psf = new ProductSubsystemFacade();
		ipg = new Product(proName, "06-02-11", "5", "200");
		psf.saveNewProduct(ipg, catalogType);
	}

	@After
	public void tearDown() throws Exception {
		psf.deleteProduct(proName);
		psf = null;
	}

	@Test
	public void testSaveNewCatalogName() {
		// fail("Not yet implemented");
		try {
			
			psf.saveNewCatalogName(catalogName);
			List<String[]> catalogs = psf.refreshCatalogNames();
			
			String savedName = null;
			for(String[] s:catalogs) {
				if(s[0].equals(catalogName)){
					savedName = s[0];
					break;
				}
			}
			assertEquals(catalogName, savedName);

		} catch (DatabaseException e) {
			fail("Could not write to database");
		}

	}
	
	
	
	@Test
	public void testGetProductList() {
		//fail ("Not yet implemented");
		try {
			List<IProductFromDb> productList = psf.getProductList(catalogType);
			for(IProductFromDb pro:productList){
				if(pro.getProductName().equals(proName)){
					assertEquals(proName, pro.getProductName());
				}
			}
			
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			fail("Data not found. Check the connection");
		}
	}

}
