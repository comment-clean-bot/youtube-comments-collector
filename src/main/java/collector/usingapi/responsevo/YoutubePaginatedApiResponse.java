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
public class YoutubePaginatedApiResponse extends YoutubeApiBaseResponse {
  private PageInfoResponse pageInfo;
  private String nextPageToken;

  public YoutubePaginatedApiResponse(String kind, String etag,
      PageInfoResponse pageInfo, String nextPageToken) {
    super(kind, etag);
    this.pageInfo = pageInfo;
    this.nextPageToken = nextPageToken;
  }
}
