package application;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import middleware.DatabaseException;

import application.BrowseAndSelectController.SelectProductListener;
import application.gui.CustomTableModel;
import application.gui.EbazaarMainFrame;
import application.gui.SelectOrderWindow;
import application.gui.ViewOrderDetailsWindow;
import business.SessionContext;
import business.externalinterfaces.CustomerConstants;
import business.externalinterfaces.ICustomerProfile;
import business.externalinterfaces.ICustomerSubsystem;
import business.externalinterfaces.IOrder;
import business.externalinterfaces.IOrderItem;
import business.externalinterfaces.IOrderSubsystem;
import business.externalinterfaces.IProductFromDb;
import business.externalinterfaces.IProductSubsystem;
import business.ordersubsystem.OrderSubsystemFacade;
import business.productsubsystem.ProductSubsystemFacade;

/**
 * @author pcorazza
 */
public enum ViewOrdersController implements CleanupControl {
	// This makes ManageProductsController a Singleton. Since controllers
	// monitor the states of the windows under their control, they
	// need to be singletons. This style for implementing the Singleton
	// pattern is explained in Effective Java, 2nd ed.
	INSTANCE;

	// ///////// EVENT HANDLERS -- new code goes here ////////////

	// // control SelectOrderWindow
	class ViewOrderDetailsListener implements ActionListener {
		final String ERROR_MESSAGE = "Please select a row.";
		final String ERROR = "Error";
		private Logger log = Logger.getLogger(ViewOrderDetailsListener.class
				.getName());

		public void actionPerformed(ActionEvent evt) {
			JTable table = selectOrderWindow.getTable();
			CustomTableModel model = selectOrderWindow.getModel();
			int selectedRow = table.getSelectedRow();
			if (selectedRow >= 0) {
				// start by reading order id from screen
				selectOrderWindow.setVisible(false);
				@SuppressWarnings("unused")
				String selOrderId = (String) model.getValueAt(selectedRow, 0);
				// now get customer from SessionContext, getOrderHistory
				// and then read the appropriate order from the history, using
				// order id
				ICustomerSubsystem customerSs = (ICustomerSubsystem) SessionContext.INSTANCE
						.get(CustomerConstants.CUSTOMER);
				ICustomerProfile customerProfile = customerSs
						.getCustomerProfile();
				OrderSubsystemFacade orderSs = new OrderSubsystemFacade(
						customerProfile);

				try {
					List<IOrder> orders = orderSs.getOrderHistory();
					IOrder selectedOrder = null;
					for (IOrder ord : orders) {
						if (ord.getOrderId().equals(selOrderId)) {
							selectedOrder = ord;
							break;
						}
					}
					List<IOrderItem> orderItems = selectedOrder.getOrderItems();
					List<String[]> orderItemsAsString = new ArrayList<String[]>();
					for (IOrderItem orderItem : orderItems) {
						String productId = orderItem.getProductid();
						IProductSubsystem pss = new ProductSubsystemFacade();
						IProductFromDb product = pss
								.getProductFromId(productId);
						String productName = product.getProductName();
						String unitPrice = product.getUnitPrice();
						String quantity = orderItem.getQuantity();
						String totalPrice = orderItem.getTotalPrice();
						orderItemsAsString.add(new String[] { productName,
								unitPrice, quantity, totalPrice });
					}

					selectOrderWindow.setVisible(false);
					viewOrderDetailsWindow = new ViewOrderDetailsWindow();
					viewOrderDetailsWindow.updateModel(orderItemsAsString);
					viewOrderDetailsWindow.setVisible(true);
				} catch (DatabaseException e) {
					log.warning(e.getMessage());
					JOptionPane.showMessageDialog(selectOrderWindow,
							"Database connection error", ERROR,
							JOptionPane.ERROR_MESSAGE);
				}

			} else {
				JOptionPane.showMessageDialog(selectOrderWindow, ERROR_MESSAGE,
						ERROR, JOptionPane.ERROR_MESSAGE);

			}

		}
	}

