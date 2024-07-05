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
public class PageInfoResponse {
  private int totalResults;
  private int resultsPerPage;

  public PageInfoResponse(int totalResults, int resultsPerPage) {
    this.totalResults = totalResults;
    this.resultsPerPage = resultsPerPage;
  }
}
