
package middleware.dataaccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import middleware.DatabaseException;
import middleware.externalinterfaces.Cleanup;
import middleware.externalinterfaces.IDataAccessSubsystem;
import middleware.externalinterfaces.IDataAccessTest;
import middleware.externalinterfaces.IDbClass;

/**
 * @author pcorazza
 * @since Nov 10, 2004
 * Class Description:
 * 
 * 
 */
public class DataAccessSubsystemFacade implements IDataAccessSubsystem, IDataAccessTest {
    
	public static final DataAccessSubsystemFacade INSTANCE = new DataAccessSubsystemFacade();
	
	private DataAccessSubsystemFacade() {
	}
	
	
    public void read(IDbClass dbClass) throws DatabaseException {
        if(dbClass != null){
            DbAction dbAction = new DbAction(dbClass);
            dbAction.performRead();
        }

    }
	public void releaseConnections(Cleanup c){
        SimpleConnectionPool pool = SimpleConnectionPool.getInstance(c);
        if(pool != null) pool.releaseConnections();
		
	}

    public void save(IDbClass dbClass) throws DatabaseException  {
        if(dbClass != null){
            DbAction dbAction = new DbAction(dbClass);
            dbAction.performUpdate();
        }
        

    }

    public void delete(IDbClass dbClass) throws DatabaseException  {
        if(dbClass != null){
            DbAction dbAction = new DbAction(dbClass);
            dbAction.performUpdate();
        }
        

    }
    
    
    //testing interface
    public ResultSet[] multipleInstanceQueries(String[] queries, String[] dburls) throws DatabaseException {
    	if(queries == null || dburls == null) return null;
    	if(queries.length != dburls.length) return null;
    	int numConnections = queries.length;
    	ResultSet[] results = new ResultSet[numConnections];
    	SimpleConnectionPool pool = SimpleConnectionPool.getInstance(numConnections);
        ArrayList<Connection> cons = new ArrayList<Connection>();
        for(int i = 0; i < numConnections; ++i) {
        	cons.add(pool.getConnection(dburls[i]));
        }
        for(int i = 0; i < numConnections; ++i) {
        	results[i] = SimpleConnectionPool.doQuery(cons.get(i), queries[i]);
        }
        return results;   	
    	
    }

}
