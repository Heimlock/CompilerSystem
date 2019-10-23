/**
 * 
 */
package data.interfaces;

import java.util.List;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 8 de out de 2019
 * @version 1.0
 * @since 1.0
 */
public interface SymbolTable {
  public void addSymbol(Token token, Scope scope);

  public void addSymbol(Token token, Scope scope, Type type);

  public void addType(Token token);

  public Boolean hasSymbol(Token token);

  public Boolean hasSymbol(Token token, Scope scope);

  public Symbol getSymbol(Token token);

  public List<Symbol> getAll();

  //  UnitTest Util
  public void purgeList();
}
