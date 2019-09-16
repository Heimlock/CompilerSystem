/**
 * 
 */
package core.lexical;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import core.lexical.file.FileInterpreter;
import core.lexical.parser.LexicalParser;
import data.impl.TokenStack_Impl;
import data.interfaces.TokenStack;

/**
 * Copyright (c) 2019
 *
 * @author 16116469 / 11 de set de 2019
 * @version 1.0
 * @since 1.0
 */
public class LexicalEngine {
  private static final String FILE_PATH = "./assets/testes/test1.lpd";
  public static void main(String[] args) {
    try {
      FileInterpreter interpreter = new FileInterpreter(FILE_PATH);
      List<String> parsedProgram = interpreter.parseProgram();
      LexicalParser parser = LexicalParser.getInstance();
      TokenStack tokenStack = TokenStack_Impl.getInstance();

      parser.setProgram(parsedProgram);
      tokenStack.addTokens(parser.getTokens());
      tokenStack.getTokens().forEach(token -> {
        System.out.println(String.format("Lexeme: %s -- Symbol: %s", token.getLexeme(), token.getSymbol().name()));
      });
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
