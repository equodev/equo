package com.equo.application.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.IWorkbench;

import com.equo.application.util.ICommandConstants;

/**
 * Handler for application exit.
 */
public class ExitHandler {

  /**
   * Executes the handler.
   * @param workbench    workbench instance
   * @param mApplication model of the current application
   */
  @Execute
  public void execute(IWorkbench workbench, MApplication mApplication) {
    Runnable runnable =
        (Runnable) mApplication.getTransientData().get(ICommandConstants.EXIT_COMMAND);
    if (runnable != null) {
      runnable.run();
    }
    workbench.close();
  }

}
