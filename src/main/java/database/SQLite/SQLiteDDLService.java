package database.SQLite;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDDLService extends SQLiteManager{
  public SQLiteDDLService (){
  }
  public SQLiteDDLService (String dbPath){
    super(dbPath);
  }

  public QueryResult executeQuery(final String query) throws SQLException {
    Connection conn = null;
    QueryResult result = null;
    Statement stmt = null;

    try{
      conn = ensureConnection();
      stmt = conn.createStatement();
      stmt.execute(query);
      result = QueryResult.SUCCESS;
    } catch (SQLException e) {
      e.printStackTrace();
      if (conn != null) {
        conn.rollback();
      }
      result = QueryResult.FAILURE;
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
    return result;
  }

  public boolean checkTableExists(String tableName) throws SQLException {
    if (executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'").equals(QueryResult.SUCCESS)) {
      return true;
    }
    return false;
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
    return executeQuery("DROP TABLE " + tableName);
  }
}
