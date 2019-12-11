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

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 6 de nov de 2019
 * @version 1.0
 * @since 1.0
 */
public class WhileGenerator implements GenerateCode {
  private GlobalCounter counters;
  private PostfixNotation postfix;
  private List<List<String>> generatedBlocks;

  private final static String WHILE_START = "While_Start_%d";
  private final static String WHILE_END = "While_End_%d";

  public WhileGenerator() {
    counters = GlobalCounter_Impl.getInstance();
    postfix = new PostfixNotation_Impl();
    generatedBlocks = new ArrayList<>();
  }

  @Override
  public void addToken(Token token) throws CodeGeneratorException {
    postfix.addToken(token);
  }

  /*
   * enquanto <expressão>
   * faca
   * <comando>
   */
  @Override
  public List<String> generate() throws CodeGeneratorException {
    List<String> result = new ArrayList<>();
    List<String> postFixResult = postfix.generate();
    Integer whileCount = counters.postIncrement(TokenSymbolTable.sEnquanto);

    //  While Start Label
    result.add(String.format(String.format("%s %s", Operations.NULL.name(), WHILE_START), whileCount));
    //  Add Expression
    result.addAll(postFixResult);
    //  Negate Result
    result.add(String.format("%s", Operations.NEG.name()));
    //  Jump Else
    result.add(String.format(String.format("%s %s", Operations.JMPF.name(), WHILE_END), whileCount));
    //  Add Block
    result.addAll(generatedBlocks.get(0));
    //  Jump Start
    result.add(String.format(String.format("%s %s", Operations.JMP.name(), WHILE_START), whileCount));
    //  While End Label
    result.add(String.format(String.format("%s %s", Operations.NULL.name(), WHILE_END), whileCount));
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
