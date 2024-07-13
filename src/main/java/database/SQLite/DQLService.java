package database.SQLite;

import database.CollectedComment;
import database.CommentMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DQLService extends SQLiteManager{
  public DQLService(){
  }

  public DQLService(String dbPath){
    super(dbPath);
  }

  public List<CollectedComment> selectCollectedCommentsWithFields(String fieldName, String fieldValue) {
    final String query = "SELECT C1.id, C1.channel_id, C1.parent_id, C1.text, C1.author_id, C1.like_count, C1.published_at, C1.updated_at, C1.comment_type, C1.comment_id "
        + " FROM collected_comment C1 "
        + "WHERE 1=1 AND "
        + fieldName + " = '" + fieldValue + "'";
    final List<CollectedComment> selected = new ArrayList<>();

    Connection conn = getConnection();
    PreparedStatement pstmt = null;
    ResultSetMetaData meta;

    try {
      pstmt = conn.prepareStatement(query);
      ResultSet rs = pstmt.executeQuery();
      meta = rs.getMetaData();
      while (rs.next()) {
        Map<String, Object> row = new HashMap<>();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
          row.put(meta.getColumnName(i), rs.getObject(i));
        }
        selected.add(CommentMapper.objectToCollectedComment(row));
      }
    } catch (SQLException | ParseException e) {
      e.printStackTrace();

    } finally {
      if (pstmt != null) {
        try {
          pstmt.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    return selected;
  }

}
