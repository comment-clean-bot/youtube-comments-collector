package collector.usingapi.responsevo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import core.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentResponse extends YoutubeApiBaseResponse {

  private String id;
  private CommentSnippetResponse snippet;

  public CommentResponse(String kind, String etag, String id, CommentSnippetResponse snippet) {
    super(kind, etag);
    this.id = id;
    this.snippet = snippet;
  }

  public Comment toComment() {
    return new Comment(
        id,
        snippet.getChannelId(),
        snippet.getVideoId(),
        snippet.getParentId(),
        snippet.getTextOriginal(),
        snippet.getAuthorDisplayName(),
        snippet.getLikeCount(),
        snippet.getPublishedAt(),
        snippet.getUpdatedAt(),
        null
    );
  }
}
