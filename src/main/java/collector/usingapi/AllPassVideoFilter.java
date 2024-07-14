package collector.usingapi;

public class AllPassVideoFilter implements IVideoFilter {

  @Override
  public boolean isAcceptable(Video comment) {
    return true;
  }
}
