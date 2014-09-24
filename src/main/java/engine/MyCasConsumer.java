package engine;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import model.Gene;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.resource.ResourceSpecifier;

public class MyCasConsumer extends CasConsumer_ImplBase {
  
  private PrintWriter outFile;

  @Override
  public boolean initialize(ResourceSpecifier aSpecifier, Map<String,Object> aAdditionalParams) throws ResourceInitializationException {
    boolean out = super.initialize(aSpecifier, aAdditionalParams);
    try {
      outFile = new PrintWriter("hw1-joseph1.out", "UTF-8");
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return out;
  }
  
  @Override
  public void destroy() {
    outFile.close();
    super.destroy();
  }


  @Override
  public void processCas(CAS aCAS) throws ResourceProcessException {
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new ResourceProcessException(e);
    }
    
    FSIterator<Annotation> it = jcas.getAnnotationIndex(Gene.type).iterator(); 
    while (it.hasNext()) {
      Gene gene = (Gene) it.next();
      String out = gene.getId() + "|" + gene.getBegin() + " " + gene.getEnd() + "|" + gene.getGene();
      System.out.println(out);
      outFile.println(out);
    }
  }
}
