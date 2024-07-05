package core;

import java.time.LocalDateTime;

public record Comment(
    String channelId,
    String videoId,
    String text,
    String author,
    long likeCount,
    LocalDateTime publishedAt,
    LocalDateTime updatedAt
) {

}
