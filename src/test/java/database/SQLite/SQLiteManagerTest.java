package database.SQLite;

import java.sql.Connection;
import org.junit.jupiter.api.Test;

public class SQLiteManagerTest {

  @Test
  void 생성후조회() {
    SQLiteManager sqLiteManager = new SQLiteManager();
    sqLiteManager.createConnection();
    sqLiteManager.closeConnection();
    sqLiteManager.ensureConnection();
  }
}
