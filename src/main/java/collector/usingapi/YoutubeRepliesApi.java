package collector.usingapi;

import collector.usingapi.requestvo.CommentRequestPart;
import collector.usingapi.responsevo.CommentResponse;
import collector.usingapi.responsevo.CommentThreadsResponse;
import collector.usingapi.responsevo.CommentsResponse;
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

public class YoutubeRepliesApi {
  private final static ObjectMapper objectMapper = initObjectMapper();

  public final static String COMMENT_THREAD_API_PATH = "/comments";

  private final String apiKey;
  private final String baseUrl;

  private final Set<CommentRequestPart> parts;
  private final String parentId;
  private final int maxResults;

  private CommentsResponse lastResponse;

  private int totalTopLevelCommentCount;
  private int totalCommentCount;

  public YoutubeRepliesApi(
      String apiKey, String baseUrl,
      Set<CommentRequestPart> parts, String parentId,
      int maxResults) {
    this.apiKey = apiKey;
    this.baseUrl = baseUrl;
    this.parts = new HashSet<>(parts);
    this.parentId = parentId;
    this.maxResults = maxResults;
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

  public int getMaxResults() {
    return maxResults;
  }

  public List<Comment> requestNextPage() {
    String jsonResponseString = HttpRequestApiManage.sendGetRequest(
        baseUrl + COMMENT_THREAD_API_PATH,
        makeHeaders(),
        makeRequestQueries()
    );

    // TODO: exception handling
    try {
      lastResponse = objectMapper.readValue(jsonResponseString, CommentsResponse.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    List<Comment> collectedComments = lastResponse.getItems().stream().map(
        CommentResponse::toComment
    ).collect(Collectors.toList());

    totalTopLevelCommentCount += extractTotalResults();
    totalCommentCount += collectedComments.size();
    return collectedComments;
  }

  public boolean hasNextPage() {
    if (lastResponse == null) {
      return true;
    }
    return !extractNextPageToken().isEmpty();
  }

  private Map<String, String> makeHeaders() {
    Map<String, String> headers = new HashMap<>();
    headers.put("Accept", "application/json");
    headers.put("Content-Type", "application/json");
    return headers;
  }

  private Map<String, List<String>> makeRequestQueries() {
    Map<String, List<String>> queries = new HashMap<>();
    queries.put("part", parts.stream().map(CommentRequestPart::getPart).toList());
    queries.put("parentId", List.of(parentId));
    queries.put("maxResults", List.of(String.valueOf(maxResults)));
    queries.put("key", List.of(apiKey));

    String nextPageToken = extractNextPageToken();
    if (!nextPageToken.isEmpty()) {
      queries.put("pageToken", List.of(nextPageToken));
    }

    return queries;
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