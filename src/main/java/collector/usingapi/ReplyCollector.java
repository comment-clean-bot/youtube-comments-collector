package collector.usingapi;

import collector.usingapi.responsevo.CommentThreadResponse;
import core.Comment;
import java.util.List;

public interface ReplyCollector {

  List<Comment> collectReplies(CommentThreadResponse commentThread);

}
