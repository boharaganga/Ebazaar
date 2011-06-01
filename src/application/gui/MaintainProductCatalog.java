package application.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JWindow;

import middleware.DatabaseException;

import application.GuiUtil;
import application.IComboObserver;
import application.ManageProductsController;
import business.externalinterfaces.CustomerConstants;
import business.externalinterfaces.IProductFromDb;
import business.productsubsystem.ProductSubsystemFacade;

/**
 * 
 * @author klevi, pcorazza 
 * @since Oct 22, 2004
 * <p>
 * Class Description: This class displays all available products
 * for a particular catalog group. When a catalog group is selected,
 * the table is updated to display the products in this group. 
 * The screen provides Add, Edit and Delete buttons for modifying
 * the choices of products.
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
 * <tr>
 * 		<td>Jan 19, 2005</td>
 *      <td>klevi</td>
 *      <td>modifed the readdata comments</td>
 * </tr>
 * </table>
 *
 */
public class MaintainProductCatalog extends JWindow implements ParentWindow, IComboObserver {
	ManageProductsController control;
	private Window parent;
	CustomTableModel model;
	JTable table;
	JScrollPane tablePane;
	
	//JPanels
	JPanel mainPanel;
	JPanel upper, middle, comboPanel, lower;
	
	//widgets
	JComboBox catalogTypeCombo;	
	
	//catalog type (books, clothes, etc); set default to Books
	String catalogGroup = DefaultData.BOOKS;
	
	//constants
	private final boolean USE_DEFAULT_DATA = false;

    private final String NAME = "Name";
    private final String PRICE = "Unit Price";
    private final String MFG_DATE = "Mfg. Date";
    private final String QUANTITIES = "Quantities";
    
    private final String MAIN_LABEL = "Maintain Product Catalog";
    
    //widget labels
    private final String CAT_GROUPS = "Catalog Groups";
    private final String ADD_BUTN = "Add";
    private final String EDIT_BUTN = "Edit";
    private final String DELETE_BUTN = "Delete";
    private final String SEARCH_BUTN = "Search";
    private final String BACK_TO_MAIN = "Back to Main";
    
    
    //table config
	private final String[] DEFAULT_COLUMN_HEADERS = {NAME,PRICE,MFG_DATE,QUANTITIES};
	private final int TABLE_WIDTH = GuiUtil.SCREEN_WIDTH;
    private final int DEFAULT_TABLE_HEIGHT = Math.round(0.75f*GuiUtil.SCREEN_HEIGHT);

    //these numbers specify relative widths of the columns -- they  must add up to 1
    private final float [] COL_WIDTH_PROPORTIONS =
    	{0.4f, 0.2f, 0.2f, 0.2f};

    	
    	
	public MaintainProductCatalog() {
	    control = ManageProductsController.INSTANCE;
	    control.setMaintainProductCatalog(this);		
		initializeWindow();
		defineMainPanel();
		getContentPane().add(mainPanel);
	}
	public String getCatalogType(){
		return catalogGroup;
	}
	public JTable getTable() {
		return table;
		
	}
	public CustomTableModel getModel() {
		return model;
	}
	private void initializeWindow() {
		
		setSize(GuiUtil.SCREEN_WIDTH,GuiUtil.SCREEN_HEIGHT);		
		GuiUtil.centerFrameOnDesktop(this);
		
	}
	
