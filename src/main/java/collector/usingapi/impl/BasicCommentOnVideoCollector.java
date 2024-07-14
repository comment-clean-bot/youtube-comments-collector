package collector.usingapi.impl;

import collector.usingapi.CommentOnVideoCollector;
import collector.usingapi.ReplyCollector;
import core.Video;
import collector.usingapi.api.YoutubeCommentListApi;
import collector.usingapi.requestvo.CommentThreadRequestPart;
import core.Comment;
import core.filter.ICommentFilter;
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
  private final List<ICommentFilter> commentFilters;

  public BasicCommentOnVideoCollector(String apiKey, String baseUrl,
      int pageSize, Integer maxResults, ReplyCollector replyCollector,
      List<ICommentFilter> commentFilters) {
    this.apiKey = apiKey;
    this.baseUrl = baseUrl;
    this.pageSize = pageSize;
    this.maxResults = maxResults;
    this.replyCollector = replyCollector;
    this.commentFilters = commentFilters;
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
        replyCollector,
        commentFilters
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
