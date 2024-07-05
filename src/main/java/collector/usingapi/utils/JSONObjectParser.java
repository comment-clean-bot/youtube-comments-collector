package collector.usingapi.utils;

import collector.usingapi.exception.ResponseParseFailedException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONObjectParser {

  public static JSONObject parseResponse(String response) {
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonResponse;

    try {
      jsonResponse = (JSONObject) jsonParser.parse(response);
    } catch (ParseException e) {
      e.printStackTrace();
      throw new ResponseParseFailedException(e.getMessage());
    }

    return jsonResponse;
  }

}
