package collector.usingapi;

import collector.usingapi.responsevo.CommentResponse;
import collector.usingapi.responsevo.CommentThreadResponse;
import collector.usingapi.responsevo.CommentThreadsResponse;
import core.Comment;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExtractOnResponseReplyCollector implements ReplyCollector {

  @Override
  public List<Comment> collectReplies(CommentThreadsResponse commentThreads) {
    return commentThreads.getItems().stream().flatMap(this::extractReplies).collect(
        Collectors.toList());
  }

  private Stream<Comment> extractReplies(CommentThreadResponse commentThread) {
    if (commentThread.getReplies() == null) {
      return Stream.empty();
    }
    return commentThread.getReplies().getComments().stream().map(
        CommentResponse::toComment
    );
  }
}
