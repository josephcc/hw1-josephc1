package engine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.uima.resource.ResourceInitializationException;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;
import com.aliasi.util.AbstractExternalizable;

import model.Gene;




public class PosTagNamedEntityRecognizer extends JCasAnnotator_ImplBase {


  public PosTagNamedEntityRecognizer() throws ResourceInitializationException {
  }

  public Map<Integer, Integer> getGeneSpans(String text, Chunker chunker) {
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

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    //System.out.println("test");
    // get document text

    String currentDir = System.getProperty("user.dir");
    File modelFile = new File(currentDir + "/src/main/resources/data/ne-en-bio-genetag.HmmChunker");
    
    Chunker chunker = null;
    try {
      chunker = (Chunker) AbstractExternalizable.readObject(modelFile);
    } catch (IOException e1) {
      e1.printStackTrace();
      return;
    } catch (ClassNotFoundException e1) {
      e1.printStackTrace();
      return;
    }
    
    String docText = aJCas.getDocumentText();
    String lines[] = docText.split("\\r?\\n");
    for(String line : lines){
      int sep = line.indexOf(' ');
      String id = line.substring(0, sep);
      String text = line.substring(sep+1);
      System.out.println(id);
      System.out.println(text);
      Map<Integer, Integer> genes = getGeneSpans(text, chunker);
      for (Entry<Integer, Integer> gene : genes.entrySet())
      {
          Integer begin = gene.getKey();
          Integer end = gene.getValue();
          String name = text.substring(begin, end);
          System.out.println(begin + "/" + end + " -> " + name);
          
          Gene annotation = new Gene(aJCas);
          annotation.setBegin(begin);
          annotation.setEnd(end);
          annotation.setId(id);
          annotation.setGene(name);
          annotation.addToIndexes();
          
      }
      System.out.println("-----");
    }
    
        
  }
}
