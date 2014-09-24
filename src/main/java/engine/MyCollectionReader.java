package engine;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
    BufferedInputStream fis = 
            new BufferedInputStream(new FileInputStream(file));
    try {
      byte[] contents = new byte[(int) file.length()];
      fis.read(contents);
      String text = new String(contents);;

      jcas.setDocumentText(text);
    } finally {
      if (fis != null)
        fis.close();
    }

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
