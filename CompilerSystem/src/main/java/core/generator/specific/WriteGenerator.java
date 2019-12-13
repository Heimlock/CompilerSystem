/**
 *
 */
package core.generator.specific;

import java.util.ArrayList;
import java.util.List;

import core.engine.operations.Operations;
import core.generator.CodeGeneratorException;
import data.TokenSymbolTable;
import data.impl.GlobalCounter_Impl;
import data.impl.SymbolTable_Impl;
import data.interfaces.GenerateCode;
import data.interfaces.GlobalCounter;
import data.interfaces.Scope;
import data.interfaces.Symbol;
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
  private SymbolTable table;
  private GlobalCounter counters;
  private Token srcVar;

  public WriteGenerator() {
    table = SymbolTable_Impl.getInstance();
    counters = GlobalCounter_Impl.getInstance();
  }

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
  public List<String> generate() throws CodeGeneratorException {
    List<String> result = new ArrayList<>();
    Symbol symbol = table.getSymbol(srcVar);
    Integer memoryLocation;
    
    if (symbol.getScope().equals(Scope.Variable)) {
      memoryLocation = table.getVarMemoryLocation(srcVar);
      result.add(String.format("%s %d", Operations.LDV.name(), memoryLocation));
    } else if (symbol.getScope().equals(Scope.Function)) {
      memoryLocation = table.getProcMemoryLocation(srcVar);
      result.add(String.format("%s %s_%d", Operations.CALL.name(), srcVar.getLexeme(), memoryLocation));
    } else {
      throw new CodeGeneratorException(String.format("Unexpected Token! Token = %s", srcVar.toString()));
    }

    result.add(Operations.PRN.name());
    return result;
  }

  @Override
  public void clear() {
    srcVar = null;
  }
}
