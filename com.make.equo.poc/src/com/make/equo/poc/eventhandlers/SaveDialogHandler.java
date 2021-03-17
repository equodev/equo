package com.make.equo.poc.eventhandlers;

import java.util.HashMap;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.ws.api.IEquoEventHandler;
import com.make.equo.ws.api.IEquoRunnable;

@Component
public class SaveDialogHandler {
	private static final int CANCEL_ID = 2;

	@Reference
	private IEquoEventHandler equoEventHandler;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Activate
	public void configDialog() {
		equoEventHandler.on("savedialoghandler", (IEquoRunnable) voidParameter -> {
			MessageDialog dialog = new MessageDialog(null, "Save Resource", null, "Save changes?",
					MessageDialog.QUESTION, 2, new String[]{"&Save", "Do&n't Save", "Cancel"});
			int responseDialog = dialog.open();
			boolean close = responseDialog >= 0 && responseDialog != CANCEL_ID;
			boolean save = responseDialog == IDialogConstants.OK_ID;
			HashMap<String, Boolean> response = new HashMap<>();
			response.put("close", close);
			response.put("save", save);
			equoEventHandler.send("_savedialogresponse", response);
		});
	}

	
}
