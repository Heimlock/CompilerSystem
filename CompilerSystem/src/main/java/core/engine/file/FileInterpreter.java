/**
 *
 */
package core.engine.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import core.engine.VirtualMachineEngine;
import core.viewer.ViewerVirtualMachine;
import data.impl.ProgramStack_Impl;
import data.interfaces.ProgramStack;

/**
 * Copyright (c) 2019
 *
 * @author Felipe / 12 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public class FileInterpreter {
  private BufferedReader br;
  private ProgramStack program;

  public FileInterpreter() throws FileNotFoundException {
    final ViewerVirtualMachine mainFrame = (ViewerVirtualMachine) VirtualMachineEngine.getInstance().getViewer();
    JFileChooser fileChooser = new JFileChooser();
    FileFilter filter = new FileNameExtensionFilter("Assembly File", "obj");
    fileChooser.setFileFilter(filter);
    fileChooser.showOpenDialog(mainFrame);
    File file = fileChooser.getSelectedFile();
    if (file != null) {
      initFile(file.getAbsolutePath());
    }
  }

  public FileInterpreter(String filePath) throws FileNotFoundException {
    initFile(filePath);
  }

  private void initFile(String filePath) throws FileNotFoundException {
    this.br = new BufferedReader(new FileReader(filePath));
    this.program = ProgramStack_Impl.getInstance();
  }

  public void parseOperations() throws IOException {
    String line;
    if (br != null) {
      while ((line = br.readLine()) != null) {
        line = line.replaceAll("[ \\f\\t\\v]{1,}$", ""); //  Clean whitespace/Tabs in the end of the Line
        if (line.length() != 0) { //  Prevents clean lines
          program.push(line);
        }
      }
      br.close();
    }
  }
}
