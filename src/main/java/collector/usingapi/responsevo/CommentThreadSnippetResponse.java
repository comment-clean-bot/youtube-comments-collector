package collector.usingapi.responsevo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentThreadSnippetResponse{
  private String channelId;
  private String videoId;
  private CommentResponse topLevelComment;
  private boolean canReply;
  private long totalReplyCount;
  private boolean isPublic;

  public CommentThreadSnippetResponse(String channelId,
      String videoId,
      CommentResponse topLevelComment, boolean canReply, long totalReplyCount,
      boolean isPublic) {
    this.channelId = channelId;
    this.videoId = videoId;
    this.topLevelComment = topLevelComment;
    this.canReply = canReply;
    this.totalReplyCount = totalReplyCount;
    this.isPublic = isPublic;
  }
}
