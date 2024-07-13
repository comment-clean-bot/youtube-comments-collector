package repository;

import core.Comment;
import java.util.List;
import java.util.Optional;

public class SQLiteCommentRepository implements CommentRepository{

  @Override
  public void save(Comment comment) {

  }

  @Override
  public boolean existsByVideoId(String videoId) {
    return false;
  }

  @Override
  public boolean existsByCommentId(String commentId) {
    return false;
  }

  @Override
  public List<Comment> findByVideoId(String videoId) {
    return null;
  }

  @Override
  public List<Comment> findByChannelId(String channelId) {
    return null;
  }

  @Override
  public Optional<Comment> findByCommentId(String commentId) {
    return Optional.empty();
  }

  @Override
  public List<Comment> findByParentId(String parentId) {
    return null;
  }
}
