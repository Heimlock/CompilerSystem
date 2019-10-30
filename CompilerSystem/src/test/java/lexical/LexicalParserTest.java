/**
 * 
 */
package lexical;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Test;

import core.lexical.file.FileInterpreter;
import core.lexical.parser.LexicalParser;
import core.lexical.parser.LexicalParserException;
import data.impl.SymbolTable_Impl;
import data.impl.TokenStack_Impl;
import data.interfaces.Token;
import data.interfaces.TokenStack;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 30 de out de 2019
 * @version 1.0
 * @since 1.0
 */
public class LexicalParserTest {
  private static final String PROGRAMS_BASE_PATH = "./assets/testesLexico/";
  private static LexicalParser parser;
  private static TokenStack tokenStack;
  private static Token tokenRead;

  private void setup(String filePath) {
    Logger.getLogger("LexicalParser").setLevel(Level.OFF);
    try {
      FileInterpreter interpreter = new FileInterpreter(filePath);
      List<String> parsedProgram = interpreter.parseProgram();
      parser = LexicalParser.getInstance();
      tokenStack = TokenStack_Impl.getInstance();

      parser.setProgram(parsedProgram);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @After
  public void dumpSymbolTable() {
    System.out.println("========================================");
    System.out.println("||-------Topo da Pilha de Tokens------||");
    System.out.println("========================================");
    tokenStack.getTokens().forEach(token -> {
      System.out.println(token.toString());
    });
    purgeData();
  }

  private void purgeData() {
    SymbolTable_Impl.getInstance().purgeList();
    LexicalParser.getInstance().purgeData();
  }

  @Test
  public void program01Test() {
    this.setup(String.format("%s/test%s.lpd", PROGRAMS_BASE_PATH, 1));
    try {
      do {
        tokenRead = parser.getNextToken();
        tokenStack.addToken(tokenRead);
      } while (parser.getRemainingLines() != 0);
      tokenStack.getTokens().forEach(token -> {
        System.out.println(token.toString());
      });
    } catch (EOFException e) {
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    } catch (LexicalParserException e) {
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  public void program02Test() {
    this.setup(String.format("%s/test%s.lpd", PROGRAMS_BASE_PATH, 2));
    try {
      do {
        tokenRead = parser.getNextToken();
        tokenStack.addToken(tokenRead);
      } while (parser.getRemainingLines() != 0);
      tokenStack.getTokens().forEach(token -> {
        System.out.println(token.toString());
      });
    } catch (EOFException e) {
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    } catch (LexicalParserException e) {
      assertTrue(e.getMessage().equals("Unknown Token Exception -- Char Read: '%', Line: 5, Offset: 7"));
    }
  }

  @Test
  public void program03Test() {
    this.setup(String.format("%s/test%s.lpd", PROGRAMS_BASE_PATH, 3));
    try {
      do {
        tokenRead = parser.getNextToken();
        tokenStack.addToken(tokenRead);
      } while (parser.getRemainingLines() != 0);
      tokenStack.getTokens().forEach(token -> {
        System.out.println(token.toString());
      });
    } catch (EOFException e) {
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    } catch (LexicalParserException e) {
      assertTrue(e.getMessage().equals("Unknown Token Exception -- Char Read: '%', Line: 1, Offset: 1"));
    }
  }

  @Test
  public void program04Test() {
    this.setup(String.format("%s/test%s.lpd", PROGRAMS_BASE_PATH, 4));
    try {
      do {
        tokenRead = parser.getNextToken();
        tokenStack.addToken(tokenRead);
      } while (parser.getRemainingLines() != 0);
      tokenStack.getTokens().forEach(token -> {
        System.out.println(token.toString());
      });
    } catch (EOFException e) {
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    } catch (LexicalParserException e) {
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  public void program05Test() {
    this.setup(String.format("%s/test%s.lpd", PROGRAMS_BASE_PATH, 5));
    try {
      do {
        tokenRead = parser.getNextToken();
        tokenStack.addToken(tokenRead);
      } while (parser.getRemainingLines() != 0);
      tokenStack.getTokens().forEach(token -> {
        System.out.println(token.toString());
      });
    } catch (EOFException e) {
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    } catch (LexicalParserException e) {
      assertTrue(e.getMessage().equals("Unknown Token Exception -- Char Read: '[', Line: 2, Offset: 3"));
    }
  }

  @Test
  public void program06Test() {
    this.setup(String.format("%s/test%s.lpd", PROGRAMS_BASE_PATH, 6));
    try {
      do {
        tokenRead = parser.getNextToken();
        tokenStack.addToken(tokenRead);
      } while (parser.getRemainingLines() != 0);
      tokenStack.getTokens().forEach(token -> {
        System.out.println(token.toString());
      });
    } catch (EOFException e) {
      assertTrue(e.getMessage().equals("End of Source Program -- Read Char"));
    } catch (LexicalParserException e) {
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  public void program07Test() {
    this.setup(String.format("%s/test%s.lpd", PROGRAMS_BASE_PATH, 7));
    try {
      do {
        tokenRead = parser.getNextToken();
        tokenStack.addToken(tokenRead);
      } while (parser.getRemainingLines() != 0);
      tokenStack.getTokens().forEach(token -> {
        System.out.println(token.toString());
      });
    } catch (EOFException e) {
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    } catch (LexicalParserException e) {
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  public void program08Test() {
    this.setup(String.format("%s/test%s.lpd", PROGRAMS_BASE_PATH, 8));
    try {
      do {
        tokenRead = parser.getNextToken();
        tokenStack.addToken(tokenRead);
      } while (parser.getRemainingLines() != 0);
      tokenStack.getTokens().forEach(token -> {
        System.out.println(token.toString());
      });
    } catch (EOFException e) {
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    } catch (LexicalParserException e) {
      assertTrue(e.getMessage().equals("Unknown Token Exception -- Char Read: '/', Line: 4, Offset: 13"));
    }
  }

  @Test
  public void program09Test() {
    this.setup(String.format("%s/test%s.lpd", PROGRAMS_BASE_PATH, 9));
    try {
      do {
        tokenRead = parser.getNextToken();
        tokenStack.addToken(tokenRead);
      } while (parser.getRemainingLines() != 0);
      tokenStack.getTokens().forEach(token -> {
        System.out.println(token.toString());
      });
    } catch (EOFException e) {
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    } catch (LexicalParserException e) {
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }

  @Test
  public void program10Test() {
    this.setup(String.format("%s/test%s.lpd", PROGRAMS_BASE_PATH, 10));
    try {
      do {
        tokenRead = parser.getNextToken();
        tokenStack.addToken(tokenRead);
      } while (parser.getRemainingLines() != 0);
      tokenStack.getTokens().forEach(token -> {
        System.out.println(token.toString());
      });
    } catch (EOFException e) {
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    } catch (LexicalParserException e) {
      e.printStackTrace();
      fail("This Program Shouldn't Have Any Exception.");
    }
  }
}
