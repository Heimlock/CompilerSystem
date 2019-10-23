/**
 * 
 */
package core.syntactic;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import core.lexical.file.FileInterpreter;
import core.lexical.parser.LexicalParser;
import core.syntactic.analyzer.SyntaticAnalyzer;
import data.impl.SymbolTable_Impl;
import data.interfaces.Symbol;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 22 de out de 2019
 * @version 1.0
 * @since 1.0
 */
public class SyntacticEngine {
  private static final String FILE_PATH = "./assets/testesSintatico/teste1.lpd";

  public static void main(String[] args) {
    Logger.getLogger("LexicalParser").setLevel(Level.OFF);
    try {
      FileInterpreter interpreter = new FileInterpreter(FILE_PATH);
      List<String> parsedProgram = interpreter.parseProgram();
      LexicalParser parser = LexicalParser.getInstance();
      parser.setProgram(parsedProgram);

      SyntaticAnalyzer syntatic = SyntaticAnalyzer.getInstance(parser);
      syntatic.analyzeProgram();
      dumpSymbolTable();
    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
    }
  }

  private static void dumpSymbolTable() {
    System.out.println("========================================");
    System.out.println("||-----Topo da Tabela de Simbolos-----||");
    System.out.println("========================================");
    for (int i = 0; i < SymbolTable_Impl.getInstance().getAll().size(); i++) {
      Symbol symbol = SymbolTable_Impl.getInstance().getAll().get(i);
      System.out.println(String.format("[%d] - %s", i, symbol.toString()));
    }
    System.out.println("========================================");
  }
}
