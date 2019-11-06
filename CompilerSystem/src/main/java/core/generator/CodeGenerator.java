/**
 *
 */
package core.generator;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 5 de nov de 2019
 * @version 1.0
 * @since 1.0
 */
public class CodeGenerator {
  private static CodeGenerator instance = null;
  private List<String> generatedProgram;

  public static CodeGenerator getInstance() {
    if (instance == null) {
      instance = new CodeGenerator();
    }
    return instance;
  }

  private CodeGenerator() {
    this.generatedProgram = new ArrayList<>();
  }

  public void addProgramLine(String line) {
    this.generatedProgram.add(line);
  }

  public void addProgramLines(List<String> line) {
    this.generatedProgram.addAll(line);
  }

  public List<String> getGeneratedProgram() {
    return this.generatedProgram;
  }

  public void saveToFile() {
    //TODO -- Implementar IO com Arquivos
  }
}
