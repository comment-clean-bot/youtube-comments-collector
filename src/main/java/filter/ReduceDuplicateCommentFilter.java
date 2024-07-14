package filter;

import core.Comment;
import core.filter.ICommentFilter;
import java.time.LocalDateTime;
import repository.CommentRepository;

/**
 * ReduceDuplicateCommentFilter
 *
 * It is used for reducing duplicate top level comments.
 * This filter only accepts comments which are not duplicated
 * and newly added comments even if they are duplicated.
 */
public class ReduceDuplicateCommentFilter implements ICommentFilter {

  private final CommentRepository commentRepository;

  private final LocalDateTime timeBasis;

  public ReduceDuplicateCommentFilter(
      CommentRepository commentRepository, LocalDateTime timeBasis) {
    this.commentRepository = commentRepository;
    this.timeBasis = timeBasis;
  }

  @Override
  public boolean isAcceptable(Comment comment) {
    if (commentRepository.existsByCommentId(comment.id())) {
      return false;
    }
    return comment.publishedAt().isAfter(timeBasis);
  }
}
