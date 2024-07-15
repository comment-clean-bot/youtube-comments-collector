package core.filter;

import core.Comment;

public interface ICommentFilter {
  boolean isAcceptable(Comment comment);
}
