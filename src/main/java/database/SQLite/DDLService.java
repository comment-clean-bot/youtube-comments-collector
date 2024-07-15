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

  public QueryResult executeQuery(final String query) {
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
      try {
        conn.rollback();
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
      result = QueryResult.FAILURE;
    } finally {
      if (stmt != null) {
        try {
          stmt.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return result;
  }

  public boolean checkTableExists(String tableName) {
    boolean result = false;
    try {
      Connection conn = getConnection();
      DatabaseMetaData meta = conn.getMetaData();
      ResultSet tables = meta.getTables(null, null, tableName, null);

      result = tables.next() ? tables.getRow() != 0 : false;
    } catch (SQLException e) {
      e.printStackTrace();
      result = false;
    }
    return result;
  }

  public QueryResult createTable(String tableName, String columns) {
    if (checkTableExists(tableName)) {
      return QueryResult.WARNING;
    }
    return executeQuery("CREATE TABLE IF NOT EXISTS " + tableName + " (" + columns + ")");
  }

  public QueryResult dropTable(String tableName) {
    if (!checkTableExists(tableName)) {
      return QueryResult.WARNING;
    }
    return executeQuery("DROP TABLE IF EXISTS " + tableName);
  }
}
