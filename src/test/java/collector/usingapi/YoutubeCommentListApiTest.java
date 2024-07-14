package collector.usingapi;

import collector.usingapi.requestvo.CommentThreadRequestPart;
import java.io.FileReader;
import java.util.Properties;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class YoutubeCommentListApiTest {

  private static final String BASE_URL = "https://www.googleapis.com/youtube/v3";
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
  void PRINT__Collect_comment_from_video_id_with_top_5_replies() {
    YoutubeCommentListApi youtubeCommentListApi = new YoutubeCommentListApi(
        apiKey,
        BASE_URL,
        Set.of(CommentThreadRequestPart.ID,
            CommentThreadRequestPart.SNIPPET,
            CommentThreadRequestPart.REPLY),
        "x_y4pWtGn8I",
        50,
        null,
        new ExtractOnResponseReplyCollector(),
        new AllPassCommentFilter()
    );
    while (youtubeCommentListApi.hasNextPage()) {
      youtubeCommentListApi.requestNextPage().forEach(System.out::println);
    }
    System.out.println("youtubeCommentListApi.getTotalTopLevelCommentCount() = " + youtubeCommentListApi.getTotalTopLevelCommentCount());
    System.out.println("youtubeCommentListApi.getTotalCommentCount() = " + youtubeCommentListApi.getTotalCommentCount());
  }

  @Test
  void PRINT__Collect_comment_from_video_id_with_all_replies() {
    YoutubeCommentListApi youtubeCommentListApi = new YoutubeCommentListApi(
        apiKey,
        BASE_URL,
        Set.of(CommentThreadRequestPart.ID,
            CommentThreadRequestPart.SNIPPET,
            CommentThreadRequestPart.REPLY),
        "x_y4pWtGn8I",
        50,
        null,
        new ExtractWithRepliesApiCollector(apiKey, BASE_URL, 100, null),
        new AllPassCommentFilter()
    );
    while (youtubeCommentListApi.hasNextPage()) {
      youtubeCommentListApi.requestNextPage().forEach(System.out::println);
    }
    System.out.println("youtubeCommentListApi.getTotalTopLevelCommentCount() = " + youtubeCommentListApi.getTotalTopLevelCommentCount());
    System.out.println("youtubeCommentListApi.getTotalCommentCount() = " + youtubeCommentListApi.getTotalCommentCount());
  }

}