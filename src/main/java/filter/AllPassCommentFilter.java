package filter;

import core.Comment;
import core.filter.ICommentFilter;

public class AllPassCommentFilter implements ICommentFilter {

  @Override
  public boolean isAcceptable(Comment comment) {
    return true;
  }
}
