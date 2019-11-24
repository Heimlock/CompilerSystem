/**
 * 
 */
package core.syntactic.analyzer;

import static data.TokenSymbolTable.sAbre_Parenteses;
import static data.TokenSymbolTable.sAtribuicao;
import static data.TokenSymbolTable.sBooleano;
import static data.TokenSymbolTable.sDif;
import static data.TokenSymbolTable.sDiv;
import static data.TokenSymbolTable.sDoisPontos;
import static data.TokenSymbolTable.sE;
import static data.TokenSymbolTable.sEnquanto;
import static data.TokenSymbolTable.sEntao;
import static data.TokenSymbolTable.sEscreva;
import static data.TokenSymbolTable.sFaca;
import static data.TokenSymbolTable.sFecha_Parenteses;
import static data.TokenSymbolTable.sFim;
import static data.TokenSymbolTable.sFuncao;
import static data.TokenSymbolTable.sIdentificador;
import static data.TokenSymbolTable.sIg;
import static data.TokenSymbolTable.sInicio;
import static data.TokenSymbolTable.sInteiro;
import static data.TokenSymbolTable.sLeia;
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
import static data.TokenSymbolTable.sPonto;
import static data.TokenSymbolTable.sPonto_Virgula;
import static data.TokenSymbolTable.sProcedimento;
import static data.TokenSymbolTable.sPrograma;
import static data.TokenSymbolTable.sSe;
import static data.TokenSymbolTable.sSenao;
import static data.TokenSymbolTable.sVar;
import static data.TokenSymbolTable.sVirgula;

import java.io.EOFException;

import core.lexical.parser.LexicalParser;
import core.lexical.parser.LexicalParserException;
import data.impl.SymbolTable_Impl;
import data.impl.TokenStack_Impl;
import data.interfaces.Scope;
import data.interfaces.SymbolTable;
import data.interfaces.Token;
import data.interfaces.TokenStack;
import data.interfaces.Type;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 8 de out de 2019
 * @version 1.0
 * @since 1.0
 */
public class SyntaticAnalyzer {
  private static SyntaticAnalyzer instance = null;
  private SymbolTable symbolTable;
  private TokenStack tokenStack;
  private LexicalParser parser;
  private Token token;
  private Token bufferToken;

  public static SyntaticAnalyzer getInstance(LexicalParser parser) {
    if (instance == null) {
      instance = new SyntaticAnalyzer(parser);
    }
    return instance;
  }

  private SyntaticAnalyzer(LexicalParser parser) {
    this.tokenStack = TokenStack_Impl.getInstance();
    this.symbolTable = SymbolTable_Impl.getInstance();
    this.parser = parser;
  }

