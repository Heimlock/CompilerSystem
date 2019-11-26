/**
 *
 */
package core.generator.specific;

import java.util.ArrayList;
import java.util.List;

import core.engine.operations.Operations;
import core.generator.CodeGeneratorException;
import data.TokenSymbolTable;
import data.impl.SymbolTable_Impl;
import data.interfaces.GenerateCode;
import data.interfaces.Scope;
import data.interfaces.Symbol;
import data.interfaces.SymbolTable;
import data.interfaces.Token;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 25 de nov de 2019
 * @version 1.0
 * @since 1.0
 */
public class VariableGenerator implements GenerateCode {
  private Token identifier;
  private List<Token> variables;
  private SymbolTable table;
  private List<List<String>> generatedBlocks;

  public VariableGenerator() {
    variables = new ArrayList<>();
    generatedBlocks = new ArrayList<>();
    table = SymbolTable_Impl.getInstance();
  }

  @Override
  public void addToken(Token token) throws CodeGeneratorException {
    if (token.getSymbol().equals(TokenSymbolTable.sIdentificador)) {
      if (table.hasSymbol(token, Scope.Variable)) {
        variables.add(token);
      } else if (table.hasSymbol(token, Scope.Procedure) || table.hasSymbol(token, Scope.Function) || table.hasSymbol(token, Scope.Program)) {
        identifier = token;
      } else {
        throw new CodeGeneratorException(String.format("Identifier not Register. Token: %s", token.toString()));
      }
    } else {
      throw new CodeGeneratorException(String.format("Unexpected Token. Only Identifiers are Allowed. Token: %s", token.toString()));
    }
  }

  @Override
  public List<String> generate() throws CodeGeneratorException {
    List<String> result = new ArrayList<>();
    List<Symbol> variables = table.getAllVariablesOf(identifier);
    Integer memoryLocation = table.getProcMemoryLocation(identifier);

    //  Alloc Variables
    result.add(String.format("%s %d %d", Operations.ALLOC.name(), memoryLocation, variables.size()));

    //  Block
    result.addAll(generatedBlocks.get(0));

    //  Alloc Variables    
    result.add(String.format("%s %d %d", Operations.DALLOC.name(), memoryLocation, variables.size()));

    return result;
  }

  @Override
  public void addBlock(List<String> generatedBlock) {
    this.generatedBlocks.add(generatedBlock);
  }

  @Override
  public void clear() {
    identifier = null;
    variables.clear();
    generatedBlocks.clear();
  }
}
