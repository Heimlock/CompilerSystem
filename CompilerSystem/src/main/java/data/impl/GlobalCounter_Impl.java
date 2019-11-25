/**
 *
 */
package data.impl;

import java.util.HashMap;
import java.util.Map;

import data.TokenSymbolTable;
import data.interfaces.GlobalCounter;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 25 de nov de 2019
 * @version 1.0
 * @since 1.0
 */
public class GlobalCounter_Impl implements GlobalCounter {
  private static GlobalCounter_Impl instance = null;
  private Map<TokenSymbolTable, Integer> dataMap;

  public static GlobalCounter_Impl getInstance() {
    if (instance == null) {
      instance = new GlobalCounter_Impl();
    }
    return instance;
  }

  private GlobalCounter_Impl() {
    dataMap = new HashMap<>();
  }

  @Override
  public void increment(TokenSymbolTable symbol) {
    Integer currentValue = 0;
    if (dataMap.containsKey(symbol)) {
      currentValue = dataMap.get(symbol) + 1;
    }
    dataMap.put(symbol, currentValue);
  }

  @Override
  public Integer getCount(TokenSymbolTable symbol) {
    return dataMap.getOrDefault(symbol, 0);
  }

  @Override
  public void clear() {
    dataMap.clear();
  }
}
