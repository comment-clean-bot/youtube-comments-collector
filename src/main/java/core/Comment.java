package core;

import java.time.LocalDateTime;

public record Comment(
    String id,
    String channelId,
    String videoId,
    String parentId,
    String text,
    String author,
    long likeCount,
    LocalDateTime publishedAt,
    LocalDateTime updatedAt,
    Boolean preLabel
) {

  public Comment withPreLabel(boolean preLabel) {
    return new Comment(id, channelId, videoId, parentId, text, author, likeCount, publishedAt, updatedAt, preLabel);
  }

}
