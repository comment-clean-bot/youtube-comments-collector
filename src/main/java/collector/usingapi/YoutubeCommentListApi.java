package collector.usingapi;

import collector.usingapi.requestvo.CommentThreadRequestPart;
import collector.usingapi.responsevo.CommentThreadsResponse;
import collector.usingapi.utils.HttpRequestApiManage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import core.Comment;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class YoutubeCommentListApi {
  private final static ObjectMapper objectMapper = initObjectMapper();

  public final static String COMMENT_THREAD_API_PATH = "/commentThreads";

  private final String apiKey;
  private final String baseUrl;
  private final Set<CommentThreadRequestPart> parts;
  private final String videoId;
  private final int pageSize;
  private final Integer maxResults;

  private final ReplyCollector replyCollector;

  private CommentThreadsResponse lastResponse;

  private int totalTopLevelCommentCount;
  private int totalCommentCount;

  public YoutubeCommentListApi(
      String apiKey, String baseUrl,
      Set<CommentThreadRequestPart> parts, String videoId,
      int pageSize, ReplyCollector replyCollector) {
    this.apiKey = apiKey;
    this.baseUrl = baseUrl;
    this.parts = new HashSet<>(parts);
    this.videoId = videoId;
    this.pageSize = pageSize;
    this.maxResults = null;
    this.replyCollector = replyCollector;

    this.lastResponse = null;
    this.totalTopLevelCommentCount = 0;
    this.totalCommentCount = 0;
  }

  public YoutubeCommentListApi(
      String apiKey, String baseUrl,
      Set<CommentThreadRequestPart> parts, String videoId,
      int pageSize, int maxResults, ReplyCollector replyCollector) {
    this.apiKey = apiKey;
    this.baseUrl = baseUrl;
    this.parts = new HashSet<>(parts);
    this.videoId = videoId;
    this.pageSize = pageSize;
    this.maxResults = maxResults;
    this.replyCollector = replyCollector;

    this.lastResponse = null;
    this.totalTopLevelCommentCount = 0;
    this.totalCommentCount = 0;
  }

  public int getTotalTopLevelCommentCount() {
    return totalTopLevelCommentCount;
  }

  public int getTotalCommentCount() {
    return totalCommentCount;
  }

  public int getPageSize() {
    return pageSize;
  }

  public List<Comment> requestNextPage() {
    String jsonResponseString = HttpRequestApiManage.sendGetRequest(
        baseUrl + COMMENT_THREAD_API_PATH,
        makeHeaders(),
        makeRequestQueries()
    );

    // TODO: exception handling
    try {
      lastResponse = objectMapper.readValue(jsonResponseString, CommentThreadsResponse.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    List<Comment> collectedComments = lastResponse.getItems().stream().map(
        item -> item.getSnippet().getTopLevelComment().toComment()
    ).collect(Collectors.toList());
    collectedComments.addAll(replyCollector.collectReplies(lastResponse));

    totalTopLevelCommentCount += extractTotalResults();
    totalCommentCount += collectedComments.size();
    return collectedComments;
  }

  public boolean hasNextPage() {
    if (lastResponse == null) {
      return true;
    }
    return !extractNextPageToken().isEmpty() && leftResultsCount() > 0;
  }

  private Map<String, String> makeHeaders() {
    Map<String, String> headers = new HashMap<>();
    headers.put("Accept", "application/json");
    headers.put("Content-Type", "application/json");
    return headers;
  }

  private Map<String, List<String>> makeRequestQueries() {
    Map<String, List<String>> queries = new HashMap<>();
    queries.put("part", parts.stream().map(CommentThreadRequestPart::getPart).toList());
    queries.put("videoId", List.of(videoId));
    queries.put("maxResults", List.of(String.valueOf(Integer.min(pageSize, leftResultsCount()))));
    queries.put("key", List.of(apiKey));

    String nextPageToken = extractNextPageToken();
    if (!nextPageToken.isEmpty()) {
      queries.put("pageToken", List.of(nextPageToken));
    }

    return queries;
  }

  private int leftResultsCount() {
    if (maxResults == null) {
      return Integer.MAX_VALUE;
    }
    return maxResults - totalTopLevelCommentCount;
  }

  private String extractNextPageToken() {
    if (lastResponse == null || lastResponse.getNextPageToken() == null) {
      return "";
    }
    return lastResponse.getNextPageToken();
  }

  private int extractTotalResults() {
    if (lastResponse == null) {
      return 0;
    }
    return lastResponse.getPageInfo().getTotalResults();
  }

  private static ObjectMapper initObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
  }
}
