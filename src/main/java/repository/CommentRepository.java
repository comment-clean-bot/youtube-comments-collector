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

  //arg를 포함하는 모든 commentId에 해당하는 comment 리스트를 반환
  List<Comment> findByIncludeCommentId(String commentId);
}
