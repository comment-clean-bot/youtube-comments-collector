package proccessor.tocsv;

import core.Comment;
import core.ICommentProcessor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ToCsvProcessor implements ICommentProcessor, AutoCloseable {

  private final String filePath;
  private final File file;
  private final FileWriter fileWriter;
  private final BufferedWriter bufferedWriter;
  private final String cvsSplitBy;
  public ToCsvProcessor(String filePath) {
    this.filePath = filePath;
    this.file = new File(filePath);
    this.cvsSplitBy = ",\t";
    boolean isNewFile = false;
    try {
      if (!file.exists()) {
        isNewFile = file.createNewFile();
      }
      this.fileWriter = new FileWriter(file, true);
      this.bufferedWriter = new BufferedWriter(fileWriter);

      if (isNewFile) {
        bufferedWriter.write("Id,\tChannelId,\tVideoId,\tParentId,\tText,\tAuthor,\tLikeCount,\tPublishedAt,\tUpdatedAt,\tPreLabel\n");
        bufferedWriter.flush();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public void close(){
    cleanUp();
  }
  @Override
  public void commitData(Comment comment) {
    // Implement the logic to commit the comment to a CSV file.
    // The file path is stored in the filePath variable.
    String line;
    try {
      line = commentToString(comment, cvsSplitBy);
      bufferedWriter.write(line);
      bufferedWriter.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String commentToString(Comment comment, String splitBy){
    String textToString = textToString(comment.text());
    String labelToString = labelToString(comment.preLabel());
    String parentId = parentIdToString(comment.parentId());
    return comment.id() + splitBy +
        comment.channelId() + splitBy +
        comment.videoId() + splitBy +
        parentId + splitBy +
        textToString + splitBy +
        comment.author() + splitBy +
        comment.likeCount() + splitBy +
        comment.publishedAt() + splitBy +
        comment.updatedAt() + splitBy +
        labelToString + "\n";
  }

  private String textToString(String text){
    String match = "[^\uAC00-\uD7A30-9a-zA-Z]";
    String temp = text.replaceAll(match, " ");
    return temp;
  }

  private String labelToString(Boolean preLabel){
    return preLabel == null ? "X" : preLabel ? "O" : "X";
  }

  private String parentIdToString(String parentId){
    return parentId == null ? "no parent" : parentId;
  }

  private void cleanUp() {
    try {
      if (bufferedWriter != null) {
        bufferedWriter.flush();
        bufferedWriter.close();
      }
      if (fileWriter != null) {
        fileWriter.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
