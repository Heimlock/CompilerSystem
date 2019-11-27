/**
 *
 */
package core.generator.specific;

import java.util.ArrayList;
import java.util.List;

import core.generator.CodeGeneratorException;
import data.impl.SymbolTable_Impl;
import data.interfaces.GenerateCode;
import data.interfaces.SymbolTable;
import data.interfaces.Token;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 23 de nov de 2019
 * @version 1.0
 * @since 1.0
 */
public class LoadGenerator implements GenerateCode {
  private List<Token> tokenList;
  private SymbolTable symbolTable = SymbolTable_Impl.getInstance();

  public LoadGenerator() {
    tokenList = new ArrayList<>();
  }

  @Override
  public void addToken(Token token) {
    this.tokenList.add(token);
  }

  @Override
  public List<String> generate() throws CodeGeneratorException {
    List<String> result = new ArrayList<>();
    Token auxToken = null;
    Boolean isNeg = false;
    for (Token token : tokenList) {
      switch (token.getSymbol()) {
      case sNumero:
        result.add(String.format("LDC %s", token.getLexeme()));
        break;
      case sIdentificador:
        if (token.getLexeme().startsWith("+")) {
          auxToken = token;
          auxToken.setLexeme(token.getLexeme().substring(1));
        } else if (token.getLexeme().startsWith("-")) {
          auxToken = token;
          auxToken.setLexeme(token.getLexeme().substring(1));
          isNeg = true;
        } else {
          auxToken = token;
        }
        if (symbolTable.hasSymbol(auxToken)) {
          result.add(String.format("LDV %d", symbolTable.getVarMemoryLocation(auxToken)));
          if (isNeg) {
            result.add("NEG");
          }
        } else {
          throw new CodeGeneratorException(String.format("Var/Function not Found. Token = %s", auxToken.toString()));
        }
        break;
      default:
        throw new CodeGeneratorException(String.format("Unexpected Token. Token = %s", token.toString()));
      }
    }
    return result;
  }

  @Override
  public void clear() {
    tokenList.clear();
  }
}
