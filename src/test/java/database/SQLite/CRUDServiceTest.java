package database.SQLite;

import database.CollectedComment;
import database.CommentType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CRUDServiceTest {
  DDLService ddlService = new DDLService("jdbc:sqlite:./test.db");
  DMLService dmlService = new DMLService("jdbc:sqlite:./test.db");

  DQLService dqlService = new DQLService("jdbc:sqlite:./test.db");

  @Test
  public void insert() {
    CollectedComment comment = new CollectedComment(1,"channel_id", "parent_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), CommentType.YOUTUBE, "comment_id");
    try {
      ddlService.dropTable("collected_comment");
      ddlService.createTable("collected_comment", "id INTEGER PRIMARY KEY autoincrement, "
          + "channel_id TEXT, parent_id TEXT, comment_id TEXT, "
          + "author_id TEXT, like_count INTEGER, "
          + "published_at TEXT, updated_at TEXT, "
          + "comment_type TEXT, text TEXT");
      int inserted = dmlService.insertCollectedComment(comment);
      List<CollectedComment> selected = dqlService.selectCollectedCommentsWithFields("id", "1");
      Assertions.assertEquals(1, selected.size());
      Assertions.assertEquals(1, inserted);
      Assertions.assertEquals(comment.getCommentId(), selected.get(0).getCommentId());
      dmlService.closeConnection();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void update() {
    CollectedComment comment = new CollectedComment(1,"channel_id", "parent_id", "comment_id", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), CommentType.YOUTUBE, "text");
    try {
      ddlService.dropTable("collected_comment");
      ddlService.createTable("collected_comment", "id INTEGER PRIMARY KEY autoincrement, "
          + "channel_id TEXT, parent_id TEXT, comment_id TEXT, "
          + "author_id TEXT, like_count INTEGER, "
          + "published_at TEXT, updated_at TEXT, "
          + "comment_type TEXT, text TEXT");
      int inserted = dmlService.insertCollectedComment(comment);
      Assertions.assertEquals(1, inserted);
      Map<String,Object> updateMap = Map.of("like_count", 20);
      int updated = dmlService.updateCollectedComments(comment, updateMap);
      Assertions.assertEquals(1, updated);
      List<CollectedComment> selected = dqlService.selectCollectedCommentsWithFields("id", "1");
      Assertions.assertEquals(1, selected.size());
      Assertions.assertEquals(20, selected.get(0).getLikeCount());
      dmlService.closeConnection();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void delete() {
    CollectedComment comment = new CollectedComment(1,"channel_id", "parent_id", "comment_id", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), CommentType.YOUTUBE, "text");
    try {
      ddlService.dropTable("collected_comment");
      ddlService.createTable("collected_comment", "id INTEGER PRIMARY KEY autoincrement, "
          + "channel_id TEXT, parent_id TEXT, comment_id TEXT, "
          + "author_id TEXT, like_count INTEGER, "
          + "published_at TEXT, updated_at TEXT, "
          + "comment_type TEXT, text TEXT");
      int inserted = dmlService.insertCollectedComment(comment);
      Assertions.assertEquals(1, inserted);
      int deleted = dmlService.deleteCollectedComment("id", "1");
      Assertions.assertEquals(1, deleted);
      List<CollectedComment> selected = dqlService.selectCollectedCommentsWithFields("id", "1");
      Assertions.assertEquals(0, selected.size());
      dmlService.closeConnection();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void insertList() {
    CollectedComment comment1 = new CollectedComment(1,"channel_id", "parent_id", "comment_id", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), CommentType.YOUTUBE, "text");
    CollectedComment comment2 = new CollectedComment(2,"channel_id", "parent_id", "comment_id", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), CommentType.YOUTUBE, "text");
    try {
      ddlService.dropTable("collected_comment");
      ddlService.createTable("collected_comment", "id INTEGER PRIMARY KEY autoincrement, "
          + "channel_id TEXT, parent_id TEXT, comment_id TEXT, "
          + "author_id TEXT, like_count INTEGER, "
          + "published_at TEXT, updated_at TEXT, "
          + "comment_type TEXT, text TEXT");
      int inserted = dmlService.insertCollectedComments(List.of(comment1, comment2));
      List<CollectedComment> selected = dqlService.selectCollectedCommentsWithFields("id", "1");
      Assertions.assertEquals(1, selected.size());
      Assertions.assertEquals(2, inserted);
      Assertions.assertEquals(comment1.getCommentId(), selected.get(0).getCommentId());
      dmlService.closeConnection();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
