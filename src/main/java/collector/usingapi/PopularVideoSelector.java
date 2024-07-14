package collector.usingapi;

import collector.usingapi.requestvo.VideoRequestPart;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class PopularVideoSelector implements TargetVideoSelector{
  private final String apiKey;

  private final String baseUrl;

  private final int maxResults;

  private final int totalMaxCount;

  private final boolean offMusicCategory;

  private final IVideoFilter videoFilter;

  public PopularVideoSelector(String apiKey, String baseUrl, int maxResults, int totalMaxCount, boolean offMusicCategory, IVideoFilter videoFilter) {
    this.apiKey = apiKey;
    this.baseUrl = baseUrl;
    this.maxResults = maxResults;
    this.totalMaxCount = totalMaxCount;
    this.offMusicCategory = offMusicCategory;
    this.videoFilter = videoFilter;
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
        offMusicCategory,
        videoFilter
    );

    List<Video> output = new ArrayList<>();
    while (youtubeVideoListApi.hasNextPage()) {
      output.addAll(youtubeVideoListApi.requestNextPage());
    }

    return output.stream();
  }
}
