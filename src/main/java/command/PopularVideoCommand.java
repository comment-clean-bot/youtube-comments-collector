package command;

import collector.usingapi.impl.PopularVideoSelector;
import command.handler.BaseCommandHandler;
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

@Command(name = "Collect Most Popular Video", description = "collect top 200 current popular video", subcommands = {
    HelpCommand.class})
public class PopularVideoCommand implements Runnable {

  private static String apiKey;

  @Option(names = "--page-size", description = "The number of comments to request per page", defaultValue = "50")
  private int pageSize;

  @Option(names = "--total-max-count", description = "The number of comments to request per page", defaultValue = "200")
  private int totalMaxCount;

  @Option(names = "--off-music-category", description = "The number of comments to request per page", defaultValue = "false")
  private boolean offMusicCategory;

  public static void main(String[] args) {
    String resourcePath = "src/main/resources/secrets/youtubeapi.properties";
    try (FileReader file = new FileReader(resourcePath)) {
      Properties p = new Properties();
      p.load(file);
      apiKey = p.getProperty("key", "");
    } catch (Exception e) {
      System.out.println("cannot read properties file");
    }

    new CommandLine((new PopularVideoCommand()))
        .setExecutionExceptionHandler(new BaseCommandHandler())
        .execute(args);
  }

  @Override
  public void run() {
    String baseUrl = "https://www.googleapis.com/youtube/v3";
    List<IVideoFilter> videoFilters = offMusicCategory ? List.of(new OffMusicCategoryVideoFilter()) : List.of();

    PopularVideoSelector popularVideoSelector = new PopularVideoSelector(
        apiKey, baseUrl, pageSize, totalMaxCount, videoFilters);
    Stream<Video> popularVideos = popularVideoSelector.select();
    popularVideos.forEach(System.out::println);

    System.out.println("Total popular videos: " + popularVideos.count());
  }
}
