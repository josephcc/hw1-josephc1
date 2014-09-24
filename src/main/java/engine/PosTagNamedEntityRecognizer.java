package engine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.resource.ResourceInitializationException;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;


import model.Gene;
import model.Sentence;

public class PosTagNamedEntityRecognizer extends JCasAnnotator_ImplBase {
  
  public RangeExtractor extractor = GeneRangeExtractor.getSingletonInstance();


  public PosTagNamedEntityRecognizer() throws ResourceInitializationException {
  }


  /**
   * Process Sentence CAS
   * 
   * Extract Sentence type annotaions from CAS and process them by extract gene sequences
   */
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {

    FSIterator<Annotation> iter = aJCas.getAnnotationIndex(Sentence.type).iterator();

    while (iter.hasNext()) {
      Sentence sentence = (Sentence) iter.next();

      System.out.println(sentence.getId());
      System.out.println(sentence.getText());
      Map<Integer, Integer> genes = extractor.getSpans(sentence.getText());
      for (Entry<Integer, Integer> range : genes.entrySet()) {
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
