package application;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import middleware.DatabaseException;
import middleware.EBazaarException;
import application.gui.CartItemsWindow;
import application.gui.CustomTableModel;
import application.gui.DefaultData;
import application.gui.FinalOrderWindow;
import application.gui.PaymentWindow;
import application.gui.ShipAddressesWindow;
import application.gui.ShippingBillingWindow;
import application.gui.TermsWindow;
import business.ParseException;
import business.RuleException;
import business.SessionContext;
import business.customersubsystem.CustomerSubsystemFacade;
import business.externalinterfaces.CustomerConstants;
import business.externalinterfaces.IAddress;
import business.externalinterfaces.ICreditCard;
import business.externalinterfaces.ICustomerProfile;
import business.externalinterfaces.ICustomerSubsystem;
import business.externalinterfaces.IProductFromGui;
import business.util.CustomerUtil;
import business.util.StringParse;

//import business.util.ShoppingCartUtil;

/**
 * @author pcorazza
 * 
 */
public enum CheckoutController implements CleanupControl {
	// This makes CheckoutController a Singleton. Since controllers
	// monitor the states of the windows under their control, they
	// need to be singletons. This style for implementing the Singleton
	// pattern is explained in Effective Java, 2nd ed.
	INSTANCE;
	Logger log = Logger.getLogger(this.getClass().getPackage().getName());
	private final String TERMS_MESSAGE_FILE = CustomerConstants.CURR_DIR
			+ "\\resources\\terms.txt";
	private final String GOODBYE_FILE = CustomerConstants.CURR_DIR
			+ "\\resources\\goodbye.txt";

	String extractGoodbyeMessage() throws ParseException {
		return StringParse.extractTextFromFile(GOODBYE_FILE);
	}

	String extractTermsText() throws ParseException {
		return StringParse.extractTextFromFile(TERMS_MESSAGE_FILE);
	}

	// ///////// EVENT HANDLERS -- new code goes here ////////////

	// /// control CartItemsWindow
	class ProceedToCheckoutListener implements ActionListener, Controller {

		public void doUpdate() {
			populateScreen();
		}

		/**
		 * This method populates the shipping billing window with the default
		 * address data for this customer. Retrieves the customer name and
		 * default shipping and billing addresses from customer subsystem
		 * interface. These values should have been loaded into the Customer
		 * subsystem at login.
		 */
		void populateScreen() {
			SessionContext context = SessionContext.INSTANCE;
			/*
			 * Get customer subsystem and read customer profile and default
			 * shipping and billing addresses here
			 */
			ICustomerSubsystem customer = (ICustomerSubsystem) context
					.get(CustomerConstants.CUSTOMER);
			ICustomerProfile customerProfile = customer.getCustomerProfile();
			IAddress defaultShipAddress = customer.getDefaultShipAddress();
			IAddress defaultBillAddress = customer.getDefaultBillAddress();
			/*
			 * Get default shipping and billing address here
			 */

			shippingBillingWindow = new ShippingBillingWindow();

			/*
			 * set default shipping and billing address info here
			 * shippingBillingWindow
			 * .setShippingAddress(custProfile.getFirstName() +
			 * " "+custProfile.getLastName(), defaultShipAddress.getStreet1(),
			 * defaultShipAddress.getCity(), defaultShipAddress.getState(),
			 * defaultShipAddress.getZip());
			 * shippingBillingWindow.setBillingAddress (custName.getFirstName()
			 * + " "+custName.getLastName(), defaultBillAddress.getStreet1(),
			 * defaultBillAddress.getCity(), defaultBillAddress.getState(),
			 * defaultBillAddress.getZip());
			 */

			shippingBillingWindow.setShippingAddress(
					customerProfile.getFirstName() + " "
							+ customerProfile.getLastName(),
					defaultShipAddress.getStreet1(),
					defaultShipAddress.getCity(),
					defaultShipAddress.getState(), defaultShipAddress.getZip());
			shippingBillingWindow.setBillingAddress(
					customerProfile.getFirstName() + " "
							+ customerProfile.getLastName(),
					defaultBillAddress.getStreet1(),
					defaultBillAddress.getCity(),
					defaultBillAddress.getState(), defaultBillAddress.getZip());

			shippingBillingWindow.setVisible(true);

		}

