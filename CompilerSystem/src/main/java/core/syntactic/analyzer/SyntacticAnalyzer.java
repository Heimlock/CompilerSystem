/**
 * 
 */
package core.syntactic.analyzer;

import static data.TokenSymbolTable.sBooleano;
import static data.TokenSymbolTable.sDoisPontos;
import static data.TokenSymbolTable.sEnquanto;
import static data.TokenSymbolTable.sEscreva;
import static data.TokenSymbolTable.sFim;
import static data.TokenSymbolTable.sIdentificador;
import static data.TokenSymbolTable.sInicio;
import static data.TokenSymbolTable.sInteiro;
import static data.TokenSymbolTable.sLeia;
import static data.TokenSymbolTable.sPonto;
import static data.TokenSymbolTable.sPonto_Virgula;
import static data.TokenSymbolTable.sPrograma;
import static data.TokenSymbolTable.sSe;
import static data.TokenSymbolTable.sVar;
import static data.TokenSymbolTable.sVirgula;

import data.impl.TokenStack_Impl;
import data.interfaces.SymbolTable;
import data.interfaces.Token;
import data.interfaces.TokenStack;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 8 de out de 2019
 * @version 1.0
 * @since 1.0
 */
public class SyntacticAnalyzer {
  private static SyntacticAnalyzer instance = null;
  private SymbolTable symbolTable;
  private TokenStack tokenStack;
  private Token token;

  public static SyntacticAnalyzer getInstance() {
    if (instance == null) {
      instance = new SyntacticAnalyzer();
    }
    return instance;
  }

  private SyntacticAnalyzer() {
    this.tokenStack = TokenStack_Impl.getInstance();
  }

  public void analyzeProgram() throws SyntacticAnalyzerException {
    nextToken();
    if (token.getSymbol() == sPrograma) {
      nextToken();
      if (token.getSymbol() == sIdentificador) {
        // TODO -- Insert Symbol Table
        nextToken();
        if (token.getSymbol() == sPonto_Virgula) {
          handleBlock();
          if (token.getSymbol() == sPonto) {
            //  is End Of Program ?
            if (tokenStack.getRemainingTokens() == 0) {
              // Success
            } else {
              throwError();
            }
          } else {
            throwError();
          }
        }
      } else {
        throwError();
      }
    } else {
      throwError();
    }
  }

  private void handleBlock() throws SyntacticAnalyzerException {
    nextToken();
    handleEtVariables();
    handleSubRotines();
    handleCommands();
  }

  private void handleEtVariables() throws SyntacticAnalyzerException {
    if (token.getSymbol() == sVar) {
      nextToken();
      if (token.getSymbol() == sIdentificador) {
        while (token.getSymbol() == sIdentificador) {
          handleVariables();
          if (token.getSymbol() == sPonto_Virgula) {
            nextToken();
          } else {
            throwError();
          }
        }
      } else {
        throwError();
      }
    } else {
      throwError();
    }
  }

  private void handleVariables() throws SyntacticAnalyzerException {
    do {
      if (token.getSymbol() == sIdentificador) {
        //  Pesquisa duplicar Tabela
        nextToken();
        if (token.getSymbol() == sVirgula || token.getSymbol() == sDoisPontos) {
          if (token.getSymbol() == sVirgula) {
            nextToken();
            if (token.getSymbol() == sDoisPontos) {
              throwError();
            }
          }
        } else {
          throwError();
        }
      } else {
        throwError();
      }
    } while (token.getSymbol() == sDoisPontos);
    nextToken();
    handleType();
  }

  private void handleType() throws SyntacticAnalyzerException {
    if (token.getSymbol() != sInteiro && token.getSymbol() != sBooleano) {
      throwError();
    } else {
      // Coloca tipo tabela
    }
    nextToken();
  }

  private void handleCommands() throws SyntacticAnalyzerException {
    if (token.getSymbol() == sInicio) {
      nextToken();
      handleSimpleCommand();
      while (token.getSymbol() != sFim) {
        if (token.getSymbol() == sPonto_Virgula) {
          nextToken();
          if (token.getSymbol() != sFim) {
            handleSimpleCommand();
          }
        } else {
          throwError();
        }
      }
    } else {
      throwError();
    }
  }

  private void handleSimpleCommand() throws SyntacticAnalyzerException {
    if (token.getSymbol() == sIdentificador) {
      handleAttProcedureCall();
    } else if (token.getSymbol() == sSe) {
      handleIf();
    } else if (token.getSymbol() == sEnquanto) {
      handleWhile();
    } else if (token.getSymbol() == sLeia) {
      handleRead();
    } else if (token.getSymbol() == sEscreva) {
      handleWrite();
    } else {
      handleCommands();
    }
  }

  private void handleAttProcedureCall() {
    //TODO
  }

  private void handleIf() {
    //TODO
  }

  private void handleWhile() {
    //TODO
  }

  private void handleRead() {
    //TODO
  }

  private void handleWrite() {
    //TODO
  }

  private void handleSubRotines() {
    //TODO
  }

  private void handleDeclareProcedure() {
    //TODO
  }

  private void handleDeclareFunction() {
    //TODO
  }

  private void handleExpression() {
    //TODO
  }

  private void handleSimpleExpression() {
    //TODO
  }

  private void handleTerm() {
    //TODO
  }

  private void handleFactor() {
    //TODO
  }

  //  Utils

  private void nextToken() {
    token = tokenStack.nextToken();
  }

  private void throwError() throws SyntacticAnalyzerException {
    throw new SyntacticAnalyzerException(String.format("Unexpected Token! Token: %s", token.toString()));
  }
}
