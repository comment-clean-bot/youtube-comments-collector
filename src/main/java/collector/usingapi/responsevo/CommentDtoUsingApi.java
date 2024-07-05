package collector.usingapi.responsevo;

import core.Comment;
import java.time.LocalDateTime;

public record CommentDtoUsingApi(
    String id,
    String channelId,
    String videoId,
    String parentId,
    String textDisplay,
    String textOriginal,
    String authorDisplayName,
    String authorProfileImageUrl,
    String authorChannelUrl,
    long likeCount,
    LocalDateTime publishedAt,
    LocalDateTime updatedAt
) {

  public Comment toComment() {
    return new Comment(
        channelId,
        videoId,
        textOriginal,
        authorDisplayName,
        likeCount,
        publishedAt,
        updatedAt
    );
  }

  @Override
  public String toString() {
    return "CommentDtoUsingApi{" +
        "id='" + id + '\'' +
        ", channelId='" + channelId + '\'' +
        ", videoId='" + videoId + '\'' +
        ", parentId='" + parentId + '\'' +
        ", textDisplay='" + textDisplay + '\'' +
        ", textOriginal='" + textOriginal + '\'' +
        ", authorDisplayName='" + authorDisplayName + '\'' +
        ", authorProfileImageUrl='" + authorProfileImageUrl + '\'' +
        ", authorChannelUrl='" + authorChannelUrl + '\'' +
        ", likeCount=" + likeCount +
        ", publishedAt=" + publishedAt +
        ", updatedAt=" + updatedAt +
        '}';
  }
}
