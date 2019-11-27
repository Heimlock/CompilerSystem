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
import static data.TokenSymbolTable.sMenor;
import static data.TokenSymbolTable.sMenorIg;
import static data.TokenSymbolTable.sMenos;
import static data.TokenSymbolTable.sMult;
import static data.TokenSymbolTable.sNao;
import static data.TokenSymbolTable.sNumero;
import static data.TokenSymbolTable.sOu;
import static data.TokenSymbolTable.sVerdadeiro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    Boolean isValue = token.getSymbol().equals(sNumero) || token.getSymbol().equals(sIdentificador) || token.getSymbol().equals(sFuncao);
    if (tokenStack.size() > 0) {
      Boolean lastValueIsUnaryCapable = isTokenUnaryCapable(tokenStack.lastElement());
      //  Trata comeco de Expressao
      if (tokenStack.size() == 1 && isValue && lastValueIsUnaryCapable) {
        token.setLexeme(String.format("%s%s", tokenStack.lastElement().getLexeme(), token.getLexeme()));
        tokenStack.pop();
      } else if (tokenStack.size() >= 2 && lastValueIsUnaryCapable && isTokenOperation(tokenStack.get(tokenStack.size() - 2))) {
        token.setLexeme(String.format("%s%s", tokenStack.lastElement().getLexeme(), token.getLexeme()));
        tokenStack.pop();
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
    for (Token token : resultList) {
      switch (token.getSymbol()) {
      case sNumero:
        load.addToken(token);
        this.resultType = Type.Inteiro;
        break;
      case sIdentificador:
        load.addToken(token);
        if (token.getLexeme().startsWith("+")) {
          token.setLexeme(token.getLexeme().substring(1));
          this.resultType = symbolTable.getSymbol(token).getType().get();
        } else if (token.getLexeme().startsWith("-")) {
          token.setLexeme(token.getLexeme().substring(1));
          this.resultType = symbolTable.getSymbol(token).getType().get();
        } else {
          this.resultType = symbolTable.getSymbol(token).getType().get();
        }
        break;
      case sFuncao:
        Integer memoryLocation = symbolTable.getProcMemoryLocation(token);
        result.add(String.format("%s %s_%d", Operations.CALL.name(), token.getLexeme(), memoryLocation));
        break;
      case sMais:
        result.addAll(load.generate());
        result.add(Operations.ADD.name());
        this.resultType = Type.Inteiro;
        break;
      case sMenos:
        result.addAll(load.generate());
        result.add(Operations.SUB.name());
        this.resultType = Type.Inteiro;
        break;
      case sMult:
        result.addAll(load.generate());
        result.add(Operations.MULT.name());
        this.resultType = Type.Inteiro;
        break;
      case sDiv:
        result.addAll(load.generate());
        result.add(Operations.DIVI.name());
        this.resultType = Type.Inteiro;
        break;
      case sNao:
        result.addAll(load.generate());
        result.add(Operations.NEG.name());
        this.resultType = Type.Booleano;
        break;
      case sMaior:
        result.addAll(load.generate());
        result.add(Operations.CMA.name());
        this.resultType = Type.Booleano;
        break;
      case sMaiorIg:
        result.addAll(load.generate());
        result.add(Operations.CMAQ.name());
        this.resultType = Type.Booleano;
        break;
      case sMenor:
        result.addAll(load.generate());
        result.add(Operations.CME.name());
        this.resultType = Type.Booleano;
        break;
      case sMenorIg:
        result.addAll(load.generate());
        result.add(Operations.CMEQ.name());
        this.resultType = Type.Booleano;
        break;
      case sE:
        result.addAll(load.generate());
        result.add(Operations.AND.name());
        this.resultType = Type.Booleano;
        break;
      case sOu:
        result.addAll(load.generate());
        result.add(Operations.OR.name());
        this.resultType = Type.Booleano;
        break;
      case sIg:
        result.addAll(load.generate());
        result.add(Operations.CEQ.name());
        this.resultType = Type.Booleano;
        break;
      case sDif:
        result.addAll(load.generate());
        result.add(Operations.CDIF.name());
        this.resultType = Type.Booleano;
        break;
      case sVerdadeiro:
        result.addAll(load.generate());
        result.add(String.format("%s %d", Operations.LDC.name(), 1));
        this.resultType = Type.Booleano;
        break;
      case sFalso:
        result.addAll(load.generate());
        result.add(String.format("%s %d", Operations.LDC.name(), 0));
        this.resultType = Type.Booleano;
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
    List<TokenSymbolTable> validSymbol = Arrays.asList(sVerdadeiro, sFalso, sAbre_Parenteses, sFecha_Parenteses, sNumero, sIdentificador, sMais, sMenos, sNao, sMult, sDiv, sMaior, sMaiorIg, sMenor, sMenorIg, sE, sOu, sIg, sDif);
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