		public void actionPerformed(ActionEvent evt) {
			cartItemsWindow.setVisible(false);
			SessionContext ctx = SessionContext.INSTANCE;
			Boolean loggedIn = (Boolean) ctx.get(CustomerConstants.LOGGED_IN);
			if (!loggedIn.booleanValue()) {
				shippingBillingWindow = new ShippingBillingWindow();
				LoginControl loginControl = new LoginControl(
						shippingBillingWindow, cartItemsWindow, this);
				loginControl.startLogin();
			} else {
				populateScreen();
			}

		}
	}

	// // control ShippingBillingWindow

	class SelectShipButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			shippingBillingWindow.setVisible(false);
			ShipAddressesWindow shipAddrs = new ShipAddressesWindow();
			// //new code

			// get customer from SessionContext
			ICustomerSubsystem cust = (ICustomerSubsystem) SessionContext.INSTANCE
					.get(CustomerConstants.CUSTOMER);

			try {
				List<IAddress> addresses = cust.getAllAddresses();
				List<String[]> addressesAsArrays = CustomerUtil
						.addrListToListOfArrays(addresses);
				shipAddrs.updateModel(addressesAsArrays);
				shipAddrs.setVisible(true);
			} catch (DatabaseException e) {
				JOptionPane.showMessageDialog(shipAddressesWindow,
						"Unable to locate customer addresses", "Error",
						JOptionPane.ERROR_MESSAGE);

			}

