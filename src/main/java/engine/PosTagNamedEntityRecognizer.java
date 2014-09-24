package engine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.resource.ResourceInitializationException;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;
import com.aliasi.util.AbstractExternalizable;

import model.Gene;
import model.Sentence;




public class PosTagNamedEntityRecognizer extends JCasAnnotator_ImplBase {

  private Chunker chunker;
  
  @Override
  public void initialize(UimaContext context)
          throws ResourceInitializationException {
    super.initialize(context);

    String currentDir = System.getProperty("user.dir");
    File modelFile = new File(currentDir + "/src/main/resources/data/ne-en-bio-genetag.HmmChunker");
    
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

  public PosTagNamedEntityRecognizer() throws ResourceInitializationException {
  }

  public Map<Integer, Integer> getGeneSpans(String text) {
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
    
    FSIterator<Annotation> iter = aJCas.getAnnotationIndex(Sentence.type).iterator(); 

    while (iter.hasNext()) {
      Sentence sentence = (Sentence) iter.next();

      System.out.println(sentence.getId());
      System.out.println(sentence.getText());
      Map<Integer, Integer> genes = getGeneSpans(sentence.getText());
      for (Entry<Integer, Integer> range : genes.entrySet())
      {
          Integer begin = range.getKey();
          Integer end = range.getValue();
          String name = sentence.getText().substring(begin, end);
          System.out.println(begin + "/" + end + " -> " + name);
          
          Gene gene = new Gene(aJCas);
          gene.setBegin(begin);
          gene.setEnd(end);
          gene.setGene(name);
          gene.setId(sentence.getId());
          gene.setText(sentence.getText());
          gene.addToIndexes();
          
      }
      System.out.println("-----");
    }
    
        
  }
}
