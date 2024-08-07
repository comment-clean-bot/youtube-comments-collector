package collector.usingapi.impl;

import collector.usingapi.TargetVideoSelector;
import core.Video;
import collector.usingapi.api.YoutubeVideoListApi;
import collector.usingapi.requestvo.VideoRequestPart;
import core.filter.IVideoFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class PopularVideoSelector implements TargetVideoSelector {
  private final String apiKey;

  private final String baseUrl;

  private final int maxResults;

  private final int totalMaxCount;

  private final List<IVideoFilter> videoFilters;

  public PopularVideoSelector(String apiKey, String baseUrl, int maxResults, int totalMaxCount, List<IVideoFilter> videoFilters) {
    this.apiKey = apiKey;
    this.baseUrl = baseUrl;
    this.maxResults = maxResults;
    this.totalMaxCount = totalMaxCount;
    this.videoFilters = videoFilters;
  }

  @Override
  public Stream<Video> select() {
    YoutubeVideoListApi youtubeVideoListApi = new YoutubeVideoListApi(
        apiKey,
        baseUrl,
        Set.of(VideoRequestPart.SNIPPET),
        "mostPopular",
        "KR",
        maxResults,
        totalMaxCount,
        videoFilters
    );

    List<Video> output = new ArrayList<>();
    while (youtubeVideoListApi.hasNextPage()) {
      output.addAll(youtubeVideoListApi.requestNextPage());
    }

    return output.stream();
  }
}
