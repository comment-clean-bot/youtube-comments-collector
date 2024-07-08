package collector.usingapi;

import collector.usingapi.requestvo.CommentThreadRequestPart;
import core.Comment;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BasicCommentOnVideoCollector implements CommentOnVideoCollector {

  private final String apiKey;
  private final String baseUrl;
  private final int pageSize;
  private final int maxResults;

  private final ReplyCollector replyCollector;

  public BasicCommentOnVideoCollector(String apiKey, String baseUrl,
      int pageSize, int maxResults,
      ReplyCollector replyCollector) {
    this.apiKey = apiKey;
    this.baseUrl = baseUrl;
    this.pageSize = pageSize;
    this.maxResults = maxResults;
    this.replyCollector = replyCollector;
  }

  @Override
  public List<Comment> collectComments(Video video) {
    return extractComments(video).collect(Collectors.toList());
  }

  private Stream<Comment> extractComments(Video video) {
    YoutubeCommentListApi youtubeCommentListApi = new YoutubeCommentListApi(
        apiKey,
        baseUrl,
        Set.of(CommentThreadRequestPart.ID,
            CommentThreadRequestPart.SNIPPET,
            CommentThreadRequestPart.REPLY),
        video.id(),
        pageSize,
        maxResults,
        replyCollector
    );

    List<Comment> output = new ArrayList<>();
    int collectedCommentCount = 0; // need to be erased
    int collectedTopLevelCommentCount = 0; // need to be erased
    while (youtubeCommentListApi.hasNextPage()) {
      output.addAll(youtubeCommentListApi.requestNextPage());
      collectedCommentCount = youtubeCommentListApi.getTotalCommentCount(); // need to be erased
      collectedTopLevelCommentCount = youtubeCommentListApi.getTotalTopLevelCommentCount(); // need to be erased
    }

    System.out.println("collectedTopLevelCommentCount = " + collectedTopLevelCommentCount); // need to be erased
    System.out.println("collectedCommentCount = " + collectedCommentCount); // need to be erase
    return output.stream();
  }
}