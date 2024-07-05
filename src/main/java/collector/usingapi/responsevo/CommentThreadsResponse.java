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
public class CommentThreadsResponse extends YoutubePaginatedApiResponse {
  private List<CommentThreadResponse> items;

  public CommentThreadsResponse(String kind, String etag,
      PageInfoResponse pageInfo, String nextPageToken, List<CommentThreadResponse> items) {
    super(kind, etag, pageInfo, nextPageToken);
    this.items = new ArrayList<>(items);
  }

}
