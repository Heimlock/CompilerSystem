/**
 * 
 */
package core.lexical.parser;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import data.TokenSymbolTable;
import data.impl.Token_Impl;
import data.interfaces.Token;
import utils.StringUtils;

/**
 * Copyright (c) 2019
 *
 * @author 16116469 / 11 de set de 2019
 * @version 1.0
 * @since 1.0
 */
public class LexicalParser {
  private static LexicalParser instance = null;
  private Logger Log = Logger.getLogger("LexicalParser");
  private List<String> parsedProgram;
  private Integer lineIndex;
  private Integer lineOffset;
  private char[] lineArray = null;

  public static LexicalParser getInstance() {
    if (instance == null) {
      instance = new LexicalParser();
    }
    return instance;
  }

  private LexicalParser() {
    this.parsedProgram = new ArrayList<>();
    this.lineIndex = 0;
    this.lineOffset = 0;
  }

  public void setProgram(List<String> parsedProgram) {
    this.parsedProgram = parsedProgram;
  }

  public Integer getRemainingLines() {
    return this.parsedProgram.size();
  }

  public List<Token> getTokens() throws EOFException {
    final List<Token> result = new ArrayList<>();
    Token token = null;
    char charRead;

    try {
      do {
        charRead = readChar(FALSE);
        //  Deal with Comments
        if (charRead == '{') {
          do {
            charRead = readChar(TRUE);
          } while (charRead != '}');
          charRead = readChar(FALSE);
        }
        token = getToken(charRead);
        result.add(token);
      } while (parsedProgram.size() > 0);
    } catch (LexicalParserException e) {
      e.printStackTrace();
    }
    return result;
  }

  public Token getNextToken() throws EOFException, LexicalParserException {
    Token token = null;
    char charRead;
    if (this.parsedProgram.size() == 0 && lineOffset == lineArray.length) {
      throw new EOFException("End of Source Program");
    } else {
      charRead = readChar(FALSE);
      do {
        //  Deal with Comments
        if (charRead == '{') {
          do {
            charRead = readChar(TRUE);
          } while (charRead != '}');
          charRead = readChar(FALSE);
        }
      } while (charRead == '{');
      token = getToken(charRead);
    }
    return token;
  }

  private char readChar(Boolean allowWhiteSpace) throws EOFException {
    char result;
    do {
      while (lineArray == null || lineOffset == lineArray.length || lineArray.length == 0) {
        lineOffset = 0;
        lineIndex++;
        if (parsedProgram.size() == 0) {
          throw new EOFException("End of Source Program -- Read Char");
        } else {
          lineArray = parsedProgram.remove(0).toLowerCase().toCharArray();
          Log.log(Level.INFO, String.format("At Line #%d", lineIndex));
        }
      }
      result = lineArray[lineOffset++];
    } while (result == ' ' && !allowWhiteSpace);
    return result;
  }

  private char peekNextChar() {
    int nextCharOffset = lineOffset;
    if (lineArray == null || lineOffset == lineArray.length) {
      return parsedProgram.get(0).toLowerCase().toCharArray()[0];
    }
    return lineArray[nextCharOffset];
  }

  private boolean isEndOfLine() {
    boolean result = false;
    if (lineOffset == lineArray.length) {
      result = true;
    }
    return result;
  }

  private Token getToken(char charRead) throws LexicalParserException, EOFException {
    Token result = null;
    if (Character.isDigit(charRead)) {
      result = this.handleDigit(charRead);
    } else if (StringUtils.isRelationalOperator(charRead)) {
      result = handleRelationalOperator(charRead);
    } else if (Character.isLetter(charRead)) {
      result = this.handleIdentificationOrCommand(charRead);
    } else if (charRead == ':') {
      result = this.handleAttribution(charRead);
    } else if (StringUtils.isAritmeticOperator(charRead)) {
      result = handleArithmeticOperator(charRead);
    } else if (StringUtils.isPunctuation(charRead)) {
      result = handlePunctuation(charRead);
    } else {
      throw new LexicalParserException(String.format("Unknown Token Exception -- Char Read: '%c', Line: %d, Offset: %d", charRead, lineIndex, lineOffset));
    }
    return result;
  }

  private Token handleDigit(char charInput) throws EOFException {
    Token result = null;
    char charRead;
    String lexeme = String.valueOf(charInput);

    while (Character.isDigit(peekNextChar())) {
      charRead = readChar(FALSE);
      lexeme = String.format("%s%c", lexeme, charRead);
    }

    result = new Token_Impl(this.lineIndex, this.lineOffset);
    result.setLexeme(lexeme);
    result.setSymbol(TokenSymbolTable.sNumero);
    return result;
  }

