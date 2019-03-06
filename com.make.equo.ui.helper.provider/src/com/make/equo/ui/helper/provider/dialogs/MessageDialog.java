package com.make.equo.ui.helper.provider.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.make.equo.ui.helper.provider.dialogs.util.IDialogConstants;
import com.make.equo.ui.helper.provider.model.MButton;
import com.make.equo.ui.helper.provider.model.MWebDialog;
import com.make.equo.ui.helper.provider.model.ModelElementInjector;
import com.make.equo.ui.helper.provider.model.WebdialogFactory;

/**
 * A dialog for showing messages to the user.
 * <p>
 * </p>
 * <p>
 * <strong>Note:</strong> This class does not use button IDs from
 * IDialogConstants. Instead, the ID is the index of the button in the supplied
 * array.
 * </p>
 */

public class MessageDialog {

   static {
      try {
         BundleContext bndContext = FrameworkUtil.getBundle(ModelElementInjector.class).getBundleContext();
         ServiceReference<ModelElementInjector> svcReference = bndContext
               .getServiceReference(ModelElementInjector.class);
         injector = bndContext.getService(svcReference);
      } catch (Exception e) {
         System.out.println("Couldn't retrieve model injector");
      }
   }

   /**
    * Constant for no image (value 0).
    *
    * @see #MessageDialog(Shell, String, Image, String, int, int, String...)
    */
   public final static int NONE = 0;

   /**
    * Constant for the error image, or a simple dialog with the error image and a
    * single OK button (value 1).
    *
    * @see #MessageDialog(Shell, String, Image, String, int, int, String...)
    * @see #open(int, Shell, String, String, int)
    */
   public final static int ERROR = 1;

   /**
    * Constant for the info image, or a simple dialog with the info image and a
    * single OK button (value 2).
    *
    * @see #MessageDialog(Shell, String, Image, String, int, int, String...)
    * @see #open(int, Shell, String, String, int)
    */
   public final static int INFORMATION = 2;

   /**
    * Constant for the question image, or a simple dialog with the question image
    * and Yes/No buttons (value 3).
    *
    * @see #MessageDialog(Shell, String, Image, String, int, int, String...)
    * @see #open(int, Shell, String, String, int)
    */
   public final static int QUESTION = 3;

   /**
    * Constant for the warning image, or a simple dialog with the warning image and
    * a single OK button (value 4).
    *
    * @see #MessageDialog(Shell, String, Image, String, int, int, String...)
    * @see #open(int, Shell, String, String, int)
    */
   public final static int WARNING = 4;

   /**
    * Constant for a simple dialog with the question image and OK/Cancel buttons
    * (value 5).
    *
    * @see #open(int, Shell, String, String, int)
    * @since 3.5
    */
   public final static int CONFIRM = 5;

   /**
    * Constant for a simple dialog with the question image and Yes/No/Cancel
    * buttons (value 6).
    *
    * @see #open(int, Shell, String, String, int)
    * @since 3.5
    */
   public final static int QUESTION_WITH_CANCEL = 6;

   /**
    * Used to hook this dialog to the application model
    */
   private static ModelElementInjector injector;

//   /**
//    * Index into <code>buttonLabels</code> of the default button.
//    */
//   private int defaultButtonIndex;

   /**
    * Dialog title (a localized string).
    */
   private String title;

   /**
    * Dialog message (a string)
    */
   private String message;

   private MWebDialog thisDialog;

   /**
    * Create a message dialog. Note that the dialog will have no visual
    * representation (no widgets) until it is told to open.
    * <p>
    * The labels of the buttons to appear in the button bar are supplied in this
    * constructor as an array. The <code>open</code> method will return the index
    * of the label in this array corresponding to the button that was pressed to
    * close the dialog.
    * </p>
    * <p>
    * <strong>Note:</strong> If the dialog was dismissed without pressing a button
    * (ESC key, close box, etc.) then {@link SWT#DEFAULT} is returned. Note that
    * the <code>open</code> method blocks.
    * </p>
    * As of 3.11 you can also use the other constructor which is based on varargs
    *
    * @param parentShell
    *                           the parent shell, or <code>null</code> to create a
    *                           top-level shell
    * @param dialogTitle
    *                           the dialog title, or <code>null</code> if none
    * @param dialogTitleImage
    *                           the dialog title image, or <code>null</code> if
    *                           none
    * @param dialogMessage
    *                           the dialog message
    * @param dialogImageType
    *                           one of the following values:
    *                           <ul>
    *                           <li><code>MessageDialog.NONE</code> for a dialog
    *                           with no image</li>
    *                           <li><code>MessageDialog.ERROR</code> for a dialog
    *                           with an error image</li>
    *                           <li><code>MessageDialog.INFORMATION</code> for a
    *                           dialog with an information image</li>
    *                           <li><code>MessageDialog.QUESTION </code> for a
    *                           dialog with a question image</li>
    *                           <li><code>MessageDialog.WARNING</code> for a dialog
    *                           with a warning image</li>
    *                           </ul>
    * @param dialogButtonLabels
    *                           an array of labels for the buttons in the button
    *                           bar
    * @param defaultIndex
    *                           the index in the button label array of the default
    *                           button
    */
   public MessageDialog(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage,
         int dialogImageType, String[] dialogButtonLabels, int defaultIndex) {
      init(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, defaultIndex,
            dialogButtonLabels);
   }

