package command;

import collector.usingapi.PopularVideoSelector;
import collector.usingapi.Video;
import command.handler.BaseCommandHandler;
import java.io.FileReader;
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

  public static void main(String[] args) {
    String resourcePath = "src/main/resources/secrets/youtubeapi.properties";
    try (FileReader file = new FileReader(resourcePath)) {
      Properties p = new Properties();
      p.load(file);
      apiKey = p.getProperty("videoKey", "");
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
    System.out.println("Collecting top 200 popular videos");
    PopularVideoSelector popularVideoSelector = new PopularVideoSelector(apiKey, baseUrl);
    Stream<Video> popularVideos = popularVideoSelector.select();
    popularVideos.forEach(System.out::println);

    System.out.println("Total popular videos: " + popularVideos.count());
  }
}
