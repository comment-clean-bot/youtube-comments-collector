package collector.usingapi.requestvo;

public enum VideoRequestPart {
  SNIPPET("snippet"),
  CONTENTDETAILS("contentDetails"),
  STATISTICS("statistics"),
  ;

  private final String part;

  VideoRequestPart(String part) {
    this.part = part;
  }

  public String getPart() {
    return part;
  }
}
