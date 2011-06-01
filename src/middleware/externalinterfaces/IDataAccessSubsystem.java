
package middleware.externalinterfaces;

import middleware.DatabaseException;


/**
 * @author pcorazza
 * @since Nov 10, 2004
 * Class Description:
 * 
 * 
 */
public interface IDataAccessSubsystem {
    public void read(IDbClass dbClass) throws DatabaseException;
    
    public void save(IDbClass dbClass) throws DatabaseException;
    public void delete(IDbClass dbClass) throws DatabaseException;
	public void releaseConnections(Cleanup c);
}
