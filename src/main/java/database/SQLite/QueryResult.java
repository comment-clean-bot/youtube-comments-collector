package database.SQLite;

public enum QueryResult {
  SUCCESS(1),
  WARNING(0),
  FAILURE(-1);

  public int value;

  private QueryResult(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
