package database.SQLite;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DDLService extends SQLiteManager{
  public DDLService(){
  }
  public DDLService(String dbPath){
    super(dbPath);
  }

  public QueryResult executeQuery(final String query) throws SQLException {
    Connection conn = null;
    QueryResult result = QueryResult.FAILURE;
    Statement stmt = null;

    try{
      conn = getConnection();
      stmt = conn.createStatement();
      stmt.execute(query);
      result = QueryResult.SUCCESS;
      conn.commit();
    } catch (SQLException e) {
      e.printStackTrace();
      conn.rollback();

      result = QueryResult.FAILURE;
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
    return result;
  }

  public boolean checkTableExists(String tableName) throws SQLException {
    Connection conn = getConnection();
    DatabaseMetaData meta = conn.getMetaData();
    ResultSet tables = meta.getTables(null, null, tableName, null);

    return ( tables.next() ? tables.getRow() != 0 : false );
  }

  public QueryResult createTable(String tableName, String columns) throws SQLException {
    if (checkTableExists(tableName)) {
      return QueryResult.WARNING;
    }
    return executeQuery("CREATE TABLE IF NOT EXISTS " + tableName + " (" + columns + ")");
  }

  public QueryResult dropTable(String tableName) throws SQLException {
    if (!checkTableExists(tableName)) {
      return QueryResult.WARNING;
    }
    return executeQuery("DROP TABLE IF EXISTS " + tableName);
  }
}
