package engine;

import java.util.Map;

/**
 * Generic range extractor, implement this interface as a strategy for annotators
 * 
 * @author josephcc
 * 
 */
public interface RangeExtractor {
  public Map<Integer, Integer> getSpans(String text);
}