			// //end new code

		}
	}

	class ProceedFromBillingCheckoutListener implements ActionListener {
		ICustomerSubsystem cust;
		boolean rulesOk = true;
		String fullname;

		public void actionPerformed(ActionEvent evt) {
			boolean rulesOk = true;
			IAddress cleansedAddr = null;
			shippingBillingWindow.setVisible(false);
			cust = (ICustomerSubsystem) SessionContext.INSTANCE
					.get(CustomerConstants.CUSTOMER);
			fullname = cust.getCustomerProfile().getFirstName() + " "
					+ cust.getCustomerProfile().getLastName();
			if (shippingBillingWindow.isNewShipAddress()) {

				String[] addrFlds = shippingBillingWindow
						.getShipAddressFields();

				IAddress addr = cust.createAddress(addrFlds[0], addrFlds[1],
						addrFlds[2], addrFlds[3]);

				// try {
				// cleansedAddr = cust.runAddressRules(addr);
				// cust.saveNewAddress(cleansedAddr);
				// } catch (RuleException e) {
				// rulesOk = false;
				// System.out.println(e.getMessage());
				// JOptionPane.showMessageDialog(shipAddressesWindow,
				// e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				// shippingBillingWindow.setVisible(true);
				// } catch (EBazaarException e) {
				// rulesOk = false;
				// JOptionPane
				// .showMessageDialog(
				// shipAddressesWindow,
				// "An error has occurred that prevents further processing",
				// "Error", JOptionPane.ERROR_MESSAGE);
				// shippingBillingWindow.setVisible(true);
				// }
				//
				// if (rulesOk) {
				// // do updates
				// shippingBillingWindow.setAddressFields(new String[] {
				// cleansedAddr.getStreet1(), cleansedAddr.getCity(),
				// cleansedAddr.getState(), cleansedAddr.getZip() });
				//
				// }
			}
			// load into shopping cart and set up payment window
			// if (rulesOk) {

			// load addresses into shopping cart
			String[] s = shippingBillingWindow.getShipAddressFields();
			String[] b = shippingBillingWindow.getBillAddressFields();
			IAddress shipAddr = cust.createAddress(s[0], s[1], s[2], s[3]);
			IAddress billAddr = cust.createAddress(b[0], b[1], b[2], b[3]);
			// cust.setBillingAddressInCart(billAddr);
			// cust.setShippingAddressInCart(shipAddr);

			setupPaymentWindow();
			// }
		}

		void setupPaymentWindow() {
			// get default payment info from customer object
			// ICreditCard cc = cust.getDefaultPaymentInfo();
			// String[] ccAsArray = CustomerUtil.creditCardToStringArray(cc);
			SessionContext context = SessionContext.INSTANCE;

			// ICustomerSubsystem cust = (ICustomerSubsystem)
			// SessionContext.INSTANCE
			// .get(CustomerConstants.CUSTOMER);
			ICustomerSubsystem cust = (ICustomerSubsystem) context
					.get(CustomerConstants.CUSTOMER);
			ICreditCard cc = cust.getDefaultPaymentInfo();
			String[] ccAsArray = CustomerUtil.creditCardToStringArray(cc);
			// String[] ccAsArray = new String[] { "name1", "num", "type",
			// "expir" };
			paymentWindow = new PaymentWindow();
			paymentWindow.setCredCardFields(ccAsArray[0], ccAsArray[1],
					ccAsArray[2], ccAsArray[3]);
			paymentWindow.setVisible(true);
			paymentWindow.setParentWindow(shippingBillingWindow);

		}
	}

	class BackToCartButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			cartItemsWindow.setVisible(true);
			shippingBillingWindow.setVisible(false);
		}
	}

	// // control ShipAddressesWindow
	class SelectAddressesListener implements ActionListener {
		final String ERROR_MESSAGE = "Please select a row.";
		final String ERROR = "Error";

		public void actionPerformed(ActionEvent evt) {
			JTable table = shipAddressesWindow.getTable();
			CustomTableModel model = shipAddressesWindow.getModel();
			int selectedRow = table.getSelectedRow();
			if (selectedRow >= 0) {
				shipAddressesWindow.setVisible(false);
				SessionContext context = SessionContext.INSTANCE;
				ICustomerSubsystem ss = (ICustomerSubsystem) context
						.get(CustomerConstants.CUSTOMER);
				// get cust name from customer subsystem -- for now we use fake
				// data

				String name = ss.getCustomerProfile().getFirstName() + " "
						+ ss.getCustomerProfile().getLastName();
				IAddress defaultBillAddress = ss.getDefaultBillAddress();
				if (shippingBillingWindow != null) {
//					shippingBillingWindow.setShippingAddress(name,
//							(String) model.getValueAt(selectedRow,
//									DefaultData.STREET_INT), (String) model
//									.getValueAt(selectedRow,
//											DefaultData.CITY_INT),
//							(String) model.getValueAt(selectedRow,
//									DefaultData.STATE_INT), (String) model
//									.getValueAt(selectedRow,
//											DefaultData.ZIP_INT));
					shippingBillingWindow.setShippingAddress(name,
							defaultBillAddress.getStreet1() + " "
									+ defaultBillAddress.getStreet2(),
							defaultBillAddress.getCity(),
							defaultBillAddress.getState(),
							defaultBillAddress.getZip());
					shippingBillingWindow.setVisible(true);
				}

			} else {
				JOptionPane.showMessageDialog(shipAddressesWindow,
						ERROR_MESSAGE, ERROR, JOptionPane.ERROR_MESSAGE);

			}

		}
	}

	class CancelShipAddrListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			shippingBillingWindow.setVisible(true);
			shipAddressesWindow.dispose();

		}
	}

	// // control PaymentWindow
	class ProceedFromPaymentListener implements ActionListener {
		ICustomerSubsystem cust = (ICustomerSubsystem) SessionContext.INSTANCE
				.get(CustomerConstants.CUSTOMER);

		public void actionPerformed(ActionEvent evt) {
			paymentWindow.setVisible(false);

			// check rules
			// if (false) {
			// display error message
			// }
			// rules passed, proceed
			// else {
			// create a credit card instance and set in shopping cart
			termsWindow = new TermsWindow();
			try {
				String termsText = extractTermsText();
				termsWindow.setTermsText(termsText);
				termsWindow.setVisible(true);

			} catch (ParseException e) {
				displayError(paymentWindow, e.getMessage());
				(new ApplicationCleanup()).cleanup();
				System.exit(0);
			}
			// }

		}
	}

	class BackToCartFromPayListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			paymentWindow.setVisible(false);
			cartItemsWindow.setVisible(true);
		}

	}

	// // controlTermsWindow
	class AcceptTermsListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			finalOrderWindow = new FinalOrderWindow();
			finalOrderWindow.setVisible(true);
			termsWindow.dispose();

		}
	}

	// // control FinalOrderWindow
	class SubmitFinalOrderListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			finalOrderWindow.setVisible(false);
			String msg = "Thank You for Shopping at the Ebazaar. "
					+ "We guarantee satisfaction and quality for our product.";

			JOptionPane.showMessageDialog(finalOrderWindow, msg,
					"E-Bazaar: Thank You", JOptionPane.PLAIN_MESSAGE);

			(new ApplicationCleanup()).cleanup();
			System.exit(0);
		}

	}

	class CancelFinalOrderListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			finalOrderWindow.setVisible(false);
			if (cartItemsWindow != null) {
				cartItemsWindow.setVisible(true);
			}
		}
	}

	// /////// PUBLIC INTERFACE -- for getting instances of listeners ///

	// CartItemsWindow
	public ActionListener getProceedToCheckoutListener(CartItemsWindow w) {
		return (new ProceedToCheckoutListener());
	}

	// ShippingBillingWindow
	public ActionListener getSelectShipButtonListener(ShippingBillingWindow w) {
		return (new SelectShipButtonListener());
	}

	public ActionListener getProceedFromBillingCheckoutListener(
			ShippingBillingWindow w) {
		return (new ProceedFromBillingCheckoutListener());
	}

	public ActionListener getBackToCartButtonListener(ShippingBillingWindow w) {
		return (new BackToCartButtonListener());
	}

	// ShipAddressesWindow

	public ActionListener getSelectAddressesListener(ShipAddressesWindow w) {
		return (new SelectAddressesListener());
	}

	public ActionListener getCancelShipAddrListener(ShipAddressesWindow w) {
		return (new CancelShipAddrListener());
	}

	// PaymentWindow

	public ActionListener getProceedFromPaymentListener(PaymentWindow w) {
		return (new ProceedFromPaymentListener());
	}

	public ActionListener getBackToCartFromPayListener(PaymentWindow w) {
		return (new BackToCartFromPayListener());
	}

	// TermsWindow
	public ActionListener getAcceptTermsListener(TermsWindow w) {
		return (new AcceptTermsListener());
	}

	// FinalOrderWindow
	public ActionListener getSubmitFinalOrderListener(FinalOrderWindow w) {
		return (new SubmitFinalOrderListener());
	}

	public ActionListener getCancelFinalOrderListener(FinalOrderWindow w) {
		return (new CancelFinalOrderListener());
	}

	// ////// PUBLIC ACCESSORS to register screens controlled by this class////

	public void setCartItemsWindow(CartItemsWindow w) {
		cartItemsWindow = w;
	}

	public void setShippingBillingWindow(ShippingBillingWindow w) {
		shippingBillingWindow = w;
	}

	public void setShipAddressesWindow(ShipAddressesWindow w) {
		shipAddressesWindow = w;
	}

	public void setPaymentWindow(PaymentWindow w) {
		paymentWindow = w;
	}

	public void setTermsWindow(TermsWindow w) {
		termsWindow = w;
	}

	public void setFinalOrderWindow(FinalOrderWindow w) {
		finalOrderWindow = w;
	}

	// ///// screens -- private references
	private CartItemsWindow cartItemsWindow;
	private ShippingBillingWindow shippingBillingWindow;
	private ShipAddressesWindow shipAddressesWindow;
	private PaymentWindow paymentWindow;
	private TermsWindow termsWindow;
	private FinalOrderWindow finalOrderWindow;
	private Window[] allWindows = { cartItemsWindow, shippingBillingWindow,
			shipAddressesWindow, paymentWindow, cartItemsWindow, termsWindow,
			finalOrderWindow };

	public void cleanUp() {
		for (Window w : allWindows) {
			if (w != null) {
				log.info("Disposing of window " + w.getClass().getName());
				w.dispose();
			}
		}
	}

	// helper methods

	void displayError(Window w, String msg) {
		JOptionPane.showMessageDialog(w, msg, "Error",
				JOptionPane.ERROR_MESSAGE);
	}

}
