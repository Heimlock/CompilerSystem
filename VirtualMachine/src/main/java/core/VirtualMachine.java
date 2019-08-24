/**
 *
 */
package core;

import core.engine.VirtualMachineEngine;
import core.viewer.Viewer;

/**
 * Copyright (c) 2019
 *
 * @author Felipe / 16 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public class VirtualMachine {
  public static void main(String[] args) {
    Viewer viewerInstance = new Viewer();
    VirtualMachineEngine vmEngine = VirtualMachineEngine.getInstance();

    //  Virtual Machine Setup
    vmEngine.setViewerInstance(viewerInstance);
    vmEngine.updateViewer();
    //    vmEngine.stub();

    //  Main Loop
    while (Boolean.TRUE) {
      if (vmEngine.isRunning()) {
        vmEngine.doCycle();
      }
    }
  }
}
