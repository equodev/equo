package com.make.equo.aspects;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Image;


public aspect DialogHelp {

   pointcut buildMessage(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage, int dialogImageType, String[] dialogButtonLabels, int defaultIndex):
   args(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType,dialogButtonLabels, defaultIndex) &&
  execution(org.eclipse.jface.dialogs.MessageDialog.new(Shell, String, Image, String , int , String[], int));

    pointcut openMessage(): 
        execution(* org.eclipse.jface.dialogs.MessageDialog.open());

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
    
     void around(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage, int dialogImageType, String[] dialogButtonLabels, int defaultIndex): buildMessage(parentShell, dialogTitle, dialogTitleImage, dialogMessage,
        dialogImageType,dialogButtonLabels, defaultIndex){
        com.make.equo.ui.helper.provider.dialogs.MessageDialog dialog = new com.make.equo.ui.helper.provider.dialogs.MessageDialog(parentShell, dialogTitle, dialogTitleImage, dialogMessage,
        dialogImageType,dialogButtonLabels, defaultIndex);
        dialog.open();
    }
    
   void around(): openMessage(){
        System.out.println("It is not necessary use open() method to create the widget DialogMessage");        
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


    // join points MessageDialogWithToggle
    pointcut buildMessageToggle(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage,
         int dialogImageType, String[] dialogButtonLabels, int defaultIndex, String toggleMessage, boolean toggleState):
    args(parentShell, dialogTitle, dialogTitleImage, dialogMessage,
        dialogImageType,dialogButtonLabels, defaultIndex,toggleMessage, toggleState) &&
    execution(org.eclipse.jface.dialogs.MessageDialogWithToggle.new(Shell, String, Image, String , int, String[], int, String, boolean));

    pointcut openInformationToggle(Shell parent, String title, String message):
        args(parent, title, message) &&
        execution(* org.eclipse.jface.dialogs.MessageDialogWithToggle.openInformation(Shell, String, String));

    pointcut openErrorToggle(Shell parent, String title, String message):
        args(parent, title, message) &&
        execution(* org.eclipse.jface.dialogs.MessageDialogWithToggle.openError(Shell, String, String));

    pointcut openWarningToggle(Shell parent, String title, String message):
        args(parent, title, message) &&
        execution(* org.eclipse.jface.dialogs.MessageDialogWithToggle.openWarning(Shell, String, String));
        
    pointcut openQuestionToggle(Shell parent, String title, String message):
        args(parent, title, message) &&
        execution(* org.eclipse.jface.dialogs.MessageDialogWithToggle.openQuestion(Shell, String, String));

    pointcut openConfirmToggle(Shell parent, String title, String message):
        args(parent, title, message) &&
        execution(* org.eclipse.jface.dialogs.MessageDialogWithToggle.openConfirm(Shell, String, String));



    // advices MessageDialogWithToggle
    void around(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage, int dialogImageType, String[] dialogButtonLabels, int defaultIndex, String toggleMessage, boolean toggleState): buildMessageToggle(parentShell, dialogTitle, dialogTitleImage, dialogMessage,
        dialogImageType,dialogButtonLabels, defaultIndex,toggleMessage, toggleState){
        com.make.equo.ui.helper.provider.dialogs.MessageDialogWithToggle message = new com.make.equo.ui.helper.provider.dialogs.MessageDialogWithToggle(parentShell, dialogTitle, dialogTitleImage, dialogMessage,
        dialogImageType,dialogButtonLabels, defaultIndex, toggleMessage,toggleState);
        message.open();
    }

    void around(Shell parent, String title, String message): openInformationToggle(parent, title, message) {
        com.make.equo.ui.helper.provider.dialogs.MessageDialog.openInformation(parent, title, message);
     }
     
     void around(Shell parent, String title, String message): openErrorToggle(parent, title, message) {
        com.make.equo.ui.helper.provider.dialogs.MessageDialogWithToggle.openError(parent, title, message);
     }
     
     void around(Shell parent, String title, String message): openWarningToggle(parent, title, message) {
        com.make.equo.ui.helper.provider.dialogs.MessageDialogWithToggle.openWarning(parent, title, message);
     }
     
     boolean around(Shell parent, String title, String message): openQuestionToggle(parent, title, message) {
        return com.make.equo.ui.helper.provider.dialogs.MessageDialogWithToggle.openQuestion(parent, title, message);
     }
     
     boolean around(Shell parent, String title, String message): openConfirmToggle(parent, title, message) {
        return com.make.equo.ui.helper.provider.dialogs.MessageDialogWithToggle.openConfirm(parent, title, message);
     }
     
}