package application.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import application.ApplicationCleanup;
import application.GuiUtil;
import application.LoginControl;

//import LoginControl;

/**
 * 
 * @author klevi, pcorazza 
 * @since Oct 22, 2004
 * <p>
 * Class Description: This window provides textfields to enter
 * information for payment by credit card. If the user clicks
 * the Proceed With Checkout button, the Terms and Conditions window
 * appears before the Final Order is displayed.
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
public class LoginWindow extends JDialog implements ParentWindow {
	private Window parent;
	private LoginControl loginControl;	
	private final String MAIN_LABEL = "Login";
	private final String SUBMIT_BUTN = "Submit";
	private final String CANCEL_BUTN = "Cancel";
	
	private final String CUST_ID = "Customer Id";
	private final String PASSWORD = "Password";

	
	private JTextField custIdField;
	private JPasswordField pwdField;
	
	public String getCustId(){
		return (custIdField != null ? custIdField.getText() : null);
	}
	public String getPassword() {
		return (pwdField != null ? new String(pwdField.getPassword()) : null);
	}
	//JPanels
		
	JPanel mainPanel;
	JPanel upper, middle, lower;
	
	public LoginWindow(LoginControl loginControl) {
	    this.loginControl = loginControl;
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
				Math.round(.4f*GuiUtil.SCREEN_HEIGHT));		
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
		middle = new JPanel();
		middle.setBackground(GuiUtil.FILLER_COLOR);
		middle.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel gridPanel = new JPanel();
		gridPanel.setBackground(GuiUtil.SCREEN_BACKGROUND);
		middle.add(gridPanel);
		GridLayout gl = new GridLayout(2,2);
		gl.setHgap(8);
		gl.setVgap(8);
		gridPanel.setLayout(gl);
		gridPanel.setBorder(new WindowBorder(GuiUtil.WINDOW_BORDER));



		//add fields
		makeLabel(gridPanel,CUST_ID);
		custIdField = new JTextField(10);
		gridPanel.add(custIdField);
		
		makeLabel(gridPanel,PASSWORD);
		pwdField = new JPasswordField(10);
		gridPanel.add(pwdField);
		
				
	}
	//buttons
	public void defineLowerPanel(){
		//submit button
		JButton submitButton = new JButton(SUBMIT_BUTN);
		submitButton.addActionListener(loginControl.getSubmitListener(this));
		
		
		//cancel button
		JButton cancelButton = new JButton(CANCEL_BUTN);
		cancelButton.addActionListener(loginControl.getCancelListener(this));
		

		
		//create lower panel
		JButton [] buttons = {submitButton,cancelButton};
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

	public static void main(String[] args) {
		
		(new LoginWindow(null)).setVisible(true);
	}

	private static final long serialVersionUID = 3258408422029144633L;
	
}
