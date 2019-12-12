/**
 *
 */
package semantic;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import core.generator.CodeGeneratorException;
import data.TokenSymbolTable;
import data.impl.PostfixNotation_Impl;
import data.impl.SymbolTable_Impl;
import data.impl.Token_Impl;
import data.interfaces.PostfixNotation;
import data.interfaces.Scope;
import data.interfaces.SymbolTable;
import data.interfaces.Token;
import data.interfaces.Type;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 6 de nov de 2019
 * @version 1.0
 * @since 1.0
 */
public class PostfixNotationTest {
  private Boolean operationsDebug;
  private PostfixNotation postfix;
  private SymbolTable table;

  @Before
  public void setup() {
    operationsDebug = false;
    table = SymbolTable_Impl.getInstance();
  }

  /*
   * Simple Operations Tests
   */

  @Test
  public void additionTypeTest() throws CodeGeneratorException {
    postfix = getNotation(Arrays.asList("1", "+", "2"));
    printGeneratedCode(postfix.generate());
    assertEquals("Should have Returned a Integer Expression", Type.Inteiro, postfix.getType());
  }

  @Test
  public void subtractionTypeTest() throws CodeGeneratorException {
    postfix = getNotation(Arrays.asList("1", "-", "2"));
    printGeneratedCode(postfix.generate());
    assertEquals("Should have Returned a Integer Expression", Type.Inteiro, postfix.getType());
  }

  @Test
  public void multiplicationTypeTest() throws CodeGeneratorException {
    postfix = getNotation(Arrays.asList("1", "*", "2"));
    printGeneratedCode(postfix.generate());
    assertEquals("Should have Returned a Integer Expression", Type.Inteiro, postfix.getType());
  }

  @Test
  public void divisionTypeTest() throws CodeGeneratorException {
    postfix = getNotation(Arrays.asList("1", "div", "2"));
    printGeneratedCode(postfix.generate());
    assertEquals("Should have Returned a Integer Expression", Type.Inteiro, postfix.getType());
  }

  @Test
  public void greatTypeTest() throws CodeGeneratorException {
    postfix = getNotation(Arrays.asList("1", ">", "2"));
    printGeneratedCode(postfix.generate());
    assertEquals("Should have Returned a Boolean Expression", Type.Booleano, postfix.getType());
  }

  @Test
  public void greatEqualTypeTest() throws CodeGeneratorException {
    postfix = getNotation(Arrays.asList("1", ">=", "2"));
    printGeneratedCode(postfix.generate());
    assertEquals("Should have Returned a Boolean Expression", Type.Booleano, postfix.getType());
  }

  @Test
  public void lessTypeTest() throws CodeGeneratorException {
    postfix = getNotation(Arrays.asList("1", "<", "2"));
    printGeneratedCode(postfix.generate());
    assertEquals("Should have Returned a Boolean Expression", Type.Booleano, postfix.getType());
  }

  @Test
  public void lessEqualTypeTest() throws CodeGeneratorException {
    postfix = getNotation(Arrays.asList("1", "<=", "2"));
    printGeneratedCode(postfix.generate());
    assertEquals("Should have Returned a Boolean Expression", Type.Booleano, postfix.getType());
  }

  @Test
  public void equalTypeTest() throws CodeGeneratorException {
    postfix = getNotation(Arrays.asList("1", "=", "2"));
    printGeneratedCode(postfix.generate());
    assertEquals("Should have Returned a Boolean Expression", Type.Booleano, postfix.getType());
  }

  @Test
  public void notTypeTest() throws CodeGeneratorException {
    postfix = getNotation(Arrays.asList("nao", "falso"));
    printGeneratedCode(postfix.generate());
    assertEquals("Should have Returned a Boolean Expression", Type.Booleano, postfix.getType());
  }

  @Test
  public void andTypeTest() throws CodeGeneratorException {
    postfix = getNotation(Arrays.asList("falso", "e", "falso"));
    printGeneratedCode(postfix.generate());
    assertEquals("Should have Returned a Boolean Expression", Type.Booleano, postfix.getType());
  }

  @Test
  public void orTypeTest() throws CodeGeneratorException {
    postfix = getNotation(Arrays.asList("falso", "ou", "verdadeiro"));
    printGeneratedCode(postfix.generate());
    assertEquals("Should have Returned a Boolean Expression", Type.Booleano, postfix.getType());
  }

