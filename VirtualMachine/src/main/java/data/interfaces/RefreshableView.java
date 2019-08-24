/**
 * 
 */
package data.interfaces;

import java.util.logging.Logger;

import javax.swing.JPanel;

/**
 * Copyright (c) 2019
 *
 * @author Felipe / 16 de ago de 2019
 * @version 1.0
 * @since 1.0
 */
public abstract class RefreshableView extends JPanel {
  private static final long serialVersionUID = 6518701003704276718L;
  protected static Logger Log = Logger.getGlobal();

  public abstract void refresh();
}
