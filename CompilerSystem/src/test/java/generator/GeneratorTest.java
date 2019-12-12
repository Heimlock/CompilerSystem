/**
 *
 */
package generator;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Test;

import core.generator.CodeGenerator;
import core.lexical.file.FileInterpreter;
import core.lexical.parser.LexicalParser;
import core.syntactic.analyzer.SyntaticAnalyzer;
import data.impl.GlobalCounter_Impl;
import data.impl.SymbolTable_Impl;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 8 de dez de 2019
 * @version 1.0
 * @since 1.0
 */
public class GeneratorTest {
  //  private static final String PROGRAMS_BASE_PATH = "E:/TestesFinais/Felipe/";
  //  private static final String PROGRAMS_BASE_PATH = "./assets/testesGerador";
  private static final String PROGRAMS_BASE_PATH = "./assets/";
  private static SyntaticAnalyzer syntatic;
  private static CodeGenerator generator;

  private void setup(String filename) {
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
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @After
  public void cleanUp() {
    if (generator.getGeneratedProgram().size() > 2) {
      generator.getGeneratedProgram().forEach(line -> System.out.println(line));
      generator.saveToFile();
    }
    purgeData();
  }

  private void purgeData() {
    SymbolTable_Impl.getInstance().purgeList();
    LexicalParser.getInstance().purgeData();
    GlobalCounter_Impl.getInstance().clear();
  }

  @Test
  public void genericProgramTest() {
    this.setup("TesteFinal04");
    try {
      syntatic.analyzeProgram();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  public void teste01ProgramTest() {
    this.setup(String.format("teste%s", 1));
    try {
      syntatic.analyzeProgram();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  public void teste02ProgramTest() {
    this.setup(String.format("teste%s", 2));
    try {
      syntatic.analyzeProgram();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  public void teste03ProgramTest() {
    this.setup(String.format("teste%s", 3));
    try {
      syntatic.analyzeProgram();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  public void teste04ProgramTest() {
    this.setup(String.format("teste%s", 4));
    try {
      syntatic.analyzeProgram();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  public void teste05ProgramTest() {
    this.setup(String.format("teste%s", 5));
    try {
      syntatic.analyzeProgram();
      fail("This Program Should Have a Exception.");
    } catch (Exception e) {
      assertTrue("Should Throw a Incompatible Type Exception", e.getMessage().startsWith("Incompatible Type! Last Type Inteiro Operation sE;"));
    }
  }

  @Test
  public void teste06ProgramTest() {
    this.setup(String.format("teste%s", 6));
    try {
      syntatic.analyzeProgram();
      fail("This Program Should Have a Exception.");
    } catch (Exception e) {
      assertTrue("Should Throw a Incompatible Type Exception", e.getMessage().equals("Incompatible Expression Type! Context: If"));
    }
  }

  @Test
  public void teste07ProgramTest() {
    this.setup(String.format("teste%s", 7));
    try {
      syntatic.analyzeProgram();
      fail("This Program Should Have a Exception.");
    } catch (Exception e) {
      assertTrue("Should Throw a Incompatible Type Exception", e.getMessage().startsWith("Incompatible Type! Last Type Inteiro Operation sIdentificador;"));
    }
  }

  @Test
  public void teste08ProgramTest() {
    this.setup(String.format("teste%s", 8));
    try {
      syntatic.analyzeProgram();
      fail("This Program Should Have a Exception.");
    } catch (Exception e) {
      assertTrue("Should Throw a Incompatible Type Exception", e.getMessage().startsWith("Symbol Type does not match Postfix result Type."));
    }
  }

  @Test
  public void teste09ProgramTest() {
    this.setup(String.format("teste%s", 9));
    try {
      syntatic.analyzeProgram();
      fail("This Program Should Have a Exception.");
    } catch (Exception e) {
      assertTrue("Should Throw a Incompatible Type Exception", e.getMessage().endsWith("Context: Variavel Duplicada"));
    }
  }

  @Test
  public void teste10ProgramTest() {
    this.setup(String.format("teste%s", 10));
    try {
      syntatic.analyzeProgram();
      fail("This Program Should Have a Exception.");
    } catch (Exception e) {
      assertTrue("Should Throw a Incompatible Type Exception", e.getMessage().endsWith("Context: Variavel Nao declarada"));
    }
  }

  @Test
  public void teste11ProgramTest() {
    this.setup(String.format("teste%s", 11));
    try {
      syntatic.analyzeProgram();
      fail("This Program Should Have a Exception.");
    } catch (Exception e) {
      assertTrue("Should Throw a Incompatible Type Exception", e.getMessage().endsWith("Context: Procedimento Duplicado"));
    }
  }

  @Test
  public void teste12ProgramTest() {
    this.setup(String.format("teste%s", 12));
    try {
      syntatic.analyzeProgram();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  public void teste13ProgramTest() {
    this.setup(String.format("teste%s", 13));
    try {
      syntatic.analyzeProgram();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }
}
