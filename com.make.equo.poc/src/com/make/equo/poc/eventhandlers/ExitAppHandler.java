package com.make.equo.poc.eventhandlers;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.osgi.service.component.annotations.Component;

import com.make.equo.poc.payloads.Payload;
import com.make.equo.ws.api.actions.IActionHandler;

@SuppressWarnings("serial")
@Component
public class ExitAppHandler implements IActionHandler<Payload> {

	@Override
	public Object call(Payload payload) {
		if (showDialog() == IDialogConstants.OK_ID) {
			System.exit(0);
		}
		return null;
	}

	public int showDialog() {
		MessageDialog dialog = new MessageDialog(null, "Confirm Exit", null, "Exit application?",
				MessageDialog.QUESTION, 1, new String[] { "&Exit", "Cancel" });
		return dialog.open();
	}

}
