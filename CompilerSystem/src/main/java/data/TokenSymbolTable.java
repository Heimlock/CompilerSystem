/**
 * 
 */
package data;

import java.util.Optional;

import utils.StringUtils;

/**
 * Copyright (c) 2019
 *
 * @author 16116469 / 11 de set de 2019
 * @version 1.0
 * @since 1.0
 */
public enum TokenSymbolTable {
  sPrograma("programa"),
  sInicio("inicio"),
  sFim("fim"),
  sProcedimento("procedimento"),
  sFuncao("funcao"),
  sSe("se"),
  sEntao("entao"),
  sSenao("senao"),
  sEnquanto("enquanto"),
  sFaca("faca"),
  sAtribuicao(":="),
  sEscreva("escreva"),
  sLeia("leia"),
  sVar("var"),
  sInteiro("inteiro"),
  sBooleano("booleano"),
  sIdentificador,
  sNumero,
  sPonto("."),
  sPonto_Virgula(";"),
  sVirgula(","),
  sAbre_Parenteses("("),
  sFecha_Parenteses(")"),
  sMaior(">", 3),
  sMaiorIg(">=", 3),
  sIg("=", 3),
  sMenor("<", 3),
  sMenorIg("<=", 3),
  sDif("!=", 3),
  sMais("+", 4),
  sMenos("-", 4),
  sMult("*", 5),
  sDiv("div", 5),
  sVerdadeiro("verdadeiro"),
  sFalso("falso"),
  sE("e", 2),
  sOu("ou", 1),
  sNao("nao", 6),
  sDoisPontos(":"),
  ;
  private String lexeme;
  private Integer precedency;

  private TokenSymbolTable() {
    this.lexeme = "";
    this.precedency = -1;
  }

  private TokenSymbolTable(Integer precedency) {
    this.lexeme = "";
    this.precedency = precedency;
  }

  private TokenSymbolTable(String lexeme) {
    this.lexeme = lexeme;
    this.precedency = -1;
  }

  private TokenSymbolTable(String lexeme, Integer precedency) {
    this.lexeme = lexeme;
    this.precedency = precedency;
  }

  public String getLexeme() {
    return this.lexeme;
  }

  public static TokenSymbolTable getSymbolByLexeme(String lexeme) {
    TokenSymbolTable result = null;
    for (TokenSymbolTable symbol : TokenSymbolTable.values()) {
      if (symbol.getLexeme().equals(lexeme)) {
        result = symbol;
        break;
      }
    }
    if (!Optional.ofNullable(result).isPresent()) {
      if (StringUtils.isNumeric(lexeme)) {
        result = sNumero;
      } else {
        result = sIdentificador;
      }
    }
    return result;
  }

  public Integer getPrecedency() {
    return precedency;
  }
}