  private Token handleIdentificationOrCommand(char charInput) throws EOFException {
    Token result = null;
    char charRead;
    TokenSymbolTable symbol = TokenSymbolTable.sIdentificador;
    String lexeme = String.valueOf(charInput);

    while (!isEndOfLine() && (Character.isLetter(peekNextChar()) || Character.isDigit(peekNextChar()) || peekNextChar() == '_')) {
      charRead = readChar(FALSE);
      lexeme = String.format("%s%c", lexeme, charRead);
    }
    if (TokenSymbolTable.getSymbolByLexeme(lexeme) != null) {
      symbol = TokenSymbolTable.getSymbolByLexeme(lexeme);
    }
    if (result == null) {
      result = new Token_Impl(this.lineIndex, this.lineOffset);
      result.setLexeme(lexeme);
      result.setSymbol(symbol);
    }
    return result;
  }

  private Token handleAttribution(char charInput) throws EOFException {
    Token result = null;
    char charRead;
    String lexeme = String.valueOf(charInput);

    result = new Token_Impl(this.lineIndex, this.lineOffset);
    if (peekNextChar() == '=') {
      charRead = readChar(FALSE);
      lexeme = String.format("%s%c", lexeme, charRead);
      result.setSymbol(TokenSymbolTable.sAtribuicao);
    } else {
      result.setSymbol(TokenSymbolTable.sDoisPontos);
    }
    result.setLexeme(lexeme);
    return result;
  }

  private Token handleArithmeticOperator(char charInput) {
    Token result = null;
    String lexeme = String.valueOf(charInput);

    result = new Token_Impl(this.lineIndex, this.lineOffset);
    result.setLexeme(lexeme);
    if (charInput == '+') {
      result.setSymbol(TokenSymbolTable.sMais);
    } else if (charInput == '-') {
      result.setSymbol(TokenSymbolTable.sMenos);
    } else if (charInput == '*') {
      result.setSymbol(TokenSymbolTable.sMult);
    }
    return result;
  }

  private Token handleRelationalOperator(char charInput) throws LexicalParserException, EOFException {
    Token result = null;
    char charRead;
    String lexeme = String.valueOf(charInput);

    if (charInput == '!' && peekNextChar() == '=') {
      charRead = readChar(TRUE);
      lexeme = String.format("%s%c", lexeme, charRead);
      result = new Token_Impl(this.lineIndex, this.lineOffset);
      result.setLexeme(lexeme);
      result.setSymbol(TokenSymbolTable.sDif);
    } else if (charInput == '<') {
      if (peekNextChar() == '=') {
        charRead = readChar(TRUE);
        lexeme = String.format("%s%c", lexeme, charRead);
        result = new Token_Impl(this.lineIndex, this.lineOffset);
        result.setLexeme(lexeme);
        result.setSymbol(TokenSymbolTable.sMenorIg);
      } else {
        result = new Token_Impl(this.lineIndex, this.lineOffset);
        result.setLexeme(lexeme);
        result.setSymbol(TokenSymbolTable.sMenor);
      }
    } else if (charInput == '>') {
      if (peekNextChar() == '=') {
        charRead = readChar(TRUE);
        lexeme = String.format("%s%c", lexeme, charRead);
        result = new Token_Impl(this.lineIndex, this.lineOffset);
        result.setLexeme(lexeme);
        result.setSymbol(TokenSymbolTable.sMaiorIg);
      } else {
        result = new Token_Impl(this.lineIndex, this.lineOffset);
        result.setLexeme(lexeme);
        result.setSymbol(TokenSymbolTable.sMaior);
      }
    } else if (charInput == '=') {
      result = new Token_Impl(this.lineIndex, this.lineOffset);
      result.setLexeme(lexeme);
      result.setSymbol(TokenSymbolTable.sIg);
    } else {
      charRead = readChar(TRUE);
      throw new LexicalParserException(String.format("Unknown Token Exception -- Char Read: '%c', Line: %d, Offset: %d", charRead, lineIndex, lineOffset));
    }
    return result;
  }

  private Token handlePunctuation(char charInput) {
    Token result = null;
    String lexeme = String.valueOf(charInput);

    result = new Token_Impl(this.lineIndex, this.lineOffset);
    result.setLexeme(lexeme);
    if (charInput == ';') {
      result.setSymbol(TokenSymbolTable.sPonto_Virgula);
    } else if (charInput == ',') {
      result.setSymbol(TokenSymbolTable.sVirgula);
    } else if (charInput == '(') {
      result.setSymbol(TokenSymbolTable.sAbre_Parenteses);
    } else if (charInput == ')') {
      result.setSymbol(TokenSymbolTable.sFecha_Parenteses);
    } else if (charInput == '.') {
      result.setSymbol(TokenSymbolTable.sPonto);
    }
    return result;
  }

  //  Unit Test
  public void purgeData() {
    this.parsedProgram.clear();
    this.lineIndex = 0;
    this.lineOffset = 0;
    this.lineArray = null;
  }
}
