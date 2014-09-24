package engine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;
import com.aliasi.util.AbstractExternalizable;

public class GeneRangeExtractor implements RangeExtractor {

  /**
   * Chunker for GENETAG NER tagger
   */
  private Chunker chunker;
  
  /**
   * relative path to the pre-trained model file 
   */
  private final String filename = "src/main/resources/data/ne-en-bio-genetag.HmmChunker";

  // Static member holds only one instance of the
  // SingletonExample class
  private static GeneRangeExtractor singletonInstance;

  // SingletonExample prevents any other class from instantiating
  private GeneRangeExtractor() {
  }

  // Providing Global point of access
  public static GeneRangeExtractor getSingletonInstance() {
      if (null == singletonInstance) {
          singletonInstance = new GeneRangeExtractor();
          singletonInstance.initialize();
      }
      return singletonInstance;
  }
  
  /**
   * Initializer
   * 
   * Load the pre-trained model once when object is initialized called when the singleton is created
   */
  public void initialize()  {

    String currentDir = System.getProperty("user.dir");
    File modelFile = new File(currentDir + "/" + filename);

    try {
      chunker = (Chunker) AbstractExternalizable.readObject(modelFile);
    } catch (IOException e1) {
      e1.printStackTrace();
      return;
    } catch (ClassNotFoundException e1) {
      e1.printStackTrace();
      return;
    }

  }
  

  /**
   * Extract gene sequence from text
   * 
   * Find spans of gene appearance in a given English text
   * 
   * @param text A English sentence that may or may not contains some gene sequence
   * @return A Map that map begin indexes to end indexes of where gene sequence appear in the input text 
   */
  public Map<Integer, Integer> getSpans(String text) {
    Map<Integer, Integer> begin2end = new HashMap<Integer, Integer>();
    Chunking chunking = chunker.chunk(text);
    Set<Chunk> chunkSet = chunking.chunkSet();
    Iterator<Chunk> it = chunkSet.iterator();
    while (it.hasNext()) {
      Chunk chunk = it.next();
      begin2end.put(chunk.start(), chunk.end());
    }
    return begin2end;
  }
}
