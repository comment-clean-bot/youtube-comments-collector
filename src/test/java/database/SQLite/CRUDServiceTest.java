package database.SQLite;

import database.CollectedComment;
import database.CommentType;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CRUDServiceTest {
  DMLService dmlService = new DMLService("jdbc:sqlite:./test.db");

  DQLService dqlService = new DQLService("jdbc:sqlite:./test.db");

  @BeforeEach
  public void setUp() throws SQLException {
    DDLService ddlService = new DDLService("jdbc:sqlite:./test.db");
    ddlService.dropTable("collected_comment");
    ddlService.createTable("collected_comment", "id INTEGER PRIMARY KEY autoincrement, "
        + "channel_id TEXT, parent_id TEXT, comment_id TEXT, "
        + "author_id TEXT, like_count INTEGER, "
        + "published_at TEXT, updated_at TEXT, "
        + "comment_type TEXT, text TEXT, pre_label INTEGER, video_id TEXT");
    ddlService.closeConnection();
  }

  @AfterEach
  public void tearDown() {
    File file = new File("./test.db");
    file.delete();
  }


  @Test
  public void insert() {
    CollectedComment comment = new CollectedComment(1,"channel_id", "parent_id", "video_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), CommentType.YOUTUBE, "comment_id",false);
    try {
      int inserted = dmlService.insertCollectedComment(comment);
      List<CollectedComment> selected = dqlService.selectCollectedCommentsWithFields("id", "1");
      Assertions.assertEquals(1, selected.size());
      Assertions.assertEquals(1, inserted);
      Assertions.assertEquals(comment.getCommentId(), selected.get(0).getCommentId());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void update() {
    CollectedComment comment = new CollectedComment(1,"channel_id", "parent_id", "video_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), CommentType.YOUTUBE, "comment_id",false);
    try {
      int inserted = dmlService.insertCollectedComment(comment);
      Assertions.assertEquals(1, inserted);
      Map<String,Object> updateMap = new HashMap<>(Map.of("like_count", 20));
      int updated = dmlService.updateCollectedComment(comment, updateMap);

      List<CollectedComment> selected = dqlService.selectCollectedCommentsWithFields("id", "1");
      Assertions.assertEquals(1, updated);
      Assertions.assertEquals(1, selected.size());
      Assertions.assertEquals(20, selected.get(0).getLikeCount());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void delete() {
    CollectedComment comment = new CollectedComment(1,"channel_id", "parent_id", "video_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), CommentType.YOUTUBE, "comment_id",false);
    try {
      int inserted = dmlService.insertCollectedComment(comment);
      Assertions.assertEquals(1, inserted);
      int deleted = dmlService.deleteCollectedComment("id", "1");
      Assertions.assertEquals(1, deleted);
      List<CollectedComment> selected = dqlService.selectCollectedCommentsWithFields("id", "1");
      Assertions.assertEquals(0, selected.size());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void insertList() {
    CollectedComment comment1 = new CollectedComment(1,"channel_id", "parent_id", "video_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), CommentType.YOUTUBE, "comment_id",false);
    CollectedComment comment2 = new CollectedComment(2,"channel_id", "parent_id", "video_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), CommentType.YOUTUBE, "comment_id2",false);
    try {
      int inserted = dmlService.insertCollectedComments(List.of(comment1, comment2));
      List<CollectedComment> selected = dqlService.selectCollectedCommentsWithFields("id", "1");
      Assertions.assertEquals(1, selected.size());
      Assertions.assertEquals(2, inserted);
      Assertions.assertEquals(comment1.getCommentId(), selected.get(0).getCommentId());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void updateList() {
    CollectedComment comment1 = new CollectedComment(1,"channel_id", "parent_id", "video_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), CommentType.YOUTUBE, "comment_id",false);
    CollectedComment comment2 = new CollectedComment(2,"channel_id", "parent_id", "video_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), CommentType.YOUTUBE, "comment_id2",false);
    try {
      int inserted = dmlService.insertCollectedComments(List.of(comment1, comment2));
      Assertions.assertEquals(2, inserted);
      CollectedComment updateComment1 = new CollectedComment(1,"channel_id", "parent_id", "video_id", "text", "author_id", 20,
          LocalDateTime.now(), LocalDateTime.now(), CommentType.YOUTUBE, "comment_id",false);
      int updated = dmlService.updateCollectedComments(List.of(updateComment1, comment2));

      List<CollectedComment> selected = dqlService.selectCollectedCommentsWithFields("id", "1");
      System.out.println(selected.toString());
      Assertions.assertEquals(2, updated);
      Assertions.assertEquals(1, selected.size());
      Assertions.assertEquals(20, selected.get(0).getLikeCount());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
