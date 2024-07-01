package container;

import core.Comment;
import core.ICommentProcessor;
import core.ICommentsContainer;
import java.util.ArrayList;
import java.util.List;

public class ListContainer implements ICommentsContainer {

  private final List<Comment> data;
  private final ICommentProcessor processor;

  public ListContainer(ICommentProcessor processor) {
    this.data = new ArrayList<>();
    this.processor = processor;
  }

  @Override
  public void addData(Comment newComment) {
    data.add(newComment);
  }

  @Override
  public void flush() {
    data.forEach(processor::commitData);
    data.clear();
  }
}
