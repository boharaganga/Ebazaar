
package application;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import middleware.DatabaseException;

import application.gui.AddEditCatalog;
import application.gui.AddEditProduct;
import application.gui.CustomTableModel;
import application.gui.DefaultData;
import application.gui.EbazaarMainFrame;
import application.gui.MaintainCatalogTypes;
import application.gui.MaintainProductCatalog;
import business.SessionContext;
import business.externalinterfaces.CustomerConstants;
import business.externalinterfaces.IProductFromDb;
import business.externalinterfaces.IProductFromGui;
import business.productsubsystem.ProductSubsystemFacade;

public enum ManageProductsController implements CleanupControl  {
	//This makes ManageProductsController a Singleton. Since controllers
	//monitor the states of the windows under their control, they
	//need to be singletons. This style for implementing the Singleton
	//pattern is explained in Effective Java, 2nd ed.
	INSTANCE;
	
	//	a singleton instance
    static ComboAction comboAction = null;
    /////////// EVENT HANDLERS -- new code goes here //////////// 
    
    //// control MaintainCatalogTYpes
	class AddCatalogListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
        	
        	addEditCatalog  = new AddEditCatalog(GuiUtil.ADD_NEW,null);
        	maintainCatalogTypes.setVisible(false);
        	
        	addEditCatalog.setVisible(true);
        	
        }        	
 
	
	}
	class EditCatalogListener implements ActionListener {
		final String ERROR_MESSAGE = "Please select a row.";
		final String ERROR = "Error";	    
        public void actionPerformed(ActionEvent evt) {
        	JTable table = maintainCatalogTypes.getTable();
        	CustomTableModel model = maintainCatalogTypes.getModel();
        	int selectedRow = table.getSelectedRow();
        	if(selectedRow >= 0) {
        		String selectedType = (String)model.getValueAt(selectedRow,0);
        								       														
        		AddEditCatalog editType = new AddEditCatalog(GuiUtil.EDIT,selectedType);
        		editType.setVisible(true);
        		
        		
        	}
        	else {
       			JOptionPane.showMessageDialog(maintainCatalogTypes,         									          
        									  ERROR_MESSAGE,
        									  ERROR, 
        									  JOptionPane.ERROR_MESSAGE);
        		
        	}
        		

        }
	}
	class DeleteCatalogListener implements ActionListener {
		final String ERROR_MESSAGE = "Please select a row.";
		final String ERROR = "Error";	    
        public void actionPerformed(ActionEvent evt) {
            JTable table = maintainCatalogTypes.getTable();
        	int selectedRow = table.getSelectedRow();
        	if(selectedRow >= 0) {
				// Students: code goes here.
				JOptionPane.showMessageDialog(maintainCatalogTypes, 
										  "Need to write code for this!", 
										  "Information", 
										  JOptionPane.INFORMATION_MESSAGE);        		
        		
        	}
        	else {
       			JOptionPane.showMessageDialog(maintainCatalogTypes,         									          
        									  ERROR_MESSAGE,
        									  ERROR, 
        									  JOptionPane.ERROR_MESSAGE);
        		
        	}        	
        	

        }
	}	
    class MaintainProductActionListener implements ActionListener, Controller {
        /* this method is called when LoginControl needs this class to load products 
         */
        public void doUpdate() {
            if(maintainProductCatalog != null){
                //implement by requesting product catalog for selected
                //catalogtype from Product Subsystem
                
            }
        }   
         public void actionPerformed(ActionEvent e) {
             SessionContext ctx = SessionContext.INSTANCE;
             Boolean loggedIn = (Boolean)ctx.get(CustomerConstants.LOGGED_IN);
             if(!loggedIn.booleanValue()) {
                 maintainProductCatalog = new MaintainProductCatalog();
                 LoginControl loginControl = 
                     new LoginControl(maintainProductCatalog,
                                      mainFrame);
                 loginControl.startLogin();
             }   
             else {
                 maintainProductCatalog = new MaintainProductCatalog();
                 mainFrame.setVisible(false);
                 maintainProductCatalog.setVisible(true);
             }
        }
    }

   
    class MaintainCatalogTypesActionListener implements ActionListener, Controller {
        /* this method is called when LoginControl needs this class to load catalogs 
         */
        public void doUpdate() {
            if(maintainCatalogTypes != null){
                //implement by requesting catalog list from Product Subsystem
                
            }
        }   
         public void actionPerformed(ActionEvent e) {
             SessionContext ctx = SessionContext.INSTANCE;
             Boolean loggedIn = (Boolean)ctx.get(CustomerConstants.LOGGED_IN);
             if(!loggedIn.booleanValue()) {
                 maintainCatalogTypes  = new  MaintainCatalogTypes();
                 LoginControl loginControl = 
                     new LoginControl(maintainCatalogTypes,
                                      mainFrame);
                 loginControl.startLogin();
             } 
             else {
                 maintainCatalogTypes  = new  MaintainCatalogTypes();
                 maintainCatalogTypes.setVisible(true);
                 mainFrame.setVisible(false);
             }
        }
    }	
	
	class BackToMainListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			BrowseAndSelectController.INSTANCE.makeMainFrameVisible();
		    maintainCatalogTypes.dispose();		
		}
	}	
   
    
	//// control MaintainProductCatalog
	class AddProductListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        	
       		//no field values need to be passed into AddEditProduct when adding a new product
       		//so we create an empty Properties instance
        	Properties emptyProductInfo = new Properties();
        	
        	String catalogType = maintainProductCatalog.getCatalogType();
        	addEditProduct = new AddEditProduct(GuiUtil.ADD_NEW,catalogType, emptyProductInfo);
        	maintainProductCatalog.setVisible(false);
        	
        	addEditProduct.setVisible(true);
        	
        }        	
 
	}
	
	
	class EditProductListener implements ActionListener {
		final String ERROR_MESSAGE = "Please select a row.";
		final String ERROR = "Error";		    
        public void actionPerformed(ActionEvent evt) {
        	JTable table = maintainProductCatalog.getTable();
        	CustomTableModel model = maintainProductCatalog.getModel();
        	String catalogType = maintainProductCatalog.getCatalogType();
        	int selectedRow = table.getSelectedRow();
        	if(selectedRow >= 0) {
        		String[] fldNames = DefaultData.FIELD_NAMES;
        		Properties productInfo = new Properties();
        		
        		//index for Product Name
        		int columnIndex = DefaultData.PRODUCT_NAME_INT;
        		productInfo.setProperty(fldNames[columnIndex],
        								(String)model.getValueAt(selectedRow,columnIndex));
        								
         		//index for Price Per Unit
        		columnIndex = DefaultData.PRICE_PER_UNIT_INT;
        		productInfo.setProperty(fldNames[columnIndex],
        								(String)model.getValueAt(selectedRow,columnIndex));
        								       								
        		//index for Mfg Date
        		columnIndex = DefaultData.MFG_DATE_INT;
        		productInfo.setProperty(fldNames[columnIndex],
        								(String)model.getValueAt(selectedRow,columnIndex));
        								        								       								
        		//index for Quantity						
        		columnIndex = DefaultData.QUANTITY_INT;					
         		productInfo.setProperty(fldNames[columnIndex],
        								(String)model.getValueAt(selectedRow,columnIndex));
        								       														
        		AddEditProduct editProd = new AddEditProduct(GuiUtil.EDIT,catalogType, productInfo);
        		editProd.setVisible(true);
        		
        		
        	}
        	else {
       			JOptionPane.showMessageDialog(maintainProductCatalog,         									          
        									  ERROR_MESSAGE,
        									  ERROR, 
        									  JOptionPane.ERROR_MESSAGE);
        		
        	}
        		

        }
	}
	class DeleteProductListener implements ActionListener {
		final String ERROR_MESSAGE = "Please select a row.";
		final String ERROR = "Error";		    
        public void actionPerformed(ActionEvent evt) {
            JTable table = maintainProductCatalog.getTable();
        	int selectedRow = table.getSelectedRow();
        	if(selectedRow >= 0) {
 				// Students: code goes here.
        		
				JOptionPane.showMessageDialog(maintainProductCatalog, 
										  "Need to write code for this!", 
										  "Information", 
										  JOptionPane.INFORMATION_MESSAGE);        		
        		
        	}
        	else {
       			JOptionPane.showMessageDialog(maintainProductCatalog,         									          
        									  ERROR_MESSAGE,
        									  ERROR, 
        									  JOptionPane.ERROR_MESSAGE);
        		
        	}        	
        	

        }
	}	
	
	class SearchProductListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        	//Students: code goes here
        	

        }
	}
	class BackToMainFromProdsListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
				BrowseAndSelectController.INSTANCE.makeMainFrameVisible();
			    maintainProductCatalog.dispose();		
		}
	}	
	
	// control AddEditCatalog
	class SaveAddEditCatListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        	String name = addEditCatalog.getProductNameField().getText();
        	
   	//TODO get the product ID from the event
        	
        	ProductSubsystemFacade psf = new  ProductSubsystemFacade();
        	try {
				psf.saveNewCatalogName(name);
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	/**
	 * Returns the user to the previous screen
	 */
	class BackFromAddEditCatListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        	
        	maintainCatalogTypes.setVisible(true);
        	addEditCatalog.dispose();

        }
	}	
  
	////  control AddEditProduct
	
	class SaveAddEditProductListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        	    String productName = addEditProduct.getProductNameField().getText();
        	    String quantity = addEditProduct.getQuantityField().getText();
        	    String unitPrice = addEditProduct.getPricePerUnitField().getText();
        	    String mfgDate = addEditProduct.getMfgDateField().getText();
        	    //String catalogId = addEditProduct.getCatalogGroupField().getSelectedItem().toString();
        	    String description = addEditProduct.getProductNameField().getText();
        	    String catalogType = addEditProduct.getCatalogGroupField().getSelectedItem().toString();
        	    
        		ProductSubsystemFacade psf = new ProductSubsystemFacade();
        		IProductFromGui p = psf.createProduct(productName, mfgDate, quantity, unitPrice);
        		try {
					psf.saveNewProduct(p,catalogType);
				} catch (DatabaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				JOptionPane.showMessageDialog(addEditProduct, 
//										  "Need to write code for this!", 
//										  "Information", 
//										  JOptionPane.INFORMATION_MESSAGE); 
 
        }
	}
	class BackFromAddEditProductListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        	
           	maintainProductCatalog.setVisible(true);
        	addEditProduct.dispose();


        }
	}
	class ComboAction extends AbstractAction {
		IComboObserver[] observers = {maintainProductCatalog, addEditProduct};

		public void actionPerformed(ActionEvent evt) {
			String catalogGroup = (String)((JComboBox)evt.getSource()).getSelectedItem();
			
			// TODO: Read product list from Product Subsystem 
			/**
			 * @author Ganga
			 */
		    ProductSubsystemFacade psf = new ProductSubsystemFacade();
		    List<String[]> products = new ArrayList<String[]>();
		    List<IProductFromDb> productsFromDb = null;
			try {
				productsFromDb = psf.getProductList(catalogGroup);
				for(IProductFromDb idb : productsFromDb){
					String[] str = {idb.getProductName(),idb.getUnitPrice(),idb.getMfgDate(),idb.getQuantityAvail()};
					products.add(str);
				}
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//DefaultData dd = DefaultData.INSTANCE;
			//List<String[]> products = dd.getProductCatalogChoices(catalogGroup);
			for(IComboObserver o: observers) {
				if(o != null) {
					o.setCatalogGroup(catalogGroup);
					o.refreshData();					
				}
			}
			if(maintainProductCatalog != null) {
				maintainProductCatalog.updateModel(products);
				maintainProductCatalog.repaint();
			}	
		}
		private static final long serialVersionUID = 1L;	
	}
	
    
    
    ///////// PUBLIC INTERFACE -- for getting instances of listeners ///
	//mainFrame
    public ActionListener getMaintainProductActionListener(EbazaarMainFrame w){
            return (new MaintainProductActionListener());
    }     
        
    public ActionListener getMaintainCatalogTypesActionListener(EbazaarMainFrame w){
            return (new MaintainCatalogTypesActionListener());
    }     
	// MaintainCatalogTypes
    public ActionListener getAddCatalogListener(MaintainCatalogTypes w){
        return (new AddCatalogListener());
    }     
    
    public ActionListener getEditCatalogListener(MaintainCatalogTypes w){
        return (new EditCatalogListener());
    } 
    
    public ActionListener getDeleteCatalogListener(MaintainCatalogTypes w){
        return (new DeleteCatalogListener());
    } 
    
    public ActionListener getBackToMainListener(MaintainCatalogTypes w){
        return (new BackToMainListener());
    } 
    
    // MaintainProductCatalog
    public ActionListener getAddProductListener(MaintainProductCatalog w){
        return (new AddProductListener());
    } 
    public ActionListener getEditProductListener(MaintainProductCatalog w){
        return (new EditProductListener());
    } 
    public ActionListener getDeleteProductListener(MaintainProductCatalog w){
        return (new DeleteProductListener());
    } 
    public ActionListener getSearchProductListener(MaintainProductCatalog w){
        return (new SearchProductListener());
    } 
    public ActionListener getBackToMainFromProdsListener(MaintainProductCatalog w){
        return (new BackToMainFromProdsListener());
    }     
    
    // AddEditCatalog
    public ActionListener getSaveAddEditCatListener(AddEditCatalog w){
        return (new SaveAddEditCatListener());
    } 
    public ActionListener getBackFromAddEditCatListener(AddEditCatalog w){
        return (new BackFromAddEditCatListener());
    }  
    public Action getComboAction(Window w){
        if(comboAction==null) {
        	comboAction = new ComboAction();
        }
        return comboAction;
    }
    
    // AddEditProduct
    public ActionListener getSaveAddEditProductListener(AddEditProduct w){
        return (new SaveAddEditProductListener());
    } 
    public ActionListener getBackFromAddEditProductListener(AddEditProduct w){
        return (new BackFromAddEditProductListener());
    }          
    
    ////////  PUBLIC ACCESSORS to register screens controlled by this class////    
    public void setMaintainCatalogTypes(MaintainCatalogTypes w){
        maintainCatalogTypes = w;
    }     
    public void setMaintainProductCatalog(MaintainProductCatalog w){
        maintainProductCatalog = w;
    }  
    public void setAddEditCatalog(AddEditCatalog w){
        addEditCatalog = w;
    }  
    public void setAddEditProduct(AddEditProduct w){
        addEditProduct = w;
    } 
    public void setMainFrame(EbazaarMainFrame f){
        mainFrame = f;
    }    
    
    /////// screens -- private references
    private MaintainCatalogTypes maintainCatalogTypes;
    private MaintainProductCatalog maintainProductCatalog;
    private AddEditCatalog addEditCatalog;
    private AddEditProduct addEditProduct;
    private EbazaarMainFrame mainFrame;
	private Window[] allWindows = {
			maintainCatalogTypes,
			maintainProductCatalog,
			addEditCatalog,
			addEditProduct,
			mainFrame
	};
	public void cleanUp(){
		for(Window w : allWindows){
			if(w != null){
				System.out.println("Disposing of window "+w.getClass().getName());
				w.dispose();
			}
		}
	}     
}
