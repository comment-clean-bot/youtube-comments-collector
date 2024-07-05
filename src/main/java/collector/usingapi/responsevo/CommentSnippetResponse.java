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
public class CommentSnippetResponse {

  private String channelId;
  private String videoId;
  private String parentId;
  private String textDisplay;
  private String textOriginal;
  private String authorDisplayName;
  private String authorProfileImageUrl;
  private String authorChannelUrl;
  private long likeCount;
  private LocalDateTime publishedAt;
  private LocalDateTime updatedAt;

  public CommentSnippetResponse(String channelId, String videoId,
      String parentId, String textDisplay, String textOriginal,
      String authorDisplayName, String authorProfileImageUrl,
      String authorChannelUrl, long likeCount,
      LocalDateTime publishedAt, LocalDateTime updatedAt) {
    this.channelId = channelId;
    this.videoId = videoId;
    this.parentId = parentId;
    this.textDisplay = textDisplay;
    this.textOriginal = textOriginal;
    this.authorDisplayName = authorDisplayName;
    this.authorProfileImageUrl = authorProfileImageUrl;
    this.authorChannelUrl = authorChannelUrl;
    this.likeCount = likeCount;
    this.publishedAt = publishedAt;
    this.updatedAt = updatedAt;
  }
}
