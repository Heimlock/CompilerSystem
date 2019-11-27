/**
 *
 */
package core;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import core.generator.CodeGenerator;
import core.lexical.file.FileInterpreter;
import core.lexical.parser.LexicalParser;
import core.syntactic.analyzer.SyntaticAnalyzer;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 27 de nov de 2019
 * @version 1.0
 * @since 1.0
 */
public class CodeGeneratorMain {
  private static final String PROGRAMS_BASE_PATH = "./assets/";
  private static final String filename = "TesteFinal01";
  private static SyntaticAnalyzer syntatic;
  private static CodeGenerator generator;

  public static void main(String[] args) {
    Logger.getLogger("LexicalParser").setLevel(Level.OFF);
    try {
      generator = CodeGenerator.getInstance();
      generator.clear();
      FileInterpreter interpreter = new FileInterpreter(String.format("%s/%s.lpd", PROGRAMS_BASE_PATH, filename));
      generator.setFilename(filename);
      List<String> parsedProgram = interpreter.parseProgram();
      LexicalParser parser = LexicalParser.getInstance();
      parser.setProgram(parsedProgram);
      syntatic = SyntaticAnalyzer.getInstance(parser);
      syntatic.analyzeProgram();
      generator.saveToFile();
    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
    }
  }
}
