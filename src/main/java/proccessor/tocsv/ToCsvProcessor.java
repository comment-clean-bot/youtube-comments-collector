package proccessor.tocsv;

import core.Comment;
import core.ICommentProcessor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ToCsvProcessor implements ICommentProcessor {

  private final String filePath;
  private final File file;
  private final FileWriter fileWriter;
  private final BufferedWriter bufferedWriter;
  public ToCsvProcessor(String filePath) {
    this.filePath = filePath;
    this.file = new File(filePath);

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
    String cvsSplitBy = ", ";
    try {
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
    String temp = comment.text().replace("\n", " ");

    return comment.id() + splitBy +
        comment.channelId() + splitBy +
        comment.videoId() + splitBy +
        comment.parentId() + splitBy +
        temp + splitBy +
        comment.author() + splitBy +
        comment.likeCount() + splitBy +
        comment.publishedAt() + splitBy +
        comment.updatedAt() +
        (comment.preLabel() ? "O" : "X") + "\n";
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
