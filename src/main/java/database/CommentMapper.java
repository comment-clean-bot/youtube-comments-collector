package database;

import core.Comment;
import database.SQLite.SQLiteDMLService;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

public class CommentMapper {
  public static Comment toComment(CollectedComment collectedComment){
    return new Comment(
      collectedComment.getChannelId(),
      collectedComment.getParentId(),
      collectedComment.getText(),
      collectedComment.getAuthorId(),
      collectedComment.getLikeCount(),
      collectedComment.getPublishedAt(),
      collectedComment.getUpdatedAt()
    );
  }

  public static CollectedComment objectToCollectedComment(Map<String, Object> objectMap)
      throws ParseException {
    return new CollectedComment(
      (String) objectMap.get("id"),
      (String) objectMap.get("channel_id"),
      (String) objectMap.get("parent_id"),
      (String) objectMap.get("text"),
      (String) objectMap.get("author_id"),
      (long) objectMap.get("like_count"),
      LocalDateTime.parse((String) objectMap.get("published_at"), SQLiteDMLService.DATE_FORMAT),
      LocalDateTime.parse((String) objectMap.get("updated_at"), SQLiteDMLService.DATE_FORMAT),
      CommentType.valueOf((String) objectMap.get("comment_type"))
    );
  }
}
