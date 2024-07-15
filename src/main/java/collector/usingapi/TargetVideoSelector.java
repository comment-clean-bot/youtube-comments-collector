package collector.usingapi;

import core.Video;
import java.util.stream.Stream;

public interface TargetVideoSelector {

  Stream<Video> select();

}
