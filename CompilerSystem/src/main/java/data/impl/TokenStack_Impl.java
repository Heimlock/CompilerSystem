/**
 * 
 */
package data.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import data.interfaces.Token;
import data.interfaces.TokenStack;

/**
 * Copyright (c) 2019
 *
 * @author 16116469 / 11 de set de 2019
 * @version 1.0
 * @since 1.0
 */
public class TokenStack_Impl implements TokenStack {
  private static TokenStack_Impl instance = null;
  private Integer tokenCounter;
  private List<Token> tokenList;

  public static TokenStack_Impl getInstance() {
    if (instance == null) {
      instance = new TokenStack_Impl();
    }
    return instance;
  }

  private TokenStack_Impl() {
    tokenList = new ArrayList<>();
  }

  /* (non-Javadoc)
   * @see data.interfaces.TokenStack#addToken(data.interfaces.Token)
   */
  @Override
  public void addToken(Token token) {
    this.tokenList.add(token);
    this.tokenCounter = 0;
  }

  /*
   * (non-Javadoc)
   * @see data.interfaces.TokenStack#addTokens(java.util.List)
   */
  @Override
  public void addTokens(List<Token> tokens) {
    tokens.forEach(token -> {
      this.addToken(token);
    });
  }

  /*
   * (non-Javadoc)
   * @see data.interfaces.TokenStack#getToken(int)
   */
  @Override
  public void getToken(int index) {
    //TODO Validate Range
    this.tokenList.get(index);
  }

  /* (non-Javadoc)
   * @see data.interfaces.TokenStack#nextToken()
   */
  @Override
  public Token nextToken() {
    return this.tokenList.get(tokenCounter++);
  }

  /*
   * (non-Javadoc)
   * @see data.interfaces.TokenStack#nextToken()
   */
  @Override
  public Token peekToken() {
    return this.tokenList.get(tokenCounter);
  }

  /*
   * (non-Javadoc)
   * @see data.interfaces.TokenStack#getTokens()
   */
  @Override
  public List<Token> getTokens() {
    return Collections.unmodifiableList(this.tokenList);
  }

  /* (non-Javadoc)
   * @see data.interfaces.TokenStack#getCounter()
   */
  @Override
  public Integer getCounter() {
    return this.tokenCounter;
  }

  @Override
  public Integer getRemainingTokens() {
    return this.tokenList.size() - this.tokenCounter;
  }
}
