package collector.usingapi;

import collector.usingapi.requestvo.VideoRequestPart;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class YoutubeVideoListApiTest {
  private static final String BASE_URL = "https://www.googleapis.com/youtube/v3";

  private static String apiKey;

  @BeforeAll
  static void setUp() {
    System.out.println("BeforeAll YoutubeVideoListApiTest");
    String keyPath = "src/main/resources/secrets/youtubeapi.properties";
    try (FileReader file = new FileReader(keyPath)) {
      Properties p = new Properties();
      p.load(file);
      apiKey = p.getProperty("key", "");
    } catch (Exception e) {
      System.out.println("cannot read properties file");
    }
  }

  @Test
  void PRINT__Collect_popular_videos() {
    YoutubeVideoListApi youtubeVideoListApi = new YoutubeVideoListApi(
        apiKey,
        BASE_URL,
        Set.of(VideoRequestPart.SNIPPET),
        "mostPopular",
        "KR",
        50,
        200,
        false
    );
    List<Video> output = new ArrayList<>();
    while (youtubeVideoListApi.hasNextPage()) {
      output.addAll(youtubeVideoListApi.requestNextPage());
    }
    output.forEach(System.out::println);

    Assertions.assertTrue(output.size() > 0);
  }

  @Test
  void PRINT__Collect_popular_videos_without_music() {
    YoutubeVideoListApi youtubeVideoListApi = new YoutubeVideoListApi(
        apiKey,
        BASE_URL,
        Set.of(VideoRequestPart.SNIPPET),
        "mostPopular",
        "KR",
        50,
        200,
        true
    );
    List<Video> output = new ArrayList<>();
    while (youtubeVideoListApi.hasNextPage()) {
      output.addAll(youtubeVideoListApi.requestNextPage());
    }
    output.forEach(System.out::println);

    Assertions.assertTrue(output.size() > 0);

    for (Video video : output) {
      Assertions.assertFalse(video.categoryId().equals("10"));
    }

  }
}
