package com.equo.application;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.services.statusreporter.StatusReporter;

import com.equo.aer.internal.api.IEquoCrashReporter;
import com.google.gson.JsonObject;

/**
 * Listener for application logs to be sended to Error Reporting.
 *
 */
public class LogListener implements ILogListener {

  private static final String ERROR = "Error";
  private static final String WARNING = "Warning";
  private static final String INFO = "Info";

  private IEquoCrashReporter equoCrashReporter;

  public LogListener(IEquoCrashReporter equoCrashReporter) {
    this.equoCrashReporter = equoCrashReporter;
  }

  @Override
  public void logging(IStatus status, String plugin) {
    if (equoCrashReporter == null) {
      return;
    }

    JsonObject json = new JsonObject();

    Throwable throwable = status.getException();
    if (throwable != null) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      throwable.printStackTrace(pw);
      String stackTrace = sw.toString();
      stackTrace = stackTrace.replace("\n", "\\n");
      json.addProperty("stackTrace", stackTrace);
    }

    json.addProperty("message", status.getMessage());

    if (status.matches(StatusReporter.ERROR)) {
      json.addProperty("severity", ERROR);
      equoCrashReporter.logEclipse(json);
    } else if (status.matches(StatusReporter.WARNING)) {
      json.addProperty("severity", WARNING);
      equoCrashReporter.logEclipse(json);
    } else if (status.matches(StatusReporter.INFO)) {
      json.addProperty("severity", INFO);
      equoCrashReporter.logEclipse(json);
    }
  }
}
