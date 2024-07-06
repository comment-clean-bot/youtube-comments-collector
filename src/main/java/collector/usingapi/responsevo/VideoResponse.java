package collector.usingapi.responsevo;

import collector.usingapi.Video;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoResponse extends YoutubeApiBaseResponse{
  private String id;
  private VideoSnippetResponse snippet;

  public VideoResponse(String kind, String etag, String id, VideoSnippetResponse snippet) {
    super(kind, etag);
    this.id = id;
    this.snippet = snippet;
  }

  public Video toVideo() {
    return new Video(
        id,
        snippet.getPublishedAt(),
        snippet.getTitle(),
        snippet.getChannelId(),
        snippet.getChannelTitle(),
        snippet.getCategoryId()
    );
  }
}
