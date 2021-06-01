package com.equo.application.handlers;

import java.util.List;
import java.util.Optional;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import com.equo.application.model.browser.BrowserParams;
import com.equo.application.util.IConstants;
import com.equo.logging.client.api.Logger;
import com.equo.logging.client.api.LoggerFactory;

/**
 * Interface of a handler for browser commands such as change browser URL or
 * open a new browser.
 */
public interface BrowserCommandHandler {
  /**
   * Check if a browser with the given name already exists.
   * @param  mApplication  model of the current application
   * @param  browserParams params of the request to open/update a browser
   * @param  modelService  Eclipse model service
   * @return               an optional with the application Part of the browser,
   *                       or an empty optional if it does not exists
   */
  default Optional<MPart> existingBrowserFor(MApplication mApplication, BrowserParams browserParams,
      EModelService modelService) {
    Logger logger = LoggerFactory.getLogger(BrowserCommandHandler.class);
    String browserName = browserParams.getName();
    if (browserName != null) {
      String browserIdSuffix = browserName.toLowerCase();
      String browserId = IConstants.EQUO_BROWSER_IN_PARTSTACK_ID + "." + browserIdSuffix;
      if (IConstants.MAIN_PART_ID.equals(browserName)) {
        browserId = IConstants.MAIN_PART_ID;
      }
      logger.debug("The browser id is " + browserId);
      List<MPart> partElements =
          modelService.findElements(mApplication, browserId, MPart.class, null);
      if (!partElements.isEmpty()) {
        return Optional.of(partElements.get(0));
      }
    }
    return Optional.empty();
  }
}
