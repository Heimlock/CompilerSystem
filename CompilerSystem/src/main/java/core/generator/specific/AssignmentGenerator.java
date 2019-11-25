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
 * @author Felipe Moreira Ferreira / 6 de nov de 2019
 * @version 1.0
 * @since 1.0
 */
public class AssignmentGenerator implements GenerateCode {
  private SymbolTable table;
  private Token identifier;
  private PostfixNotation postfix;

  public AssignmentGenerator() {
    table = SymbolTable_Impl.getInstance();
    postfix = new PostfixNotation_Impl();
  }

  @Override
  public void addToken(Token token) {
    if (Optional.ofNullable(identifier).isPresent()) {
      if (!token.getSymbol().equals(TokenSymbolTable.sAtribuicao)) {
        postfix.addToken(token);
      }
    } else {
      identifier = token;
    }
  }

  /*
   * <identificador> := <expressão>
   */
  @Override
  public List<String> generate() throws CodeGeneratorException {
    List<String> result = new ArrayList<>();
    List<String> postFixResult;
    Integer memoryLocation = table.getMemoryLocation(identifier);
    Symbol symbol = table.getSymbol(identifier);

    postFixResult = postfix.generate();
    if (postfix.getType().equals(symbol.getType().get())) {
      result.addAll(postFixResult);
      result.add(String.format("%s %d", Operations.STR.name(), memoryLocation));
    } else {
      throw new CodeGeneratorException(String.format("Symbol Type does not match Postfix result Type. Symbol.type: %s, Postfix.type: %s", symbol.getType().get().name(), postfix.getType().name()));
    }
    return result;
  }

  @Override
  public void clear() {
    identifier = null;
    postfix.clear();
  }
}
