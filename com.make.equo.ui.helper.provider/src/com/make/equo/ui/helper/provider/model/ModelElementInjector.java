package com.make.equo.ui.helper.provider.model;

import javax.inject.Inject;

import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;

public class ModelElementInjector {

	@Inject
	private UISynchronize sync;

	@Inject
	private MApplication mApplication;
	
	public void attachMWebDialog(MWebDialog webDialog) {
		sync.syncExec(() -> {
			mApplication.getChildren().add(webDialog);
		});
	}
	
	
	
}
