/**
 * 
 */
package data.impl;

import java.util.Optional;

import data.interfaces.Scope;
import data.interfaces.Symbol;
import data.interfaces.Token;
import data.interfaces.Type;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 19 de out de 2019
 * @version 1.0
 * @since 1.0
 */
public class Symbol_Impl implements Symbol {
  private Token token;
  private Scope lexemeScope;
  private Type lexemeType;
  private Integer stackAddr;

  public Symbol_Impl(Token token, Scope scope) {
    this.token = token;
    this.lexemeScope = scope;
  }

  public Symbol_Impl(Token token, Scope scope, Type type) {
    this.token = token;
    this.lexemeScope = scope;
    this.lexemeType = type;
  }

  @Override
  public String getLexeme() {
    return this.token.getLexeme();
  }

  @Override
  public Token getToken() {
    return this.token;
  }

  @Override
  public Scope getScope() {
    return lexemeScope;
  }

  @Override
  public Optional<Type> getType() {
    return Optional.ofNullable(lexemeType);
  }

  @Override
  public void setType(Type type) {
    this.lexemeType = type;
  }

  @Override
  public Optional<Integer> getStackAddr() {
    return Optional.ofNullable(stackAddr);
  }

  @Override
  public void setStackAddr(int stackAddr) {
    this.stackAddr = stackAddr;
  }

  @Override
  public String toString() {
    String type = Optional.ofNullable(lexemeType).isPresent() ? lexemeType.name() : "N/A";
    return String.format("Symbol [line='%2d', offset='%2d', lexeme='%s', lexemeScope='%s', lexemeType='%s']", token.getLineIndex(), token.getLineOffset(), token.getLexeme(), lexemeScope.toString(), type);
  }
}
