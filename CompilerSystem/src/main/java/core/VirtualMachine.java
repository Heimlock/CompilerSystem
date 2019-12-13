/**
 *
 */
package core;

import core.engine.VirtualMachineEngine;
import core.viewer.ViewerVirtualMachine;
import utils.ThreadManager;

/**
 * Copyright (c) 2019
 *
 * @author Felipe / 16 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public class VirtualMachine {
  public static void main(String[] args) {
    VirtualMachineEngine vmEngine = VirtualMachineEngine.getInstance();
    ViewerVirtualMachine viewerInstance = new ViewerVirtualMachine();

    //  Virtual Machine Setup
    vmEngine.setViewerInstance(viewerInstance);
    vmEngine.updateViewer();
    //    vmEngine.stub();

    //  Main Loop
    Thread mainThread = ThreadManager.createThread("Main Loop", () -> {
      System.out.println("Thread Running");
      try {
        while (Boolean.TRUE) {
          if (vmEngine.isRunning()) {
            vmEngine.doCycle();
        }
          Thread.sleep(vmEngine.getSleep());
      }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    try {
      mainThread.start();
      mainThread.join();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
