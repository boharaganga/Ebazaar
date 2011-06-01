
package application;

import middleware.EBazaarException;


public class UserException extends EBazaarException {
   

	public UserException(String msg){
        super(msg);
    }
	private static final long serialVersionUID = 3690196564010546740L;

}
