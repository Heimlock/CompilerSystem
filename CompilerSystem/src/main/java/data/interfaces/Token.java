/**
 * 
 */
package data.interfaces;

import data.TokenSymbolTable;

/**
 * Copyright (c) 2019
 *
 * @author 16116469 / 11 de set de 2019
 * @version 1.0
 * @since 1.0
 */
public interface Token {
  public Integer getLineIndex();

  public Integer getLineOffset();

  public String getLexeme();

  public void setLexeme(String symbol);

  public TokenSymbolTable getSymbol();

  public void setSymbol(TokenSymbolTable lexeme);
}
