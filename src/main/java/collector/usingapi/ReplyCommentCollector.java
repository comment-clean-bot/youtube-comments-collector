package collector.usingapi;

import core.Comment;
import core.ICommentsContainer;

public class ReplyCommentCollector {

  private final ICommentsContainer container;

  private final Comment comment;

  public ReplyCommentCollector(ICommentsContainer container,
      Comment comment) {
    this.container = container;
    this.comment = comment;
  }

  public boolean hasNextPage() {
    // do something
    return true;
  }

  public void collectNextPage() {
    // do something
  }

  public void collectOnVideo() {
    while (hasNextPage()) {
      collectNextPage();
    }
  }
}
