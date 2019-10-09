/**
 * 
 */
package data.interfaces;

import java.util.List;

/**
 * Copyright (c) 2019
 *
 * @author 16116469 / 11 de set de 2019
 * @version 1.0
 * @since 1.0
 */
public interface TokenStack {
  public void addToken(Token token);

  public void addTokens(List<Token> tokens);

  public void getToken(int index);

  public Token nextToken();

  public List<Token> getTokens();

  public Integer getCounter();

  public Integer getRemainingTokens();
}
