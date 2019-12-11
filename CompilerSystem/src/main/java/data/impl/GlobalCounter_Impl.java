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
  private Map<String, Integer> dataMap;

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
  public Integer postIncrement(TokenSymbolTable symbol) {
    return this.postIncrement(symbol.name());
  }

  @Override
  public Integer postIncrement(String identifier) {
    Integer currentValue = dataMap.getOrDefault(identifier, 0);
    dataMap.put(identifier, currentValue + 1);
    return currentValue;
  }

  //  @Override
  //  public Integer getCount(TokenSymbolTable symbol) {
  //    return this.getCount(symbol.name());
  //  }

  @Override
  public Integer getCount(String identifier) {
    return dataMap.getOrDefault(identifier, 1) - 1;
  }

  @Override
  public void clear() {
    dataMap.clear();
  }
}
