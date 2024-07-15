package collector.usingapi.api;

import core.Video;
import collector.usingapi.requestvo.VideoRequestPart;
import collector.usingapi.responsevo.VideoResponse;
import collector.usingapi.responsevo.VideosResponse;
import collector.usingapi.utils.HttpRequestApiManage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import core.filter.IVideoFilter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class YoutubeVideoListApi {
  private final static ObjectMapper objectMapper = initObjectMapper();

  public final static String VIDEO_API = "/videos";

  private final String apiKey;
  private final String baseUrl;
  private final Set<VideoRequestPart> parts;

  private final String chart;

  private final String regionCode;
  private final int maxResults;

  private final int totalMaxCount;

  private final List<IVideoFilter> videoFilters;

  private VideosResponse lastResponse;

  private int totalVideoCount;

  public YoutubeVideoListApi(
      String apiKey, String baseUrl,
      Set<VideoRequestPart> parts, String chart, String regionCode,
      int maxResults, int totalMaxCount,
      List<IVideoFilter> videoFilters) {
    this.apiKey = apiKey;
    this.baseUrl = baseUrl;
    this.parts = new HashSet<>(parts);
    this.chart = chart;
    this.regionCode = regionCode;
    this.maxResults = maxResults;
    this.totalMaxCount = totalMaxCount;
    this.lastResponse = null;
    this.totalVideoCount = 0;
    this.videoFilters = videoFilters;
  }

  public int getTotalVideoCount() {
    return totalVideoCount;
  }

  public int getMaxResults() {
    return maxResults;
  }

  public List<Video> requestNextPage() {
    String jsonResponseString = HttpRequestApiManage.sendGetRequest(
        baseUrl + VIDEO_API,
        makeHeaders(),
        makeRequestQueries()
    );

    try {
      lastResponse = objectMapper.readValue(jsonResponseString, VideosResponse.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    List<Video> videos = lastResponse.getItems().stream().map(VideoResponse::toVideo)
        .filter(video -> {
          for (IVideoFilter videoFilter : videoFilters) {
            if (!videoFilter.isAcceptable(video)) {
              return false;
            }
          }
          return true;
        })
        .collect(Collectors.toList());

    totalVideoCount += extractTotalResults(videos);

    return videos;
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
    queries.put("part", parts.stream().map(VideoRequestPart::getPart).toList());
    queries.put("chart", List.of(chart));
    queries.put("regionCode", List.of(regionCode));
    queries.put("maxResults", List.of(String.valueOf(maxResults + totalVideoCount > totalMaxCount ? totalMaxCount - totalVideoCount : maxResults)));
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

  private int extractTotalResults(List<Video> videos) {
    if (lastResponse == null) {
      return 0;
    }
    return videos.size();
  }

  private static ObjectMapper initObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
  }
}
