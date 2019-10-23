/**
 * 
 */
package syntatic;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.EOFException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Test;

import core.lexical.file.FileInterpreter;
import core.lexical.parser.LexicalParser;
import core.syntactic.analyzer.SyntaticAnalyzer;
import core.syntactic.analyzer.SyntaticAnalyzerException;
import data.impl.SymbolTable_Impl;
import data.interfaces.Symbol;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 22 de out de 2019
 * @version 1.0
 * @since 1.0
 */
public class SyntaticAnalyserTest {
  private static final String PROGRAMS_BASE_PATH = "./assets/testesSintatico/";
  private static SyntaticAnalyzer syntatic;

  private void setup(String filePath) {
    Logger.getLogger("LexicalParser").setLevel(Level.OFF);
    try {
      FileInterpreter interpreter = new FileInterpreter(filePath);
      List<String> parsedProgram = interpreter.parseProgram();
      LexicalParser parser = LexicalParser.getInstance();
      parser.setProgram(parsedProgram);
      syntatic = SyntaticAnalyzer.getInstance(parser);
    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
    }
  }

  @After
  public void dumpSymbolTable() {
    System.out.println("========================================");
    System.out.println("||-----Topo da Tabela de Simbolos-----||");
    System.out.println("========================================");
    for (int i = 0; i < SymbolTable_Impl.getInstance().getAll().size(); i++) {
      Symbol symbol =SymbolTable_Impl.getInstance().getAll().get(i); 
      System.out.println(String.format("[%d] - %s", i, symbol.toString()));
    }
    purgeData();
  }

  private void purgeData() {
    SymbolTable_Impl.getInstance().purgeList();
    LexicalParser.getInstance().purgeData();
  }

