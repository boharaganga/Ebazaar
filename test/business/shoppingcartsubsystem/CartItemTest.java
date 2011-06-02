package business.shoppingcartsubsystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CartItemTest {
	CartItem ct;

	@Before
	public void setUp() throws Exception {
		String cartid = "21";
		String productid = "5";
		String lineitemid = "6";
		String quantity = "20";
		String totalprice = "22";
		boolean alreadySaved = true;
		ct = new CartItem(cartid, productid, lineitemid, quantity, totalprice,
				alreadySaved);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetCartId() {
		assertEquals("21", ct.getCartid());
	}
	
	@Test
	public void testGetProductid(){
		assertEquals("5", ct.getProductid());
	}

}
