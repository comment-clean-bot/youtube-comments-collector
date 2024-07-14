package collector.usingapi;

import collector.usingapi.responsevo.CommentResponse;
import collector.usingapi.responsevo.CommentThreadResponse;
import core.Comment;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExtractOnResponseReplyCollector implements ReplyCollector {

  @Override
  public List<Comment> collectReplies(CommentThreadResponse commentThread) {
    if (commentThread.getReplies() == null) {
      return new ArrayList<>();
    }
    return commentThread.getReplies().getComments().stream().map(
        CommentResponse::toComment
    ).collect(Collectors.toList());
  }
}
