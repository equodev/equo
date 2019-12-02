package com.make.equo.ui.helper.provider.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import com.make.equo.ui.helper.provider.dialogs.util.IDialogConstants;
import com.make.equo.ui.helper.provider.model.MButton;
import com.make.equo.ui.helper.provider.model.MButtonToggle;
import com.make.equo.ui.helper.provider.model.WebdialogFactory;


public class MessageDialogWithToggle extends MessageDialog {

	private static MButtonToggle toggle;
	
	public MessageDialogWithToggle(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage,
			int dialogImageType, String[] dialogButtonLabels,int defaultIndex, String messageToggle, boolean status) {
		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, defaultIndex, dialogButtonLabels);
		initToggle(messageToggle,status);
	}
	
		      
	private void initToggle(String messageToggle, boolean status) {
	         MButtonToggle button = WebdialogFactory.eINSTANCE.createMButtonToggle();
	         button.setMessage(messageToggle);
	         button.setStatus(status);
	         thisDialog.setToggle(button);
	}
	

	public MButtonToggle getToggle() {
		return toggle;
	}

	public void setToggle(MButtonToggle toggle) {
		this.toggle = toggle;
	}

	
	
}
