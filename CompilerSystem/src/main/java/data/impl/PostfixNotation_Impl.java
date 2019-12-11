/**
 *
 */
package data.impl;

import static data.TokenSymbolTable.sAbre_Parenteses;
import static data.TokenSymbolTable.sDif;
import static data.TokenSymbolTable.sDiv;
import static data.TokenSymbolTable.sE;
import static data.TokenSymbolTable.sFalso;
import static data.TokenSymbolTable.sFecha_Parenteses;
import static data.TokenSymbolTable.sFuncao;
import static data.TokenSymbolTable.sIdentificador;
import static data.TokenSymbolTable.sIg;
import static data.TokenSymbolTable.sMaior;
import static data.TokenSymbolTable.sMaiorIg;
import static data.TokenSymbolTable.sMais;
import static data.TokenSymbolTable.sMais_Unario;
import static data.TokenSymbolTable.sMenor;
import static data.TokenSymbolTable.sMenorIg;
import static data.TokenSymbolTable.sMenos;
import static data.TokenSymbolTable.sMenos_Unario;
import static data.TokenSymbolTable.sMult;
import static data.TokenSymbolTable.sNao;
import static data.TokenSymbolTable.sNumero;
import static data.TokenSymbolTable.sOu;
import static data.TokenSymbolTable.sVerdadeiro;
import static data.interfaces.Type.Booleano;
import static data.interfaces.Type.Inteiro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.logging.Logger;

import core.engine.operations.Operations;
import core.generator.CodeGeneratorException;
import core.generator.GeneratorTypes;
import core.generator.specific.LoadGenerator;
import data.TokenSymbolTable;
import data.interfaces.PostfixNotation;
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
public class PostfixNotation_Impl implements PostfixNotation {
  private Logger logger = Logger.getLogger("PostfixNotation");
  private SymbolTable symbolTable;
  private Stack<Token> tokenStack;
  private Type resultType;

  private Stack<Token> stack;
  private List<Token> resultList;

  public PostfixNotation_Impl() {
    this.tokenStack = new Stack<>();
    this.resultList = new ArrayList<>();
    this.stack = new Stack<>();
    this.symbolTable = SymbolTable_Impl.getInstance();
  }

  /*
   * (non-Javadoc)
   * @see data.interfaces.GenerateCode#addToken(data.interfaces.Token)
   */
  @Override
  public void addToken(Token token) {
    Token auxToken = null;
    Boolean isValue = token.getSymbol().equals(sNumero) || token.getSymbol().equals(sIdentificador) || token.getSymbol().equals(sFuncao) || token.getSymbol().equals(sAbre_Parenteses);
    if (tokenStack.size() > 0) {
      Boolean lastValueIsUnaryCapable = isTokenUnaryCapable(tokenStack.lastElement());
      //  Trata comeco de Expressao
      if (tokenStack.size() == 1 && isValue && lastValueIsUnaryCapable) {
        auxToken = tokenStack.pop();
        auxToken.setSymbol(token.getSymbol().equals(sMais) ? sMais_Unario : sMenos_Unario);
        tokenStack.add(auxToken);
      } else if (tokenStack.size() >= 2 && lastValueIsUnaryCapable && isTokenOperation(tokenStack.get(tokenStack.size() - 2))) {
        auxToken = tokenStack.pop();
        auxToken.setSymbol(token.getSymbol().equals(sMais) ? sMais_Unario : sMenos_Unario);
        tokenStack.add(auxToken);
      }
    }
    this.tokenStack.add(token);
  }

  /*
   * (non-Javadoc)
   * @see data.interfaces.PostfixNotation#getType()
   */
  @Override
  public Type getType() {
    return this.resultType;
  }

