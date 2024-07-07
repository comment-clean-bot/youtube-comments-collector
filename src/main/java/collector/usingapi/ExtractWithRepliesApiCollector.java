package collector.usingapi;

import collector.usingapi.requestvo.CommentRequestPart;
import collector.usingapi.responsevo.CommentThreadResponse;
import collector.usingapi.responsevo.CommentThreadsResponse;
import core.Comment;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExtractWithRepliesApiCollector implements ReplyCollector {

  private final String apiKey;
  private final String baseUrl;
  private final int pageSize;
  private final int maxResults;

  public ExtractWithRepliesApiCollector(String apiKey, String baseUrl,
      int pageSize, int maxResults) {
    this.apiKey = apiKey;
    this.baseUrl = baseUrl;
    this.pageSize = pageSize;
    this.maxResults = maxResults;
  }

  @Override
  public List<Comment> collectReplies(CommentThreadsResponse commentThreads) {
    return commentThreads.getItems().stream().flatMap(this::extractReplies).collect(
        Collectors.toList());
  }

  private Stream<Comment> extractReplies(CommentThreadResponse commentThread) {
    if (commentThread.getReplies() == null) {
      return Stream.empty();
    }
    YoutubeRepliesApi youtubeRepliesApi = new YoutubeRepliesApi(
        apiKey,
        baseUrl,
        Set.of(CommentRequestPart.ID, CommentRequestPart.SNIPPET),
        commentThread.getSnippet().getTopLevelComment().getId(),
        pageSize,
        maxResults
    );

    List<Comment> output = new ArrayList<>();
    while (youtubeRepliesApi.hasNextPage()) {
      output.addAll(youtubeRepliesApi.requestNextPage());
    }

    return output.stream();
  }
}
