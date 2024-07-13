package database;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CollectedComment {
  private int id;
  private String channelId;
  private String parentId;
  private String text;
  private String authorId;
  private long likeCount;
  private LocalDateTime publishedAt;
  private LocalDateTime updatedAt;
  private CommentType commentType;
  private String commentId;

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String toString(){
    return "id: " + id + ", channelId: " + channelId + ", parentId: " + parentId + ", text: " + text + ", authorId: " + authorId + ", likeCount: " + likeCount + ", publishedAt: " + publishedAt + ", updatedAt: " + updatedAt + ", commentType: " + commentType + ", commentId: " + commentId;
  }
}
