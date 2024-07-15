package proccessor.todb;

import core.Comment;
import core.ICommentProcessor;
import java.util.HashMap;
import java.util.Map;
import repository.CommentRepository;
import repository.SQLiteCommentRepository;

public class ToDbProcessor implements ICommentProcessor {

  private final CommentRepository commentRepository = new SQLiteCommentRepository();
  @Override
  public void commitData(Comment comment) {
    if (!commentRepository.existsByCommentId(comment.id())) {
      commentRepository.save(comment);
    } else {
      System.out.println("Comment already exists in the database: " + comment.id());
      Map<String, Object> updateFields = new HashMap<>();
      updateFields.put("like_count", comment.likeCount());
      updateFields.put("text", comment.text());
      updateFields.put("pre_label", comment.preLabel());
      commentRepository.update(comment, updateFields);
    }
  }
}