	private void defineMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(GuiUtil.FILLER_COLOR);
		mainPanel.setBorder(new WindowBorder(GuiUtil.WINDOW_BORDER));
		defineUpperPanel();
		defineMiddlePanel();
		defineLowerPanel();
		mainPanel.add(upper,BorderLayout.NORTH);
		mainPanel.add(middle,BorderLayout.CENTER);
		mainPanel.add(lower,BorderLayout.SOUTH);
			
	}
	//label
	public void defineUpperPanel(){
		upper = new JPanel();
		upper.setBackground(GuiUtil.FILLER_COLOR);
		upper.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JLabel mainLabel = new JLabel(MAIN_LABEL);
		Font f = GuiUtil.makeVeryLargeFont(mainLabel.getFont());
		f = GuiUtil.makeBoldFont(f);
		mainLabel.setFont(f);
		upper.add(mainLabel);					
	}
	

	//middle -- table and combo box
	public void defineMiddlePanel(){
		
		middle = new JPanel();
		middle.setLayout(new BorderLayout());
		
		defineComboPanel();
		middle.add(comboPanel,BorderLayout.NORTH);
		
		//table
		createTableAndTablePane();
		GuiUtil.createCustomColumns(table, 
		                               TABLE_WIDTH,
		                               COL_WIDTH_PROPORTIONS,
		                               DEFAULT_COLUMN_HEADERS);
		                   		
		middle.add(GuiUtil.createStandardTablePanePanel(table,tablePane),
				   BorderLayout.CENTER);
				
	}
	
	//upper middle -- the combo panel
	public void defineComboPanel() {
		comboPanel = new JPanel();
		comboPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		//label
		JLabel comboLabel = new JLabel(CAT_GROUPS);
		comboPanel.add(comboLabel);
		
		//combo box
		catalogTypeCombo = new JComboBox();
		ProductSubsystemFacade psf = new ProductSubsystemFacade();
		List<String[]> catalogs = null;
		try {
		 catalogs = psf.getCatalogNames();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(String[] s:catalogs){
			catalogTypeCombo.addItem(s[1]);
		}
		//catalogTypeCombo.addActionListener(new ComboBoxListener());
		Action comboAction = control.getComboAction(this);
		comboAction.putValue(CustomerConstants.COMBO,this.getClass().getName() );
		//catalogTypeCombo.addActionListener(control.getComboActionListener());
		catalogTypeCombo.addActionListener(comboAction);
		comboPanel.add(catalogTypeCombo);

		
	}	
	
	//buttons
	public void defineLowerPanel(){
		//add button
		JButton addButton = new JButton(ADD_BUTN);
		addButton.addActionListener(control.getAddProductListener(this));
		
		
		//edit button
		JButton editButton = new JButton(EDIT_BUTN);
		editButton.addActionListener(control.getEditProductListener(this));
		
		//delete button
		JButton deleteButton = new JButton(DELETE_BUTN);
		deleteButton.addActionListener(control.getDeleteProductListener(this));
		
		//search button
		JButton searchButton = new JButton(SEARCH_BUTN);
		searchButton.addActionListener(control.getSearchProductListener(this));
		searchButton.setEnabled(false);
		
		//exit button
		JButton backToMainButton = new JButton(BACK_TO_MAIN);
		backToMainButton.addActionListener(control.getBackToMainFromProdsListener(this));		
		
		//create lower panel
		JButton [] buttons = {addButton,editButton,deleteButton,searchButton,backToMainButton};
		lower = GuiUtil.createStandardButtonPanel(buttons);		
	}
	
	private void createTableAndTablePane() {
		updateModel();
		table = new JTable(model);
		tablePane = new JScrollPane();
		tablePane.setPreferredSize(new Dimension(TABLE_WIDTH, DEFAULT_TABLE_HEIGHT));
		tablePane.getViewport().add(table);
		
	}
	
	public void updateModel(List<String[]> list){
		//if(model==null){
			model = new CustomTableModel();
		//}
		model.setTableValues(list);	
		if(table != null) updateTable();
	}
	
	/**
	 * If default data is being used, this method obtains it
	 * and then passes it to updateModel(List). If real data is
	 * being used, the public updateModel(List) should be called by
	 * the controller class.
	 */
	private void updateModel() {
		List<String[]> theData = new ArrayList<String[]>();
		ProductSubsystemFacade psf = new ProductSubsystemFacade();
		List<IProductFromDb> productList = new ArrayList<IProductFromDb>();
		try {
			productList = psf.getProductList("Books");
			for(IProductFromDb idb:productList){
				String[] str = {idb.getProductName(),idb.getUnitPrice(),idb.getMfgDate(),idb.getQuantityAvail()};
				theData.add(str);
			}
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if(USE_DEFAULT_DATA) {
			DefaultData dd = DefaultData.INSTANCE;
			theData = dd.getProductCatalogChoices(catalogGroup);
        }
		updateModel(theData);
 	}	
	
    private void updateTable() {
        
        table.setModel(model);
        table.updateUI();
        repaint();
        
    }	
	
	public void setParentWindow(Window parentWindow) {
		parent = parentWindow;
	}
	
	public Window getParentWindow() {
		return parent;
	}
	public void refreshData() {
		updateComboBox();
		updateTable();
		repaint();
	}
	public void setCatalogGroup(String cg) {
		this.catalogGroup = cg;
	}
	private void updateComboBox() {
		catalogTypeCombo.setSelectedItem(catalogGroup);
	}	
	//data for Listeners
	
	final String ERROR_MESSAGE = "Please select a row.";
	final String ERROR = "Error";
	

	
	public static void main(String[] args) {
		(new MaintainProductCatalog()).setVisible(true);
	}	
	private static final long serialVersionUID = 3257569511937880631L;
	
}
