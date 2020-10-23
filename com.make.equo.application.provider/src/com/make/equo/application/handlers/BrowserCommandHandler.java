package com.make.equo.application.handlers;

import java.util.List;
import java.util.Optional;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.make.equo.application.util.IConstants;

public interface BrowserCommandHandler {
	default Optional<MPart> existingBrowserFor(MApplication mApplication, BrowserParams browserParams,
			EModelService modelService) {
		Logger logger = LoggerFactory.getLogger(BrowserCommandHandler.class);
		if (browserParams.getName() != null) {
			String browserIdSuffix = browserParams.getName().toLowerCase();
			String browserId = IConstants.EQUO_BROWSER_IN_PARTSTACK_ID + "." + browserIdSuffix;
			logger.debug("The browser id is " + browserId);
			List<MPart> partElements = modelService.findElements(mApplication,
					browserId, MPart.class, null);
			if (!partElements.isEmpty()) {
				return Optional.of(partElements.get(0));
			}
		}
		return Optional.empty();
	}
}
