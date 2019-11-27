package com.make.equo.ui.helper.provider.dialogs;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import com.make.equo.ui.helper.provider.model.MButtonToggle;

public class MessageDialogWithToggle extends MessageDialog {

	private MButtonToggle toggle;
	
	public MessageDialogWithToggle(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage,
			int dialogImageType, int defaultIndex, String[] dialogButtonLabels) {
		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, defaultIndex, dialogButtonLabels);
	}

	public MButtonToggle getToggle() {
		return toggle;
	}

	public void setToggle(MButtonToggle toggle) {
		this.toggle = toggle;
	}

	
	
}
