package collector.usingapi;

import core.Comment;

public class AllPassCommentFilter implements ICommentFilter {

  @Override
  public boolean isAcceptable(Comment comment) {
    return true;
  }
}
