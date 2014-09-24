package engine;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import model.Gene;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceProcessException;

public class MyCasConsumer extends CasConsumer_ImplBase {

  @Override
  public void processCas(CAS aCAS) throws ResourceProcessException {
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new ResourceProcessException(e);
    }
    
    FSIterator<Annotation> it = jcas.getAnnotationIndex(Gene.type).iterator(); 
    try {
      PrintWriter outFile = new PrintWriter("hw1-joseph1.out", "UTF-8");
      while (it.hasNext()) {
        Gene gene = (Gene) it.next();
        
        String out = gene.getId() + "|" + gene.getBegin() + " " + gene.getEnd() + "|" + gene.getGene();
        System.out.println(out);
        outFile.println(out);
      }
      outFile.close();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }



  }

}
