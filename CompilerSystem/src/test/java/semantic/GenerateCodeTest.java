/**
 *
 */
package semantic;

import static core.generator.GeneratorTypes.Assignment;
import static core.generator.GeneratorTypes.Function;
import static core.generator.GeneratorTypes.If;
import static core.generator.GeneratorTypes.Procedure;
import static core.generator.GeneratorTypes.Read;
import static core.generator.GeneratorTypes.Variable;
import static core.generator.GeneratorTypes.While;
import static core.generator.GeneratorTypes.Write;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
    isDebug = false;
    table = SymbolTable_Impl.getInstance();
    counters = GlobalCounter_Impl.getInstance();
    if (isDebug) {
      System.out.println("========================================");
    }
  }

  @After
  public void cleanUp() {
    table.purgeList();
    counters.clear();
  }

  @SuppressWarnings("deprecation")
  @Test
  public void readTest() throws CodeGeneratorException {
    //  Context
    table.addSymbol(new Token_Impl("ProgramRead"), Scope.Program, Type.Inteiro);
    table.addSymbol(new Token_Impl("var1"), Scope.Variable, Type.Inteiro);

    generator = Read.getGenerator();
    buildContext(generator, Arrays.asList("var1"));
    assertAssembly(generator, Arrays.asList("RD", "STR 0"));
  }

  @SuppressWarnings("deprecation")
  @Test
  public void writeTest() throws CodeGeneratorException {
    //  Context
    table.addSymbol(new Token_Impl("ProgramWrite"), Scope.Program, Type.Inteiro);
    table.addSymbol(new Token_Impl("var1"), Scope.Variable, Type.Inteiro);

    generator = Write.getGenerator();
    buildContext(generator, Arrays.asList("var1"));
    assertAssembly(generator, Arrays.asList("LDV 0", "PRN"));
  }

  @SuppressWarnings("deprecation")
  @Test
  public void assignmentTest() throws CodeGeneratorException {
    //  Context
    table.addSymbol(new Token_Impl("ProgramAssignment"), Scope.Program, Type.Inteiro);
    table.addSymbol(new Token_Impl("var1"), Scope.Variable, Type.Inteiro);

    generator = Assignment.getGenerator();
    buildContext(generator, Arrays.asList("var1", ":=", "1", "+", "2"));
    assertAssembly(generator, Arrays.asList("LDC 1", "LDC 2", "ADD", "STR 0"));
  }

  @SuppressWarnings("deprecation")
  @Test
  public void assignmentResultTypetTest() {
    //  Context
    table.addSymbol(new Token_Impl("ProgramAssignmentResultType"), Scope.Program, Type.Inteiro);
    table.addSymbol(new Token_Impl("var1"), Scope.Variable, Type.Inteiro);

    generator = Assignment.getGenerator();
    try {
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

    //  Context
    table.addSymbol(new Token_Impl("ProgramIf"), Scope.Program, Type.Inteiro);
    table.addSymbol(new Token_Impl("var1"), Scope.Variable, Type.Inteiro);

    //  If Block
    generator = Assignment.getGenerator();
    buildContext(generator, Arrays.asList("var1", ":=", "1", "+", "2"));
    ifBlockResult = generator.generate();

    //  If
    generator = If.getGenerator();
    buildContext(generator, Arrays.asList("3", "=", "4"));
    generator.addBlock(ifBlockResult);
    assertAssembly(generator, Arrays.asList(//
        "LDC 3", "LDC 4", "CEQ",  //  PostFix 
        "JMPF Else_Start_0", //  If Decision
        "LDC 1", "LDC 2", "ADD", "STR 0", // If Block
        "NULL Else_Start_0" // Else Start
        ));
  }

  @SuppressWarnings("deprecation")
  @Test
  public void ifElseTest() throws CodeGeneratorException {
    List<String> ifBlockResult;
    List<String> elseBlockResult;

    //  Context
    table.addSymbol(new Token_Impl("ProgramIfElse"), Scope.Program, Type.Inteiro);
    table.addSymbol(new Token_Impl("var1"), Scope.Variable, Type.Inteiro);
    table.addSymbol(new Token_Impl("var2"), Scope.Variable, Type.Booleano);

    //  If Block
    generator = Assignment.getGenerator();
    buildContext(generator, Arrays.asList("var1", ":=", "1", "+", "2"));
    ifBlockResult = generator.generate();

    //  else Block
    generator = Assignment.getGenerator();
    buildContext(generator, Arrays.asList("var2", ":=", "falso", "e", "falso"));
    elseBlockResult = generator.generate();

    //  If and Else
    generator = If.getGenerator();
    buildContext(generator, Arrays.asList("5", "=", "6"));
    generator.addBlock(ifBlockResult);
    generator.addBlock(elseBlockResult);
    assertAssembly(generator, Arrays.asList(//
        "LDC 5", "LDC 6", "CEQ", //  PostFix 
        "JMPF Else_Start_0", //  If Decision
        "LDC 1", "LDC 2", "ADD", "STR 0", // If Block
        "JMP Else_End_0", // If Block, Jump to the End
        "NULL Else_Start_0", // Else Start
        "LDC 0", "LDC 0", "AND", "STR 1", // Else Block
        "NULL Else_End_0" // Else Start
    ));
  }

  @SuppressWarnings("deprecation")
  @Test
  public void whileTest() throws CodeGeneratorException {
    List<String> blockResult;

    //  Context
    table.addSymbol(new Token_Impl("ProgramWhile"), Scope.Program, Type.Inteiro);
    table.addSymbol(new Token_Impl("var1"), Scope.Variable, Type.Inteiro);

    //  Block
    generator = Assignment.getGenerator();
    buildContext(generator, Arrays.asList("var1", ":=", "1", "+", "2"));
    blockResult = generator.generate();

    //  While
    generator = While.getGenerator();
    buildContext(generator, Arrays.asList("3", "=", "4"));
    generator.addBlock(blockResult);
    assertAssembly(generator, Arrays.asList(//
        "NULL While_Start_0", // While Start
        "LDC 3", "LDC 4", "CEQ", //  PostFix 
        "NEG", "JMPF While_End_0", //  If Decision
        "LDC 1", "LDC 2", "ADD", "STR 0", // Block
        "JMP While_Start_0", // Jump Start
        "NULL While_End_0" // End Label
    ));
  }

  @SuppressWarnings("deprecation")
  @Test
  public void procedureTest() throws CodeGeneratorException {
    List<String> blockResult;

    //  Context
    table.addSymbol(new Token_Impl("Procedure"), Scope.Procedure);
    table.addSymbol(new Token_Impl("var1"), Scope.Variable, Type.Inteiro);

    //  Block
    generator = Assignment.getGenerator();
    buildContext(generator, Arrays.asList("var1", ":=", "1", "+", "2"));
    blockResult = generator.generate();

    //  Procedure
    generator = Procedure.getGenerator();
    buildContext(generator, Arrays.asList("Procedure"));
    generator.addBlock(blockResult);
    assertAssembly(generator, Arrays.asList(//
        "NULL Procedure_0", // Procedure Label 
        "LDC 1", "LDC 2", "ADD", "STR 0", // Block
        "RETURN" // Return
    ));
  }

  @SuppressWarnings("deprecation")
  @Test
  public void variableTest() throws CodeGeneratorException { //  TODO
    List<String> blockResult;

    //  Var Inits
    table.addSymbol(new Token_Impl("Program"), Scope.Program);
    table.addSymbol(new Token_Impl("int_1"), Scope.Variable, Type.Inteiro);
    table.addSymbol(new Token_Impl("int_2"), Scope.Variable, Type.Inteiro);
    table.addSymbol(new Token_Impl("bool_1"), Scope.Variable, Type.Booleano);
    table.addSymbol(new Token_Impl("bool_2"), Scope.Variable, Type.Booleano);

    //  Block
    generator = Assignment.getGenerator();
    buildContext(generator, Arrays.asList("int_1", ":=", "1", "+", "2"));
    blockResult = generator.generate();

    //  Variable
    generator = Variable.getGenerator();
    buildContext(generator, Arrays.asList("Program"));
    generator.addBlock(blockResult);
    assertAssembly(generator, Arrays.asList(//
        "ALLOC 0 4", //  Alloc Vars
        "LDC 1", "LDC 2", "ADD", "STR 0", // Block
        "DALLOC 0 4" //  Dalloc Boolean
    ));
  }

  @SuppressWarnings("deprecation")
  @Test
  @Ignore
  public void functionTest() throws CodeGeneratorException {
    List<String> blockResult;

    //  Context
    table.addSymbol(new Token_Impl("Function"), Scope.Function);
    table.addSymbol(new Token_Impl("int_1"), Scope.Variable, Type.Inteiro);
    table.addSymbol(new Token_Impl("int_2"), Scope.Variable, Type.Inteiro);
    table.addSymbol(new Token_Impl("bool_1"), Scope.Variable, Type.Booleano);
    table.addSymbol(new Token_Impl("bool_2"), Scope.Variable, Type.Booleano);

    //  Block
    generator = Assignment.getGenerator();
    buildContext(generator, Arrays.asList("int_1", ":=", "1", "+", "2"));
    blockResult = generator.generate();

    //  Function
    generator = Function.getGenerator();
    buildContext(generator, Arrays.asList("Function", "int_1", "int_2", "bool_1", "bool_2"));
    generator.addBlock(blockResult);
    assertAssembly(generator, Arrays.asList(//
        "NULL Function_0", // Procedure Label
        "ALLOC 0 4", // Alloc Vars
        "LDC 1", "LDC 2", "ADD", "STR 0", // Block
        "RETURNF 0 4" // ReturnF
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
  private void buildContext(GenerateCode generator, List<String> expression) throws CodeGeneratorException {
    Token token;
    for (int i = 0; i < expression.size(); i++) {
      token = new Token_Impl(expression.get(i));
      generator.addToken(token);
    }
  }
}
