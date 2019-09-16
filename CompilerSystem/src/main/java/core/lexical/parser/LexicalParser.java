/**
 * 
 */
package core.lexical.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import data.SymbolTable;
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
  private Logger Log = Logger.getGlobal();
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

  private char readChar() {
    char result;
    while (lineArray == null || lineOffset == lineArray.length || lineArray.length == 0) {
      lineOffset = 0;
      lineIndex++;
      lineArray = parsedProgram.remove(0).toLowerCase().toCharArray();
      Log.log(Level.INFO, String.format("At Line #%d", lineIndex));
    }
    do {
      result = lineArray[lineOffset++];
    } while (result == ' ');
    return result;
  }

  private char peekNextChar() {
    int nextCharOffset = lineOffset;
    if (lineArray == null || lineOffset == lineArray.length) {
      return parsedProgram.get(0).toLowerCase().toCharArray()[0];
    }
    return lineArray[nextCharOffset];
  }

  public List<Token> getTokens() {
    final List<Token> result = new ArrayList<>();
    Token token = null;
    char charRead;

    try {
      do {
        charRead = readChar();
        //  Deal with Comments
        if (charRead == '{') {
          do {
            charRead = readChar();
          } while (charRead != '}');
          charRead = readChar();
        }
        token = getToken(charRead);
        result.add(token);
      } while (parsedProgram.size() > 0);

    } catch (LexicalParserException e) {
      e.printStackTrace();
    }
    return result;
  }

  private Token getToken(char charRead) throws LexicalParserException {
    Token result = null;
    if (Character.isDigit(charRead)) {
      result = this.handleDigit(charRead);
    } else if (Character.isLetter(charRead)) {
      result = this.handleIdentificationOrCommand(charRead);
    } else if (charRead == ':') {
      result = this.handleAttribution(charRead);
    } else if (StringUtils.isAritmeticOperator(charRead)) {
      result = handleArithmeticOperator(charRead);
    } else if (StringUtils.isRelationalOperator(charRead)) {
      result = handleRelationalcOperator(charRead);
    } else if (StringUtils.isPunctuation(charRead)) {
      result = handlePunctuation(charRead);
    } else {
      throw new LexicalParserException(String.format("Unknown Token Exception -- Char Read: '%c', Line: %d, Offset: %d", charRead, lineIndex, lineOffset));
    }
    return result;
  }

  private Token handleDigit(char charInput) {
    Token result = null;
    char charRead;
    String lexeme = String.valueOf(charInput);

    while (Character.isDigit(peekNextChar())) {
      charRead = readChar();
      lexeme = String.format("%s%c", lexeme, charRead);
    }

    result = new Token_Impl(this.lineIndex, this.lineOffset);
    result.setLexeme(lexeme);
    result.setSymbol(SymbolTable.sNumero);
    return result;
  }

  private Token handleIdentificationOrCommand(char charInput) {
    Token result = null;
    char charRead;
    SymbolTable symbol = null;
    String lexeme = String.valueOf(charInput);

    while (Character.isLetter(peekNextChar()) || peekNextChar() == '_') {
      charRead = readChar();
      lexeme = String.format("%s%c", lexeme, charRead);
      if (SymbolTable.getSymbolByLexeme(lexeme) != null) {
        result = new Token_Impl(this.lineIndex, this.lineOffset);
        result.setLexeme(lexeme);
        symbol = SymbolTable.getSymbolByLexeme(lexeme);
        result.setSymbol(symbol);
        break;
      }
    }
    if (result == null) {
      result = new Token_Impl(this.lineIndex, this.lineOffset);
      result.setLexeme(lexeme);
      symbol = SymbolTable.sIdentificador;
      result.setSymbol(symbol);
    }
    return result;
  }

  private Token handleAttribution(char charInput) {
    Token result = null;
    char charRead;
    String lexeme = String.valueOf(charInput);

    result = new Token_Impl(this.lineIndex, this.lineOffset);
    if (peekNextChar() == '=') {
      charRead = readChar();
      lexeme = String.format("%s%c", lexeme, charRead);
      result.setSymbol(SymbolTable.sAtribuicao);
    } else {
      result.setSymbol(SymbolTable.sDoisPontos);
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
      result.setSymbol(SymbolTable.sMais);
    } else if (charInput == '-') {
      result.setSymbol(SymbolTable.sMenos);
    } else if (charInput == '*') {
      result.setSymbol(SymbolTable.sMult);
    }
    return result;
  }

  private Token handleRelationalcOperator(char charInput) {
    Token result = null;
    char charRead;
    String lexeme = String.valueOf(charInput);

    if (charInput == '=') {
      result = new Token_Impl(this.lineIndex, this.lineOffset);
      result.setLexeme(lexeme);
      result.setSymbol(SymbolTable.sIg);
    } else if (charInput == '!') {
      charRead = readChar();
      if (charRead == '!') {
        lexeme = String.format("%s%c", lexeme, charRead);
        result = new Token_Impl(this.lineIndex, this.lineOffset);
        result.setLexeme(lexeme);
        result.setSymbol(SymbolTable.sDif);
      } else if (charInput == '<') {
        if (peekNextChar() == '=') {
          lexeme = String.format("%s%c", lexeme, charRead);
          result = new Token_Impl(this.lineIndex, this.lineOffset);
          result.setLexeme(lexeme);
          result.setSymbol(SymbolTable.sMenorIg);
        } else {
          result = new Token_Impl(this.lineIndex, this.lineOffset);
          result.setLexeme(lexeme);
          result.setSymbol(SymbolTable.sMenor);
        }
      } else if (charInput == '>') {
        if (peekNextChar() == '=') {
          lexeme = String.format("%s%c", lexeme, charRead);
          result = new Token_Impl(this.lineIndex, this.lineOffset);
          result.setLexeme(lexeme);
          result.setSymbol(SymbolTable.sMaiorIg);
        } else {
          result = new Token_Impl(this.lineIndex, this.lineOffset);
          result.setLexeme(lexeme);
          result.setSymbol(SymbolTable.sMaior);
        }
      }
    }
    return result;
  }

  private Token handlePunctuation(char charInput) {
    Token result = null;
    String lexeme = String.valueOf(charInput);

    result = new Token_Impl(this.lineIndex, this.lineOffset);
    result.setLexeme(lexeme);
    if (charInput == ';') {
      result.setSymbol(SymbolTable.sPonto_Virgula);
    } else if (charInput == ',') {
      result.setSymbol(SymbolTable.sVirgula);
    } else if (charInput == '(') {
      result.setSymbol(SymbolTable.sAbre_Parenteses);
    } else if (charInput == ')') {
      result.setSymbol(SymbolTable.sFecha_Parenteses);
    } else if (charInput == '.') {
      result.setSymbol(SymbolTable.sPonto);
    }
    return result;
  }
}
