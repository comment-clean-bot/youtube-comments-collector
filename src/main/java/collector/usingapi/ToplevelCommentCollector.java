package collector.usingapi;

import core.ICommentsContainer;

public class ToplevelCommentCollector {

  private final ICommentsContainer container;

  private final Video video;

  public ToplevelCommentCollector(ICommentsContainer container,
      Video video) {
    this.container = container;
    this.video = video;
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
