package repository;

import core.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentRepository {
  void save(Comment comment);
  boolean existsByVideoId(String videoId);
  boolean existsByCommentId(String commentId);
  List<Comment> findByVideoId(String videoId);
  List<Comment> findByChannelId(String channelId);
  Optional<Comment> findByCommentId(String commentId);
  List<Comment> findByParentId(String parentId);
}