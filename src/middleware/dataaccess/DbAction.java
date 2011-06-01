
package middleware.dataaccess;

import java.sql.ResultSet;

import middleware.DatabaseException;
import middleware.externalinterfaces.IDbClass;

/**
 * @author pcorazza
 * @since Nov 10, 2004
 * Class Description:
 * 
 * 
 */
class DbAction {
    protected String query;
    protected ResultSet resultSet;
    protected IDbClass concreteDbClass;
    
    DbAction(IDbClass c){
        concreteDbClass = c;
    }
    void performRead() throws DatabaseException {
        concreteDbClass.buildQuery();
        resultSet = DataAccessUtil.runQuery(concreteDbClass.getDbUrl(),
                                            concreteDbClass.getQuery());
        concreteDbClass.populateEntity(resultSet);
    }
    
    void performUpdate() throws DatabaseException {
        concreteDbClass.buildQuery();
        DataAccessUtil.runUpdate(concreteDbClass.getDbUrl(),
                				 concreteDbClass.getQuery());        
    }
    
    void performDelete() throws DatabaseException {
        concreteDbClass.buildQuery();
        DataAccessUtil.runUpdate(concreteDbClass.getDbUrl(),
                				 concreteDbClass.getQuery());
    }

}
