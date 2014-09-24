package engine;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import model.Sentence;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

public class MyCollectionReader extends CollectionReader_ImplBase {

  /**
   * Indicates if there are no more data to read.
   */
  private Boolean empty = false;

  /**
   * Handle for the (only) input file
   */
  private BufferedReader br = null;
  
  /**
   * Path to the only input file
   */
  private final String filename = "src/main/resources/data/sample.in";


  /**
   * Total number of lines in the input file, to provide progress.
   */
  private int TotalLines;

  /**
   * Number of lines already read, to provide progress.
   */
  private int CurrentLines = 0;

  /**
   * Count number of lines in a file
   * 
   * Static function that counts how many lines a file has.
   * @param filename path to the target file
   * @return number of lines
   * @throws IOException if file does not exists throws IOException
   */
  public static int countLines(String filename) throws IOException {
    InputStream is = new BufferedInputStream(new FileInputStream(filename));
    try {
      byte[] c = new byte[1024];
      int count = 0;
      int readChars = 0;
      boolean empty = true;
      while ((readChars = is.read(c)) != -1) {
        empty = false;
        for (int i = 0; i < readChars; ++i) {
          if (c[i] == '\n') {
            ++count;
          }
        }
      }
      return (count == 0 && !empty) ? 1 : count;
    } finally {
      is.close();
    }
  }

  /**
   * initializer
   * 
   * initialize the input file handle
   * 
   * @see org.apache.uima.collection.CollectionReader_ImplBase#initialize()
   */
   
  @Override
  public void initialize() throws ResourceInitializationException {
    super.initialize();
    // open input stream to file
    try {
      TotalLines = countLines(filename);
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    File file = new File(filename);
    try {
      br = new BufferedReader(new FileReader(file));
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Read records
   * 
   * If the input file still contains more data, read one line, parse ID / text, and package into Sentence
   * 
   * @see org.apache.uima.collection.CollectionReader#getNext(org.apache.uima.cas.CAS)
   */

  @Override
  public void getNext(CAS aCAS) throws IOException, CollectionException {
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new CollectionException(e);
    }
    String line;
    if ((line = br.readLine()) != null) {
      CurrentLines += 1;
      int sep = line.indexOf(' ');
      String id = line.substring(0, sep);
      String text = line.substring(sep + 1);

      Sentence sentence = new Sentence(jcas);
      sentence.setId(id);
      ;
      sentence.setText(text);
      sentence.addToIndexes(jcas);
    } else {
      br.close();
      empty = true;
    }
  }

  /**
   * Close the reader
   * 
   * Closing the reader causes the input file stream to be closed cleanly
   */
  @Override
  public void close() throws IOException {
    // TODO Auto-generated method stub
    br.close();
  }

  /**
   * Show progress
   * 
   * Expose reading progress
   */
  @Override
  public Progress[] getProgress() {
    // TODO Auto-generated method stub
    return new Progress[] { new ProgressImpl(CurrentLines, TotalLines, Progress.ENTITIES) };
  }

  /**
   * Has next record
   * 
   * Whether the input file has more data to read
   */
  @Override
  public boolean hasNext() throws IOException, CollectionException {
    return !empty;
  }

}
