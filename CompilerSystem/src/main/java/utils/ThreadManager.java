/**
 * 
 */
package utils;

/**
 * Copyright (c) 2019
 *
 * @author Felipe / 1 de set de 2019
 * @version 1.0
 * @since 1.0
 */
public class ThreadManager {

  public static Thread createThread(String name, Runnable runnable) {
    Thread thread = new Thread(runnable);
    thread.setName(name);
    return thread;
  }
}
