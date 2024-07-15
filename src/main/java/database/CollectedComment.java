package database;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  private boolean preLabel;

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String toString(){
    return "id: " + id + ", channelId: " + channelId + ", parentId: " + parentId + ", text: " + text + ", authorId: " + authorId + ", likeCount: " + likeCount + ", publishedAt: " + publishedAt + ", updatedAt: " + updatedAt + ", commentType: " + commentType + ", commentId: " + commentId;
  }

  public Map<String, Object> toMap(List<String> fields) {
    Map<String, Object> toReturn = new HashMap<>();
    for (String field : fields) {
      switch (field) {
        case "id":
          toReturn.put("id", id);
          break;
        case "channel_id":
          toReturn.put("channel_id", channelId);
          break;
        case "parent_id":
          toReturn.put("parent_id", parentId);
          break;
        case "text":
          toReturn.put("text", text);
          break;
        case "author_id":
          toReturn.put("author_id", authorId);
          break;
        case "like_count":
          toReturn.put("like_count", likeCount);
          break;
        case "published_at":
          toReturn.put("published_at", publishedAt);
          break;
        case "updated_at":
          toReturn.put("updated_at", updatedAt);
          break;
        case "comment_type":
          toReturn.put("comment_type", commentType);
          break;
        case "comment_id":
          toReturn.put("comment_id", commentId);
          break;
        case "pre_label":
          toReturn.put("pre_label", preLabel);
          break;
      }
    }
    return toReturn;
  }
}
