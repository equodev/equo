package com.make.equo.renderers.contributions;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MAddon;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationFactory;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

public class ToolBarModelProcessor {

	private static final String ECLIPSE_RCP_APP_ID = "org.eclipse.ui.ide.workbench";

	@Inject
	protected MApplication app;

	@Inject
	EModelService modelService;

	@Execute
	public void execute() {
		hideMainTrimBar(app);
	}

	private void hideMainTrimBar(MApplication mainApplication) {
		String addonId = ToolBarModelProcessorAddon.class.getName();
		for (MAddon addon : app.getAddons()) {
			if (addonId.equals(addon.getElementId())) {
				// our addon was found
				return;
			}
		}
		if (isAnEclipseBasedApp()) {
			MAddon addon = MApplicationFactory.INSTANCE.createAddon();
			addon.setContributionURI(
					"bundleclass://com.make.equo.renderers/com.make.equo.renderers.contributions.ToolBarModelProcessorAddon");
			addon.setElementId(addonId);
			app.getAddons().add(addon);
		}
	}

	private boolean isAnEclipseBasedApp() {
		return ECLIPSE_RCP_APP_ID.equals(System.getProperty("eclipse.application"));
	}
}
