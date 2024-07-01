package command.handler;

import picocli.CommandLine;
import picocli.CommandLine.IExecutionExceptionHandler;
import picocli.CommandLine.ParseResult;

public class BaseCommandHandler implements IExecutionExceptionHandler {

  @Override
  public int handleExecutionException(Exception ex, CommandLine commandLine,
      ParseResult fullParseResult) throws Exception {
    return 0;
  }
}
