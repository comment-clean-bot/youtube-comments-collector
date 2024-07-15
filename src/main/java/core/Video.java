package core;

import java.time.LocalDateTime;

public record Video(
    String id,
    LocalDateTime publishedAt,
    String title,
    String channelId,
    String channelTitle,
    String categoryId
) {

}
