package command;

import collector.usingapi.CommentOnVideoCollector;
import collector.usingapi.ReplyCollector;
import collector.usingapi.impl.BasicCommentOnVideoCollector;
import collector.usingapi.impl.ExtractOnResponseReplyCollector;
import collector.usingapi.impl.ExtractWithRepliesApiCollector;
import command.handler.BaseCommandHandler;
import core.Comment;
import core.Video;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "Collect Comments On Video Command", description = "collect all comments on a single video", subcommands = {
    HelpCommand.class})
public class CollectCommentsOnVideoCommand implements Runnable {

  private static String apiKey;

  @Parameters(index = "0", description = "The video id to collect comments from")
  private String videoId;

  @Option(names = "--collect-all-replies", description = "If set this value to false, collect only up to 5 replies.", defaultValue = "true")
  private boolean collectAllReplies;

  @Option(names = "--comment-page-size", description = "The number of comments to request per page", defaultValue = "100")
  private int commentPageSize;

  @Option(names = "--comment-max-results", description = "The maximum number of top level comments to collect. If this value is set below zero, collect all pages.", defaultValue = "0")
  private int commentMaxResults;

  @Option(names = "--reply-page-size", description = "The number of replies to request per page", defaultValue = "100")
  private int replyPageSize;

  @Option(names = "--reply-max-results", description = "The maximum number of replies to collect. If this value is set below zero, collect all pages.", defaultValue = "0")
  private int replyMaxResults;

  public static void main(String[] args) {
    String resourcePath = "src/main/resources/secrets/youtubeapi.properties";
    try (FileReader file = new FileReader(resourcePath)) {
      Properties p = new Properties();
      p.load(file);
      apiKey = p.getProperty("key", "");
    } catch (Exception e) {
      System.out.println("cannot read properties file");
    }

    new CommandLine((new CollectCommentsOnVideoCommand()))
        .setExecutionExceptionHandler(new BaseCommandHandler())
        .execute(args);
  }

  @Override
  public void run() {
    String baseUrl = "https://www.googleapis.com/youtube/v3";
    ReplyCollector replyCollector = collectAllReplies ?
        new ExtractWithRepliesApiCollector(apiKey, baseUrl, replyPageSize, replyMaxResults) :
        new ExtractOnResponseReplyCollector();

    CommentOnVideoCollector commentCollector = new BasicCommentOnVideoCollector(
        apiKey, baseUrl, commentPageSize, commentMaxResults,
        replyCollector, List.of());

    Video targetVideo = new Video(videoId, LocalDateTime.now(), "", "", "", "");

    List<Comment> results = commentCollector.collectComments(targetVideo);
    results.forEach(System.out::println);
    System.out.println("results.size() = " + results.size());
  }
}
