package application.gui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import application.ApplicationCleanup;

/**
 * 
 * @author klevi, pcorazza 
 * @since Oct 22, 2004
 * <p>
 * Class Description:  This class provides a common listener class for all
  *   screens that need to provide an exit button.
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
public class ExitButtonListener implements ActionListener {
	private Window w;
	
	public ExitButtonListener(Window w) {
		this.w = w;
	}
	
    public void actionPerformed(ActionEvent evt) {
    	w.dispose();
		(new ApplicationCleanup()).cleanup();
    	System.exit(0);
    
    }

}
