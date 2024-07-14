package filter;

import core.Video;
import core.filter.IVideoFilter;

public class OffMusicCategoryVideoFilter implements IVideoFilter {

  @Override
  public boolean isAcceptable(Video video) {
    return !video.categoryId().equals("10");
  }
}
