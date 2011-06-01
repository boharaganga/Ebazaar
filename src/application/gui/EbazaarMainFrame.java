package application.gui;

import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;

import application.ApplicationCleanup;
import application.BrowseAndSelectController;
import application.GuiUtil;
import application.ManageProductsController;
import application.ViewOrdersController;
/**
 * 
 * @author klevi, pcorazza 
 * @since Oct 22, 2004
 * <p>
 * Class Description: This is the entry point into the E-Bazaar application.
 * It is implemented as a JFrame and provides a splash screen. Navigation
 * is by way of a menu bar. This class represents the starting point
 * for all use cases. <p>
 * Note: If splash image does not appear, modify the 
 * value stored in GuiControl.SPLASH_IMAGE so that it points
 * to logo.jpg.
 * <p>
 * <table border="1">
 * <tr>
 * 		<th colspan="3">Change Log</th>
 * </tr>
 * <tr>
 * 		<th>Date</th> <th>Author</th> <th>Change</th>
 * </tr>
 * <tr>
 * 		<td>Oct 22, 2004</td>
 *      <td>klevi, pcorazza</td>
 *      <td>New class file</td>
 * </tr>
 * </table>
 *
 */
public class EbazaarMainFrame extends javax.swing.JFrame implements ParentWindow {
	private BrowseAndSelectController control;
	private ViewOrdersController orderControl;
    private ManageProductsController prodControl;

	//ebazaar application title
	private final String EBAZAAR_APP_NAME = "Ebazaar Online Shopping Application";
	
	//menu item names
	private final String LOGIN = "Login";
	private final String CUSTOMER = "Customer";
	private final String ADMINISTRATOR = "Administrator";
	private final String ONLINE_PURCHASE = "Online Purchase";
	private final String REVIEW_ORDERS = "Review Orders";
	private final String EXIT = "Exit";
	private final String RETRIEVE_CART = "Retrieve Saved Cart";
	private final String MAINTAIN_CATALOGUE = "Maintain Product Catalog";
	private final String MAINTAIN_CAT_TYPES = "Maintain Catalog Types";
	
	JPanel mainPanel;
	JMenuBar menuBar;
    JMenu menuCustomer, menuAdministrator;
    JMenuItem menuItemLogin, menuItemPurchaseOnline, menuItemMaintainProduct, 
      menuItemMaintainCatalogTypes,menuItemExit, menuItemRevOrders;
      
    //1)add the following line to the EbazaarMainFrame attribute declarations
    JMenuItem menuItemRetrieveSavedCart;  

    
	public EbazaarMainFrame() {
		//controllers
	    control = BrowseAndSelectController.INSTANCE;
	    control.setMainFrame(this);
	    orderControl = ViewOrdersController.INSTANCE;
	    orderControl.setMainFrame(this);
        prodControl = ManageProductsController.INSTANCE;
        prodControl.setMainFrame(this);		
		//gui
		setLookAndFeel();
		handleWindowClosing();
		setTitle(EBAZAAR_APP_NAME);
		setSize(GuiUtil.SCREEN_WIDTH, GuiUtil.SCREEN_HEIGHT);
		
		formatContentPane();		
		setBackgroundColor();
		insertLogo();
		
		createMenus();		
		//do not pack() -- this will override frame size selection
		GuiUtil.centerFrameOnDesktop(this);	
			
		setVisible(true);
	}
	
	
	private void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());//getSystemLookAndFeelClassName());		
		}
		catch(Exception e) {}
	}
	
	private void formatContentPane() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1,1));
		getContentPane().add(mainPanel);	
	}
	
	private void insertLogo() {
		ImageIcon image = new ImageIcon(GuiUtil.SPLASH_IMAGE);
		
		mainPanel.add(new JLabel(image));	
	}
	
	/** 
	 * To switch off background color, and to use default instead, comment
	 * out the reference to this method
	 */
	private void setBackgroundColor() {
		mainPanel.setBackground(GuiUtil.MAIN_SCREEN_COLOR);
	}
	
	private void createMenus() {
		menuBar = new JMenuBar();
		//menuBar.setBorder(BorderFactory.createRaisedBevelBorder());
		addMenuItems(menuBar);
		setJMenuBar(menuBar);		
	}


	static public void main(String args[]) {
	
	    new EbazaarMainFrame();
	    //long v = CustomerSubsystemFacade.serialVersionUID;
	    //System.out.println(v);
	    
	}

	
	void addMenuItems(JMenuBar menuBar) {
		//these instructions belong somewhere...
	   //2)add the following 6 lines to the AddMenuItems method of EbazaarMainFrame 
		//(before the exit menu item is added to the menu)		
    
       //create and add the menus to the menu bar
	   menuCustomer = new JMenu(CUSTOMER);  
	   menuAdministrator = new JMenu(ADMINISTRATOR); 
	   menuBar.add(menuCustomer);
	   menuBar.add(menuAdministrator);
	   
       //login menu item
       menuItemLogin = new JMenuItem(LOGIN);    
       menuItemLogin.addActionListener(control.getLoginListener(this));
       menuCustomer.add(menuItemLogin);
	    
	   //purchase online menu item     
	   menuItemPurchaseOnline = new JMenuItem(ONLINE_PURCHASE);
	   menuItemPurchaseOnline.addActionListener(control.getNewOnlinePurchaseListener(this));
	   menuCustomer.add(menuItemPurchaseOnline);
	   
	   //review orders menu item
	   menuItemRevOrders = new JMenuItem(REVIEW_ORDERS);
	   menuItemRevOrders.addActionListener(orderControl.getSelectOrderActionListener(this));	   
	   menuCustomer.add(menuItemRevOrders);
	   
	   //retrieve saved cart item
	   menuItemRetrieveSavedCart = new JMenuItem(RETRIEVE_CART);	   
	   menuItemRetrieveSavedCart.addActionListener(control.getRetrieveCartActionListener(this)); 
	   menuCustomer.add(menuItemRetrieveSavedCart);	    
	   
	   
	   //exit menu item
	   menuItemExit = new JMenuItem(EXIT);
	   menuItemExit.addActionListener(new ExitButtonListener(this));
	   menuCustomer.add(menuItemExit);
	   
	   
	   //maintain catalogue menu item
	   menuItemMaintainProduct = new JMenuItem(MAINTAIN_CATALOGUE);
	   menuItemMaintainProduct.addActionListener(prodControl.getMaintainProductActionListener(this));
	   menuAdministrator.add( menuItemMaintainProduct);
	   
	   //maintain catalogue type menu item
       menuItemMaintainCatalogTypes = new JMenuItem(MAINTAIN_CAT_TYPES);
	   menuItemMaintainCatalogTypes.addActionListener(prodControl.getMaintainCatalogTypesActionListener(this));
	   menuAdministrator.add( menuItemMaintainCatalogTypes);

	}
	/** This method makes sure that the frame is cleaned up and 
	 *  exits when the upper X is clicked 
	 */
	private void handleWindowClosing() {
        addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent w) {
                dispose();
				(new ApplicationCleanup()).cleanup();
                System.exit(0);
           }
        }); 		
		
	}	 
    //for delegation hierarchy, this is necessary:
    Window parentWindow = null;
    public void setParentWindow(Window w){
        parentWindow = null;
    }
    public Window getParentWindow() {
        return parentWindow;
        
    }
	private static final long serialVersionUID = 3618418228695610676L;
   
    
}