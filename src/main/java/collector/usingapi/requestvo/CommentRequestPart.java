package collector.usingapi.requestvo;

public enum CommentRequestPart {
  ID("id"),
  SNIPPET("snippet"),
  REPLY("replies"),
  ;

  private final String part;

  CommentRequestPart(String part) {
    this.part = part;
  }

  public String getPart() {
    return part;
  }
}
