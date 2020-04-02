package com.make.equo.builders.tests.util;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.application.model.EquoApplicationBuilderConfigurator;
import com.make.equo.application.model.EquoApplicationModel;

import org.eclipse.e4.ui.model.application.impl.ApplicationPackageImpl;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.e4.ui.internal.workbench.E4XMIResourceFactory;

public class ModelTestingConfigurator {

	private MApplication getModelApplication() {
		ResourceSet resourceSet = new ResourceSetImpl();
		ApplicationPackageImpl.init();
		URI uri = URI.createURI(
				"../com.make.equo.application.provider/resources/Application.e4xmi");
		
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("e4xmi", new E4XMIResourceFactory());
		Resource res = resourceSet.getResource(uri, true);
		MApplication app = (MApplication) res.getContents().get(0);
		System.out.println(app.getElementId());

		return app;
	}

	public void configure(EquoApplicationBuilder equoApplicationBuilder, IEquoApplication equoApp) {
		MApplication mainApplication = getModelApplication();
		EquoApplicationModel equoApplicationModel = new EquoApplicationModel();
		equoApplicationModel.setMainApplication(mainApplication);
		EquoApplicationBuilderConfigurator equoApplicationBuilderConfigurator = new EquoApplicationBuilderConfigurator(
				equoApplicationModel, equoApplicationBuilder, equoApp);
		equoApplicationBuilderConfigurator.configure();
	}
	
	

}
