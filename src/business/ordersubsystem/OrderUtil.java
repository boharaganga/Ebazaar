package business.ordersubsystem;

import business.externalinterfaces.ICartItem;
import business.externalinterfaces.IOrderItem;

public class OrderUtil {

	public static IOrderItem createOrderItemFromCartItem(String orderId,
			ICartItem cartItem) {
		return new OrderItem(cartItem.getLineitemid(), cartItem.getProductid(),
				orderId, cartItem.getQuantity(), cartItem.getTotalprice());

	}

}
