
package business.customersubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import middleware.DatabaseException;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.externalinterfaces.DbConfigKey;
import middleware.externalinterfaces.IDataAccessSubsystem;
import middleware.externalinterfaces.IDbClass;

class DbClassCreditCard implements IDbClass {
    private final String READ = "Read";
    private String custId;
    String query;
    private String queryType;
    private CreditCard defaultPaymentInfo;
    
    CreditCard getDefaultPaymentInfo(){
        return defaultPaymentInfo;
    }
    public void readDefaultPaymentInfo(String custId) throws DatabaseException {
        this.custId = custId;
        queryType=READ;
        IDataAccessSubsystem dataAccess = DataAccessSubsystemFacade.INSTANCE;
        dataAccess.read(this);
    }
 
    public void buildQuery() {
        if(queryType.equals(READ)){
            buildReadQuery();
        }
    }
    
    void buildReadQuery() {
        query = "SELECT nameoncard,expdate,cardtype,cardnum "+
                "FROM Customer "+
                "WHERE custid = '"+custId+"'";
    }
    
 
    public void populateEntity(ResultSet resultSet) throws DatabaseException {
        try {
        
            //we take the first returned row
            if(resultSet.next()){
                defaultPaymentInfo = new CreditCard(resultSet.getString("nameoncard"),
                                               resultSet.getString("expdate"),
                                               resultSet.getString("cardtype"),
                                               resultSet.getString("cardnum"));
            }
        }
        catch(SQLException e){
            throw new DatabaseException(e);
        }
    }
    public String getDbUrl() {
    	DbConfigProperties props = new DbConfigProperties();	
    	return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
        
    }
    
    public String getQuery() {
        return query;
    }
}
