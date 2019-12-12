/**
 *
 */
package core.generator.specific;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import core.engine.operations.Operations;
import data.impl.GlobalCounter_Impl;
import data.impl.SymbolTable_Impl;
import data.interfaces.GenerateCode;
import data.interfaces.GlobalCounter;
import data.interfaces.SymbolTable;
import data.interfaces.Token;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 6 de nov de 2019
 * @version 1.0
 * @since 1.0
 */
public class FunctionGenerator implements GenerateCode {
  private Token identifier;
  private List<Token> vars;
  private SymbolTable table;
  private GlobalCounter counters;
  private List<List<String>> generatedBlocks;


  private final static String FUNCTION_LABEL = "%s_%d";

  public FunctionGenerator() {
    vars = new ArrayList<>();
    generatedBlocks = new ArrayList<>();
    table = SymbolTable_Impl.getInstance();
    counters = GlobalCounter_Impl.getInstance();
  }

  @Override
  public void addToken(Token token) {
    if (!Optional.ofNullable(this.identifier).isPresent()) {
      this.identifier = token;
    } else {
      this.vars.add(token);
    }
  }

  @Override
  public List<String> generate() {
    List<String> result = new ArrayList<>();
    //    Integer funcNumber = counters.postIncrement(identifier.getLexeme());
    Integer funcNumber = table.getProcMemoryLocation(identifier);
    //    List<Symbol> variables = table.getAllVariablesOf(identifier);
    //    Integer memoryLocation = table.getVarMemoryLocation(variables.get(0));

    //  Procedure Label
    result.add(String.format(String.format("%s %s", Operations.NULL.name(), FUNCTION_LABEL), identifier.getLexeme(), funcNumber));
    //    //  Alloc Variables
    //    result.add(String.format("%s %d %d", Operations.ALLOC.name(), memoryLocation, variables.size()));
    //  Add Block
    result.addAll(generatedBlocks.get(0));
    //    //  Add ReturnF
    //    result.add(String.format("%s %d %d", Operations.RETURNF.name(), memoryLocation, variables.size()));
    return result;
  }

  @Override
  public void addBlock(List<String> generatedBlock) {
    this.generatedBlocks.add(generatedBlock);
  }

  @Override
  public void clear() {
    identifier = null;
    vars.clear();
    generatedBlocks.clear();
  }
}
