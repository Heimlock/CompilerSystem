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
public class WriteGenerator implements GenerateCode {
  private SymbolTable table = SymbolTable_Impl.getInstance();
  private Token srcVar;

  @Override
  public void addToken(Token token) {
    if (token.getSymbol().equals(TokenSymbolTable.sIdentificador)) {
      this.srcVar = token;
    }
  }

  /*
   * escreva ( <identificador> )
   */
  @Override
  public List<String> generate() {
    List<String> result = new ArrayList<>();
    Integer memoryLocation = table.getVarMemoryLocation(srcVar);
    result.add(String.format("%s %d", Operations.LDV.name(), memoryLocation));
    result.add(Operations.PRN.name());
    return result;
  }

  @Override
  public void clear() {
    srcVar = null;
  }
}
