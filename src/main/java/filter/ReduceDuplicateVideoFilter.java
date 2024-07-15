package filter;

import core.Comment;
import java.time.LocalDateTime;
import repository.CommentRepository;

public class ReduceDuplicateVideoFilter {

  private final CommentRepository commentRepository;

  private final LocalDateTime timeBasis;

  public ReduceDuplicateVideoFilter(
      CommentRepository commentRepository, LocalDateTime timeBasis) {
    this.commentRepository = commentRepository;
    this.timeBasis = timeBasis;
  }

  public boolean isAcceptable(Comment comment) {
    if (commentRepository.existsByCommentId(comment.id())) {
      return false;
    }
    return comment.publishedAt().isAfter(timeBasis);
  }
}