   /**
    * Create a message dialog. Note that the dialog will have no visual
    * representation (no widgets) until it is told to open.
    * <p>
    * The labels of the buttons to appear in the button bar are supplied in this
    * constructor as a varargs of Strings. The <code>open</code> method will return
    * the index of the label in this array corresponding to the button that was
    * pressed to close the dialog.
    * </p>
    * <p>
    * <strong>Note:</strong> If the dialog was dismissed without pressing a button
    * (ESC key, close box, etc.) then {@link SWT#DEFAULT} is returned. Note that
    * the <code>open</code> method blocks.
    * </p>
    *
    * @param parentShell
    *                           the parent shell, or <code>null</code> to create a
    *                           top-level shell
    * @param dialogTitle
    *                           the dialog title, or <code>null</code> if none
    * @param dialogTitleImage
    *                           the dialog title image, or <code>null</code> if
    *                           none
    * @param dialogMessage
    *                           the dialog message
    * @param dialogImageType
    *                           one of the following values:
    *                           <ul>
    *                           <li><code>MessageDialog.NONE</code> for a dialog
    *                           with no image</li>
    *                           <li><code>MessageDialog.ERROR</code> for a dialog
    *                           with an error image</li>
    *                           <li><code>MessageDialog.INFORMATION</code> for a
    *                           dialog with an information image</li>
    *                           <li><code>MessageDialog.QUESTION </code> for a
    *                           dialog with a question image</li>
    *                           <li><code>MessageDialog.WARNING</code> for a dialog
    *                           with a warning image</li>
    *                           </ul>
    * @param defaultIndex
    *                           the index in the button label array of the default
    *                           button
    * @param dialogButtonLabels
    *                           varargs of Strings for the button labels in the
    *                           button bar
    */
   public MessageDialog(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage,
         int dialogImageType, int defaultIndex, String... dialogButtonLabels) {
      init(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, defaultIndex,
            dialogButtonLabels);
   }

   private void init(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage,
         int dialogImageType, int defaultIndex, String... dialogButtonLabels) {
      this.title = dialogTitle;
      this.message = dialogMessage;
//      this.defaultButtonIndex = defaultIndex;

      initWebDialog(parentShell, title, message, dialogImageType, dialogButtonLabels);
   }

   private void initWebDialog(Shell parentShell, String title, String message, int dialogImageType, String... dialogButtonLabels) {
      if (thisDialog == null) {
         thisDialog = WebdialogFactory.eINSTANCE.createMWebDialog();
      }
      thisDialog.setTitle(title);
      thisDialog.setMessage(message);
      thisDialog.setType(dialogImageType);
      thisDialog.setParentShell(parentShell);
      thisDialog.setBlocker(true);
      thisDialog.setVisible(true);
      thisDialog.setOnTop(true);
      thisDialog.setToBeRendered(true);
      
      int i = 0;
      for (String label : dialogButtonLabels) {
         MButton button = WebdialogFactory.eINSTANCE.createMButton();
         button.setLabel(label);
         button.setAction(i);
         thisDialog.getButtons().add(button);
         i++;
      }
   }

   protected MWebDialog getDialog() {
      return thisDialog;
   }

   /**
    * Opens this message dialog, creating it first if it has not yet been created.
    * <p>
    * This method waits until the dialog is closed by the end user, and then it
    * returns the dialog's return code. The dialog's return code is either the
    * index of the button the user pressed, or {@link SWT#DEFAULT} if the dialog
    * has been closed by other means.
    * </p>
    *
    * @return the return code
    */
   public int open() {
      injector.attachMWebDialog(thisDialog);
      return getResponse();
   }

   /**
    * Convenience method to open a simple dialog as specified by the
    * <code>kind</code> flag.
    *
    * @param kind
    *                the kind of dialog to open, one of {@link #ERROR},
    *                {@link #INFORMATION}, {@link #QUESTION}, {@link #WARNING},
    *                {@link #CONFIRM}, or {@link #QUESTION_WITH_CANCEL}.
    * @param parent
    *                the parent shell of the dialog, or <code>null</code> if none
    * @param title
    *                the dialog's title, or <code>null</code> if none
    * @param message
    *                the message
    * @param style
    *                {@link SWT#NONE} for a default dialog, or {@link SWT#SHEET}
    *                for a dialog with sheet behavior
    * @return <code>true</code> if the user presses the OK or Yes button,
    *         <code>false</code> otherwise
    */
   public static boolean open(int kind, Shell parent, String title, String message, int style) {
      MessageDialog dialog = new MessageDialog(parent, title, null, message, kind, 0, getButtonLabels(kind));
      injector.attachMWebDialog(dialog.getDialog());
      return dialog.getResponse() == IDialogConstants.OK_ID;
   }

