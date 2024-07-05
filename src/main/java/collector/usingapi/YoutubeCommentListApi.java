package collector.usingapi;

import collector.usingapi.requestvo.CommentRequestPart;
import collector.usingapi.responsevo.CommentDtoUsingApi;
import collector.usingapi.utils.HttpRequestApiManage;
import collector.usingapi.utils.JSONObjectParser;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class YoutubeCommentListApi {
  private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

  public final static String COMMENT_THREAD_API_PATH = "/commentThreads";

  private final String apiKey;
  private final String baseUrl;
  private final Set<CommentRequestPart> parts;
  private final String videoId;
  private final int maxResults;

  private JSONObject lastResponse;

  private int totalTopLevelCommentCount;
  private int totalCommentCount;

  public YoutubeCommentListApi(
      String apiKey, String baseUrl,
      Set<CommentRequestPart> parts, String videoId, int maxResults) {
    this.apiKey = apiKey;
    this.baseUrl = baseUrl;
    this.parts = new HashSet<>(parts);
    this.videoId = videoId;
    this.maxResults = maxResults;
    this.lastResponse = new JSONObject();

    this.totalTopLevelCommentCount = 0;
    this.totalCommentCount = 0;
  }

  public int getTotalTopLevelCommentCount() {
    return totalTopLevelCommentCount;
  }

  public int getTotalCommentCount() {
    return totalCommentCount;
  }

  public List<CommentDtoUsingApi> requestNextPage() {
    lastResponse = JSONObjectParser.parseResponse(HttpRequestApiManage.sendGetRequest(
        baseUrl + COMMENT_THREAD_API_PATH,
        makeHeaders(),
        makeRequestQueries()
    ));

    List<CommentDtoUsingApi> output = extractCommentDtoList();
    totalTopLevelCommentCount += extractTotalResults();
    totalCommentCount += output.size();
    return output;
  }

  public boolean hasNextPage() {
    if (lastResponse.isEmpty()) {
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
    queries.put("videoId", List.of(videoId));
    queries.put("maxResults", List.of(String.valueOf(maxResults)));
    queries.put("key", List.of(apiKey));

    String nextPageToken = extractNextPageToken();
    if (!nextPageToken.isEmpty()) {
      queries.put("pageToken", List.of(nextPageToken));
    }

    return queries;
  }

  private String extractNextPageToken() {
    if (lastResponse.isEmpty()) {
      return "";
    }
    if (lastResponse.containsKey("nextPageToken")) {
      return (String) lastResponse.get("nextPageToken");
    }
    return "";
  }

  private int extractTotalResults() {
    if (lastResponse.isEmpty()) {
      return 0;
    }
    if (lastResponse.containsKey("pageInfo")) {
      JSONObject pageInfo = (JSONObject) lastResponse.get("pageInfo");
      return (int) (long) pageInfo.get("totalResults");
    }
    return 0;
  }

  private List<CommentDtoUsingApi> extractCommentDtoList() {
    if (!lastResponse.containsKey("items")) {
      return new ArrayList<>();
    }

    JSONArray items = (JSONArray) lastResponse.get("items");
    List<CommentDtoUsingApi> output = new ArrayList<>();
    for (Object item: items) {
      JSONObject commentThread = (JSONObject) item;
      if (!commentThread.containsKey("snippet")) {
        continue;
      }
      JSONObject commentThreadSnippet = (JSONObject) commentThread.get("snippet");

      if (!commentThreadSnippet.containsKey("topLevelComment")) {
        continue;
      }
      JSONObject topLevelComment = (JSONObject) commentThreadSnippet.get("topLevelComment");
      output.add(toCommentDto(topLevelComment));

      if (commentThread.containsKey("replies")) {
        output.addAll(extractReplyDtoList(commentThread));
      }
    }
    return output;
  }

  private List<CommentDtoUsingApi> extractReplyDtoList(JSONObject commentThread) {
    if (!commentThread.containsKey("replies")) {
      return new ArrayList<>();
    }

    JSONObject replies = (JSONObject) commentThread.get("replies");
    if (!replies.containsKey("comments")) {
      return new ArrayList<>();
    }

    JSONArray items = (JSONArray) replies.get("comments");
    List<CommentDtoUsingApi> output = new ArrayList<>();
    for (Object item: items) {
      JSONObject comment = (JSONObject) item;
      output.add(toCommentDto(comment));
    }
    return output;
  }

  private CommentDtoUsingApi toCommentDto(JSONObject commentJson) {
    String id = (String) commentJson.getOrDefault("id", "");
    JSONObject snippetJson = (JSONObject) commentJson.getOrDefault("snippet", new JSONObject());
    return new CommentDtoUsingApi(
        id,
        (String) snippetJson.getOrDefault("channelId", ""),
        (String) snippetJson.getOrDefault("videoId", ""),
        (String) snippetJson.getOrDefault("parentId", ""),
        (String) snippetJson.getOrDefault("textDisplay", ""),
        (String) snippetJson.getOrDefault("textOriginal", ""),
        (String) snippetJson.getOrDefault("authorDisplayName", ""),
        (String) snippetJson.getOrDefault("authorProfileImageUrl", ""),
        (String) snippetJson.getOrDefault("authorChannelUrl", ""),
        (long) snippetJson.getOrDefault("likeCount", 0),
        LocalDateTime.parse((String) snippetJson.getOrDefault("publishedAt", ""), formatter),
        LocalDateTime.parse((String) snippetJson.getOrDefault("updatedAt", ""), formatter)
    );
  }

}
