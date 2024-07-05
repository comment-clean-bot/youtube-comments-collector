package command;

import collector.usingapi.ExtractOnResponseReplyCollector;
import collector.usingapi.YoutubeCommentListApi;
import collector.usingapi.requestvo.CommentRequestPart;
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
public class CollectCommentsOnVideoCommand implements Runnable {

  private static String apiKey;

  @Parameters(index = "0", description = "The video id to collect comments from")
  private String videoId;

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
    YoutubeCommentListApi youtubeCommentListApi = new YoutubeCommentListApi(
        apiKey,
        "https://www.googleapis.com/youtube/v3",
        Set.of(CommentRequestPart.ID,
            CommentRequestPart.SNIPPET,
            CommentRequestPart.REPLY),
        videoId,
        pageSize,
        new ExtractOnResponseReplyCollector()
    );
    while (youtubeCommentListApi.hasNextPage()) {
      youtubeCommentListApi.requestNextPage().forEach(System.out::println);
    }
  }
}
