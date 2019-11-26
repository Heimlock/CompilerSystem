/**
 * 
 */
package data.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

  @Override
  public Integer getVarMemoryLocation(Symbol symbol) {
    return getVarMemoryLocation(symbol.getToken());
  }

  @Override
  public Integer getVarMemoryLocation(Token token) {
    Symbol var = getSymbol(token);
    Symbol parent = var;

    List<Symbol> variables;
    List<Symbol> procedures = symbolStack.stream()//
        .filter(s -> s.getScope().equals(Scope.Program) || s.getScope().equals(Scope.Procedure) || s.getScope().equals(Scope.Function))//
        .collect(Collectors.toList());

    Integer parentIndex = null;
    Integer varIndex = null;

    for (Symbol s : symbolStack.subList(0, symbolStack.indexOf(var))) {
      if (s.getScope().equals(Scope.Program) || s.getScope().equals(Scope.Procedure) || s.getScope().equals(Scope.Function)) {
        parent = s;
      }
    }
    variables = getAllVariablesOf(parent.getToken());

    parentIndex = procedures.indexOf(parent);
    varIndex = variables.indexOf(var);
    return parentIndex + varIndex;
  }

  @Override
  public Integer getProcMemoryLocation(Symbol symbol) {
    return getProcMemoryLocation(symbol.getToken());
  }

  @Override
  public Integer getProcMemoryLocation(Token token) {
    Symbol procSymbol = getSymbol(token);
    List<Symbol> procedures = symbolStack.stream()//
        .filter(s -> s.getScope().equals(Scope.Program) || s.getScope().equals(Scope.Procedure) || s.getScope().equals(Scope.Function))//
        .collect(Collectors.toList());
    Integer index = null;

    index = procedures.indexOf(procSymbol);
    return index;
  }

  @Override
  public List<Symbol> getAllVariablesOf(Token token) {
    List<Symbol> variables = new ArrayList<>();
    Symbol varParent = getSymbol(token);
    for (Symbol actual : symbolStack.subList(symbolStack.indexOf(varParent) + 1, symbolStack.size())) {
      if (actual.getScope().equals(Scope.Variable)) {
        variables.add(actual);
      } else {
        break;
      }
    }
    return variables;
  }
}
