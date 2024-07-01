package collector.usingapi;

import core.ICommentCollector;
import core.ICommentsContainer;

public class CommentCollector implements ICommentCollector {

  final private ICommentsContainer container;

  final private TargetVideoSelector targetVideoSelector;

  public CommentCollector(
      ICommentsContainer container,
      TargetVideoSelector targetVideoSelector) {
    this.container = container;
    this.targetVideoSelector = targetVideoSelector;
  }

  @Override
  public void collect() {
    targetVideoSelector.select().forEach(video -> {
      // do something
      container.flush();
    });
  }
}
