package com.equo.application;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.inject.Inject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.ui.internal.workbench.swt.WorkbenchStatusReporter;

import com.google.gson.JsonObject;
import com.equo.aer.internal.api.IEquoCrashReporter;
import com.equo.logging.client.api.Logger;
import com.equo.logging.client.api.LoggerFactory;

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
		JsonObject json = new JsonObject();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

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