  /*
   * (non-Javadoc)
   * @see data.interfaces.GenerateCode#generate()
   */
  @Override
  public List<String> generate() throws CodeGeneratorException {
    List<String> result = new ArrayList<>();
    LoadGenerator load = GeneratorTypes.Load.getGenerator();

    convert();
    System.out.println();
    for (Token token : resultList) {
      switch (token.getSymbol()) {
      case sNumero:
        if (Optional.ofNullable(this.resultType).isPresent()) {
          verifyCompatibility(resultType.equals(Inteiro), token.getSymbol());
        } else {
          this.resultType = Inteiro;
        }
        load.addToken(token);
        result.addAll(load.generate());
        load.clear();
        this.resultType = Inteiro;
        break;
      case sIdentificador:
        if (Optional.ofNullable(this.resultType).isPresent()) {
          verifyCompatibility(resultType.equals(symbolTable.getSymbol(token).getType().get()), token.getSymbol());
        } else {
          this.resultType = symbolTable.getSymbol(token).getType().get();
        }
        load.addToken(token);
        result.addAll(load.generate());
        load.clear();
        this.resultType = symbolTable.getSymbol(token).getType().get();
        break;
      case sFuncao:
        if (Optional.ofNullable(this.resultType).isPresent()) {
          verifyCompatibility(resultType.equals(symbolTable.getSymbol(token).getType().get()), token.getSymbol());
        } else {
          this.resultType = symbolTable.getSymbol(token).getType().get();
        }
        Integer memoryLocation = symbolTable.getProcMemoryLocation(token);
        result.add(String.format("%s %s_%d", Operations.CALL.name(), token.getLexeme(), memoryLocation));
        break;
      case sMais:
        verifyCompatibility(resultType.equals(Inteiro), token.getSymbol());

        result.addAll(load.generate());
        load.clear();
        result.add(Operations.ADD.name());
        this.resultType = Inteiro;
        break;
      case sMais_Unario:
        if (Optional.ofNullable(this.resultType).isPresent()) {
          verifyCompatibility(resultType.equals(Inteiro), token.getSymbol());
        } else {
          this.resultType = Inteiro;
        }
        this.resultType = Inteiro;
        break;
      case sMenos:
        verifyCompatibility(resultType.equals(Inteiro), token.getSymbol());
        result.addAll(load.generate());
        load.clear();
        result.add(Operations.SUB.name());
        this.resultType = Inteiro;
        break;
      case sMenos_Unario:
        if (Optional.ofNullable(this.resultType).isPresent()) {
          verifyCompatibility(resultType.equals(Inteiro), token.getSymbol());
        } else {
          this.resultType = Inteiro;
        }
        result.add(Operations.INV.name());
        this.resultType = Inteiro;
        break;
      case sMult:
        verifyCompatibility(resultType.equals(Inteiro), token.getSymbol());
        result.addAll(load.generate());
        load.clear();
        result.add(Operations.MULT.name());
        this.resultType = Inteiro;
        break;
      case sDiv:
        verifyCompatibility(resultType.equals(Inteiro), token.getSymbol());
        result.addAll(load.generate());
        load.clear();
        result.add(Operations.DIVI.name());
        this.resultType = Inteiro;
        break;
      case sNao:
        if (Optional.ofNullable(this.resultType).isPresent()) {
          verifyCompatibility(resultType.equals(Booleano), token.getSymbol());
        } else {
          this.resultType = Booleano;
        }
        result.addAll(load.generate());
        load.clear();
        result.add(Operations.NEG.name());
        this.resultType = Booleano;
        break;
      case sMaior:
        verifyCompatibility(resultType.equals(Inteiro), token.getSymbol());
        result.addAll(load.generate());
        load.clear();
        result.add(Operations.CMA.name());
        this.resultType = Booleano;
        break;
      case sMaiorIg:
        verifyCompatibility(resultType.equals(Inteiro), token.getSymbol());
        result.addAll(load.generate());
        load.clear();
        result.add(Operations.CMAQ.name());
        this.resultType = Booleano;
        break;
      case sMenor:
        verifyCompatibility(resultType.equals(Inteiro), token.getSymbol());
        result.addAll(load.generate());
        load.clear();
        result.add(Operations.CME.name());
        this.resultType = Booleano;
        break;
      case sMenorIg:
        verifyCompatibility(resultType.equals(Inteiro), token.getSymbol());
        result.addAll(load.generate());
        load.clear();
        result.add(Operations.CMEQ.name());
        this.resultType = Booleano;
        break;
      case sE:
        verifyCompatibility(resultType.equals(Booleano), token.getSymbol());
        result.addAll(load.generate());
        load.clear();
        result.add(Operations.AND.name());
        this.resultType = Booleano;
        break;
      case sOu:
        verifyCompatibility(resultType.equals(Booleano), token.getSymbol());
        result.addAll(load.generate());
        load.clear();
        result.add(Operations.OR.name());
        this.resultType = Booleano;
        break;
      case sIg:
        verifyCompatibility(resultType.equals(Inteiro), token.getSymbol());
        result.addAll(load.generate());
        load.clear();
        result.add(Operations.CEQ.name());
        this.resultType = Booleano;
        break;
      case sDif:
        verifyCompatibility(resultType.equals(Inteiro), token.getSymbol());
        result.addAll(load.generate());
        load.clear();
        result.add(Operations.CDIF.name());
        this.resultType = Booleano;
        break;
      case sVerdadeiro:
        if (Optional.ofNullable(this.resultType).isPresent()) {
          verifyCompatibility(resultType.equals(Booleano), token.getSymbol());
        } else {
          this.resultType = Booleano;
        }
        result.addAll(load.generate());
        load.clear();
        result.add(String.format("%s %d", Operations.LDC.name(), 1));
        this.resultType = Booleano;
        break;
      case sFalso:
        if (Optional.ofNullable(this.resultType).isPresent()) {
          verifyCompatibility(resultType.equals(Booleano), token.getSymbol());
        } else {
          this.resultType = Booleano;
        }
        result.addAll(load.generate());
        load.clear();
        result.add(String.format("%s %d", Operations.LDC.name(), 0));
        this.resultType = Booleano;
        break;
      default:
        logger.severe(String.format("Unexpected Token. Token = %s", token.toString()));
        throw new CodeGeneratorException(String.format("Unexpected Token. Token = %s", token.toString()));
      }
    }
    return Collections.unmodifiableList(result);
  }

