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
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.resource.ResourceSpecifier;

public class GeneConsumer extends CasConsumer_ImplBase {

  /**
   * Handle to the final output file
   */
  private PrintWriter outFile;

  /**
   * Path to the final output file
   */
  private final String filename = "hw1-joseph1.out";

  /**
   * Initializer
   * 
   * Initialize output file handle
   * 
   * @see org.apache.uima.collection.CasConsumer_ImplBase#initialize(org.apache.uima.resource.ResourceSpecifier,
   *      java.util.Map)
   */
  @Override
  public boolean initialize(ResourceSpecifier aSpecifier, Map<String, Object> aAdditionalParams)
          throws ResourceInitializationException {
    boolean out = super.initialize(aSpecifier, aAdditionalParams);
    try {
      outFile = new PrintWriter(filename, "UTF-8");
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return out;
  }

  /**
   * Release resources
   * 
   * Release output file handle before class is destroyed
   * 
   * @see org.apache.uima.collection.CasConsumer_ImplBase#destroy()
   */
  @Override
  public void destroy() {
    outFile.close();
    super.destroy();
  }

  /**
   * Gather data for output
   * 
   * This function is called multiple-times during runtime, each time with a CAS and potentially
   * multiple annotations.
   * 
   * 
   * @see org.apache.uima.collection.base_cpm.CasObjectProcessor#processCas(org.apache.uima.cas.CAS)
   */
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
      String preText = gene.getText().substring(0, gene.getBegin());
      String name = gene.getGene();
      int preSpace = preText.length() - preText.replaceAll(" ", "").length();
      int inSpace = name.length() - name.replaceAll(" ", "").length();
      String out = gene.getId() + "|" + (gene.getBegin() - preSpace) + " "
              + (gene.getEnd() - preSpace - inSpace - 1) + "|" + gene.getGene();

      // System.out.println(out);
      outFile.println(out);
    }
  }
}
