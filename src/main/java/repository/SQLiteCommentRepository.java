package repository;

import core.Comment;
import database.CollectedComment;
import database.CommentMapper;
import database.SQLite.DMLService;
import database.SQLite.DQLService;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLiteCommentRepository implements CommentRepository{
  private final DMLService dmlService;
  private final DQLService dqlService;

  public SQLiteCommentRepository(String dbUrl) {
    this.dmlService = new DMLService(dbUrl);
    this.dqlService = new DQLService(dbUrl);
  }
  public SQLiteCommentRepository() {
    this.dmlService = new DMLService();
    this.dqlService = new DQLService();
  }
  @Override
  public int save(Comment comment) {
    try {
      CollectedComment collectedComment = CommentMapper.toCollectedComment(comment);
      return dmlService.insertCollectedComment(collectedComment);
    } catch (SQLException e){
      e.printStackTrace();
      return 0;
    }
  }

  @Override
  public int saveAll(List<Comment> comments) {
    List<CollectedComment> collectedComments = new ArrayList<>();
    for (Comment comment : comments) {
      collectedComments.add(CommentMapper.toCollectedComment(comment));
    }
    return dmlService.insertCollectedComments(collectedComments);
  }

  @Override
  public boolean existsByVideoId(String videoId) {
    List<CollectedComment> collectedComments = dqlService.selectCollectedCommentsWithFields("video_id", videoId);
    return !collectedComments.isEmpty();
  }

  @Override
  public boolean existsByCommentId(String commentId) {
    List<CollectedComment> collectedComments = dqlService.selectCollectedCommentsWithFields("comment_id", commentId);
    return !collectedComments.isEmpty();
  }

  @Override
  public List<Comment> findByVideoId(String videoId) {
    List<CollectedComment> collectedComments = dqlService.selectCollectedCommentsWithFields("video_id", videoId);
    List<Comment> comments = new ArrayList<>();
    for (CollectedComment collectedComment : collectedComments) {
      comments.add(CommentMapper.toComment(collectedComment));
    }
    return comments;
  }

  @Override
  public List<Comment> findByChannelId(String channelId) {
    List<CollectedComment> collectedComments = dqlService.selectCollectedCommentsWithFields("channel_id", channelId);
    List<Comment> comments = new ArrayList<>();
    for (CollectedComment collectedComment : collectedComments) {
      comments.add(CommentMapper.toComment(collectedComment));
    }
    return comments;
  }

  @Override
  public Optional<Comment> findByCommentId(String commentId) {
    List<CollectedComment> collectedComments = dqlService.selectCollectedCommentsWithFields("comment_id", commentId);
    if (collectedComments.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(CommentMapper.toComment(collectedComments.get(0)));
  }

  @Override
  public List<Comment> findByParentId(String parentId) {
    List<CollectedComment> collectedComments = dqlService.selectCollectedCommentsWithFields("parent_id", parentId);
    List<Comment> comments = new ArrayList<>();
    for (CollectedComment collectedComment : collectedComments) {
      comments.add(CommentMapper.toComment(collectedComment));
    }
    return comments;
  }
}