  public void analyzeProgram() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    nextToken();
    if (token.getSymbol() == sPrograma) {
      nextToken();
      if (token.getSymbol() == sIdentificador) {
        // TODO -- Insert Symbol Table
        this.symbolTable.addSymbol(token, Scope.Program);
        nextToken();
        if (token.getSymbol() == sPonto_Virgula) {
          handleBlock();
          if (token.getSymbol() == sPonto) {
            //  is End Of Program ?
            //            if (parser.getRemainingLines() == 0) {
            //              // Success
            //            } else {
            //              throwError("analyzeProgram", "Not EOF");
            //            }
          } else {
            throwError("analyzeProgram", "Ponto");
          }
        }
      } else {
        throwError("analyzeProgram", "Identificador");
      }
    } else {
      throwError("analyzeProgram", "Programa");
    }
  }

  private void handleBlock() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    nextToken();
    handleEtVariables();
    handleSubRotines();
    handleCommands();
  }

  private void handleEtVariables() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    if (token.getSymbol() == sVar) {
      nextToken();
      if (token.getSymbol() == sIdentificador) {
        while (token.getSymbol() == sIdentificador) {
          handleVariables();
          if (token.getSymbol() == sPonto_Virgula) {
            nextToken();
          } else {
            throwError("handleEtVariables", "Ponto e Virgula");
          }
        }
      } else {
        throwError("handleEtVariables", "Identificador");
      }
    }
  }

  //  ALTERADO DA APOSTILA
  private void handleVariables() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    do {
      if (token.getSymbol() == sIdentificador) {
        //  Pesquisa duplicar Tabela
        if (!this.symbolTable.hasSymbol(token, Scope.Variable)) {
          this.symbolTable.addSymbol(token, Scope.Variable);
          nextToken();
          if (token.getSymbol() == sVirgula || token.getSymbol() == sDoisPontos) {
            if (token.getSymbol() == sVirgula) {
              nextToken();
              if (token.getSymbol() == sDoisPontos) {
                throwError("handleVariables", "Dois Pontos");
              }
            }
          } else {
            throwError("handleVariables", "Virgula ou Dois Pontos");
          }
        } else {
          throwError("handleVariables", "Variavel Duplicada");
        }
      } else {
        throwError("handleVariables", "Identificador");
      }
    } while (token.getSymbol() != sDoisPontos);
    nextToken();
    handleType();
  }

  private void handleType() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    if (token.getSymbol() != sInteiro && token.getSymbol() != sBooleano) {
      throwError("handleType", "~Inteiro && ~Booleano");
    } else {
      if (token.getSymbol() == sInteiro || token.getSymbol() == sBooleano) {
        this.symbolTable.addType(token);
      } else {
        throwError("handleType", "~Inteiro && ~Booleano");
      }
    }
    nextToken();
  }

  //  ALTERADO DA APOSTILA
  private void handleCommands() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    if (token.getSymbol() == sInicio) {
      nextToken();
      handleSimpleCommand();
      while (token.getSymbol() != sFim) {
        //  TODO -- Comando nao exige um ';' no final
        if (token.getSymbol() == sPonto_Virgula) {
          nextToken();
          if (token.getSymbol() != sFim) {
            handleSimpleCommand();
          }
        } else {
          throwError("handleCommands", "Ponto e Virgula");
        }
      }
      nextToken(); //  Consome o sFim
    } else {
      throwError("handleCommands", "Inicio");
    }
  }

  private void handleSimpleCommand() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
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

  private void handleAttProcedureCall() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    bufferToken = token; //  Store if it's a Procedure
    nextToken();
    if (token.getSymbol() == sAtribuicao) {
      handleAssignment();
    } else {
      callProcedure();
    }
  }

  private void handleAssignment() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    //  TODO -- Verifica se eh var e se existe
    nextToken();
    handleExpression();
    //  TODO -- Tipo Pos-Fixa == Tipo Var
  }

  private void handleIf() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    nextToken();
    handleExpression();
    if (token.getSymbol() == sEntao) {
      nextToken();
      handleSimpleCommand();
      if (token.getSymbol() == sSenao) {
        nextToken();
        handleSimpleCommand();
      }
    } else {
      throwError("handleIf", "Entao");
    }
  }

  private void handleWhile() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    // Gerador de Codigo
    nextToken();
    handleExpression();
    if (token.getSymbol() == sFaca) {
      // Gerador de Codigo
      nextToken();
      handleSimpleCommand();
      // Gerador de Codigo
    } else {
      throwError("handleWhile", "Faca");
    }
  }

  private void handleRead() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    nextToken();
    if (token.getSymbol() == sAbre_Parenteses) {
      nextToken();
      if (token.getSymbol() == sIdentificador) {
        if (true) { // TODO -- Pesquisa Declarar Variavel Tabela
          nextToken();
          if (token.getSymbol() == sFecha_Parenteses) {
            nextToken();
          } else {
            throwError("handleRead", "Fecha Parenteses");
          }
        } else {
          throwError("handleRead", "Pesquisa Declarar Variavel Tabela");
        }
      } else {
        throwError("handleRead", "Identificador");
      }
    } else {
      throwError("handleRead", "Abre Parenteses");
    }
  }

  private void handleWrite() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    nextToken();
    if (token.getSymbol() == sAbre_Parenteses) {
      nextToken();
      if (token.getSymbol() == sIdentificador) {
        if (true) {// TODO -- Pesquisa Declaracao de Funcao Tabela
          nextToken();
          if (token.getSymbol() == sFecha_Parenteses) {
            nextToken();
          } else {
            throwError("handleWrite", "Fecha Parenteses");
          }
        } else {
          throwError("handleWrite", "Pesquisa Declaracao de Funcao na Tabela");
        }
      } else {
        throwError("handleWrite", "Identificador");
      }
    } else {
      throwError("handleWrite", "Abre Parenteses");
    }
  }

  private void handleSubRotines() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    if (token.getSymbol() == sProcedimento || token.getSymbol() == sFuncao) {
      // Gera Código
    }
    while (token.getSymbol() == sProcedimento || token.getSymbol() == sFuncao) {
      if (token.getSymbol() == sProcedimento) {
        handleDeclareProcedure();
      } else {
        handleDeclareFunction();
      }
      if (token.getSymbol() == sPonto_Virgula) {
        nextToken();
      } else {
        throwError("handleSubRotines", "Ponto Virgula");
      }
    }
    // Gera Código
  }

  private void callProcedure() throws SyntaticAnalyzerException {
    //TODO Verify Symbol Table for Occurences
    if (this.symbolTable.hasSymbol(bufferToken)) {
      //  TODO -- Semantico
    } else {
      throwError("callProcedure", "Procedimento Desconhecido");
    }
  }

  private void handleDeclareProcedure() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    nextToken();
    if (token.getSymbol() == sIdentificador) {
      //FIXME -- Insere na Tabela de Simbolos
      this.symbolTable.addSymbol(token, Scope.Procedure);
      nextToken();
      if (token.getSymbol() == sPonto_Virgula) {
        handleBlock();
      } else {
        throwError("handleDeclareProcedure", "Ponto Virgula");
      }
    } else {
      throwError("handleDeclareProcedure", "Identificador");
    }
  }

  private void handleFunctionCall() throws SyntaticAnalyzerException {
    //TODO Verify Symbol Table for Occurences
    if (this.symbolTable.hasSymbol(token)) {
      //  TODO -- Semantico
    } else {
      throwError("handleFunctionCall", "Funcao Desconhecido");
    }
  }

  private void handleDeclareFunction() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    Token id;
    Token type;

    nextToken();
    if (token.getSymbol() == sIdentificador) {
      id = token;
      nextToken();
      if (token.getSymbol() == sInteiro || token.getSymbol() == sBooleano) {
        type = token;
        //FIXME -- Insere na Tabela de Simbolos
        this.symbolTable.addSymbol(id, Scope.Function, Type.getType(type.getLexeme()));
        nextToken();
        if (token.getSymbol() == sPonto_Virgula) {
          handleBlock();
        } else {
          throwError("handleDeclareFunction", "Ponto Virgula");
        }
      } else {
        throwError("handleDeclareFunction", "Ponto Virgula");
      }
    } else {
      throwError("handleDeclareFunction", "Identificador");
    }
  }

  private void handleExpression() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    handleSimpleExpression();
    if (token.getSymbol() == sMaior || token.getSymbol() == sMaiorIg || token.getSymbol() == sIg || //
        token.getSymbol() == sMenor || token.getSymbol() == sMenorIg || token.getSymbol() == sDif) {
      nextToken();
      handleSimpleExpression();
    }
  }

  //  ALTERADO DA APOSTILA
  private void handleSimpleExpression() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    if (token.getSymbol() == sMais || token.getSymbol() == sMenos) {
      nextToken();
    }
    handleTerm();

    while (token.getSymbol() == sMais || token.getSymbol() == sMenos || token.getSymbol() == sOu) {
      nextToken();
      handleTerm();
    }
  }

  private void handleTerm() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    handleFactor();
    while (token.getSymbol() == sMult || token.getSymbol() == sDiv || token.getSymbol() == sE) {
      nextToken();
      handleFactor();
    }
  }

  private void handleFactor() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    if (token.getSymbol() == sIdentificador) {
      if (this.symbolTable.hasSymbol(token)) {
        if (this.symbolTable.getSymbol(token).getScope() == Scope.Function) {
          handleFunctionCall();
        } else {
          nextToken();
        }
      } else {
        nextToken();
        //        throwError("handleFactor", "Identificador Desconhecido"); //FIXME -- Acionar no Semantico
      }
    } else if (token.getSymbol() == sNumero) {
      nextToken();
    } else if (token.getSymbol() == sNao) {
      nextToken();
      handleFactor();
    } else if (token.getSymbol() == sAbre_Parenteses) {
      nextToken();
      handleExpression();
      if (token.getSymbol() == sFecha_Parenteses) {
        nextToken();
      } else {
        throwError("handleFactor", "Fecha Parenteses");
      }
    } else if (token.getLexeme().equals("verdadeiro") || token.getLexeme().equals("falso")) {
      nextToken();
    } else {
      throwError("handleFactor", "Verdadeiro ou Falso");
    }
  }

  //  Utils

  private void nextToken() throws EOFException, LexicalParserException {
    token = parser.getNextToken();
    tokenStack.addToken(token);
  }

  private void throwError(String functionName, String context) throws SyntaticAnalyzerException {
    throw new SyntaticAnalyzerException(String.format("[%s] Unexpected Token! Token: %s, Context: %s", functionName, token.toString(), context));
  }
}
