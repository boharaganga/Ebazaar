package application.gui;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Properties;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import middleware.DatabaseException;

import business.externalinterfaces.CustomerConstants;
import business.productsubsystem.ProductSubsystemFacade;

import application.ApplicationCleanup;
import application.GuiUtil;
import application.IComboObserver;
import application.ManageProductsController;

/**
 * 
 * @author klevi, pcorazza 
 * @since Oct 22, 2004
 * <p>
 * Class Description: This class is responsible for building
 * the window for adding or editing a product. 
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
public class AddEditProduct extends JDialog implements ParentWindow, IComboObserver {

	private Window parent;
	private ManageProductsController control;

	/** final value of label will be set in the constructor */
	private String mainLabel = " Product";
	private final String SAVE_BUTN = "Save";
	private final String BACK_BUTN = "Close";
	
	private JTextField productNameField;
	private JComboBox catalogGroupField;
	private JTextField pricePerUnitField;
	private JTextField mfgDateField;	
	private JTextField quantityField;
	
	/** group is "Books", "Clothes" etc */
	private String catalogGroup;
	
	/** value is "Add New" or "Edit" */
	private String addOrEdit = GuiUtil.ADD_NEW;
	
	/** map of initial field values */
	private Properties fieldValues;
	

	//JPanels		
	JPanel mainPanel;
	JPanel upper, middle, lower;
	
	
	
	public JTextField getProductNameField() {
		return productNameField;
	}

	public void setProductNameField(JTextField productNameField) {
		this.productNameField = productNameField;
	}

	public JComboBox getCatalogGroupField() {
		return catalogGroupField;
	}

	public void setCatalogGroupField(JComboBox catalogGroupField) {
		this.catalogGroupField = catalogGroupField;
	}

	public JTextField getPricePerUnitField() {
		return pricePerUnitField;
	}

	public void setPricePerUnitField(JTextField pricePerUnitField) {
		this.pricePerUnitField = pricePerUnitField;
	}

	public JTextField getMfgDateField() {
		return mfgDateField;
	}

	public void setMfgDateField(JTextField mfgDateField) {
		this.mfgDateField = mfgDateField;
	}

	public JTextField getQuantityField() {
		return quantityField;
	}

	public void setQuantityField(JTextField quantityField) {
		this.quantityField = quantityField;
	}

	/**
	 * Constructor sets instance variables and builds gui. 
	 * @param addOrEdit - has value "add" or "edit", indicating which gui window will be built
	 * @param catalogGroup - has value "Books" or "Clothes"
	 * @param fieldValues - values to be set in data fields of gui
	 */
	public AddEditProduct(String addOrEdit, String catalogGroup, Properties fieldValues) {
	    control = ManageProductsController.INSTANCE;
	    control.setAddEditProduct(this);
		this.catalogGroup = catalogGroup;
		this.addOrEdit = addOrEdit;
		this.fieldValues = fieldValues;
		handleWindowClosing();
		initializeWindow();
		defineMainPanel();
		getContentPane().add(mainPanel);
			
	}
	
	private void handleWindowClosing() {
        addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent w) {
                dispose();
				(new ApplicationCleanup()).cleanup();
                System.exit(0);
           }
        }); 		
		
	}
	
	private void initializeWindow() {
		
		setSize(Math.round(.7f*GuiUtil.SCREEN_WIDTH),
				Math.round(.7f*GuiUtil.SCREEN_HEIGHT));
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
		
		JLabel mainLabel = new JLabel(finalMainLabelName());
		Font f = GuiUtil.makeVeryLargeFont(mainLabel.getFont());
		f = GuiUtil.makeBoldFont(f);
		mainLabel.setFont(f);
		upper.add(mainLabel);					
	}
	
	private String finalMainLabelName() {
		return addOrEdit+" "+mainLabel;
	}
	//table
	public void defineMiddlePanel(){
		middle = new JPanel();
		middle.setBackground(GuiUtil.FILLER_COLOR);
		middle.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel gridPanel = new JPanel();
		gridPanel.setBackground(GuiUtil.SCREEN_BACKGROUND);
		middle.add(gridPanel);
		GridLayout gl = new GridLayout(5,2);
		gl.setHgap(8);
		gl.setVgap(8);
		gridPanel.setLayout(gl);
		gridPanel.setBorder(new WindowBorder(GuiUtil.WINDOW_BORDER));


		//add fields
		String[] fldNames = DefaultData.FIELD_NAMES;
		
		String labelName = fldNames[DefaultData.PRODUCT_NAME_INT];
		makeLabel(gridPanel,labelName);
		productNameField = new JTextField(10);
		productNameField.setText(fieldValues.getProperty(labelName));
		gridPanel.add(productNameField);
		
		//catalog group is different from the other fields
		//because it plays a different role in MaintainCatalog
		//so it is set differently
		labelName = "Catalog";
		makeLabel(gridPanel,labelName);
		catalogGroupField = new JComboBox();
		ProductSubsystemFacade psf = new ProductSubsystemFacade();
		List<String[]> catalogs = null;
		try {
		 catalogs = psf.getCatalogNames();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(String[] s:catalogs){
			catalogGroupField.addItem(s[0]);
		}
		//catalogGroupField.addItem(DefaultData.BOOKS);
		//catalogGroupField.addItem(DefaultData.CLOTHES);
		catalogGroupField.setSelectedItem(catalogGroup);
		Action comboAction = control.getComboAction(this);
		comboAction.putValue(CustomerConstants.COMBO,this.getClass().getName() );
		catalogGroupField.addActionListener(comboAction);
		gridPanel.add(catalogGroupField);
		
		labelName = fldNames[DefaultData.PRICE_PER_UNIT_INT];
		makeLabel(gridPanel,labelName);
		pricePerUnitField = new JTextField(10);
		pricePerUnitField.setText(fieldValues.getProperty(labelName));
		gridPanel.add(pricePerUnitField);		
		
		labelName = fldNames[DefaultData.MFG_DATE_INT];
		makeLabel(gridPanel,labelName);
		mfgDateField = new JTextField(10);
		mfgDateField.setText(fieldValues.getProperty(labelName));
		gridPanel.add(mfgDateField);
						
		labelName = fldNames[DefaultData.QUANTITY_INT];
		makeLabel(gridPanel,labelName);
		quantityField = new JTextField(10);
		quantityField.setText(fieldValues.getProperty(labelName));
		gridPanel.add(quantityField);
		

	}
	//buttons
	public void defineLowerPanel(){
		//proceed button
		JButton saveButton = new JButton(SAVE_BUTN);
		saveButton.addActionListener(control.getSaveAddEditProductListener(this));
		
		
		//back to cart button
		JButton backButton = new JButton(BACK_BUTN);
		backButton.addActionListener(control.getBackFromAddEditProductListener(this));
		

		
		//create lower panel
		JButton [] buttons = {saveButton,backButton};
		lower = GuiUtil.createStandardButtonPanel(buttons);
	}
	
	private void makeLabel(JPanel p, String s) {
		JLabel l = new JLabel(s);
		p.add(leftPaddedPanel(l));
	}
	private JPanel leftPaddedPanel(JLabel label) {
		JPanel paddedPanel = new JPanel();
		paddedPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		paddedPanel.add(GuiUtil.createHBrick(1));
		paddedPanel.add(label);
		paddedPanel.setBackground(GuiUtil.SCREEN_BACKGROUND);
		return paddedPanel;		
	}
	public void setParentWindow(Window parentWindow) {
		parent = parentWindow;
	}
	
	public Window getParentWindow() {
		return parent;
	}	
	
	public void refreshData() {
		
	}

	
	public static void main(String[] args) {		
	}	
	private static final long serialVersionUID = 1L;

	public void setCatalogGroup(String s) {
		catalogGroup = s;
		
	}	
}
