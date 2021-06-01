/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of Equo Framework.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equoplatform.com/terms.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3 as published by the Free Software
** Foundation. Please review the following
** information to ensure the GNU General Public License requirements will
** be met: https://www.gnu.org/licenses/gpl-3.0.html.
**
****************************************************************************/

package com.equo.application;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.inject.Inject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.ui.internal.workbench.swt.WorkbenchStatusReporter;

import com.equo.aer.internal.api.IEquoCrashReporter;
import com.equo.logging.client.api.Logger;
import com.equo.logging.client.api.LoggerFactory;
import com.google.gson.JsonObject;

/**
 * Captures crashes and application errors and reports to AER component.
 *
 */
public class EquoStatusReporter extends WorkbenchStatusReporter {

  @Inject
  private IDependencyInjector dependencyInjector;

  private IEquoCrashReporter equoCrashReporter;

  protected static final Logger logger = LoggerFactory.getLogger(EquoStatusReporter.class);

  @Override
  public void report(IStatus status, int style, Object... information) {
    int action = style & (IGNORE | LOG | SHOW | BLOCK);
    if (action == 0) {
      if (status.matches(ERROR)) {
        action = SHOW;
      } else {
        action = LOG;
      }
    }
    if (style != IGNORE) {
      if ((action & (SHOW | BLOCK)) != 0) {
        equoCrashReporter = dependencyInjector.getEquoCrashReporter();
        if (equoCrashReporter != null) {
          registerEvent(status);
        } else {
          log(status);
        }
      } else {
        log(status);
      }
    }
  }

  private void log(IStatus status) {
    if (status.matches(ERROR)) {
      logger.error(status.getMessage(), status.getException());
    } else if (status.matches(WARNING)) {
      logger.warn(status.getMessage(), status.getException());
    } else if (status.matches(INFO)) {
      logger.info(status.getMessage(), status.getException());
    }
  }

  private void registerEvent(IStatus status) {
    final JsonObject json = new JsonObject();
    final StringWriter sw = new StringWriter();
    final PrintWriter pw = new PrintWriter(sw);

    Throwable t = status.getException().getCause();

    if (t == null) {
      t = status.getException();
    }

    t.printStackTrace(pw);

    String stackTrace = sw.toString();
    stackTrace = stackTrace.replace("\n", "\\n");

    String message = t.getMessage();
    if (message == null) {
      message = status.getMessage();
    }

    json.addProperty("stackTrace", stackTrace);
    json.addProperty("crashCause", message);
    equoCrashReporter.logCrash(json);
  }

}
