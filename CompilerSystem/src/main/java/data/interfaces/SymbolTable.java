/**
 * 
 */
package data.interfaces;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 8 de out de 2019
 * @version 1.0
 * @since 1.0
 */
public interface SymbolTable {
  public void addSymbol(String id, Token token);

  public Boolean hasSymbol(Token token);

  public Symbol getSymbol(Token token);
}
