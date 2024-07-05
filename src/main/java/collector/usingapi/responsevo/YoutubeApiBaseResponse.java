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
public class YoutubeApiBaseResponse {
  private String kind;
  private String etag;

  public YoutubeApiBaseResponse(String kind, String etag) {
    this.kind = kind;
    this.etag = etag;
  }
}
