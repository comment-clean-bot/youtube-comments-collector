package proccessor.tocsv;

import com.opencsv.CSVWriter;
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
  private final CSVWriter csvWriter;
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
      this.csvWriter = new CSVWriter(fileWriter);

      if (isNewFile) {
        String[] header = {"id", "channelId", "videoId", "parentId", "text", "author", "likeCount", "publishedAt", "updatedAt", "preLabel"};
        csvWriter.writeNext(header);
        csvWriter.flush();
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
    String[] toWrite;
    try {
      toWrite = commentToString(comment, cvsSplitBy);
      csvWriter.writeNext(toWrite);
      csvWriter.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String[] commentToString(Comment comment, String splitBy){
    String textToString = escapeSpecialCharacters(comment.text());
    String labelToString = labelToString(comment.preLabel());
    String parentId = parentIdToString(comment.parentId());
    return new String[]{
        comment.id(),
        comment.channelId(),
        comment.videoId(),
        parentId,
        textToString,
        comment.author(),
        String.valueOf(comment.likeCount()),
        comment.publishedAt().toString(),
        comment.updatedAt().toString(),
        labelToString
    };
  }

  private static String escapeSpecialCharacters(String data) {
    if (data == null) {
      return "";
    }
    String escapedData = data.replaceAll("\"", "\"\"");
    escapedData = escapedData.replaceAll("\n", "\\n");
    escapedData = escapedData.replaceAll("\r", "\\r");
    escapedData = escapedData.replaceAll(",", "\",\"");
    if (data.contains(",") || data.contains("\n") || data.contains("\"")) {
      escapedData = "\"" + escapedData + "\"";
    }
    return escapedData;
  }

  private String labelToString(Boolean preLabel){
    return preLabel == null ? "X" : preLabel ? "O" : "X";
  }

  private String parentIdToString(String parentId){
    return parentId == null ? "no parent" : parentId;
  }

  private void cleanUp() {
    try {
      if (csvWriter != null) {
        csvWriter.flush();
        csvWriter.close();
      }
      if (fileWriter != null) {
        fileWriter.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
