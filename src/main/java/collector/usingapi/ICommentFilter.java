package collector.usingapi;

import core.Comment;

public interface ICommentFilter {
  boolean isAcceptable(Comment comment);
}
