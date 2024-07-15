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
    String query = "INSERT INTO collected_comment (channel_id, parent_id, text, author_id, like_count, published_at, updated_at, comment_type, comment_id, pre_label) VALUES ("
        + "'" + comment.getChannelId() + "', "
        + "'" + comment.getParentId() + "', "
        + "'" + comment.getText() + "', "
        + "'" + comment.getAuthorId() + "', "
        + comment.getLikeCount() + ", "
        + "'" + DATE_FORMAT.format(comment.getPublishedAt()) + "', "
        + "'" + DATE_FORMAT.format(comment.getUpdatedAt()) + "', "
        + "'" + comment.getCommentType().name() + "', "
        + "'" + comment.getCommentId() + "', "
        + (comment.isPreLabel() ? 1 : 0)
        + ")";

    Connection conn = getConnection();
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
    final StringBuilder query = new StringBuilder("UPDATE collected_comment \n");
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
    query.append("AND id = ? ");

    final String finalQuery = query.toString();
    Connection conn = getConnection();
    PreparedStatement pstmt = null;

    int updated = 0;

    try {
      pstmt = conn.prepareStatement(finalQuery);
      int i = 1;
      for (String key : updateSeq) {
        pstmt.setObject(i++, updateField.get(key));
      }
      pstmt.setString(i, Integer.toString(comment.getId()));

      updated = pstmt.executeUpdate();
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

  public int updateCollectedComments(List<CollectedComment> comments) {
    final String query = "UPDATE collected_comment SET text = ?, like_count = ?, updated_at = ?, pre_label = ? WHERE id = ?";
    final List<String> updateSeq = new ArrayList<>(List.of("text", "like_count", "updated_at", "pre_label"));
    Connection conn = getConnection();
    PreparedStatement pstmt = null;
    int updated = 0;
    try {
      pstmt = conn.prepareStatement(query);
      for (CollectedComment comment : comments) {
        final Map<String, Object> updateField = comment.toMap(List.of("text", "like_count", "pre_label"));
        updateField.put("updated_at", DATE_FORMAT.format(LocalDateTime.now()));
        int i = 1;
        for (String key : updateSeq) {
          pstmt.setObject(i++, updateField.get(key));
        }

        pstmt.setString(i, Integer.toString(comment.getId()));
        pstmt.addBatch();
        if (updated % batchSize == 0) {
          updated += pstmt.executeBatch().length;
        }
      }
      updated += pstmt.executeBatch().length;
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
    Connection conn = getConnection();
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
    final String query = "INSERT INTO collected_comment (channel_id, parent_id, text, author_id, like_count, published_at, updated_at, comment_type, comment_id, pre_label) VALUES (?,?,?,?,?,?,?,?,?,?)";
    Connection conn = getConnection();
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
        pstmt.setString(9, comment.getCommentId());
        pstmt.setInt(10, comment.isPreLabel() ? 1 : 0);

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
