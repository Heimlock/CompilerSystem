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
import data.impl.PostfixNotation_Impl;
import data.interfaces.GenerateCode;
import data.interfaces.GlobalCounter;
import data.interfaces.PostfixNotation;
import data.interfaces.Token;
import data.interfaces.Type;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 6 de nov de 2019
 * @version 1.0
 * @since 1.0
 */
public class IfGenerator implements GenerateCode {
  private GlobalCounter counters;
  private PostfixNotation postfix;
  private List<List<String>> generatedBlocks;

  private final static String ELSE_START = "Else_Start_%d";
  private final static String ELSE_END = "Else_End_%d";

  public IfGenerator() {
    counters = GlobalCounter_Impl.getInstance();
    postfix = new PostfixNotation_Impl();
    generatedBlocks = new ArrayList<>();
  }

  @Override
  public void addToken(Token token) throws CodeGeneratorException {
    postfix.addToken(token);
  }

  /*
   * se <expressão>
   * entao <comando>
   * [senao <comando>]
   */
  @Override
  public List<String> generate() throws CodeGeneratorException {
    List<String> result = new ArrayList<>();
    List<String> postFixResult = postfix.generate();
    Integer ifCount = counters.postIncrement(TokenSymbolTable.sSe);

    //  Verify if Expression is a Boolean
    if (!postfix.getType().equals(Type.Booleano)) {
      throw new CodeGeneratorException("Incompatible Expression Type! Context: If");
    }

    //  Add Expression
    result.addAll(postFixResult);
    //  Negate Result
    //    result.add(String.format("%s", Operations.NEG.name()));
    //  Jump Else
    result.add(String.format(String.format("%s %s", Operations.JMPF.name(), ELSE_START), ifCount));
    //  If Block
    result.addAll(generatedBlocks.get(0));
    //  Jump End
    if (generatedBlocks.size() == 2) {
      result.add(String.format(String.format("%s %s", Operations.JMP.name(), ELSE_END), ifCount));
    }
    //  Else Start Label
    result.add(String.format(String.format("%s %s", Operations.NULL.name(), ELSE_START), ifCount));
    //  Else Block
    if (generatedBlocks.size() == 2) {
      result.addAll(generatedBlocks.get(1));
      // Else End
      result.add(String.format(String.format("%s %s", Operations.NULL.name(), ELSE_END), ifCount));
    }
    return result;
  }

  @Override
  public void addBlock(List<String> generatedBlock) {
    this.generatedBlocks.add(generatedBlock);
  }

  @Override
  public void clear() {
    postfix.clear();
    generatedBlocks.clear();
  }
}
