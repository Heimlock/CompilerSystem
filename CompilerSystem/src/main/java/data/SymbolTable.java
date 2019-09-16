/**
 * 
 */
package data;

/**
 * Copyright (c) 2019
 *
 * @author 16116469 / 11 de set de 2019
 * @version 1.0
 * @since 1.0
 */
public enum SymbolTable {
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
  sAtribuicao,
  sEscreva("escreva"),
  sLeia("leia"),
  sVar("var"),
  sInteiro("inteiro"),
  sBooleano("booleano"),
  sIdentificador,
  sNumero,
  sPonto,
  sPonto_Virgula,
  sVirgula,
  sAbre_Parenteses,
  sFecha_Parenteses,
  sMaior,
  sMaiorIg,
  sIg,
  sMenor,
  sMenorIg,
  sDif,
  sMais,
  sMenos,
  sMult,
  sDiv("div"),
  sE("e"),
  sOu("ou"),
  sNao("nao"),
  sDoisPontos
  ;
  private String lexeme;

  private SymbolTable() {
    this.lexeme = "";
  }

  private SymbolTable(String lexeme) {
    this.lexeme = lexeme;
  }

  public String getLexeme() {
    return this.lexeme;
  }

  public static SymbolTable getSymbolByLexeme(String lexeme) {
    SymbolTable result = null;
    for (SymbolTable symbol : SymbolTable.values()) {
      if (symbol.getLexeme().equals(lexeme)) {
        result = symbol;
        break;
      }
    }
    return result;
  }
}
