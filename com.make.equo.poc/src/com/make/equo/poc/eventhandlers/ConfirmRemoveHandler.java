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
public class ConfirmRemoveHandler {
	@Reference
	private IEquoEventHandler equoEventHandler;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Activate
	public void configDialog() {
		equoEventHandler.on("confirmremovehandler", (IEquoRunnable) voidParameter -> {
			MessageDialog dialog = new MessageDialog(null, "Delete Resource", null,
					"Are you sure you want to delete this resource?", MessageDialog.QUESTION, 1,
					new String[] { "&Remove", "Cancel" });
			int responseDialog = dialog.open();
			HashMap<String, Boolean> response = new HashMap<>();
			response.put("proceed", responseDialog == IDialogConstants.OK_ID);
			equoEventHandler.send("_confirmremoveresponse", response);
		});
	}

}
