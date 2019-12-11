/**
 *
 */
package core.generator;

import core.generator.specific.AssignmentGenerator;
import core.generator.specific.FunctionGenerator;
import core.generator.specific.FunctionReturnGenerator;
import core.generator.specific.IfGenerator;
import core.generator.specific.LoadGenerator;
import core.generator.specific.ProcedureGenerator;
import core.generator.specific.ReadGenerator;
import core.generator.specific.VariableGenerator;
import core.generator.specific.WhileGenerator;
import core.generator.specific.WriteGenerator;
import data.interfaces.GenerateCode;

/**
 * Copyright (c) 2019
 *
 * @author Felipe Moreira Ferreira / 5 de nov de 2019
 * @version 1.0
 * @since 1.0
 */
public enum GeneratorTypes {
  If(IfGenerator.class), // Ok
  While(WhileGenerator.class), // Ok
  Procedure(ProcedureGenerator.class), // Ok
  Variable(VariableGenerator.class), // Ok
  Function(FunctionGenerator.class), // Ok
  Read(ReadGenerator.class), //  Ok
  Write(WriteGenerator.class), // Ok
  Assignment(AssignmentGenerator.class), //  Ok
  Load(LoadGenerator.class), //  Ok
  FunctionReturn(FunctionReturnGenerator.class), //
  ;

  private Class<GenerateCode> generator;

  @SuppressWarnings("unchecked")
  private <T extends GenerateCode> GeneratorTypes(Class<T> generator) {
    this.generator = (Class<GenerateCode>) generator;
  }

  @SuppressWarnings("unchecked")
  public <T extends GenerateCode> T getGenerator() {
    T result = null;
    try {
      result = (T) this.generator.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return result;
  }
}
