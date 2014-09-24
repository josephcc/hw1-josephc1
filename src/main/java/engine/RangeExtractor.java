package engine;

import java.util.Map;

public interface RangeExtractor {
  public Map<Integer, Integer> getSpans(String text);
}
