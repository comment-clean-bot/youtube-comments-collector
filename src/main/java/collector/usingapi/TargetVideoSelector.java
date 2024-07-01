package collector.usingapi;

import java.util.stream.Stream;

public interface TargetVideoSelector {

  Stream<Video> select();

}
