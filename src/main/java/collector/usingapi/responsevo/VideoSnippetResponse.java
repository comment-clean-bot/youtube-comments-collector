package collector.usingapi.responsevo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoSnippetResponse {
  private LocalDateTime publishedAt;
  private String title;
  private String channelId;
  private String channelTitle;
  private String categoryId;

  public VideoSnippetResponse(LocalDateTime publishedAt, String title, String channelId, String channelTitle, String categoryId) {
    this.publishedAt = publishedAt;
    this.title = title;
    this.channelId = channelId;
    this.channelTitle = channelTitle;
    this.categoryId = categoryId;
  }
}