  /*
   * Ok
   */
  @Test
  public void program01Test() {
    this.setup(String.format("%s/teste%s.lpd", PROGRAMS_BASE_PATH, 1));
    try {
      syntatic.analyzeProgram();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  /*
   * Teste de ponto e virgula excedente
   */
  public void program02Test() {
    this.setup(String.format("%s/teste%s.lpd", PROGRAMS_BASE_PATH, 2));
    try {
      syntatic.analyzeProgram();
    } catch (SyntaticAnalyzerException e) {
      assertTrue(e.getMessage().startsWith("[handleCommands]") && e.getMessage().endsWith(", Context: Inicio"));
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  /*
   * Teste de virgula a mais
   */
  public void program03Test() {
    this.setup(String.format("%s/teste%s.lpd", PROGRAMS_BASE_PATH, 3));
    try {
      syntatic.analyzeProgram();
    } catch (SyntaticAnalyzerException e) {
      assertTrue(e.getMessage().startsWith("[handleVariables]") && e.getMessage().endsWith(", Context: Dois Pontos"));
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  /*
   * Ok
   */
  public void program04Test() {
    this.setup(String.format("%s/teste%s.lpd", PROGRAMS_BASE_PATH, 4));
    try {
      syntatic.analyzeProgram();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  /*
   * Ok
   */
  public void program05Test() {
    this.setup(String.format("%s/teste%s.lpd", PROGRAMS_BASE_PATH, 5));
    try {
      syntatic.analyzeProgram();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  /*
   * Teste de operador relacional
   */
  public void program06Test() {
    this.setup(String.format("%s/teste%s.lpd", PROGRAMS_BASE_PATH, 6));
    try {
      syntatic.analyzeProgram();
    } catch (SyntaticAnalyzerException e) {
      assertTrue(e.getMessage().startsWith("[handleFactor]") && e.getMessage().endsWith(", Context: Verdadeiro ou Falso"));
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  /*
   * Teste de atribuicao
   */
  public void program07Test() {
    this.setup(String.format("%s/teste%s.lpd", PROGRAMS_BASE_PATH, 7));
    try {
      syntatic.analyzeProgram();
    } catch (SyntaticAnalyzerException e) {
      assertTrue(e.getMessage().startsWith("[handleCommands]") && e.getMessage().endsWith(", Context: Ponto e Virgula"));
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  /*
   * Teste de operador logico
   */
  public void program08Test() {
    this.setup(String.format("%s/teste%s.lpd", PROGRAMS_BASE_PATH, 8));
    try {
      syntatic.analyzeProgram();
    } catch (SyntaticAnalyzerException e) {
      assertTrue(e.getMessage().startsWith("[handleFactor]") && e.getMessage().endsWith(", Context: Verdadeiro ou Falso"));
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  /*
   * Ok
   */
  public void program09Test() {
    this.setup(String.format("%s/teste%s.lpd", PROGRAMS_BASE_PATH, 9));
    try {
      syntatic.analyzeProgram();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  /*
   * Teste de numero com ponto
   */
  public void program10Test() {
    this.setup(String.format("%s/teste%s.lpd", PROGRAMS_BASE_PATH, 10));
    try {
      syntatic.analyzeProgram();
    } catch (SyntaticAnalyzerException e) {
      assertTrue(e.getMessage().startsWith("[handleFactor]") && e.getMessage().endsWith(", Context: Fecha Parenteses"));
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  /*
   * Teste de expressao em lugar errado
   */
  public void program11Test() {
    this.setup(String.format("%s/teste%s.lpd", PROGRAMS_BASE_PATH, 11));
    try {
      syntatic.analyzeProgram();
    } catch (SyntaticAnalyzerException e) {
      assertTrue(e.getMessage().startsWith("[handleCommands]") && e.getMessage().endsWith(", Context: Ponto e Virgula"));
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  /*
   * Teste de fim a mais
   */
  public void program12Test() {
    this.setup(String.format("%s/teste%s.lpd", PROGRAMS_BASE_PATH, 12));
    try {
      syntatic.analyzeProgram();
    } catch (SyntaticAnalyzerException e) {
      assertTrue(e.getMessage().startsWith("[handleSubRotines]") && e.getMessage().endsWith(", Context: Ponto Virgula"));
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  /*
   * Teste que falta inicio
   */
  public void program13Test() {
    this.setup(String.format("%s/teste%s.lpd", PROGRAMS_BASE_PATH, 13));
    try {
      syntatic.analyzeProgram();
    } catch (SyntaticAnalyzerException e) {
      assertTrue(e.getMessage().startsWith("[handleCommands]") && e.getMessage().endsWith(", Context: Inicio"));
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  /*
   * Ok
   */
  public void program14Test() {
    this.setup(String.format("%s/teste%s.lpd", PROGRAMS_BASE_PATH, 14));
    try {
      syntatic.analyzeProgram();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  /*
   * Teste falta ; depois do procedimento
   */
  public void program15Test() {
    this.setup(String.format("%s/teste%s.lpd", PROGRAMS_BASE_PATH, 15));
    try {
      syntatic.analyzeProgram();
    } catch (SyntaticAnalyzerException e) {
      assertTrue(e.getMessage().startsWith("[handleSubRotines]") && e.getMessage().endsWith(", Context: Ponto Virgula"));
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  /*
   * Teste falta ponto final
   */
  public void program16Test() {
    this.setup(String.format("%s/teste%s.lpd", PROGRAMS_BASE_PATH, 16));
    try {
      syntatic.analyzeProgram();
    } catch (EOFException e) {
      assertTrue(e.getMessage().contains("End of Source Program -- Read Char"));
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  /*
   * Ok
   */
  public void program17Test() {
    this.setup(String.format("%s/teste%s.lpd", PROGRAMS_BASE_PATH, 17));
    try {
      syntatic.analyzeProgram();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  /*
   * Ok
   */
  public void program18Test() {
    this.setup(String.format("%s/teste%s.lpd", PROGRAMS_BASE_PATH, 18));
    try {
      syntatic.analyzeProgram();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }
}
