/**
 *
 */
package core.generator.specific;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import core.engine.operations.Operations;
import core.generator.CodeGeneratorException;
import data.TokenSymbolTable;
import data.impl.PostfixNotation_Impl;
import data.impl.SymbolTable_Impl;
import data.interfaces.GenerateCode;
import data.interfaces.PostfixNotation;
import data.interfaces.Symbol;
import data.interfaces.SymbolTable;
import data.interfaces.Token;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 8 de dez de 2019
 * @version 1.0
 * @since 1.0
 */
public class FunctionReturnGenerator implements GenerateCode {
  private Token functionId;
  private SymbolTable table;
  private PostfixNotation postfix;

  public FunctionReturnGenerator() {
    table = SymbolTable_Impl.getInstance();
    postfix = new PostfixNotation_Impl();
  }

  @Override
  public void addToken(Token token) throws CodeGeneratorException {
    if (Optional.ofNullable(functionId).isPresent()) {
      if (!token.getSymbol().equals(TokenSymbolTable.sAtribuicao)) {
        postfix.addToken(token);
      }
    } else {
      this.functionId = token;
    }
  }

  @Override
  public List<String> generate() throws CodeGeneratorException {
    List<String> result = new ArrayList<>();
    List<String> postFixResult;

    Symbol function = table.getSymbol(functionId);
    List<Symbol> variables = table.getAllVariablesOf(functionId);
    Integer variablesCounter = variables.size();
    Integer memoryLocation = variablesCounter != 0 ? table.getVarMemoryLocation(variables.get(0)) : -1;

    postFixResult = postfix.generate();
    try {
      if (postfix.getType().equals(function.getType().get())) {
        result.addAll(postFixResult);
        //  ReturnF
        result.add(String.format("%s %d %d", Operations.RETURNF.name(), memoryLocation, variablesCounter != 0 ? variablesCounter : -1));
      } else {
        throw new CodeGeneratorException(String.format("Symbol Type does not match Postfix result Type. Symbol.type: %s, Postfix.type: %s", function.getType().get().name(), postfix.getType().name()));
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
      throw new CodeGeneratorException("No Identifier Or Expression");
    }
    return result;
  }

  @Override
  public void clear() {
    this.functionId = null;
    this.postfix.clear();
  }
}
