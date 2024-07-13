package database.SQLite;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DDLServiceTest {
  DDLService ddlService = new DDLService("jdbc:sqlite:./test.db");

  @AfterEach
  public void afterEach() {
    try{
      ddlService.dropTable("test");
      ddlService.closeConnection();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void 테이블_생성() {
    try {
      QueryResult result = ddlService.createTable("test", "id INTEGER PRIMARY KEY, name TEXT");
      Assertions.assertEquals(result.name(), "SUCCESS");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void 테이블_삭제() {
    try {
      QueryResult result = ddlService.dropTable("test");
      // 생성 된 테이블이 없기 때문에
      Assertions.assertEquals(result.name(), "WARNING");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void 테이블_생성_후_삭제() {
    try {
      QueryResult result = ddlService.createTable("test", "id INTEGER PRIMARY KEY, name TEXT");
      Assertions.assertEquals(result.name(), "SUCCESS");
      result = ddlService.dropTable("test");
      Assertions.assertEquals(result.name(), "SUCCESS");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void 테이블_존재_여부_확인() {
    try {
      QueryResult result = ddlService.createTable("test", "id INTEGER PRIMARY KEY, name TEXT");
      Assertions.assertEquals(result.name(), "SUCCESS");
      boolean res = ddlService.checkTableExists("test");
      Assertions.assertTrue(res);
      res = ddlService.checkTableExists("test2");
      Assertions.assertFalse(res);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
