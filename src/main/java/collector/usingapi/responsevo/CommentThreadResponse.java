package collector.usingapi.responsevo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentThreadResponse {
  private String id;
  private CommentThreadSnippetResponse snippet;
  private RepliesResponse replies;

  public CommentThreadResponse(String id,
      CommentThreadSnippetResponse snippet,
      RepliesResponse replies) {
    this.id = id;
    this.snippet = snippet;
    this.replies = replies;
  }
}
