package com.make.equo.aspects;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import java.util.LinkedHashMap;
import java.util.Hashtable;

public aspect DialogHelp {

	Hashtable<MessageDialog, com.make.equo.ui.helper.provider.dialogs.MessageDialog> dialogos = new Hashtable<>();

	com.make.equo.ui.helper.provider.dialogs.MessageDialog dialog;

	pointcut buildMessage(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage,
			int dialogImageType, String[] dialogButtonLabels, int defaultIndex):
	args(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType,dialogButtonLabels, defaultIndex) &&
	execution(org.eclipse.jface.dialogs.MessageDialog.new(Shell, String, Image, String , int , String[], int));

	pointcut openMessage(MessageDialog message): 
        execution(int org.eclipse.jface.dialogs.MessageDialog.open()) &&
        target(message);

	pointcut openInformation(Shell parent, String title, String message):
        args(parent, title, message) &&
        execution(* org.eclipse.jface.dialogs.MessageDialog.openInformation(Shell, String, String));

	pointcut openError(Shell parent, String title, String message):
        args(parent, title, message) &&
        execution(* org.eclipse.jface.dialogs.MessageDialog.openError(Shell, String, String));

	pointcut openWarning(Shell parent, String title, String message):
        args(parent, title, message) &&
        execution(* org.eclipse.jface.dialogs.MessageDialog.openWarning(Shell, String, String));

	pointcut openQuestion(Shell parent, String title, String message):
        args(parent, title, message) &&
        execution(* org.eclipse.jface.dialogs.MessageDialog.openQuestion(Shell, String, String));

	pointcut openConfirm(Shell parent, String title, String message):
        args(parent, title, message) &&
        execution(* org.eclipse.jface.dialogs.MessageDialog.openConfirm(Shell, String, String));

//    pointcut openWithLabels(int kind, Shell parent, String title, String message, int style,
//         String... dialogButtonLabels):
//        args(kind, parent, title, message, style, dialogButtonLabels) &&
//        execution(* org.eclipse.jface.dialogs.MessageDialog.open(int, Shell, String, String, int));
//        
//    pointcut openWithoutLabels(int kind, Shell parent, String title, String message, int style):
//        args(kind, parent, title, message, style) &&
//        execution(* org.eclipse.jface.dialogs.MessageDialog.open(int, Shell, String, String, int));

	// MessageDialog advices ----------------------

	void around(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage,
			int dialogImageType, String[] dialogButtonLabels, int defaultIndex): buildMessage(parentShell, dialogTitle, dialogTitleImage, dialogMessage,
        dialogImageType,dialogButtonLabels, defaultIndex){
		this.dialog = new com.make.equo.ui.helper.provider.dialogs.MessageDialog(parentShell, dialogTitle,
				dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels, defaultIndex);
	}

	int around(MessageDialog message): openMessage(message){
		dialog.open();
		// System.out.println("--------> " + thisJoinPoint);
		System.out.println("It is not necessary use open() method to create the widget DialogMessage");
		return 0;
	}

	void around(Shell parent, String title, String message): openInformation(parent, title, message) {
		com.make.equo.ui.helper.provider.dialogs.MessageDialog.openInformation(parent, title, message);
	}

	void around(Shell parent, String title, String message): openError(parent, title, message) {
		com.make.equo.ui.helper.provider.dialogs.MessageDialog.openError(parent, title, message);
	}

	void around(Shell parent, String title, String message): openWarning(parent, title, message) {
		com.make.equo.ui.helper.provider.dialogs.MessageDialog.openWarning(parent, title, message);
	}

	boolean around(Shell parent, String title, String message): openQuestion(parent, title, message) {
		return com.make.equo.ui.helper.provider.dialogs.MessageDialog.openQuestion(parent, title, message);
	}

	boolean around(Shell parent, String title, String message): openConfirm(parent, title, message) {
		return com.make.equo.ui.helper.provider.dialogs.MessageDialog.openConfirm(parent, title, message);
	}

//     int around(int kind, Shell parent, String title, String message, int style, String[] dialogButtonLabels): openWithLabels(int kind, Shell parent, String title, String message, int style, String... dialogButtonLabels) {
//        return com.make.equo.ui.helper.provider.dialogs.MessageDialog.open(kind, parent, title, message, style, dialogButtonLabels);
//     }
//     
//     boolean around(int kind, Shell parent, String title, String message, int style): openWithoutLabels(int kind, Shell parent, String title, String message, int style) {
//        return com.make.equo.ui.helper.provider.dialogs.MessageDialog.open(kind, parent, title, message, style);
//     }

	// join points MessageDialogWithToggle ------------

	pointcut buildMessageToggle(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage,
			int dialogImageType, String[] dialogButtonLabels, int defaultIndex, String toggleMessage,
			boolean toggleState):
    args(parentShell, dialogTitle, dialogTitleImage, dialogMessage,
        dialogImageType,dialogButtonLabels, defaultIndex,toggleMessage, toggleState) &&
    execution(org.eclipse.jface.dialogs.MessageDialogWithToggle.new(Shell, String, Image, String , int, String[], int, String, boolean));

	pointcut buildMessageToggleHash(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage,
			int dialogImageType, LinkedHashMap<String, Integer> buttonLabelToIdMap, int defaultIndex,
			String toggleMessage, boolean toggleState):
    args(parentShell, dialogTitle, dialogTitleImage, dialogMessage,
        dialogImageType,buttonLabelToIdMap, defaultIndex,toggleMessage, toggleState) &&
    execution(org.eclipse.jface.dialogs.MessageDialogWithToggle.new(Shell, String, Image, String , int, LinkedHashMap<String, Integer>, int, String, boolean));

	pointcut openInformationToggle(Shell parent, String title, String message, String toggleMessage,
			boolean toggleState, IPreferenceStore store, String key):
        args(parent,title, message, toggleMessage,
            toggleState, store, key) &&
        execution( MessageDialogWithToggle org.eclipse.jface.dialogs.MessageDialogWithToggle.openInformation(Shell, String, String , String ,
            boolean, IPreferenceStore , String));

	pointcut openErrorToggle(Shell parent, String title, String message, String toggleMessage, boolean toggleState,
			IPreferenceStore store, String key):
        args(parent,title, message, toggleMessage,
            toggleState, store, key) &&
        execution( MessageDialogWithToggle org.eclipse.jface.dialogs.MessageDialogWithToggle.openError(Shell, String, String , String ,
            boolean, IPreferenceStore , String));

	pointcut openWarningToggle(Shell parent, String title, String message, String toggleMessage, boolean toggleState,
			IPreferenceStore store, String key):
        args(parent,title, message, toggleMessage,
            toggleState, store, key) &&
        execution( MessageDialogWithToggle org.eclipse.jface.dialogs.MessageDialogWithToggle.openWarning(Shell, String, String , String ,
            boolean, IPreferenceStore , String));

	pointcut openYesNoCancelQuestionToggle(Shell parent, String title, String message, String toggleMessage,
			boolean toggleState, IPreferenceStore store, String key):
        args(parent,title, message, toggleMessage,
            toggleState, store, key) &&
        execution( MessageDialogWithToggle org.eclipse.jface.dialogs.MessageDialogWithToggle.openYesNoCancelQuestion(Shell, String, String , String ,
            boolean, IPreferenceStore , String));

	pointcut openOkCancelConfirmToggle(Shell parent, String title, String message, String toggleMessage,
			boolean toggleState, IPreferenceStore store, String key):
        args(parent,title, message, toggleMessage,
            toggleState, store, key) &&
        execution( MessageDialogWithToggle org.eclipse.jface.dialogs.MessageDialogWithToggle.openOkCancelConfirm(Shell, String, String , String ,
            boolean, IPreferenceStore , String));

	pointcut openYesNoQuestionToggle(Shell parent, String title, String message, String toggleMessage,
			boolean toggleState, IPreferenceStore store, String key):
        args(parent,title, message, toggleMessage,
            toggleState, store, key) &&
        execution( MessageDialogWithToggle org.eclipse.jface.dialogs.MessageDialogWithToggle.openYesNoQuestion(Shell, String, String , String ,
            boolean, IPreferenceStore , String));

	pointcut openToggle(int kind, Shell parent, String title, String message, String toggleMessage, boolean toggleState,
			IPreferenceStore store, String key, int style):
        args(kind,parent,title, message, toggleMessage, toggleState, store, key, style) &&
        execution( MessageDialogWithToggle org.eclipse.jface.dialogs.MessageDialogWithToggle.open(int, Shell, String, String, String,
            boolean , IPreferenceStore , String , int ));

	pointcut openToggleHash(int kind, Shell parent, String title, String message, String toggleMessage,
			boolean toggleState, IPreferenceStore store, String key, int style,
			LinkedHashMap<String, Integer> buttonLabelToIdMap):
        args(kind,parent,title, message, toggleMessage, toggleState, store, key, style,buttonLabelToIdMap) &&
        execution( MessageDialogWithToggle org.eclipse.jface.dialogs.MessageDialogWithToggle.open(int, Shell, String, String, String,
            boolean , IPreferenceStore , String , int, LinkedHashMap<String, Integer>));

	// advices MessageDialogWithToggle -------------------------

	void around(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage,
			int dialogImageType, String[] dialogButtonLabels, int defaultIndex, String toggleMessage,
			boolean toggleState): buildMessageToggle(parentShell, dialogTitle, dialogTitleImage, dialogMessage,
        dialogImageType,dialogButtonLabels, defaultIndex,toggleMessage, toggleState){
		this.dialog = new com.make.equo.ui.helper.provider.dialogs.MessageDialogWithToggle(parentShell, dialogTitle,
				dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels, defaultIndex, toggleMessage,
				toggleState);
	}

	void around(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage,
			int dialogImageType, LinkedHashMap<String, Integer> buttonLabelToIdMap, int defaultIndex,
			String toggleMessage, boolean toggleState): buildMessageToggleHash(parentShell, dialogTitle, dialogTitleImage, dialogMessage,
        dialogImageType, buttonLabelToIdMap, defaultIndex,toggleMessage, toggleState){
		this.dialog = new com.make.equo.ui.helper.provider.dialogs.MessageDialogWithToggle(parentShell, dialogTitle,
				dialogTitleImage, dialogMessage, dialogImageType, buttonLabelToIdMap, defaultIndex, toggleMessage,
				toggleState);
	}

	MessageDialogWithToggle around(Shell parent, String title, String message, String toggleMessage,
			boolean toggleState, IPreferenceStore store, String key): openInformationToggle(parent,title, message,toggleMessage,toggleState, store, key) {
		com.make.equo.ui.helper.provider.dialogs.MessageDialogWithToggle.openInformation(parent, title, message,
				toggleMessage, toggleState, store, key);
		return null;
	}

	MessageDialogWithToggle around(Shell parent, String title, String message, String toggleMessage,
			boolean toggleState, IPreferenceStore store, String key): openErrorToggle(parent,title, message,toggleMessage,toggleState, store, key) {
		com.make.equo.ui.helper.provider.dialogs.MessageDialogWithToggle.openError(parent, title, message,
				toggleMessage, toggleState, store, key);
		return null;
	}

	MessageDialogWithToggle around(Shell parent, String title, String message, String toggleMessage,
			boolean toggleState, IPreferenceStore store, String key): openWarningToggle(parent,title, message,toggleMessage,toggleState, store, key) {
		com.make.equo.ui.helper.provider.dialogs.MessageDialogWithToggle.openWarning(parent, title, message,
				toggleMessage, toggleState, store, key);
		return null;
	}

	MessageDialogWithToggle around(Shell parent, String title, String message, String toggleMessage,
			boolean toggleState, IPreferenceStore store, String key): openYesNoCancelQuestionToggle(parent,title, message,toggleMessage,toggleState, store, key) {
		com.make.equo.ui.helper.provider.dialogs.MessageDialogWithToggle.openYesNoCancelQuestion(parent, title, message,
				toggleMessage, toggleState, store, key);
		return null;
	}

	MessageDialogWithToggle around(Shell parent, String title, String message, String toggleMessage,
			boolean toggleState, IPreferenceStore store, String key): openOkCancelConfirmToggle(parent,title, message,toggleMessage,toggleState, store, key) {
		com.make.equo.ui.helper.provider.dialogs.MessageDialogWithToggle.openOkCancelConfirm(parent, title, message,
				toggleMessage, toggleState, store, key);
		return null;
	}

	MessageDialogWithToggle around(Shell parent, String title, String message, String toggleMessage,
			boolean toggleState, IPreferenceStore store, String key): openYesNoQuestionToggle(parent,title, message,toggleMessage,toggleState, store, key) {
		com.make.equo.ui.helper.provider.dialogs.MessageDialogWithToggle.openYesNoQuestion(parent, title, message,
				toggleMessage, toggleState, store, key);
		return null;
	}

	MessageDialogWithToggle around(int kind, Shell parent, String title, String message, String toggleMessage,
			boolean toggleState, IPreferenceStore store, String key, int style): openToggle(kind,parent,title, message, toggleMessage, toggleState, store, key, style) {
		com.make.equo.ui.helper.provider.dialogs.MessageDialogWithToggle.open(kind, parent, title, message,
				toggleMessage, toggleState, store, key, style);
		return null;
	}

	MessageDialogWithToggle around(int kind, Shell parent, String title, String message, String toggleMessage,
			boolean toggleState, IPreferenceStore store, String key, int style,
			LinkedHashMap<String, Integer> buttonLabelToIdMap): openToggleHash(kind,parent,title, message, toggleMessage, toggleState, store, key, style,buttonLabelToIdMap) {
		com.make.equo.ui.helper.provider.dialogs.MessageDialogWithToggle.open(kind, parent, title, message,
				toggleMessage, toggleState, store, key, style, buttonLabelToIdMap);
		return null;
	}

}