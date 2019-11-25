/**
 *
 */
package semantic;

import static core.generator.GeneratorTypes.Assignment;
import static core.generator.GeneratorTypes.If;
import static core.generator.GeneratorTypes.Read;
import static core.generator.GeneratorTypes.Write;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.generator.CodeGeneratorException;
import data.impl.GlobalCounter_Impl;
import data.impl.SymbolTable_Impl;
import data.impl.Token_Impl;
import data.interfaces.GenerateCode;
import data.interfaces.GlobalCounter;
import data.interfaces.Scope;
import data.interfaces.SymbolTable;
import data.interfaces.Token;
import data.interfaces.Type;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 24 de nov de 2019
 * @version 1.0
 * @since 1.0
 */
public class GenerateCodeTest {
  private Boolean isDebug;
  private GlobalCounter counters;
  private SymbolTable table;
  private GenerateCode generator;

  @Before
  public void setup() {
    isDebug = true;
    table = SymbolTable_Impl.getInstance();
    counters = GlobalCounter_Impl.getInstance();
  }

  @After
  public void cleanUp() {
    table.purgeList();
    counters.clear();
  }

  @Test
  public void readTest() throws CodeGeneratorException {
    generator = Read.getGenerator();
    buildContext(generator, Arrays.asList("var1"));
    assertAssembly(generator, Arrays.asList("RD", "STR -1"));
  }

  @Test
  public void writeTest() throws CodeGeneratorException {
    generator = Write.getGenerator();
    buildContext(generator, Arrays.asList("var1"));
    assertAssembly(generator, Arrays.asList("LDV -1", "PRN"));
  }

  @SuppressWarnings("deprecation")
  @Test
  public void assignmentTest() throws CodeGeneratorException {
    generator = Assignment.getGenerator();
    table.addSymbol(new Token_Impl("var1"), Scope.Variable, Type.Inteiro);
    buildContext(generator, Arrays.asList("var1", ":=", "1", "+", "2"));
    assertAssembly(generator, Arrays.asList("LDC 1", "LDC 2", "ADD", "STR -1"));
  }

  @SuppressWarnings("deprecation")
  @Test
  public void assignmenResultTypetTest() {
    generator = Assignment.getGenerator();
    try {
      table.addSymbol(new Token_Impl("var1"), Scope.Variable, Type.Inteiro);
      buildContext(generator, Arrays.asList("var1", ":=", "1", "=", "2"));
      generator.generate();
      fail("Should throw Error based On variable Type and Expression Type Difference.");
    } catch (CodeGeneratorException e) {
      assertEquals("Should throw Error based On variable Type and Expression Type Difference.", "Symbol Type does not match Postfix result Type. Symbol.type: Inteiro, Postfix.type: Booleano", e.getMessage());
    }
  }

  @SuppressWarnings("deprecation")
  @Test
  public void ifTest() throws CodeGeneratorException {
    List<String> ifBlockResult;

    //  If Block
    generator = Assignment.getGenerator();
    table.addSymbol(new Token_Impl("var1"), Scope.Variable, Type.Inteiro);
    buildContext(generator, Arrays.asList("var1", ":=", "1", "+", "2"));
    ifBlockResult = generator.generate();

    //  If
    generator = If.getGenerator();
    buildContext(generator, Arrays.asList("3", "=", "4"));
    generator.addBlock(ifBlockResult);
    assertAssembly(generator, Arrays.asList(//
        "LDC 3", "LDC 4", "CEQ",  //  PostFix 
        "NEG", "JMPF Else_Start_0", //  If Decision
        "LDC 1", "LDC 2", "ADD", "STR -1", // If Block
        "NULL Else_Start_0" // Else Start
        ));
  }

  @SuppressWarnings("deprecation")
  @Test
  public void ifElseTest() throws CodeGeneratorException {
    List<String> ifBlockResult;
    List<String> elseBlockResult;

    //  If Block
    generator = Assignment.getGenerator();
    table.addSymbol(new Token_Impl("var1"), Scope.Variable, Type.Inteiro);
    buildContext(generator, Arrays.asList("var1", ":=", "1", "+", "2"));
    ifBlockResult = generator.generate();

    //  else Block
    generator = Assignment.getGenerator();
    table.addSymbol(new Token_Impl("var1"), Scope.Variable, Type.Inteiro);
    buildContext(generator, Arrays.asList("var1", ":=", "3", "+", "4"));
    elseBlockResult = generator.generate();

    //  If and Else
    generator = If.getGenerator();
    buildContext(generator, Arrays.asList("5", "=", "6"));
    generator.addBlock(ifBlockResult);
    generator.addBlock(elseBlockResult);
    assertAssembly(generator, Arrays.asList(//
        "LDC 5", "LDC 6", "CEQ", //  PostFix 
        "NEG", "JMPF Else_Start_0", //  If Decision
        "LDC 1", "LDC 2", "ADD", "STR -1", // If Block
        "JMP Else_End_0", // If Block, Jump to the End
        "NULL Else_Start_0", // Else Start
        "LDC 3", "LDC 4", "ADD", "STR -1", // Else Block
        "NULL Else_End_0" // Else Start
    ));
  }

  private void assertAssembly(GenerateCode generator, List<String> code) throws CodeGeneratorException {
    List<String> generatorResult = generator.generate();
    assertEquals(String.format("Should Return %d Lines", code.size()), code.size(), generatorResult.size());
    for (int i = 0; i < code.size(); i++) {
      if (isDebug) {
        System.out.println(generatorResult.get(i));
      }
      assertEquals("Should be Equals", code.get(i), generatorResult.get(i));
    }
  }

  @SuppressWarnings("deprecation")
  private void buildContext(GenerateCode generator, List<String> expression) {
    Token token;
    for (int i = 0; i < expression.size(); i++) {
      token = new Token_Impl(expression.get(i));
      generator.addToken(token);
    }
  }
}
