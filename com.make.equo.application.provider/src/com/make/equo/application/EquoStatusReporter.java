package com.make.equo.application;


import java.io.PrintWriter;
import java.io.StringWriter;

import javax.inject.Inject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.internal.workbench.swt.WorkbenchStatusReporter;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.google.gson.JsonObject;
import com.make.equo.aer.internal.api.IEquoCrashReporter;

@Component
public class EquoStatusReporter extends WorkbenchStatusReporter {
	
	private static IEquoCrashReporter equoCrashReporter;
	
	@Inject
	Logger logger;
	
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
				if (equoCrashReporter != null) {
					registerEvent(status);
				}
			} else {
				log(status);
			}
		}
	}

	private void log(IStatus status) {
		if (status.matches(ERROR)) {
			logger.error(status.getException(), status.getMessage());
		} else if (status.matches(WARNING)) {
			logger.warn(status.getException(), status.getMessage());
		} else if (status.matches(INFO)) {
			logger.info(status.getException(), status.getMessage());
		}
	}
	
	private void registerEvent(IStatus status) {
		JsonObject json = new JsonObject();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		status.getException().getCause().printStackTrace(pw);
		String stackTrace = sw.toString();
		stackTrace = stackTrace.replace("\n", "\\n");
		
		String message = status.getException().getCause().getMessage();
		if (message == null) {
			message = status.getMessage();
		}
		
		json.addProperty("stackTrace", stackTrace);
		json.addProperty("crashCause", message);
		equoCrashReporter.logCrash(json);
	}
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
	void setErrorReporter(IEquoCrashReporter errorReporter) {
		equoCrashReporter = errorReporter;
	}

	void unsetErrorReporter(IEquoCrashReporter errorReporter) {
		equoCrashReporter = null;
	}

}