  /*
   * (non-Javadoc)
   * @see data.interfaces.PostfixNotation#convert()
   */
  @Override
  public List<Token> convert() throws CodeGeneratorException {
    for (Token token : tokenStack) {
      if (canHandle(token)) {
        handleToken(token);
      } else {
        logger.severe(String.format("Unexpected Token. Token = %s", token.toString()));
        throw new CodeGeneratorException(String.format("Unexpected Token, Token: %s", token.toString()));
      }
    }
    while (stack.size() > 0) {
      resultList.add(stack.pop());
    }
    return resultList;
  }

  private void handleToken(Token token) throws CodeGeneratorException {
    if (token.getSymbol().equals(sNumero) || token.getSymbol().equals(sIdentificador)) {
      resultList.add(token);
    } else {
      if (token.getSymbol().equals(sFecha_Parenteses)) {
        //  Desempilha Tudo ate achar (
        while (stack.lastElement().getSymbol() != sAbre_Parenteses) {
          resultList.add(stack.pop());
        }
        //  Consome (
        stack.pop();
      } else if (canStack(token.getSymbol())) {
        stack.add(token);
      } else {
        //  Desempilha ate achar um Token com menor precedencia
        while (!canStack(token.getSymbol())) {
          resultList.add(stack.pop());
        }
        //  Adiciona Token
        stack.add(token);
      }
    }
  }

  /**
   * @param token
   * @param symbol
   * @return
   */
  private Boolean canStack(TokenSymbolTable newSymbol) {
    Boolean result = null;
    if(newSymbol.equals(sAbre_Parenteses) || stack.size() == 0) {
      result = true;
    } else if (stack.lastElement().getSymbol().getPrecedency() < newSymbol.getPrecedency()) {
      result = true;
    } else {
      result = false;
    }
    return result;
  }

  private Boolean canHandle(Token token) {
    List<TokenSymbolTable> validSymbol = Arrays.asList(sVerdadeiro, sFalso, sAbre_Parenteses, sFecha_Parenteses, sNumero, sIdentificador, sMais, sMenos, sMais_Unario, sMenos_Unario, sNao, sMult, sDiv, sMaior, sMaiorIg, sMenor, sMenorIg, sE, sOu, sIg, sDif);
    return validSymbol.contains(token.getSymbol());
  }

  private Boolean isTokenUnaryCapable(Token token) {
    List<TokenSymbolTable> unarySymbol = Arrays.asList(sMais, sMenos);
    return unarySymbol.contains(token.getSymbol());
  }

  private Boolean isTokenOperation(Token token) {
    List<TokenSymbolTable> operationSymbol = Arrays.asList(sMais, sMenos, sNao, sMult, sDiv, sMaior, sMaiorIg, sMenor, sMenorIg, sE, sOu, sIg, sDif);
    return operationSymbol.contains(token.getSymbol());
  }

  private void verifyCompatibility(Boolean condition, TokenSymbolTable symbol) throws CodeGeneratorException {
    if (!condition) {
      String message = String.format("Incompatible Type! Last Type %s Operation %s; Line %d, PostFix == '%s'", resultType.name(), symbol.name(), tokenStack.lastElement().getLineIndex(), toString());
      logger.severe(message);
      throw new CodeGeneratorException(message);
    }
  }

  @Override
  public String toString() {
    String result = "";
    for (Token line : resultList) {
      result = String.format("%s%s ", result, line.getLexeme());
    }
    return result;
  }

  @Override
  public void clear() {
    tokenStack.clear();
    resultType = null;
    stack.clear();
    resultList.clear();
  }
}
