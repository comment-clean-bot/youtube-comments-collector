package collector.usingapi;

import collector.usingapi.requestvo.VideoRequestPart;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class PopularVideoSelector implements TargetVideoSelector{
  private final String apiKey;

  private final String baseUrl;

  public PopularVideoSelector(String apiKey, String baseUrl) {
    this.apiKey = apiKey;
    this.baseUrl = baseUrl;
  }

  @Override
  public Stream<Video> select() {
    YoutubeVideoListApi youtubeVideoListApi = new YoutubeVideoListApi(
        apiKey,
        baseUrl,
        Set.of(VideoRequestPart.SNIPPET),
        "mostPopular",
        "KR",
        10
    );

    List<Video> output = new ArrayList<>();
    while (youtubeVideoListApi.hasNextPage()) {
      output.addAll(youtubeVideoListApi.requestNextPage());
    }

    return output.stream();
  }
}
