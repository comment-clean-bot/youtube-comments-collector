package command;

import command.handler.BaseCommandHandler;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.Option;

@Command(name = "Tutorial Command", description = "Example of command line", subcommands = {
    HelpCommand.class})
public class TutorialCommand implements Runnable {

  @Option(names = "--user", defaultValue = "World", description = "Say Hello to user")
  String user;

  // manage collector instances using private fields
  /* ex.
  private final ICommentCollector collector;

  public BaseCollectCommand(ICommentCollector collector) {
    this.collector = collector;
  }
  */

  public static void main(String[] args) {
    // setup collector instances
    // ex. ICommentCollector collector = new CommentCollector();

    // inject collector instances
    // ex. new CommandLine((new BaseCollectCommand(collector)))
    new CommandLine((new TutorialCommand()))
        .setExecutionExceptionHandler(new BaseCommandHandler())
        .execute(args);

    // teardown collector instances
  }

  @Override
  public void run() {
    // do something (collecting logic)
    // ex. collector.collect();
    System.out.println("Hello " + user + "!");
  }
}
