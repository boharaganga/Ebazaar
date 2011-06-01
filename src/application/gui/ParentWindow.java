package application.gui;

import java.awt.Window;

/**
 * 
 * @author klevi, pcorazza 
 * @since Oct 22, 2004
 * <p>
 * Class Description: Each window that is accessed from another window
 * should implement this interface. This supports
 * navigation back to calling windows.
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
public interface ParentWindow {
	public void setVisible(boolean b);
	public Window getParentWindow();
	public void setParentWindow(Window w);

}
