/**
 *
 */
package core.generator.specific;

import java.util.ArrayList;
import java.util.List;

import core.engine.operations.Operations;
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
public class ProcedureGenerator implements GenerateCode {
  private Token identifier;
  private SymbolTable table;
  private List<List<String>> generatedBlocks;

  private final static String PROCEDURE_LABEL = "%s_%d";

  public ProcedureGenerator() {
    generatedBlocks = new ArrayList<>();
    table = SymbolTable_Impl.getInstance();
  }

  @Override
  public void addToken(Token token) {
    this.identifier = token;
  }

  /*
   * procedimento <identificador>;
   * <bloco>
   */
  @Override
  public List<String> generate() {
    List<String> result = new ArrayList<>();
    Integer memoryLocation = table.getProcMemoryLocation(identifier);
    //  Procedure Label
    result.add(String.format(String.format("%s %s", Operations.NULL.name(), PROCEDURE_LABEL), identifier.getLexeme(), memoryLocation));
    //  Add Block
    result.addAll(generatedBlocks.get(0));
    //  Add Return
    result.add(String.format("%s", Operations.RETURN.name()));
    return result;
  }

  @Override
  public void addBlock(List<String> generatedBlock) {
    this.generatedBlocks.add(generatedBlock);
  }

  @Override
  public void clear() {
    identifier = null;
    generatedBlocks.clear();
  }
}
