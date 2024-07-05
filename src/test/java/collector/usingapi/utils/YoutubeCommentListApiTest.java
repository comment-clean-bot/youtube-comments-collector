package collector.usingapi.utils;

import java.io.FileReader;
import java.util.Properties;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class YoutubeCommentListApiTest {

  private static String apiKey;

  @BeforeAll
  static void setUp() {
    System.out.println("BeforeAll");
    String resourcePath = "src/main/resources/secrets/youtubeapi.properties";
    try (FileReader file = new FileReader(resourcePath)) {
      Properties p = new Properties();
      p.load(file);
      apiKey = p.getProperty("key", "");
    } catch (Exception e) {
      System.out.println("cannot read properties file");
    }
  }

  @Test
  void PRINT__Collect_comment_from_video_id() {
    YoutubeCommentListApi youtubeCommentListApi = new YoutubeCommentListApi(
        apiKey,
        "https://www.googleapis.com/youtube/v3",
        Set.of(collector.usingapi.utils.CommentRequestPart.ID,
            collector.usingapi.utils.CommentRequestPart.SNIPPET,
            collector.usingapi.utils.CommentRequestPart.REPLY),
        "cAczQwTAtGQ",
        3
    );
    while (youtubeCommentListApi.hasNextPage()) {
      youtubeCommentListApi.requestNextPage().forEach(System.out::println);
    }
    System.out.println("youtubeCommentListApi.getTotalTopLevelCommentCount() = " + youtubeCommentListApi.getTotalTopLevelCommentCount());
    System.out.println("youtubeCommentListApi.getTotalCommentCount() = " + youtubeCommentListApi.getTotalCommentCount());
  }

}