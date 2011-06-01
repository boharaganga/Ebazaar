package business.ordersubsystem;

import java.sql.ResultSet;
import middleware.DatabaseException;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.externalinterfaces.DbConfigKey;
import middleware.externalinterfaces.IDbClass;
import business.externalinterfaces.IOrderItem;

class DbClassOrderItem implements IDbClass {

	private String query;
	private String queryType;
	private final String SAVE = "Save";
	private IOrderItem orderItem;

	public DbClassOrderItem(IOrderItem orderItem) {

	}

	public void buildQuery() {
		if (queryType.equals(SAVE)) {
			buildSaveOrderItemQuery();
		}
	}

	private void buildSaveOrderItemQuery() {
		query = "INSERT INTO OrderItem VALUES(" + orderItem.getLineitemid()
				+ ", " + orderItem.getOrderid() + ","
				+ orderItem.getProductid() + "," + orderItem.getQuantity()
				+ ");";

	}

	public void populateEntity(ResultSet resultSet) throws DatabaseException {
		throw new UnsupportedOperationException(
				"populateEntity() not supported in DbClassOrderItem.java");
	}

	public String getDbUrl() {
		DbConfigProperties props = new DbConfigProperties();
		return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());

	}

	public String getQuery() {
		return query;
	}

	public void submitOrderItem() throws DatabaseException {
		this.queryType = SAVE;
		DataAccessSubsystemFacade.INSTANCE.save(this);
	}

}
