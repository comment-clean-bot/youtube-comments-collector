package collector.usingapi.exception;

public class ResponseParseFailedException extends RuntimeException {

    public ResponseParseFailedException(String message) {
      super(message);
    }

}
