/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.make.equo.application;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Application implements IApplication {
	Shell shell;

	public Object start(IApplicationContext context) throws Exception {
		System.out.println("contexto es " + context);
		System.out.println("holaaa ");
		final Display display = Display.getDefault();
		display.asyncExec(new Runnable() {
			public void run() {
				shell = AddressBook.runApplication(display);
			}
		});
		context.applicationRunning();
		while(shell == null || !shell.isDisposed()){
			if(!display.readAndDispatch())
				display.sleep();
		}
		return null;
	}

	public void stop() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (shell != null && !shell.isDisposed())
					shell.close();
			}
		});
	}
}
