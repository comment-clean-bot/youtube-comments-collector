package filter;

import core.Video;
import core.filter.IVideoFilter;

public class AllPassVideoFilter implements IVideoFilter {

  @Override
  public boolean isAcceptable(Video comment) {
    return true;
  }
}
