package application.gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JWindow;

import application.GuiUtil;
import application.ViewOrdersController;

/**
 * 
 * @author klevi, pcorazza 
 * @since Oct 22, 2004
 * <p>
 * Class Description: This screen displays a table of all
 * orders currently stored in the database (on initial
 * creation of this class, fake data from DefaultData, is
 * used to fill out this table). If the user selects a row
 * in the table, and clicks the ViewDetails button, 
 * an instance of ViewOrderDetailsWindow is created, which
 * displays details of the selected order.
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
public class SelectOrderWindow extends JWindow implements ParentWindow {
	ViewOrdersController control;
	private Window parent;
	CustomTableModel model;
	JTable table;
	JScrollPane tablePane;
	
	//JPanels
	JPanel mainPanel;
	JPanel upper, middle, lower;
	
	//constants
	private final boolean USE_DEFAULT_DATA = false;

    private final String ORDER_ID = "Order ID";
    private final String DATE = "Date";
    
    private final String TOTAL = "Total Cost";
    private final String MAIN_LABEL = "Order History";
    
    //button labels
    private final String VIEW_DETAILS_BUTN = "View Details";
    private final String CANCEL_BUTN = "Cancel";

    
    //table config
	private final String[] DEFAULT_COLUMN_HEADERS = {ORDER_ID,DATE,TOTAL};
	private final int TABLE_WIDTH = Math.round(0.75f*GuiUtil.SCREEN_WIDTH);
    private final int DEFAULT_TABLE_HEIGHT = Math.round(0.75f*GuiUtil.SCREEN_HEIGHT);

    //these numbers specify relative widths of the columns -- they  must add up to 1
    private final float [] COL_WIDTH_PROPORTIONS =
    	{0.4f, 0.3f, 0.3f};

 	final String ERROR_MESSAGE = "Please select a row.";
	final String ERROR = "Error";   	
    	
	public SelectOrderWindow() {
	    control = ViewOrdersController.INSTANCE;
	    control.setSelectOrderWindow(this);		
		initializeWindow();
		defineMainPanel();
		getContentPane().add(mainPanel);
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
	//table
	public void defineMiddlePanel(){
		createTableAndTablePane();
		GuiUtil.createCustomColumns(table, 
		                               TABLE_WIDTH,
		                               COL_WIDTH_PROPORTIONS,
		                               DEFAULT_COLUMN_HEADERS);
		                   		
		middle = GuiUtil.createStandardTablePanePanel(table,tablePane);
				
	}
	//buttons
	public void defineLowerPanel(){
		//proceed button
		JButton detailsButton = new JButton(VIEW_DETAILS_BUTN);
		detailsButton.addActionListener(control.getViewOrderDetailsListener(this));
		
		
		//continue button
		JButton cancelButton = new JButton(CANCEL_BUTN);
		cancelButton.addActionListener(control.getCancelViewOrdersListener(this));
		

		
		//create lower panel
		JButton [] buttons = {detailsButton,cancelButton};
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
		if(model == null) {
	        model = new CustomTableModel();
    	    
		}
		model.setTableValues(list);		
	}
	
	/**
	 * If default data is being used, this method obtains it
	 * and then passes it to updateModel(List). If real data is
	 * being used, the public updateModel(List) should be called by
	 * the controller class.
	 */
	private void updateModel() {
		List<String[]> theData = new ArrayList<String[]>();
        if(USE_DEFAULT_DATA) {
			DefaultData dd = DefaultData.INSTANCE;
			theData = dd.getSelectOrderDefaultData();
        }
		updateModel(theData);
 	}	
	
	

	
    @SuppressWarnings("unused")
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
	
	public static void main(String[] args) {
		(new SelectOrderWindow()).setVisible(true);
	}
	private static final long serialVersionUID = 3834023675661071921L;
	
}
