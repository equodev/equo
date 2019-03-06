package com.make.equo.aspects;

import org.eclipse.swt.widgets.Shell;

public aspect DialogHelp {

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
     
}