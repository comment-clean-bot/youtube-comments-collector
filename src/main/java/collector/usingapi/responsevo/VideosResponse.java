package collector.usingapi.responsevo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideosResponse extends YoutubePaginatedApiResponse{
  private List<VideoResponse> items;

  public VideosResponse(String kind, String etag, PageInfoResponse pageInfo, String nextPageToken, List<VideoResponse> items) {
    super(kind, etag, pageInfo, nextPageToken);
    this.items = new ArrayList<>(items);
  }
}
