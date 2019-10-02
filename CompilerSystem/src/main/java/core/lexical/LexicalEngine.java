/**
 * 
 */
package core.lexical;

import java.util.List;

import core.lexical.file.FileInterpreter;
import core.lexical.parser.LexicalParser;
import data.impl.TokenStack_Impl;
import data.interfaces.Token;
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
      Token tokenRead;

      parser.setProgram(parsedProgram);
      do {
        tokenRead = parser.getNextToken();
        if (tokenRead != null) {
          tokenStack.addToken(tokenRead);
        } else {
          System.out.println("Null Token");
        }
      } while (parser.getRemainingLines() != 0);
      tokenStack.getTokens().forEach(token -> {
        System.out.println(token.toString());
      });
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
