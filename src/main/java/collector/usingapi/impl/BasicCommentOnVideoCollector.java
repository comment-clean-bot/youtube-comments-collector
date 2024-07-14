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
  private final static int PRE_LABEL_MIN_LENGTH = 1000;
  private final static int PRE_LABEL_THRESHOLD = 300;

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
        maxResults == 0 ? null: maxResults,
        replyCollector,
        commentFilters
    );

    List<Comment> output = new ArrayList<>();
    int collectedCommentCount = 0; // need to be erased
    int collectedTopLevelCommentCount = 0; // need to be erased
    while (youtubeCommentListApi.hasNextPage()) {
      output.addAll(youtubeCommentListApi.requestNextPage());
    }

    return preLabelComments(output).stream();
  }

  public List<Comment> preLabelComments(List<Comment> list) {
    if (list.size() < PRE_LABEL_MIN_LENGTH) {
      return list;
    }
    List<Comment> sortedList = list.stream()
        .filter(c -> c.parentId() == null || c.parentId().isEmpty() || c.id().equals(c.parentId()))
        .sorted((c1, c2) -> (int) (c2.likeCount() - c1.likeCount())).toList();

    System.out.println("Comments preLabel");
    List<Comment> output = new ArrayList<>();
    for (int i = 0; i < sortedList.size(); i++) {
      if (i < PRE_LABEL_THRESHOLD) {
        System.out.println(sortedList.get(i).likeCount() + " " + sortedList.get(i).text());
        output.add(sortedList.get(i));
      } else {
        output.add(sortedList.get(i).withPreLabel(false));
      }
    }
    return output;
  }
}
