package com.equo.application.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;

import com.equo.application.util.ICommandConstants;

/**
 * Handler for 'Preferences' menu item.
 */
public class PreferencesHandler {

  /**
   * Executes the handler.
   * @param mApplication model of the current application
   */
  @Execute
  public void execute(MApplication mApplication) {
    Runnable runnable =
        (Runnable) mApplication.getTransientData().get(ICommandConstants.PREFERENCES_COMMAND);
    if (runnable != null) {
      runnable.run();
    }
  }

}