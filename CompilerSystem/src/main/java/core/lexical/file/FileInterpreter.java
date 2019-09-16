/**
 *
 */
package core.lexical.file;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2019
 *
 * @author Felipe / 11 de set de 2019
 * @version 1.0
 * @since 1.0
 */
public class FileInterpreter {
  private BufferedReader br;

  public FileInterpreter(String filePath) throws FileNotFoundException {
    initFile(filePath);
  }

  private void initFile(String filePath) throws FileNotFoundException {
    this.br = new BufferedReader(new FileReader(filePath));
  }

  public List<String> parseProgram() throws IOException {
    List<String> result = new ArrayList<>();
    String line;
    Integer lineIndex = 0;
    if (br != null) {
      while ((line = br.readLine()) != null) {
        lineIndex++;
        line = line.replaceAll("^[ \\f\\t\\v]{1,}|[ \\f\\t\\v]{1,}$", ""); //  Clean whitespace/Tabs  //  FIXME Generalizar
        result.add(line);
      }
      br.close();
    }
    return result;
  }
}
