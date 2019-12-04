package com.make.equo.ui.helper.provider.dialogs;

import java.util.LinkedHashMap;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import com.make.equo.ui.helper.provider.dialogs.util.IDialogConstants;
import com.make.equo.ui.helper.provider.model.MButtonToggle;
import com.make.equo.ui.helper.provider.model.WebdialogFactory;

public class MessageDialogWithToggle extends MessageDialog {

	private static MButtonToggle toggle;

	public MessageDialogWithToggle(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage,
			int dialogImageType, String[] dialogButtonLabels, int defaultIndex, String messageToggle, boolean status) {
		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, defaultIndex,
				dialogButtonLabels);
		initToggle(messageToggle, status);
	}

	public MessageDialogWithToggle(Shell parentShell, String dialogTitle, Image image, String message,
			int dialogImageType, LinkedHashMap<String, Integer> buttonLabelToIdMap, int defaultIndex,
			String messageToggle, boolean status) {
		super(parentShell, dialogTitle, image, message, dialogImageType, defaultIndex,
				buttonLabelToIdMap.keySet().toArray(new String[buttonLabelToIdMap.size()]));
		initToggle(messageToggle, status);
	}

	private void initToggle(String messageToggle, boolean status) {
		MButtonToggle button = WebdialogFactory.eINSTANCE.createMButtonToggle();
		button.setMessage(messageToggle);
		button.setStatus(status);
		thisDialog.setToggle(button);
	}

	public static void openError(Shell parent, String title, String message, String toggleMessage, boolean toggleState,
			IPreferenceStore store, String key) {
		open(ERROR, parent, title, message, toggleMessage, toggleState, store, key, SWT.NONE);
	}

	public static void openInformation(Shell parent, String title, String message, String toggleMessage,
			boolean toggleState, IPreferenceStore store, String key) {
		open(INFORMATION, parent, title, message, toggleMessage, toggleState, store, key, SWT.NONE);
	}

	public static void openOkCancelConfirm(Shell parent, String title, String message, String toggleMessage,
			boolean toggleState, IPreferenceStore store, String key) {
		open(CONFIRM, parent, title, message, toggleMessage, toggleState, store, key, SWT.NONE);
	}

	public static void openWarning(Shell parent, String title, String message, String toggleMessage,
			boolean toggleState, IPreferenceStore store, String key) {
		open(WARNING, parent, title, message, toggleMessage, toggleState, store, key, SWT.NONE);
	}

	public static void openYesNoCancelQuestion(Shell parent, String title, String message, String toggleMessage,
			boolean toggleState, IPreferenceStore store, String key) {
		open(QUESTION_WITH_CANCEL, parent, title, message, toggleMessage, toggleState, store, key, SWT.NONE);
	}

	public static void openYesNoQuestion(Shell parent, String title, String message, String toggleMessage,
			boolean toggleState, IPreferenceStore store, String key) {
		open(QUESTION, parent, title, message, toggleMessage, toggleState, store, key, SWT.NONE);
	}

	public static boolean open(int kind, Shell parent, String title, String message, String toggleMessage,
			boolean toggleState, IPreferenceStore store, String key, int style) {
		MessageDialogWithToggle dialog = new MessageDialogWithToggle(parent, title, null, message, kind,
				getButtonLabels(kind), 0, toggleMessage, toggleState);
		injector.attachMWebDialog(dialog.getDialog());
		return dialog.getResponse() == IDialogConstants.OK_ID;
	}

	public static boolean open(int kind, Shell parent, String title, String message, String toggleMessage,
			boolean toggleState, IPreferenceStore store, String key, int style,
			LinkedHashMap<String, Integer> buttonLabelToIdMap) {
		MessageDialogWithToggle dialog = new MessageDialogWithToggle(parent, title, null, message, kind,
				buttonLabelToIdMap, 0, toggleMessage, toggleState);
		injector.attachMWebDialog(dialog.getDialog());
		return dialog.getResponse() == IDialogConstants.OK_ID;
	}

	public MButtonToggle getToggle() {
		return toggle;
	}

	public void setToggle(MButtonToggle toggle) {
		this.toggle = toggle;
	}

}
