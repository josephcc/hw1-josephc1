package engine;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import model.Gene;
import model.Sentence;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

public class MyCollectionReader extends CollectionReader_ImplBase {

  Boolean empty = false; 
  
  @Override
  public void getNext(CAS aCAS) throws IOException, CollectionException {
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new CollectionException(e);
    }

    // open input stream to file
    File file = new File("src/main/resources/data/sample.in");
    BufferedReader br = new BufferedReader(new FileReader(file));
    String line;
    while ((line = br.readLine()) != null) {
      int sep = line.indexOf(' ');
      String id = line.substring(0, sep);
      String text = line.substring(sep+1);
      
      System.out.println(id);
      
      Sentence sentence = new Sentence(jcas);
      sentence.setId(id);;
      sentence.setText(text);
      sentence.addToIndexes(jcas);
    }
    br.close();
    
    empty = true;
  }

  @Override
  public void close() throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public Progress[] getProgress() {
    // TODO Auto-generated method stub
    if (empty) {
      return new Progress[]{new ProgressImpl(0,1,Progress.ENTITIES)};
    }
    else {
      return new Progress[]{new ProgressImpl(1,1,Progress.ENTITIES)};
    }
  }

  @Override
  public boolean hasNext() throws IOException, CollectionException {
    return !empty;
  }

}