  @Test
  public void diffTypeTest() throws CodeGeneratorException {
    postfix = getNotation(Arrays.asList("1", "!=", "0"));
    printGeneratedCode(postfix.generate());
    assertEquals("Should have Returned a Boolean Expression", Type.Booleano, postfix.getType());
  }

  /*
   * Expressions Tests
   */

  @Test
  public void expression01Test() throws CodeGeneratorException {
    postfix = getNotation(Arrays.asList("(", "1", "+", "2", "*", "3", ")", "<=", "4", "*", "5"));
    printGeneratedCode(postfix.generate());
    printPostFix(postfix);
    assertEquals("Should have Returned a Boolean Expression", Type.Booleano, postfix.getType());
    assertEquals("Should have Equal Result", "1 2 3 * + 4 5 * <= ", postfix.toString());
  }

  @Test
  public void expression02Test() throws CodeGeneratorException {
    postfix = getNotation(Arrays.asList("1", "*", "2", "+", "3", ">=", "4", "*", "5"));
    printGeneratedCode(postfix.generate());
    printPostFix(postfix);
    assertEquals("Should have Returned a Boolean Expression", Type.Booleano, postfix.getType());
    assertEquals("Should have Equal Result", "1 2 * 3 + 4 5 * >= ", postfix.toString());
  }

  @Test
  public void expression03Test() throws CodeGeneratorException {
    postfix = getNotation(Arrays.asList("-", "1", "+", "-", "2", "*", "3"));
    printGeneratedCode(postfix.generate());
    printPostFix(postfix);
    assertEquals("Should have Returned a Integer Expression", Type.Inteiro, postfix.getType());
    assertEquals("Should have Equal Result", "1 - 2 - 3 * + ", postfix.toString());
  }

  @Test
  public void expression04Test() throws CodeGeneratorException {
    postfix = getNotation(Arrays.asList("1", "-", "2", "-", "3"));
    printGeneratedCode(postfix.generate());
    printPostFix(postfix);
    assertEquals("Should have Returned a Boolean Expression", Type.Inteiro, postfix.getType());
    assertEquals("Should have Equal Result", "1 2 - 3 - ", postfix.toString());
  }

  @Test
  @SuppressWarnings("deprecation")
  public void expression05Test() throws CodeGeneratorException {
    table.addSymbol(new Token_Impl("var1"), Scope.Variable, Type.Inteiro);
    table.addSymbol(new Token_Impl("var2"), Scope.Variable, Type.Inteiro);
    postfix = getNotation(Arrays.asList("var1", "+", "var2"));
    printGeneratedCode(postfix.generate());
    printPostFix(postfix);
    assertEquals("Should have Returned a Boolean Expression", Type.Inteiro, postfix.getType());
    assertEquals("Should have Equal Result", "var1 var2 + ", postfix.toString());
  }

  @Test
  public void expression06Test() throws CodeGeneratorException {
    postfix = getNotation(Arrays.asList("-", "(", "-", "5", "div", "2", ")"));
    printGeneratedCode(postfix.generate());
    printPostFix(postfix);
    assertEquals("Should have Returned a Integer Expression", Type.Inteiro, postfix.getType());
    assertEquals("Should have Equal Result", "5 - 2 div - ", postfix.toString());
  }

  /*
   * Auxiliary Functions
   */

  private PostfixNotation getNotation(List<String> expression) throws CodeGeneratorException {
    PostfixNotation postfix = new PostfixNotation_Impl();
    Token token;
    String lexeme;
    String result = "";
    for (int i = 0; i < expression.size(); i++) {
      token = new Token_Impl(0, i);
      lexeme = expression.get(i);
      token.setSymbol(TokenSymbolTable.getSymbolByLexeme(lexeme));
      token.setLexeme(lexeme);
      postfix.addToken(token);
      result = String.format("%s%s ", result, lexeme);
    }
    if (operationsDebug) {
      System.out.println(String.format("Expression: %s", result));
    }
    return postfix;
  }

  private void printGeneratedCode(List<String> code) {
    if (operationsDebug) {
      code.forEach(line -> System.out.println(line));
    }
  }

  private void printPostFix(PostfixNotation postfix) {
    if (operationsDebug) {
      System.out.println(String.format("PostFix: %s", postfix.toString()));
    }
  }
}
