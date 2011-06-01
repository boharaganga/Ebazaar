
package business;

import java.util.HashMap;
import business.externalinterfaces.CustomerConstants;


public enum SessionContext {
    INSTANCE;
    //public interface
      
    public void add(Object name, Object value){
        if(context != null){
            context.put(name,value);
        }
    }
    
    public Object get(Object name){
        if(context == null){
            return null;
        }
        return context.get(name);
    }
    public void remove(Object name){
        context.remove(name);
    }
   
    private HashMap<Object,Object> context;
    SessionContext(){
        context = new HashMap<Object,Object>();
        context.put(CustomerConstants.LOGGED_IN, Boolean.FALSE);    
    }
}
