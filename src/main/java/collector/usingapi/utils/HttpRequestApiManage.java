package collector.usingapi.utils;

import collector.usingapi.exception.BadRequestException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class HttpRequestApiManage {

  public static String sendGetRequest(String urlString, Map<String, String> requestHeader, Map<String, List<String>> requestQueries) {
    try {
      URL url = new URL(urlString + generateQueryString(requestQueries));
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");

      if (requestHeader != null) {
        requestHeader.forEach(con::setRequestProperty);
      }

      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      byte[] buf = new byte[4096];
      InputStream in = con.getInputStream();
      while (true) {
        int readLen = in.read(buf);
        if (readLen < 1) {
          break;
        }
        bos.write(buf, 0, readLen);
      }
      String output = bos.toString(StandardCharsets.UTF_8);

      bos.close();
      in.close();
      return output;
    } catch (Exception e) {
      e.printStackTrace();
      throw new BadRequestException(e.getMessage());
    }
  }

  private static String generateQueryString(Map<String, List<String>> queries) {
    if (queries == null) {
      return "";
    }

    StringBuilder queryBuilder = new StringBuilder("");
    for (Map.Entry<String, List<String>> query : queries.entrySet()) {
      String k = query.getKey();
      List<String> values = query.getValue();
      for (String v : values) {
        if (queryBuilder.length() == 0) {
          queryBuilder.append("?");
        } else {
          queryBuilder.append("&");
        }
        queryBuilder.append(k).append("=").append(v);
      }
    }
    return queryBuilder.toString();
  }
}
