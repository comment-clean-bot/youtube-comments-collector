package collector.usingapi;

import collector.usingapi.responsevo.CommentThreadsResponse;
import core.Comment;
import java.util.List;

public interface ReplyCollector {

  List<Comment> collectReplies(CommentThreadsResponse commentThreads);

}
