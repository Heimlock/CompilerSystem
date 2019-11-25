/**
 *
 */
package data.interfaces;

import data.TokenSymbolTable;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 25 de nov de 2019
 * @version 1.0
 * @since 1.0
 */
public interface GlobalCounter {
  public void increment(TokenSymbolTable symbol);

  public Integer getCount(TokenSymbolTable symbol);

  // Debug
  public void clear();
}
