package command;

import collector.usingapi.CommentOnVideoCollector;
import collector.usingapi.ReplyCollector;
import collector.usingapi.impl.BasicCommentOnVideoCollector;
import collector.usingapi.impl.ExtractOnResponseReplyCollector;
import collector.usingapi.impl.ExtractWithRepliesApiCollector;
import collector.usingapi.impl.PopularVideoSelector;
import command.handler.BaseCommandHandler;
import container.ListContainer;
import core.ICommentProcessor;
import core.ICommentsContainer;
import core.Video;
import core.filter.IVideoFilter;
import filter.OffMusicCategoryVideoFilter;
import java.io.FileReader;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import proccessor.tocsv.ToCsvProcessor;

@Command(name = "Collect Comments Popular Videos", description = "collect all comments on most popular videos and save to csv", subcommands = {
    HelpCommand.class})
public class CollectCommentsAndToCsvCommand implements Runnable{

  private static String apiKey;

  @Parameters(index = "0", description = "The file path to save the comments")
  private String filePath;

  @Option(names = "--collect-all-replies", description = "If set this value to false, collect only up to 5 replies.", defaultValue = "true")
  private boolean collectAllReplies;

  @Option(names = "--video-page-size", description = "The number of comments to request per page", defaultValue = "50")
  private int videoPageSize;

  @Option(names = "--video-max-results", description = "The number of comments to request per page", defaultValue = "200")
  private int videoMaxResults;

  @Option(names = "--off-music-category", description = "The number of comments to request per page", defaultValue = "false")
  private boolean offMusicCategory;

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

    new CommandLine((new CollectCommentsAndToCsvCommand()))
        .setExecutionExceptionHandler(new BaseCommandHandler())
        .execute(args);
  }

  @Override
  public void run() {
    String baseUrl = "https://www.googleapis.com/youtube/v3";

    ICommentProcessor processor = new ToCsvProcessor(filePath);
    ICommentsContainer listContainer = new ListContainer(processor);
    List<IVideoFilter> videoFilters = offMusicCategory ? List.of(new OffMusicCategoryVideoFilter()) : List.of();

    // Collect popular videos
    PopularVideoSelector popularVideoSelector = new PopularVideoSelector(
        apiKey, baseUrl, videoPageSize, videoMaxResults, videoFilters
    );
    Stream<Video> popularVideos = popularVideoSelector.select();

    ReplyCollector replyCollector = collectAllReplies ?
        new ExtractWithRepliesApiCollector(apiKey, baseUrl, replyPageSize, replyMaxResults) :
        new ExtractOnResponseReplyCollector();

    CommentOnVideoCollector commentCollector = new BasicCommentOnVideoCollector(
        apiKey, baseUrl, commentPageSize, commentMaxResults,
        replyCollector, List.of());

    popularVideos.forEach(video -> listContainer.addDatas(commentCollector.collectComments(video)));

    listContainer.flush();
    System.out.println("Total comments saved to csv: " + filePath);
  }
}
