package database;

import core.Comment;
import database.SQLite.DMLService;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Map;

public class CommentMapper {
  public static Comment toComment(CollectedComment collectedComment){
    return new Comment(
      collectedComment.getCommentId(),
      collectedComment.getChannelId(),
      collectedComment.getParentId(),
      collectedComment.getParentId(),
      collectedComment.getText(),
      collectedComment.getAuthorId(),
      collectedComment.getLikeCount(),
      collectedComment.getPublishedAt(),
      collectedComment.getUpdatedAt(),
      collectedComment.isPreLabel()
    );
  }

  public static CollectedComment toCollectedComment(Comment comment) {
    return new CollectedComment(
      0,
      comment.channelId(),
      comment.parentId(),
      comment.text(),
      comment.author(),
      comment.likeCount(),
      comment.publishedAt(),
      comment.updatedAt(),
      CommentType.YOUTUBE,
      comment.id(),
      comment.preLabel()
    );
  }

  public static CollectedComment objectToCollectedComment(Map<String, Object> objectMap)
      throws ParseException {
    return new CollectedComment(
      (int) objectMap.get("id"),
      (String) objectMap.get("channel_id"),
      (String) objectMap.get("parent_id"),
      (String) objectMap.get("text"),
      (String) objectMap.get("author_id"),
      Integer.toUnsignedLong((int) objectMap.get("like_count")),
      LocalDateTime.parse((String) objectMap.get("published_at"), DMLService.DATE_FORMAT),
      LocalDateTime.parse((String) objectMap.get("updated_at"), DMLService.DATE_FORMAT),
      CommentType.valueOf((String) objectMap.get("comment_type")),
      (String) objectMap.get("comment_id"),
      ((int) objectMap.get("pre_label") == 1)
    );
  }
}
