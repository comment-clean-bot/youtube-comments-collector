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
public class CommentsResponse extends YoutubePaginatedApiResponse {
  private List<CommentResponse> items;

  public CommentsResponse(String kind, String etag,
      PageInfoResponse pageInfo, String nextPageToken, List<CommentResponse> items) {
    super(kind, etag, pageInfo, nextPageToken);
    this.items = new ArrayList<>(items);
  }

}
