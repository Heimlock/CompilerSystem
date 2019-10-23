/**
 * 
 */
package core.syntactic;

import java.util.List;

import core.lexical.file.FileInterpreter;
import core.lexical.parser.LexicalParser;
import core.syntactic.analyzer.SyntaticAnalyzer;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 22 de out de 2019
 * @version 1.0
 * @since 1.0
 */
public class SyntacticEngine {
  private static final String FILE_PATH = "./assets/testesSintatico/teste01.lpd";

  public static void main(String[] args) {
    try {
      FileInterpreter interpreter = new FileInterpreter(FILE_PATH);
      List<String> parsedProgram = interpreter.parseProgram();
      LexicalParser parser = LexicalParser.getInstance();
      parser.setProgram(parsedProgram);

      SyntaticAnalyzer syntatic = SyntaticAnalyzer.getInstance(parser);
      syntatic.analyzeProgram();
    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
    }
  }
}
