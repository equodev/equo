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

package com.equo.builders.tests.util;

import com.equo.application.api.IEquoApplication;
import com.equo.application.model.EquoApplicationBuilder;
import com.equo.application.model.EquoApplicationBuilderConfigurator;
import com.equo.application.model.EquoApplicationModel;

import org.eclipse.e4.ui.model.application.impl.ApplicationPackageImpl;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.e4.ui.internal.workbench.E4XMIResourceFactory;

public class ModelConfigurator {

	MApplication mainApplication;
	
	private MApplication getModelApplication() {
		ResourceSet resourceSet = new ResourceSetImpl();
		ApplicationPackageImpl.init();
		URI uri = URI.createURI(
				"../com.equo.application.client/resources/Application.e4xmi");
		
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("e4xmi", new E4XMIResourceFactory());
		Resource res = resourceSet.getResource(uri, true);
		MApplication app = (MApplication) res.getContents().get(0);
		System.out.println(app.getElementId());

		return app;
	}

	public void configure(EquoApplicationBuilder equoApplicationBuilder, IEquoApplication equoApp) {
		mainApplication = getModelApplication();
		EquoApplicationModel equoApplicationModel = new EquoApplicationModel();
		equoApplicationModel.setMainApplication(mainApplication);
		EquoApplicationBuilderConfigurator equoApplicationBuilderConfigurator = new EquoApplicationBuilderConfigurator(
				equoApplicationModel, equoApplicationBuilder, equoApp);
		equoApplicationBuilderConfigurator.configure();
	}

	public MApplication getMainApplication() {
		return mainApplication;
	}
	
}
