/**
 * 
 */
package data.impl;

import data.TokenSymbolTable;
import data.interfaces.Token;

/**
 * Copyright (c) 2019
 *
 * @author 16116469 / 11 de set de 2019
 * @version 1.0
 * @since 1.0
 */
public class Token_Impl implements Token {

  private Integer lineIndex;
  private Integer lineOffset;
  private String lexeme;
  private TokenSymbolTable symbol;

  public Token_Impl(int lineIndex, int lineOffset) {
    this.lineIndex = lineIndex;
    this.lineOffset = lineOffset;
  }

  //  Used in Tests
  @Deprecated
  public Token_Impl(String lexeme) {
    this.lineIndex = -1;
    this.lineOffset = -2;
    this.lexeme = lexeme;
    this.symbol = TokenSymbolTable.getSymbolByLexeme(lexeme);
  }

  /* (non-Javadoc)
   * @see data.interfaces.Token#getSymbol()
   */
  @Override
  public String getLexeme() {
    return this.lexeme;
  }

  /* (non-Javadoc)
   * @see data.interfaces.Token#setSymbol(java.lang.String)
   */
  @Override
  public void setLexeme(String lexeme) {
    this.lexeme = lexeme;
  }

  /* (non-Javadoc)
   * @see data.interfaces.Token#getLexeme()
   */
  @Override
  public TokenSymbolTable getSymbol() {
    return this.symbol;
  }

  /* (non-Javadoc)
   * @see data.interfaces.Token#setLexeme(data.LexemeTable)
   */
  @Override
  public void setSymbol(TokenSymbolTable symbol) {
    this.symbol = symbol;
  }

  @Override
  public Integer getLineIndex() {
    return this.lineIndex;
  }

  @Override
  public Integer getLineOffset() {
    return this.lineOffset;
  }

  @Override
  public String toString() {
    return String.format("Token_Impl [lexeme='%s', symbol='%s', lineIndex=%d, lineOffset=%d]", lexeme, symbol.toString(), lineIndex, lineOffset);
  }

}
