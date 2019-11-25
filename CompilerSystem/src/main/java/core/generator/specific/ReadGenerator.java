/**
 *
 */
package core.generator.specific;

import java.util.ArrayList;
import java.util.List;

import core.engine.operations.Operations;
import data.TokenSymbolTable;
import data.impl.SymbolTable_Impl;
import data.interfaces.GenerateCode;
import data.interfaces.SymbolTable;
import data.interfaces.Token;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 6 de nov de 2019
 * @version 1.0
 * @since 1.0
 */
public class ReadGenerator implements GenerateCode {
  private SymbolTable table = SymbolTable_Impl.getInstance();
  private Token destVar;

  @Override
  public void addToken(Token token) {
    if (token.getSymbol().equals(TokenSymbolTable.sIdentificador)) {
      this.destVar = token;
    }
  }

  /*
   * leia ( <identificador> )
   */
  @Override
  public List<String> generate() {
    List<String> result = new ArrayList<>();
    Integer memoryLocation = table.getMemoryLocation(destVar);
    result.add(Operations.RD.name());
    result.add(String.format("%s %d", Operations.STR.name(), memoryLocation));
    return result;
  }

  @Override
  public void clear() {
    destVar = null;
  }
}
