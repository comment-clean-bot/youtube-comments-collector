package collector.usingapi;

import core.Comment;
import java.util.List;

public interface CommentOnVideoCollector {

  List<Comment> collectComments(Video video); // Video 없애고 Argument를 VideoResponse로 바꾸고 싶어

}
