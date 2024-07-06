package command;

import collector.usingapi.ExtractOnResponseReplyCollector;
import collector.usingapi.ExtractWithRepliesApiCollector;
import collector.usingapi.ReplyCollector;
import collector.usingapi.YoutubeCommentListApi;
import collector.usingapi.requestvo.CommentThreadRequestPart;
import command.handler.BaseCommandHandler;
import java.io.FileReader;
import java.util.Properties;
import java.util.Set;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "Collect Comments On Video Command", description = "collect all comments on a single video", subcommands = {
    HelpCommand.class})
public class PopularVideoCommand implements Runnable {

  private static String apiKey;

  @Parameters(index = "0", description = "The video id to collect comments from")
  private String videoId;

  @Option(names = "--collect-all-replies", description = "If set this value to false, collect only up to 5 replies.", defaultValue = "true")
  private boolean collectAllReplies;

  @Option(names = "--page-size", description = "The number of comments to request per page", defaultValue = "50")
  private int pageSize;

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
        new ExtractWithRepliesApiCollector(apiKey, baseUrl) :
        new ExtractOnResponseReplyCollector();

    YoutubeCommentListApi youtubeCommentListApi = new YoutubeCommentListApi(
        apiKey,
        baseUrl,
        Set.of(CommentThreadRequestPart.ID,
            CommentThreadRequestPart.SNIPPET,
            CommentThreadRequestPart.REPLY),
        videoId,
        pageSize,
        replyCollector
    );
    int collectedCommentCount = 0;
    int collectedTopLevelCommentCount = 0;
    while (youtubeCommentListApi.hasNextPage()) {
      youtubeCommentListApi.requestNextPage().forEach(System.out::println);
      collectedCommentCount += youtubeCommentListApi.getTotalCommentCount();
      collectedTopLevelCommentCount += youtubeCommentListApi.getTotalTopLevelCommentCount();
    }
    System.out.println("collectedTopLevelCommentCount = " + collectedTopLevelCommentCount);
    System.out.println("collectedCommentCount = " + collectedCommentCount);
  }
}
