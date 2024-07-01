package command;

import command.handler.BaseCommandHandler;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

@Command(name = "Base Collect Command", description = "Example of Collect command", subcommands = {
    HelpCommand.class})
public class BaseCollectCommand implements Runnable {

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
    new CommandLine((new BaseCollectCommand()))
        .setExecutionExceptionHandler(new BaseCommandHandler())
        .execute(args);

    // teardown collector instances
  }

  @Override
  public void run() {
    // do something (collecting logic)
    // ex. collector.collect();
  }
}
