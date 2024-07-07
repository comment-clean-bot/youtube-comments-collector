package proccessor.tocsv;

import core.Comment;
import core.ICommentProcessor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ToCsvProcessor implements ICommentProcessor {

  private final String filePath;

  public ToCsvProcessor(String filePath) {
    this.filePath = filePath;
  }

  @Override
  public void commitData(Comment comment) {
    // Implement the logic to commit the comment to a CSV file.
    // The file path is stored in the filePath variable.
    File file = new File(filePath);
    BufferedWriter bufferedWriter = null;
    String line;
    String cvsSplitBy = ", ";
    if (!file.exists()) {
      try {
        bufferedWriter = new BufferedWriter(new FileWriter(file, true));
        bufferedWriter.write("ChannelId, VideoId, Text ,Author,LikeCount,PublishedAt, UpdatedAt\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    try {
      bufferedWriter = new BufferedWriter(new FileWriter(file, true));
      line = commentToString(comment, cvsSplitBy);
      bufferedWriter.write(line);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (bufferedWriter != null) {
          bufferedWriter.flush();
          bufferedWriter.close();
        }
      } catch (IOException e){
        e.printStackTrace();
      }
    }
  }

  private String commentToString(Comment comment, String splitBy){
    return comment.channelId() + splitBy +
        comment.videoId() + splitBy +
        comment.text() + splitBy +
        comment.author() + splitBy +
        comment.likeCount() + splitBy +
        comment.publishedAt() + splitBy +
        comment.updatedAt() + "\n";
  }
}
