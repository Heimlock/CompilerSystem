/**
 * 
 */
package data.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import data.interfaces.Scope;
import data.interfaces.Symbol;
import data.interfaces.SymbolTable;
import data.interfaces.Token;
import data.interfaces.Type;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 19 de out de 2019
 * @version 1.0
 * @since 1.0
 */
public class SymbolTable_Impl implements SymbolTable {
  private static SymbolTable_Impl instance = null;
  private List<Symbol> symbolStack;

  public static SymbolTable getInstance() {
    if (instance == null) {
      instance = new SymbolTable_Impl();
    }
    return instance;
  }

  private SymbolTable_Impl() {
    this.symbolStack = new ArrayList<>();
  }

  @Override
  public void addSymbol(Token token, Scope scope) {
    this.symbolStack.add(new Symbol_Impl(token, scope));
  }

  @Override
  public void addSymbol(Token token, Scope scope, Type type) {
    this.symbolStack.add(new Symbol_Impl(token, scope, type));
  }

  @Override
  public void addType(Token token) {
    for (int i = symbolStack.size() - 1; i >= 0; i--) {
      if (symbolStack.get(i).getScope() == Scope.Variable) {
        if (!symbolStack.get(i).getType().isPresent()) {
          symbolStack.get(i).setType(Type.getType(token.getLexeme()));
        } else {
          break;
        }
      } else {
        break;
      }
    }
  }

  @Override
  public Boolean hasSymbol(Token token) {
    Boolean result = this.symbolStack.stream().anyMatch(symbol -> symbol.getLexeme().equals(token.getLexeme()));
    return result;
  }

  @Override
  public Boolean hasSymbol(Token token, Scope scope) {
    Boolean result = this.symbolStack.stream().anyMatch(symbol -> symbol.getLexeme() == token.getLexeme() && symbol.getScope() == scope);
    return result;
  }

  @Override
  public Symbol getSymbol(Token token) {
    Optional<Symbol> result = null;
    result = this.symbolStack.stream().filter(symbol -> symbol.getLexeme().equals(token.getLexeme())).findFirst();
    return result.orElse(null); // TODO Avaliate if needs to handle invalid requests
  }

  @Override
  public List<Symbol> getAll() {
    return Collections.unmodifiableList(this.symbolStack);
  }

  @Override
  public void purgeList() {
    this.symbolStack.clear();
  }
}
