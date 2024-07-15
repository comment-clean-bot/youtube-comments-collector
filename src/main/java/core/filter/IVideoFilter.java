package core.filter;

import core.Video;

public interface IVideoFilter {
  boolean isAcceptable(Video comment);
}
