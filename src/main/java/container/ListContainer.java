package container;

import core.Comment;
import core.ICommentProcessor;
import core.ICommentsContainer;
import java.util.ArrayList;
import java.util.List;

public class ListContainer implements ICommentsContainer {

  private final ICommentProcessor processor;
  private final int batchSize;

  private final List<Comment> data;

  public ListContainer(ICommentProcessor processor, int batchSize) {
    this.processor = processor;
    this.batchSize = batchSize;
    this.data = new ArrayList<>();
  }

  @Override
  public void addData(Comment newComment) {
    data.add(newComment);
    if (data.size() >= batchSize) {
      flush();
    }
  }

  @Override
  public void addDatas(List<Comment> newComments) {
    data.addAll(newComments);
    if (data.size() >= batchSize) {
      flush();
    }
  }

  @Override
  public void flush() {
    data.forEach(processor::commitData);
    data.clear();
  }
}