   public int getResponse() {
      return thisDialog.getResponse();
   }

   /**
    * Method to open a simple dialog as specified by the <code>kind</code> flag.
    * This method accepts varargs of String to set custom button labels. Use this
    * method if you want to override the default labels.
    *
    * @param kind
    *                           the kind of dialog to open, one of {@link #ERROR},
    *                           {@link #INFORMATION}, {@link #QUESTION},
    *                           {@link #WARNING}, {@link #CONFIRM}, or
    *                           {@link #QUESTION_WITH_CANCEL}.
    * @param parent
    *                           the parent shell of the dialog, or
    *                           <code>null</code> if none
    * @param title
    *                           the dialog's title, or <code>null</code> if none
    * @param message
    *                           the message
    * @param style
    *                           {@link SWT#NONE} for a default dialog, or
    *                           {@link SWT#SHEET} for a dialog with sheet behavior
    * @param dialogButtonLabels
    *                           varargs of Strings for the button labels in the
    *                           button bar
    * @return the index of the button that was pressed.
    */
   public static int open(int kind, Shell parent, String title, String message, int style,
         String... dialogButtonLabels) {
      MessageDialog dialog = new MessageDialog(parent, title, null, message, kind, 0, dialogButtonLabels);
      injector.attachMWebDialog(dialog.getDialog());
      return dialog.getResponse();
   }

   static String[] getButtonLabels(int kind) {
      String[] dialogButtonLabels;
      switch (kind) {
         case ERROR:
         case INFORMATION:
         case WARNING: {
            dialogButtonLabels = new String[] { IDialogConstants.OK_LABEL_KEY };
            break;
         }
         case CONFIRM: {
            dialogButtonLabels = new String[] { IDialogConstants.OK_LABEL_KEY, IDialogConstants.CANCEL_LABEL_KEY };
            break;
         }
         case QUESTION: {
            dialogButtonLabels = new String[] { IDialogConstants.YES_LABEL_KEY, IDialogConstants.NO_LABEL_KEY };
            break;
         }
         case QUESTION_WITH_CANCEL: {
            dialogButtonLabels = new String[] { IDialogConstants.YES_LABEL_KEY, IDialogConstants.NO_LABEL_KEY,
                  IDialogConstants.CANCEL_LABEL_KEY };
            break;
         }
         default: {
            throw new IllegalArgumentException("Illegal value for kind in MessageDialog.open()"); //$NON-NLS-1$
         }
      }
      return dialogButtonLabels;
   }

   /**
    * Convenience method to open a simple confirm (OK/Cancel) dialog.
    *
    * @param parent
    *                the parent shell of the dialog, or <code>null</code> if none
    * @param title
    *                the dialog's title, or <code>null</code> if none
    * @param message
    *                the message
    * @return <code>true</code> if the user presses the OK button,
    *         <code>false</code> otherwise
    */
   public static boolean openConfirm(Shell parent, String title, String message) {
      return open(CONFIRM, parent, title, message, SWT.NONE);
   }

   /**
    * Convenience method to open a standard error dialog.
    *
    * @param parent
    *                the parent shell of the dialog, or <code>null</code> if none
    * @param title
    *                the dialog's title, or <code>null</code> if none
    * @param message
    *                the message
    */
   public static void openError(Shell parent, String title, String message) {
      open(ERROR, parent, title, message, SWT.NONE);
   }

   /**
    * Convenience method to open a standard information dialog.
    *
    * @param parent
    *                the parent shell of the dialog, or <code>null</code> if none
    * @param title
    *                the dialog's title, or <code>null</code> if none
    * @param message
    *                the message
    */
   public static void openInformation(Shell parent, String title, String message) {
      open(INFORMATION, parent, title, message, SWT.NONE);
   }

   /**
    * Convenience method to open a simple Yes/No question dialog.
    *
    * @param parent
    *                the parent shell of the dialog, or <code>null</code> if none
    * @param title
    *                the dialog's title, or <code>null</code> if none
    * @param message
    *                the message
    * @return <code>true</code> if the user presses the Yes button,
    *         <code>false</code> otherwise
    */
   public static boolean openQuestion(Shell parent, String title, String message) {
      return open(QUESTION, parent, title, message, SWT.NONE);
   }

   /**
    * Convenience method to open a standard warning dialog.
    *
    * @param parent
    *                the parent shell of the dialog, or <code>null</code> if none
    * @param title
    *                the dialog's title, or <code>null</code> if none
    * @param message
    *                the message
    */
   public static void openWarning(Shell parent, String title, String message) {
      open(WARNING, parent, title, message, SWT.NONE);
   }

}
