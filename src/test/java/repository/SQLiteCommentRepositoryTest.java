package repository;

import core.Comment;
import database.SQLite.DDLService;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SQLiteCommentRepositoryTest {
  SQLiteCommentRepository sqLiteCommentRepository = new SQLiteCommentRepository("jdbc:sqlite:./test.db");

  @BeforeEach
  public void setUp() {
    File file = new File("./test.db");
    file.delete();
    DDLService ddlService = new DDLService("jdbc:sqlite:./test.db");
    ddlService.dropTable("collected_comment");
    ddlService.createTable("collected_comment", "id INTEGER PRIMARY KEY autoincrement, "
        + "channel_id TEXT, parent_id TEXT, comment_id TEXT, "
        + "author_id TEXT, like_count INTEGER, "
        + "published_at TEXT, updated_at TEXT, "
        + "comment_type TEXT, text TEXT, pre_label INTEGER, video_id TEXT");
    ddlService.closeConnection();
  }

  @AfterAll
  public static void tearDown() {
    File file = new File("./test.db");
    file.delete();
  }

  @Test
  public void save() {
    Comment comment = new Comment("comment_id","channel_id", "video_id", "parent_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), false);
    int inserted = sqLiteCommentRepository.save(comment);
    Optional<Comment> saved = sqLiteCommentRepository.findByCommentId("comment_id");
    Assertions.assertEquals(1, inserted);
    Assertions.assertTrue(saved.isPresent());
    Assertions.assertEquals(comment.id(), saved.get().id());
  }

  @Test
  public void saveAll() {
    Comment comment1 = new Comment("comment_id1-1","channel_id", "video_id1-1", "parent_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), false);
    Comment comment2 = new Comment("comment_id1-2","channel_id", "video_id1-1", "parent_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), false);
    List<Comment> comments = List.of(comment1, comment2);
    int inserted = sqLiteCommentRepository.saveAll(comments);
    List<Comment> savedComments = sqLiteCommentRepository.findByVideoId("video_id1-1");
    Assertions.assertEquals(2, inserted);
    Assertions.assertEquals(2, savedComments.size());
    Assertions.assertEquals(comment1.id(), savedComments.get(0).id());
  }

  @Test
  public void existsByVideoIdIfExist() {
    Comment comment = new Comment("comment_id2-1","channel_id", "video_id2-1", "parent_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), false);
    Comment comment2 = new Comment("comment_id2-2","channel_id", "video_id2-2", "parent_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), false);
    sqLiteCommentRepository.save(comment);
    sqLiteCommentRepository.save(comment2);
    boolean exists = sqLiteCommentRepository.existsByVideoId("video_id2-1");
    Assertions.assertTrue(exists);
  }

  @Test
  public void existsByVideoIdIfNotExist() {
    Comment comment2 = new Comment("comment_id3-1","channel_id", "video_id3-1", "parent_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), false);
    sqLiteCommentRepository.save(comment2);
    boolean exists = sqLiteCommentRepository.existsByVideoId("video_id3-0");
    Assertions.assertFalse(exists);
  }

  @Test
  public void existsByCommentIdIfExists() {
    Comment comment1 = new Comment("comment_id4-1","channel_id", "video_id4-1", "parent_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), false);
    Comment comment2 = new Comment("comment_id4-2","channel_id", "video_id4-2", "parent_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), false);
    sqLiteCommentRepository.save(comment1);
    sqLiteCommentRepository.save(comment2);
    boolean exists = sqLiteCommentRepository.existsByCommentId("comment_id4-1");
    Assertions.assertTrue(exists);
  }

  @Test
  public void existsByCommentIdIfNotExist() {
    Comment comment2 = new Comment("comment_id5-1","channel_id", "video_id5-1", "parent_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), false);
    sqLiteCommentRepository.save(comment2);
    boolean exists = sqLiteCommentRepository.existsByCommentId("comment_id5-0");
    Assertions.assertFalse(exists);
  }

  @Test
  public void findByVideoId() {
    Comment comment1 = new Comment("comment_id6-1","channel_id", "video_id6-1", "parent_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), false);
    Comment comment2 = new Comment("comment_id6-2","channel_id", "video_id6-1", "parent_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), false);
    Comment comment3 = new Comment("comment_id6-3","channel_id", "video_id6-2", "parent_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), false);
    sqLiteCommentRepository.save(comment1);
    sqLiteCommentRepository.save(comment2);
    sqLiteCommentRepository.save(comment3);
    List<Comment> comments = sqLiteCommentRepository.findByVideoId("video_id6-1");
    Assertions.assertEquals(2, comments.size());
  }

  @Test
  public void findByChannelId() {
    Comment comment1 = new Comment("comment_id7-1","channel_id7-1", "video_id7-1", "parent_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), false);
    Comment comment2 = new Comment("comment_id7-2","channel_id7-1", "video_id7-1", "parent_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), false);
    Comment comment3 = new Comment("comment_id7-3","channel_id7-2", "video_id7-2", "parent_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), false);
    sqLiteCommentRepository.saveAll(List.of(comment1, comment2, comment3));
    List<Comment> comments = sqLiteCommentRepository.findByChannelId("channel_id7-1");
    Assertions.assertEquals(2, comments.size());
  }

  @Test
  public void findByCommentId() {
    Comment comment1 = new Comment("comment_id8-1","channel_id", "video_id8-1", "parent_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), false);
    Comment comment2 = new Comment("comment_id8-2","channel_id", "video_id8-1", "parent_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), false);
    Comment comment3 = new Comment("comment_id8-3","channel_id2", "video_id8-2", "parent_id", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), false);
    sqLiteCommentRepository.saveAll(List.of(comment1, comment2, comment3));
    Optional<Comment> comment = sqLiteCommentRepository.findByCommentId("comment_id8-1");
    Assertions.assertTrue(comment.isPresent());
    Assertions.assertEquals(comment1.id(), comment.get().id());
  }

  @Test
  public void findByParentId() {
    Comment comment1 = new Comment("comment_id9-1","channel_id", "video_id", "parent_id9-1", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), false);
    Comment comment2 = new Comment("comment_id9-2","channel_id", "video_id", "parent_id9-1", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), false);
    Comment comment3 = new Comment("comment_id9-3","channel_id2", "video_id2", "parent_id9-2", "text", "author_id", 10,
        LocalDateTime.now(), LocalDateTime.now(), false);
    sqLiteCommentRepository.saveAll(List.of(comment1, comment2, comment3));
    List<Comment> comments = sqLiteCommentRepository.findByParentId("parent_id9-1");
    Assertions.assertEquals(2, comments.size());
  }
}
