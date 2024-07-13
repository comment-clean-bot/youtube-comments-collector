package database.SQLite;

import database.CollectedComment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DMLService extends SQLiteManager{

  public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private int batchSize = 100;

  public DMLService(){
  }

  public DMLService(String dbPath){
    super(dbPath);
  }

  public DMLService(String dbPath, int batchSize){
    super(dbPath);
    this.batchSize = batchSize;
  }

  public int insertCollectedComment(CollectedComment comment) throws SQLException {
    String query = "INSERT INTO comments (channel_id, parent_id, text, author_id, like_count, published_at, updated_at, comment_type) VALUES ("
        + "'" + comment.getChannelId() + "', "
        + "'" + comment.getParentId() + "', "
        + "'" + comment.getText() + "', "
        + "'" + comment.getAuthorId() + "', "
        + comment.getLikeCount() + ", "
        + "'" + DATE_FORMAT.format(comment.getPublishedAt()) + "', "
        + "'" + DATE_FORMAT.format(comment.getUpdatedAt()) + "', "
        + "'" + comment.getCommentType().name() + "'"
        + ")";
    Connection conn = ensureConnection();
    PreparedStatement pstmt = null;

    int inserted = 0;

    try {
      pstmt = conn.prepareStatement(query);
      inserted = pstmt.executeUpdate();
      conn.commit();
    } catch (SQLException e) {
      e.printStackTrace();
      conn.rollback();
      inserted -= 1;
    } finally {
      if (pstmt != null) {
        try{
          pstmt.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    return inserted;
  }

  public int updateCollectedComments(CollectedComment comment, Map<String,Object> updateField){
    final StringBuilder query = new StringBuilder("UPDATE collected_comment SET \n");
    final List<String> updateSeq = new ArrayList<>();
    updateField.put("updated_at", DATE_FORMAT.format(LocalDateTime.now()));

    query.append("SET ");
    query.append(
        updateField.keySet().stream().reduce("", (prev, cur)-> {
          updateSeq.add(cur);
          cur = cur + " = ?";
          if( !"".equals(prev) ) {
            prev = prev+", ";
          }
          return prev+cur+"\n";
        })
    );
    query.append("WHERE 1 = 1 \n");
    query.append("AND channel_id = ?");
    query.append("AND parent_id = ?");
    query.append("AND comment_id = ?");
    query.append("AND comment_type = ?");

    final String finalQuery = query.toString();
    Connection conn = ensureConnection();
    PreparedStatement pstmt = null;

    int updated = 0;
    try {
      pstmt = conn.prepareStatement(finalQuery);
      int i = 1;
      for (String key : updateSeq) {
        pstmt.setObject(i++, updateField.get(key));
      }
      pstmt.setString(i++, comment.getChannelId());
      pstmt.setString(i++, comment.getParentId());
      pstmt.setString(i++, comment.getId());
      pstmt.setString(i, comment.getCommentType().name());

      pstmt.executeUpdate();
      updated += pstmt.getUpdateCount();
      conn.commit();
    } catch (SQLException e) {
      e.printStackTrace();
      updated = -1;
      try {
        conn.rollback();
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
    } finally {
      if (pstmt != null) {
        try {
          pstmt.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return updated;
  }

  public int deleteCollectedComment(String fieldName, String fieldValue) throws SQLException {
    final String query = "DELETE FROM collected_comment WHERE " + fieldName + " = '" + fieldValue + "'";
    Connection conn = ensureConnection();
    PreparedStatement pstmt = null;

    int deleted = 0;

    try {
      pstmt = conn.prepareStatement(query);
      deleted += pstmt.executeUpdate();
      conn.commit();
    } catch (SQLException e) {
      e.printStackTrace();
      conn.rollback();
      deleted = -1;
    } finally {
      if (pstmt != null) {
        try {
          pstmt.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return deleted;
  }

  public int insertCollectedComments(List<CollectedComment> comments) {
    int inserted = 0;
    final String query = "INSERT INTO collected_comment (channel_id, parent_id, text, author_id, like_count, published_at, updated_at, comment_type) VALUES (?,?,?,?,?,?,?,?)";
    Connection conn = ensureConnection();
    PreparedStatement pstmt = null;

    try {
      pstmt = conn.prepareStatement(query);
      for (CollectedComment comment : comments) {
        pstmt.setString(1, comment.getChannelId());
        pstmt.setString(2, comment.getParentId());
        pstmt.setString(3, comment.getText());
        pstmt.setString(4, comment.getAuthorId());
        pstmt.setLong(5, comment.getLikeCount());
        pstmt.setString(6, DATE_FORMAT.format(comment.getPublishedAt()));
        pstmt.setString(7, DATE_FORMAT.format(comment.getUpdatedAt()));
        pstmt.setString(8, comment.getCommentType().name());

        pstmt.addBatch();

        if (inserted % batchSize == 0) {
          inserted += pstmt.executeBatch().length;
        }
      }
      inserted += pstmt.executeBatch().length;
      conn.commit();
    } catch (SQLException e) {
      e.printStackTrace();
      inserted = -1;
      try {
        conn.rollback();
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
    } finally {
      if (pstmt != null) {
        try {
          pstmt.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return inserted;
  }
}
