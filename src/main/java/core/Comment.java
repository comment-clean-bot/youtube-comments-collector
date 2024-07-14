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
    LocalDateTime updatedAt
) {

}
