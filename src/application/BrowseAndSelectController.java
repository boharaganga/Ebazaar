package application;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import middleware.DatabaseException;

import business.externalinterfaces.IProductFromDb;
import business.externalinterfaces.IProductFromGui;
import business.externalinterfaces.IProductSubsystem;
import business.productsubsystem.ProductSubsystemFacade;

import application.gui.CartItemsWindow;
import application.gui.CatalogListWindow;
import application.gui.DefaultData;
import application.gui.EbazaarMainFrame;
import application.gui.MaintainCatalogTypes;
import application.gui.MaintainProductCatalog;
import application.gui.ProductDetailsWindow;
import application.gui.ProductListWindow;
import application.gui.QuantityWindow;
import application.gui.SelectOrderWindow;

public enum BrowseAndSelectController implements CleanupControl {
	// This makes BrowseAndSelectController a Singleton. Since controllers
	// monitor the states of the windows under their control, they
	// need to be singletons. This style for implementing the Singleton
	// pattern is explained in Effective Java, 2nd ed.
	INSTANCE;
	// ///////// EVENT HANDLERS -- new code goes here ////////////

	// control of mainFrame
	class PurchaseOnlineActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			catalogListWindow = CatalogListWindow.getInstance();
			mainFrame.setVisible(false);
			catalogListWindow.setVisible(true);
		}
	}

	class LoginListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			LoginControl loginControl = new LoginControl(mainFrame, mainFrame);
			loginControl.startLogin();
		}
	}

	class RetrieveCartActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (cartItemsWindow == null) {
				cartItemsWindow = new CartItemsWindow();
			}

			cartItemsWindow.setVisible(true);
			mainFrame.setVisible(false);
		}
	}

	// control of CatalogListWindow
	class SelectCatalogListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			JTable table = catalogListWindow.getTable();
			int selectedRow = table.getSelectedRow();
			if (selectedRow >= 0) {
				String type = (String) table.getValueAt(selectedRow, 0);
				System.out.println(type);
				catalogListWindow.setVisible(false);
				productListWindow = new ProductListWindow(type);
				productListWindow.setVisible(true);
			}
			// value of selectedRow is -1, which means no row was selected
			else {
				String errMsg = "Please select a row.";
				JOptionPane.showMessageDialog(catalogListWindow, errMsg,
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class BackToMainFrameListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			mainFrame.setVisible(true);
			catalogListWindow.setVisible(false);
		}
	}

	// control of ProductListWindow
	class SelectProductListener implements ActionListener {
		private Logger logger = Logger.getLogger(SelectProductListener.class
				.getName());

		public void actionPerformed(ActionEvent evt) {
			JTable table = productListWindow.getTable();
			int selectedRow = table.getSelectedRow();

			if (selectedRow >= 0) {
				String type = (String) table.getValueAt(selectedRow, 0);
				System.out.println(type);
				IProductSubsystem facade = new ProductSubsystemFacade();
				try {
					IProductFromDb product = facade.getProduct(type);
					String[] productParams = new String[] {
							product.getProductName(), product.getUnitPrice(),
							product.getQuantityAvail(), product.getMfgDate() };
					productDetailsWindow = new ProductDetailsWindow(
							productParams);
					productListWindow.setVisible(false);
					productDetailsWindow.setVisible(true);
				} catch (DatabaseException e) {
					JOptionPane.showMessageDialog(productDetailsWindow,
							"Unable to get product by name", "Error",
							JOptionPane.ERROR_MESSAGE);
					logger.warning(e.getMessage());
					return;
				}
			}
			// value of selectedRow is -1, which means no row was selected
			else {
				String errMsg = "Please select a row.";
				JOptionPane.showMessageDialog(productListWindow, errMsg,
						"Error", JOptionPane.ERROR_MESSAGE);

			}
		}
	}

	class BackToCatalogListListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			catalogListWindow.setVisible(true);
			productListWindow.setVisible(false);
		}
	}

	// ///// control ProductDetails
	class AddToCartListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			productDetailsWindow.setVisible(false);
			quantityWindow = new QuantityWindow();
			quantityWindow.setVisible(true);
			quantityWindow.setParentWindow(productDetailsWindow);

		}
	}

	class QuantityOkListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			quantityWindow.setVisible(false);
			CartItemsWindow cartWindow = new CartItemsWindow();
			cartWindow.setVisible(true);

		}
	}

	class BackToProductListListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			productDetailsWindow.setVisible(false);
			productListWindow.setVisible(true);
		}
	}

	// /// control CartItemsWindow

	class ContinueShoppingListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			cartItemsWindow.setVisible(false);

			// user has been looking at this product list
			if (productListWindow != null) {
				productListWindow.setVisible(true);
			}
			// user has just retrieved saved cart
			else {
				catalogListWindow = CatalogListWindow.getInstance();
				catalogListWindow.setVisible(true);
			}
		}
	}

	class SaveCartListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			// implement
		}
	}

	// /////// PUBLIC INTERFACE -- for getting instances of listeners ///
	// EbazaarMainFrame
	public ActionListener getNewOnlinePurchaseListener(EbazaarMainFrame f) {
		return (new PurchaseOnlineActionListener());
	}

	public LoginListener getLoginListener(EbazaarMainFrame f) {
		return new LoginListener();
	}

	public ActionListener getRetrieveCartActionListener(EbazaarMainFrame f) {
		return (new RetrieveCartActionListener());
	}

	// CatalogListWindow
	public ActionListener getSelectCatalogListener(CatalogListWindow w) {
		return new SelectCatalogListener();
	}

	public ActionListener getBackToMainFrameListener(CatalogListWindow w) {
		return new BackToMainFrameListener();
	}

	// ProductListWindow
	public ActionListener getSelectProductListener(ProductListWindow w) {
		return new SelectProductListener();
	}

	public ActionListener getBackToCatalogListListener(ProductListWindow w) {
		return new BackToCatalogListListener();
	}

	// ProductDetails Window
	public ActionListener getAddToCartListener(ProductDetailsWindow w) {
		return new AddToCartListener();
	}

	public ActionListener getBackToProductListListener(ProductDetailsWindow w) {
		return new BackToProductListListener();
	}

	// CartItemsWindow

	public ActionListener getContinueShoppingListener(CartItemsWindow w) {
		return (new ContinueShoppingListener());
	}

	public ActionListener getSaveCartListener(CartItemsWindow w) {
		return (new SaveCartListener());
	}

	public ActionListener getQuantityOkListener(QuantityWindow w) {
		return new QuantityOkListener();
	}

	// ////// PUBLIC ACCESSORS to register screens controlled by this class////

	public void setCatalogList(CatalogListWindow w) {
		catalogListWindow = w;
	}

	public void setMainFrame(EbazaarMainFrame m) {
		mainFrame = m;
	}

	public void setProductListWindow(ProductListWindow p) {
		productListWindow = p;
	}

	public void setProductDetailsWindow(ProductDetailsWindow p) {
		productDetailsWindow = p;
	}

	public void setCartItemsWindow(CartItemsWindow w) {
		cartItemsWindow = w;
	}

	public void setSelectOrderWindow(SelectOrderWindow w) {
		selectOrderWindow = w;
	}

	public void setMaintainCatalogTypes(MaintainCatalogTypes w) {
		maintainCatalogTypes = w;
	}

	public void setMaintainProductCatalog(MaintainProductCatalog w) {
		maintainProductCatalog = w;
	}

	public void setQuantityWindow(QuantityWindow w) {
		quantityWindow = w;
	}

	// ///// screens -- private references
	private EbazaarMainFrame mainFrame;
	private ProductListWindow productListWindow;
	private CatalogListWindow catalogListWindow;
	private ProductDetailsWindow productDetailsWindow;
	private CartItemsWindow cartItemsWindow;
	private SelectOrderWindow selectOrderWindow;
	private MaintainCatalogTypes maintainCatalogTypes;
	private MaintainProductCatalog maintainProductCatalog;
	private QuantityWindow quantityWindow;
	private Window[] allWindows = { mainFrame, productListWindow,
			catalogListWindow, productDetailsWindow, cartItemsWindow,
			selectOrderWindow, maintainCatalogTypes, maintainProductCatalog,
			quantityWindow };

	public void cleanUp() {
		for (Window w : allWindows) {
			if (w != null) {
				System.out.println("Disposing of window "
						+ w.getClass().getName());
				w.dispose();
			}
		}
	}

	// /////// communication with other controllers
	public void makeMainFrameVisible() {
		if (mainFrame == null) {
			mainFrame = new EbazaarMainFrame();
		}
		mainFrame.setVisible(true);
	}
}
