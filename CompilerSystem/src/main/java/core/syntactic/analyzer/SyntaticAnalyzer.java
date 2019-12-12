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
import java.util.ArrayList;
import java.util.List;

import core.engine.operations.Operations;
import core.generator.CodeGenerator;
import core.generator.CodeGeneratorException;
import core.generator.GeneratorTypes;
import core.generator.specific.AssignmentGenerator;
import core.generator.specific.FunctionGenerator;
import core.generator.specific.FunctionReturnGenerator;
import core.generator.specific.IfGenerator;
import core.generator.specific.ProcedureGenerator;
import core.generator.specific.ReadGenerator;
import core.generator.specific.VariableGenerator;
import core.generator.specific.WhileGenerator;
import core.generator.specific.WriteGenerator;
import core.lexical.parser.LexicalParser;
import core.lexical.parser.LexicalParserException;
import data.impl.GlobalCounter_Impl;
import data.impl.SymbolTable_Impl;
import data.impl.TokenStack_Impl;
import data.interfaces.GlobalCounter;
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
  private GlobalCounter counter;
  private LexicalParser parser;
  private Token token;
  private Token bufferToken;
  private Token lastProcedure;

  //  Code Generator
  private CodeGenerator resultProgram;

  public static SyntaticAnalyzer getInstance(LexicalParser parser) {
    if (instance == null) {
      instance = new SyntaticAnalyzer(parser);
    }
    return instance;
  }

  private SyntaticAnalyzer(LexicalParser parser) {
    this.tokenStack = TokenStack_Impl.getInstance();
    this.symbolTable = SymbolTable_Impl.getInstance();
    this.counter = GlobalCounter_Impl.getInstance();
    this.parser = parser;
    this.resultProgram = CodeGenerator.getInstance();
  }

  public void analyzeProgram() throws SyntaticAnalyzerException, EOFException, LexicalParserException, CodeGeneratorException {
    nextToken();
    if (token.getSymbol() == sPrograma) {
      resultProgram.addProgramLine(String.format("%s", Operations.START.name()));
      nextToken();
      if (token.getSymbol() == sIdentificador) {
        this.symbolTable.addSymbol(token, Scope.Program);
        resultProgram.addProgramLine(String.format("NULL %s", token.getLexeme()));
        lastProcedure = token; // Program Name
        nextToken();
        if (token.getSymbol() == sPonto_Virgula) {
          resultProgram.addProgramLines(handleBlock());
          this.symbolTable.removeUntil(Scope.Program); //  TODO Remove da Tabela de Simbolos
          if (token.getSymbol() == sPonto) {
            //            // is End Of Program ?
            //            if (parser.getRemainingLines() == 0) {
            //              // Success
            //            } else {
            //              throwError("analyzeProgram", token, "Not EOF");
            //            }
          } else {
            throwError("analyzeProgram", token, "Ponto");
          }
        }
      } else {
        throwError("analyzeProgram", token, "Identificador");
      }
      resultProgram.addProgramLine(String.format("%s", Operations.HLT.name()));
    } else {
      throwError("analyzeProgram", token, "Programa");
    }
  }

  private List<String> handleBlock() throws SyntaticAnalyzerException, EOFException, LexicalParserException, CodeGeneratorException {
    List<String> resultSubrotines;
    List<String> resultCommands;
    VariableGenerator varGenerator;
    String BLOCK_LABEL = String.format("BLOCK_%d", counter.postIncrement("BLOCK"));
    Token localProcedure = lastProcedure; // Last Procedure

    /*
     * Generate Code
     */
    nextToken();
    varGenerator = handleEtVariables();
    varGenerator.addToken(localProcedure);
    resultSubrotines = handleSubRotines();
    resultCommands = handleCommands();

    /*
     * Ajust Block Call
     */
    resultSubrotines.add(0, String.format("%s %s", Operations.JMP, BLOCK_LABEL));
    resultSubrotines.add(String.format("%s %s", Operations.NULL, BLOCK_LABEL));

    /*
     * Sum it Up
     */
    varGenerator.addBlock(resultSubrotines);
    varGenerator.addBlock(resultCommands);
    return varGenerator.generate();
  }

  private VariableGenerator handleEtVariables() throws SyntaticAnalyzerException, EOFException, LexicalParserException, CodeGeneratorException {
    List<Token> resultVars;
    VariableGenerator varGenerator = (VariableGenerator) GeneratorTypes.Variable.getGenerator();
    if (token.getSymbol() == sVar) {
      nextToken();
      if (token.getSymbol() == sIdentificador) {
        while (token.getSymbol() == sIdentificador) {
          resultVars = handleVariables();
          varGenerator.addToken(resultVars);
          if (token.getSymbol() == sPonto_Virgula) {
            nextToken();
          } else {
            throwError("handleEtVariables", token, "Ponto e Virgula");
          }
        }
      } else {
        throwError("handleEtVariables", token, "Identificador");
      }
    }
    return varGenerator;
  }

  //  ALTERADO DA APOSTILA
  private List<Token> handleVariables() throws SyntaticAnalyzerException, EOFException, LexicalParserException, CodeGeneratorException {
    List<Token> varTokens = new ArrayList<>();
    do {
      if (token.getSymbol() == sIdentificador) {
        //  Pesquisa duplicar Tabela
        if (!this.symbolTable.duplicatedVariable(token, lastProcedure)) {
          varTokens.add(token);
          this.symbolTable.addSymbol(token, Scope.Variable);
          nextToken();
          if (token.getSymbol() == sVirgula || token.getSymbol() == sDoisPontos) {
            if (token.getSymbol() == sVirgula) {
              nextToken();
              if (token.getSymbol() == sDoisPontos) {
                throwError("handleVariables", token, "Dois Pontos");
              }
            }
          } else {
            throwError("handleVariables", token, "Virgula ou Dois Pontos");
          }
        } else {
          throwError("handleVariables", token, "Variavel Duplicada");
        }
      } else {
        throwError("handleVariables", token, "Identificador");
      }
    } while (token.getSymbol() != sDoisPontos);
    nextToken();
    handleType();
    return varTokens;
  }

  private void handleType() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    if (token.getSymbol() != sInteiro && token.getSymbol() != sBooleano) {
      throwError("handleType", token, "~Inteiro && ~Booleano");
    } else {
      if (token.getSymbol() == sInteiro || token.getSymbol() == sBooleano) {
        this.symbolTable.addType(token);
      } else {
        throwError("handleType", token, "~Inteiro && ~Booleano");
      }
    }
    nextToken();
  }

  //  ALTERADO DA APOSTILA
  private List<String> handleCommands() throws SyntaticAnalyzerException, EOFException, LexicalParserException, CodeGeneratorException {
    List<String> resultCommands = new ArrayList<>();
    if (token.getSymbol() == sInicio) {
      nextToken();
      resultCommands.addAll(handleSimpleCommand());
      while (token.getSymbol() != sFim) {
        //  TODO -- Comando nao exige um ';' no final
        if (token.getSymbol() == sPonto_Virgula) {
          nextToken();
          if (token.getSymbol() != sFim) {
            resultCommands.addAll(handleSimpleCommand());
          }
        } else {
          throwError("handleCommands", token, "Ponto e Virgula");
        }
      }
      if (token.getSymbol() == sFim) {
        nextToken();
        //        if (token.getSymbol() == sPonto_Virgula) {
        //          nextToken();
        //        }
      } else {
        throwError("handleCommands", token, "Fim");
      }
    } else {
      throwError("handleCommands", token, "Inicio");
    }
    return resultCommands;
  }

  private List<String> handleSimpleCommand() throws SyntaticAnalyzerException, EOFException, LexicalParserException, CodeGeneratorException {
    List<String> resultSimpleCommand = new ArrayList<>();
    if (token.getSymbol() == sIdentificador) {
      resultSimpleCommand.addAll(handleAttProcedureCall());
    } else if (token.getSymbol() == sSe) {
      resultSimpleCommand.addAll(handleIf());
    } else if (token.getSymbol() == sEnquanto) {
      resultSimpleCommand.addAll(handleWhile());
    } else if (token.getSymbol() == sLeia) {
      resultSimpleCommand.addAll(handleRead());
    } else if (token.getSymbol() == sEscreva) {
      resultSimpleCommand.addAll(handleWrite());
    } else {
      resultSimpleCommand.addAll(handleCommands());
    }
    return resultSimpleCommand;
  }

  private List<String> handleAttProcedureCall() throws SyntaticAnalyzerException, EOFException, LexicalParserException, CodeGeneratorException {
    List<String> result = new ArrayList<>();
    bufferToken = token; //  Store if it's a Procedure
    nextToken();
    if (token.getSymbol() == sAtribuicao) {
      result = handleAssignment();
    } else {
      result.add(callProcedure());
    }
    return result;
  }

  private List<String> handleAssignment() throws SyntaticAnalyzerException, EOFException, LexicalParserException, CodeGeneratorException {
    List<String> result = null;
    List<Token> resultExpression;
    AssignmentGenerator attGenerator = (AssignmentGenerator) GeneratorTypes.Assignment.getGenerator();
    FunctionReturnGenerator rfGenerator = (FunctionReturnGenerator) GeneratorTypes.FunctionReturn.getGenerator();
    //  Verifica se eh var e se existe
    if (symbolTable.hasSymbol(bufferToken)) {
      if (symbolTable.getSymbol(bufferToken).getScope().equals(Scope.Variable)) {
        attGenerator.addToken(bufferToken);
        nextToken();
        resultExpression = handleExpression();
        attGenerator.addToken(resultExpression);
        result = attGenerator.generate();
      } else {
        //  Adiciona o Id da Funcao
        rfGenerator.addToken(symbolTable.getSymbol(bufferToken).getToken());
        //  Handle Expression
        nextToken();
        resultExpression = handleExpression();
        rfGenerator.addToken(resultExpression);
        //  Generate Code
        result = rfGenerator.generate();
      }
    } else {
      throwError("handleAssignment", bufferToken, "Variavel Nao declarada");
    }
    return result;
  }

  private List<String> handleIf() throws SyntaticAnalyzerException, EOFException, LexicalParserException, CodeGeneratorException {
    IfGenerator ifGenerator = GeneratorTypes.If.getGenerator();
    nextToken();
    ifGenerator.addToken(handleExpression());
    if (token.getSymbol() == sEntao) {
      nextToken();
      ifGenerator.addBlock(handleSimpleCommand()); //  If Block
      if (token.getSymbol() == sSenao) {
        nextToken();
        ifGenerator.addBlock(handleSimpleCommand()); //  Else Block
      }
    } else {
      throwError("handleIf", token, "Entao");
    }
    return ifGenerator.generate();
  }

  private List<String> handleWhile() throws SyntaticAnalyzerException, EOFException, LexicalParserException, CodeGeneratorException {
    WhileGenerator whileGenerator = GeneratorTypes.While.getGenerator();
    // Gerador de Codigo
    nextToken(); //   Consome While
    whileGenerator.addToken(handleExpression());
    if (token.getSymbol() == sFaca) {
      nextToken();
      whileGenerator.addBlock(handleSimpleCommand());
    } else {
      throwError("handleWhile", token, "Faca");
    }
    return whileGenerator.generate();
  }

  private List<String> handleRead() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    ReadGenerator readGenerator = GeneratorTypes.Read.getGenerator();
    nextToken();
    if (token.getSymbol() == sAbre_Parenteses) {
      nextToken();
      if (token.getSymbol() == sIdentificador) {
        if (symbolTable.hasSymbol(token)) {
          readGenerator.addToken(token);
          nextToken();
          if (token.getSymbol() == sFecha_Parenteses) {
            nextToken();
          } else {
            throwError("handleRead", token, "Fecha Parenteses");
          }
        } else {
          throwError("handleRead", token, "Pesquisa Declarar Variavel Tabela");
        }
      } else {
        throwError("handleRead", token, "Identificador");
      }
    } else {
      throwError("handleRead", token, "Abre Parenteses");
    }
    return readGenerator.generate();
  }

  private List<String> handleWrite() throws SyntaticAnalyzerException, EOFException, LexicalParserException, CodeGeneratorException {
    WriteGenerator writeGenerator = GeneratorTypes.Write.getGenerator();
    nextToken();
    if (token.getSymbol() == sAbre_Parenteses) {
      nextToken();
      if (token.getSymbol() == sIdentificador) {
        if (symbolTable.hasSymbol(token, Scope.Variable) || symbolTable.hasSymbol(token, Scope.Function)) {
          writeGenerator.addToken(token);
          nextToken();
          if (token.getSymbol() == sFecha_Parenteses) {
            nextToken();
          } else {
            throwError("handleWrite", token, "Fecha Parenteses");
          }
        } else {
          throwError("handleWrite", token, "Pesquisa Declaracao de Funcao na Tabela");
        }
      } else {
        throwError("handleWrite", token, "Identificador");
      }
    } else {
      throwError("handleWrite", token, "Abre Parenteses");
    }
    return writeGenerator.generate();
  }

  private List<String> handleSubRotines() throws SyntaticAnalyzerException, EOFException, LexicalParserException, CodeGeneratorException {
    List<String> result = new ArrayList<>();
    if (token.getSymbol() == sProcedimento || token.getSymbol() == sFuncao) {
      if (token.getSymbol() == sProcedimento) {
        result.addAll(handleDeclareProcedure());
      } else {
        result.addAll(handleDeclareFunction());
      }
    }
    while (token.getSymbol() == sProcedimento || token.getSymbol() == sFuncao) {
      if (token.getSymbol() == sProcedimento) {
        result.addAll(handleDeclareProcedure());
      } else {
        result.addAll(handleDeclareFunction());
      }
    }
    if (token.getSymbol() == sPonto_Virgula) {
      nextToken();
    }
    return result;
  }

  private String callProcedure() throws SyntaticAnalyzerException {
    String result = null;
    Integer memoryLocation;
    // Verify Symbol Table for Occurences
    if (this.symbolTable.hasSymbol(bufferToken, Scope.Procedure)) {
      memoryLocation = symbolTable.getProcMemoryLocation(bufferToken);
      result = String.format("%s %s_%d", Operations.CALL.name(), bufferToken.getLexeme(), memoryLocation);
    } else {
      throwError("callProcedure", bufferToken, "Procedimento Desconhecido");
    }
    return result;
  }

  private List<String> handleDeclareProcedure() throws SyntaticAnalyzerException, EOFException, LexicalParserException, CodeGeneratorException {
    ProcedureGenerator procedureGenerator = GeneratorTypes.Procedure.getGenerator();
    nextToken();
    if (token.getSymbol() == sIdentificador) {
      if (!symbolTable.hasSymbol(token)) {
        // Insere na Tabela de Simbolos
        this.symbolTable.addSymbol(token, Scope.Procedure);
        procedureGenerator.addToken(token);
        lastProcedure = token; // Procedure Name
        nextToken();
        if (token.getSymbol() == sPonto_Virgula) {
          procedureGenerator.addBlock(handleBlock());
          if (token.getSymbol() == sPonto_Virgula) {
            nextToken();
          }
        } else {
          throwError("handleDeclareProcedure", token, "Ponto Virgula");
        }
      } else {
        throwError("handleDeclareProcedure", token, "Procedimento Duplicado");
      }
    } else {
      throwError("handleDeclareProcedure", token, "Identificador");
    }
    this.symbolTable.removeUntil(Scope.Procedure); //  TODO Remove da Tabela de Simbolos
    return procedureGenerator.generate();
  }

  private Token handleFunctionCall() throws SyntaticAnalyzerException {
    Token result = null;
    // Verify Symbol Table for Occurences
    if (this.symbolTable.hasSymbol(token, Scope.Function)) {
      result = this.symbolTable.getSymbol(token).getToken();
    } else {
      throwError("handleFunctionCall", token, "Funcao Desconhecido");
    }
    return result;
  }

  private List<String> handleDeclareFunction() throws SyntaticAnalyzerException, EOFException, LexicalParserException, CodeGeneratorException {
    List<String> result = null;
    FunctionGenerator functionGenerator = GeneratorTypes.Function.getGenerator();
    Token id;
    Token type;

    nextToken();
    if (token.getSymbol() == sIdentificador) {
      if (!symbolTable.hasSymbol(token)) {
        id = token;
        nextToken();
        if (token.getSymbol() == sDoisPontos) {
          nextToken();
          if (token.getSymbol() == sInteiro || token.getSymbol() == sBooleano) {
            type = token;
            // Insere Funcao e Retorno na Tabela de Simbolos
            this.symbolTable.addSymbol(id, Scope.Function, Type.getType(type.getLexeme()));
            lastProcedure = id; // Function Name
            functionGenerator.addToken(id);
            nextToken();
            if (token.getSymbol() == sPonto_Virgula) {
              functionGenerator.addBlock(handleBlock());
            } else {
              throwError("handleDeclareFunction", token, "Ponto Virgula");
            }
          } else {
            throwError("handleDeclareFunction", token, "Ponto Virgula");
          }
        } else {
          throwError("handleDeclareFunction", token, "Dois Pontos");
        }
      } else {
        throwError("handleDeclareFunction", token, "Funcao Duplicada");
      }
    } else {
      throwError("handleDeclareFunction", token, "Identificador");
    }
    result = functionGenerator.generate();
    this.symbolTable.removeUntil(Scope.Function); //  TODO Remove da Tabela de Simbolos
    return result;
  }

  private List<Token> handleExpression() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    List<Token> tokenList = new ArrayList<>();
    tokenList.addAll(handleSimpleExpression());
    if (token.getSymbol() == sMaior || token.getSymbol() == sMaiorIg || token.getSymbol() == sIg || //
        token.getSymbol() == sMenor || token.getSymbol() == sMenorIg || token.getSymbol() == sDif) {
      tokenList.add(token);
      nextToken();
      tokenList.addAll(handleSimpleExpression());
    }
    return tokenList;
  }

  //  ALTERADO DA APOSTILA
  private List<Token> handleSimpleExpression() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    List<Token> tokenList = new ArrayList<>();
    if (token.getSymbol() == sMais || token.getSymbol() == sMenos) {
      tokenList.add(token);
      nextToken();
    }
    tokenList.addAll(handleTerm());

    while (token.getSymbol() == sMais || token.getSymbol() == sMenos || token.getSymbol() == sOu) {
      tokenList.add(token);
      nextToken();
      tokenList.addAll(handleTerm());
    }
    return tokenList;
  }

  private List<Token> handleTerm() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    List<Token> tokenList = new ArrayList<>();
    tokenList.addAll(handleFactor());
    while (token.getSymbol() == sMult || token.getSymbol() == sDiv || token.getSymbol() == sE) {
      tokenList.add(token);
      nextToken();
      tokenList.addAll(handleFactor());
    }
    return tokenList;
  }

  private List<Token> handleFactor() throws SyntaticAnalyzerException, EOFException, LexicalParserException {
    List<Token> tokenList = new ArrayList<>();
    if (token.getSymbol() == sIdentificador) {
      if (this.symbolTable.hasSymbol(token)) {
        if (this.symbolTable.getSymbol(token).getScope() == Scope.Function) {
          tokenList.add(handleFunctionCall());
          nextToken();
        } else {
          tokenList.add(token);
          nextToken();
        }
      } else {
        throwError("handleFactor", token, "Identificador Desconhecido"); //FIXME -- Acionar no Semantico
      }
    } else if (token.getSymbol() == sNumero) {
      tokenList.add(token);
      nextToken();
    } else if (token.getSymbol() == sNao) {
      tokenList.add(token);
      nextToken();
      tokenList.addAll(handleFactor());
    } else if (token.getSymbol() == sAbre_Parenteses) {
      tokenList.add(token);
      nextToken();
      tokenList.addAll(handleExpression());
      if (token.getSymbol() == sFecha_Parenteses) {
        tokenList.add(token);
        nextToken();
      } else {
        throwError("handleFactor", token, "Fecha Parenteses");
      }
    } else if (token.getLexeme().equals("verdadeiro") || token.getLexeme().equals("falso")) {
      tokenList.add(token); //  TODO -- ?
      nextToken();
    } else {
      throwError("handleFactor", token, "Verdadeiro ou Falso");
    }
    return tokenList;
  }

  //  Utils

  private void nextToken() throws EOFException, LexicalParserException {
    token = parser.getNextToken();
    tokenStack.addToken(token);
  }

  private void throwError(String functionName, Token token, String context) throws SyntaticAnalyzerException {
    throw new SyntaticAnalyzerException(String.format("[%s] Unexpected Token! Token: %s, Context: %s", functionName, token.toString(), context));
  }
}
