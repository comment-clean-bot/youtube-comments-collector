package database;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CollectedComment {
  private String id;
  private String channelId;
  private String parentId;
  private String text;
  private String authorId;
  private long likeCount;
  private LocalDateTime publishedAt;
  private LocalDateTime updatedAt;
  private CommentType commentType;

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
