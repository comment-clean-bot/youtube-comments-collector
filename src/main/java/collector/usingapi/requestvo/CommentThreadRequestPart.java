package collector.usingapi.requestvo;

public enum CommentThreadRequestPart {
  ID("id"),
  SNIPPET("snippet"),
  REPLY("replies"),
  ;

  private final String part;

  CommentThreadRequestPart(String part) {
    this.part = part;
  }

  public String getPart() {
    return part;
  }
}