	class CancelViewOrdersListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			selectOrderWindow.setVisible(false);
			BrowseAndSelectController.INSTANCE.makeMainFrameVisible();

		}
	}

	// /// control of ViewOrderDetailsWindow
	class OrderDetailsOkListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			selectOrderWindow.setVisible(true);

			viewOrderDetailsWindow.dispose();
		}
	}

	class SelectOrderActionListener implements ActionListener, Controller {

		final String ERROR_MESSAGE = "Problem connecting to database to fetch orders.";
		final String ERROR = "Error";
		private Logger log = Logger.getLogger(ViewOrderDetailsListener.class
				.getName());


		/*
		 * this method is called when LoginControl needs this class to load
		 * order history data for newly logged in customer
		 */
		public void doUpdate() {
			if (selectOrderWindow != null) {
				// implement by reading order history from customer
				// customer should be available in SessionContext
				ICustomerSubsystem customerSs = (ICustomerSubsystem) SessionContext.INSTANCE
						.get(CustomerConstants.CUSTOMER);
				ICustomerProfile customerProfile = customerSs
						.getCustomerProfile();
				IOrderSubsystem orderSs = new OrderSubsystemFacade(
						customerProfile);
				try {
					List<IOrder> orders = orderSs.getOrderHistory();
					List<String[]> modelDataList = new ArrayList<String[]>();
					for (IOrder order : orders) {
						String[] orderAsStringArray = { order.getOrderId(),
								order.getOrderDate(), order.getTotalPrice() };
						modelDataList.add(orderAsStringArray);
					}
					selectOrderWindow.updateModel(modelDataList);

				} catch (DatabaseException e) {
					log.warning(e.getMessage());
					JOptionPane.showMessageDialog(selectOrderWindow,
							ERROR_MESSAGE, ERROR, JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		public void actionPerformed(ActionEvent e) {
			SessionContext ctx = SessionContext.INSTANCE;
			Boolean loggedIn = (Boolean) ctx.get(CustomerConstants.LOGGED_IN);
			ICustomerSubsystem ss = (ICustomerSubsystem) ctx
					.get(CustomerConstants.CUSTOMER);
			if (!loggedIn.booleanValue()) {
				selectOrderWindow = new SelectOrderWindow();
				LoginControl loginControl = new LoginControl(selectOrderWindow,
						mainFrame);
				loginControl.startLogin();
				doUpdate();
			} else {

				// default implementation, runs when user has logged in
				selectOrderWindow = new SelectOrderWindow();
				doUpdate();
				selectOrderWindow.setVisible(true);
				mainFrame.setVisible(false);
			}
		}
	}

	// /////// PUBLIC INTERFACE -- for getting instances of listeners ///
	public ActionListener getViewOrderDetailsListener(SelectOrderWindow w) {
		return (new ViewOrderDetailsListener());
	}

	public ActionListener getCancelViewOrdersListener(SelectOrderWindow w) {
		return (new CancelViewOrdersListener());
	}

	public ActionListener getOrderDetailsOkListener(ViewOrderDetailsWindow w) {
		return (new OrderDetailsOkListener());
	}

	public ActionListener getSelectOrderActionListener(EbazaarMainFrame f) {
		return (new SelectOrderActionListener());
	}

	// ////// PUBLIC ACCESSORS to register screens controlled by this class////
	public void setSelectOrderWindow(SelectOrderWindow w) {
		selectOrderWindow = w;
	}

	public void setViewOrderDetailsWindow(ViewOrderDetailsWindow w) {
		viewOrderDetailsWindow = w;
	}

	public void setMainFrame(EbazaarMainFrame w) {
		mainFrame = w;
	}

	// ///// screens -- private references
	private SelectOrderWindow selectOrderWindow;
	private ViewOrderDetailsWindow viewOrderDetailsWindow;
	private EbazaarMainFrame mainFrame;
	private Window[] allWindows = { selectOrderWindow, viewOrderDetailsWindow,
			mainFrame };

	public void cleanUp() {
		for (Window w : allWindows) {
			if (w != null) {
				System.out.println("Disposing of window "
						+ w.getClass().getName());
				w.dispose();
			}
		}
	}
}
