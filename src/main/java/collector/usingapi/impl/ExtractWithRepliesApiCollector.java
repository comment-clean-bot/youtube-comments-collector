package collector.usingapi.impl;

import collector.usingapi.ReplyCollector;
import collector.usingapi.api.YoutubeRepliesApi;
import collector.usingapi.requestvo.CommentRequestPart;
import collector.usingapi.responsevo.CommentThreadResponse;
import core.Comment;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExtractWithRepliesApiCollector implements ReplyCollector {
  private final static int PRE_LABEL_START_IDX = 5;

  private final String apiKey;
  private final String baseUrl;
  private final int pageSize;
  private final Integer maxResults;

  public ExtractWithRepliesApiCollector(String apiKey, String baseUrl,
      int pageSize, Integer maxResults) {
    this.apiKey = apiKey;
    this.baseUrl = baseUrl;
    this.pageSize = pageSize;
    this.maxResults = maxResults;
  }

  @Override
  public List<Comment> collectReplies(CommentThreadResponse commentThread) {
    return preLabelReplies(extractReplies(commentThread).collect(Collectors.toList()));
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

  private List<Comment> preLabelReplies(List<Comment> replies) {
    List<Comment> sortedReplies = replies.stream()
        .sorted(Comparator.comparing(Comment::publishedAt).reversed())
        .toList();
    List<Comment> output = new ArrayList<>();
    if (sortedReplies.size() > 0) {
      System.out.println("Pre-labeling replies");
    }
    for (int i = 0; i < sortedReplies.size(); i++) {
      Comment reply = sortedReplies.get(i);
      System.out.println("Reply " + i + ": " + reply.publishedAt().toString());
      if (i < PRE_LABEL_START_IDX) {
        output.add(reply);
      } else {
        output.add(reply.withPreLabel(false));
      }
    }
    return output;
  }
}
