package collector.usingapi.api;

import collector.usingapi.requestvo.CommentRequestPart;
import collector.usingapi.responsevo.CommentResponse;
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
  private final int pageSize;
  private final Integer maxResults;

  private CommentsResponse lastResponse;

  private int totalRepliesCount;

  public YoutubeRepliesApi(
      String apiKey, String baseUrl,
      Set<CommentRequestPart> parts, String parentId,
      int pageSize, Integer maxResults) {
    this.apiKey = apiKey;
    this.baseUrl = baseUrl;
    this.parts = new HashSet<>(parts);
    this.parentId = parentId;
    this.pageSize = pageSize;
    this.lastResponse = null;
    this.maxResults = maxResults;

    this.totalRepliesCount = 0;
  }

  public int getTotalRepliesCount() {
    return totalRepliesCount;
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
      lastResponse = objectMapper.readValue(jsonResponseString, CommentsResponse.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    List<Comment> collectedComments = lastResponse.getItems().stream().map(
        CommentResponse::toComment
    ).collect(Collectors.toList());

    totalRepliesCount += collectedComments.size();
    return collectedComments;
  }

  public boolean hasNextPage() {
    if (leftResultsCount() <= 0) {
      return false;
    }
    if (lastResponse == null) {
      return true;
    }
    return !extractNextPageToken().isEmpty();
  }

  private int leftResultsCount() {
    if (maxResults == null) {
      return Integer.MAX_VALUE;
    }
    return maxResults - totalRepliesCount;
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
    queries.put("maxResults", List.of(String.valueOf(Integer.min(pageSize, leftResultsCount()))));
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

  private static ObjectMapper initObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
  }
}
