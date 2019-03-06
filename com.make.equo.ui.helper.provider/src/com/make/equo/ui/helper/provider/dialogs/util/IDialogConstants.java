package com.make.equo.ui.helper.provider.dialogs.util;

public interface IDialogConstants {

   // button ids
   /**
    * Button id for an "Ok" button (value 0).
    */
   public int OK_ID = 0;

   /**
    * Button id for a "Cancel" button (value 1).
    */
   public int CANCEL_ID = 1;

   /**
    * Button id for a "Yes" button (value 2).
    */
   public int YES_ID = 2;

   /**
    * Button id for a "No" button (value 3).
    */
   public int NO_ID = 3;

   /**
    * Button id for a "Yes to All" button (value 4).
    */
   public int YES_TO_ALL_ID = 4;

   /**
    * Button id for a "Skip" button (value 5).
    */
   public int SKIP_ID = 5;

   /**
    * Button id for a "Stop" button (value 6).
    */
   public int STOP_ID = 6;

   /**
    * Button id for an "Abort" button (value 7).
    */
   public int ABORT_ID = 7;

   /**
    * Button id for a "Retry" button (value 8).
    */
   public int RETRY_ID = 8;

   /**
    * Button id for an "Ignore" button (value 9).
    */
   public int IGNORE_ID = 9;

   /**
    * Button id for a "Proceed" button (value 10).
    */
   public int PROCEED_ID = 10;

   /**
    * Button id for an "Open" button (value 11).
    */
   public int OPEN_ID = 11;

   /**
    * Button id for a "Close" button (value 12).
    */
   public int CLOSE_ID = 12;

   /**
    * Button id for a "Details" button (value 13).
    */
   public int DETAILS_ID = 13;

   /**
    * Button id for a "Back" button (value 14).
    */
   public int BACK_ID = 14;

   /**
    * Button id for a "Next" button (value 15).
    */
   public int NEXT_ID = 15;

   /**
    * Button id for a "Finish" button (value 16).
    */
   public int FINISH_ID = 16;

   /**
    * Button id for a "Help" button (value 17).
    */
   public int HELP_ID = 17;

   /**
    * Button id for a "Select All" button (value 18).
    */
   public int SELECT_ALL_ID = 18;

   /**
    * Button id for a "Deselect All" button (value 19).
    */
   public int DESELECT_ALL_ID = 19;

   /**
    * Button id for a "Select types" button (value 20).
    */
   public int SELECT_TYPES_ID = 20;

   /**
    * Button id for a "No to All" button (value 21).
    */
   public int NO_TO_ALL_ID = 21;

   // button labels
   /**
    * The key used to retrieve the label for OK buttons. Clients should use the
    * pattern <code>JFaceResources.getString(IDialogLabelKeys.OK_LABEL_KEY)</code>
    * to retrieve the label dynamically when using multiple locales.
    *
    * @since 3.7
    */
   public String OK_LABEL_KEY = "Confirm"; //$NON-NLS-1$

   /**
    * The key used to retrieve the label for cancel buttons. Clients should use the
    * pattern
    * <code>JFaceResources.getString(IDialogLabelKeys.CANCEL_LABEL_KEY)</code> to
    * retrieve the label dynamically when using multiple locales.
    * 
    * @since 3.7
    */
   public String CANCEL_LABEL_KEY = "Cancel"; //$NON-NLS-1$

   /**
    * The key used to retrieve the label for yes buttons. Clients should use the
    * pattern <code>JFaceResources.getString(IDialogLabelKeys.YES_LABEL_KEY)</code>
    * to retrieve the label dynamically when using multiple locales.
    * 
    * @since 3.7
    */
   public String YES_LABEL_KEY = "Yes"; //$NON-NLS-1$

   /**
    * The key used to retrieve the label for no buttons. Clients should use the
    * pattern <code>JFaceResources.getString(IDialogLabelKeys.NO_LABEL_KEY)</code>
    * to retrieve the label dynamically when using multiple locales.
    * 
    * @since 3.7
    */
   public String NO_LABEL_KEY = "No"; //$NON-NLS-1$

   /**
    * The key used to retrieve the label for no to all buttons. Clients should use
    * the pattern
    * <code>JFaceResources.getString(IDialogLabelKeys.NO_TO_ALL_LABEL_KEY)</code>
    * to retrieve the label dynamically when using multiple locales.
    * 
    * @since 3.7
    */
   public String NO_TO_ALL_LABEL_KEY = "No to all"; //$NON-NLS-1$

   /**
    * The key used to retrieve the label for yes to all buttons. Clients should use
    * the pattern
    * <code>JFaceResources.getString(IDialogLabelKeys.YES_TO_ALL_LABEL_KEY)</code>
    * to retrieve the label dynamically when using multiple locales.
    * 
    * @since 3.7
    */
   public String YES_TO_ALL_LABEL_KEY = "Yes to all"; //$NON-NLS-1$

   /**
    * The key used to retrieve the label for skip buttons. Clients should use the
    * pattern
    * <code>JFaceResources.getString(IDialogLabelKeys.SKIP_LABEL_KEY)</code> to
    * retrieve the label dynamically when using multiple locales.
    * 
    * @since 3.7
    */
   public String SKIP_LABEL_KEY = "Skip"; //$NON-NLS-1$

   /**
    * The key used to retrieve the label for stop buttons. Clients should use the
    * pattern
    * <code>JFaceResources.getString(IDialogLabelKeys.STOP_LABEL_KEY)</code> to
    * retrieve the label dynamically when using multiple locales.
    * 
    * @since 3.7
    */
   public String STOP_LABEL_KEY = "Stop"; //$NON-NLS-1$

   /**
    * The key used to retrieve the label for abort buttons. Clients should use the
    * pattern
    * <code>JFaceResources.getString(IDialogLabelKeys.ABORT_LABEL_KEY)</code> to
    * retrieve the label dynamically when using multiple locales.
    * 
    * @since 3.7
    */
   public String ABORT_LABEL_KEY = "Abort"; //$NON-NLS-1$

   /**
    * The key used to retrieve the label for retry buttons. Clients should use the
    * pattern
    * <code>JFaceResources.getString(IDialogLabelKeys.RETRY_LABEL_KEY)</code> to
    * retrieve the label dynamically when using multiple locales.
    * 
    * @since 3.7
    */
   public String RETRY_LABEL_KEY = "Retry"; //$NON-NLS-1$

   /**
    * The key used to retrieve the label for ignore buttons. Clients should use the
    * pattern
    * <code>JFaceResources.getString(IDialogLabelKeys.IGNORE_LABEL_KEY)</code> to
    * retrieve the label dynamically when using multiple locales.
    * 
    * @since 3.7
    */
   public String IGNORE_LABEL_KEY = "Ignore"; //$NON-NLS-1$

   /**
    * The key used to retrieve the label for proceed buttons. Clients should use
    * the pattern
    * <code>JFaceResources.getString(IDialogLabelKeys.PROCEED_LABEL_KEY)</code> to
    * retrieve the label dynamically when using multiple locales.
    * 
    * @since 3.7
    */
   public String PROCEED_LABEL_KEY = "Proceed"; //$NON-NLS-1$

   /**
    * The key used to retrieve the label for open buttons. Clients should use the
    * pattern
    * <code>JFaceResources.getString(IDialogLabelKeys.OPEN_LABEL_KEY)</code> to
    * retrieve the label dynamically when using multiple locales.
    * 
    * @since 3.7
    */
   public String OPEN_LABEL_KEY = "Open"; //$NON-NLS-1$

   /**
    * The key used to retrieve the label for close buttons. Clients should use the
    * pattern
    * <code>JFaceResources.getString(IDialogLabelKeys.CLOSE_LABEL_KEY)</code> to
    * retrieve the label dynamically when using multiple locales.
    * 
    * @since 3.7
    */
   public String CLOSE_LABEL_KEY = "Close"; //$NON-NLS-1$

   /**
    * The key used to retrieve the label for show details buttons. Clients should
    * use the pattern
    * <code>JFaceResources.getString(IDialogLabelKeys.SHOW_DETAILS_LABEL_KEY)</code>
    * to retrieve the label dynamically when using multiple locales.
    * 
    * @since 3.7
    */
   public String SHOW_DETAILS_LABEL_KEY = "Show details"; //$NON-NLS-1$

   /**
    * The key used to retrieve the label for hide details buttons. Clients should
    * use the pattern
    * <code>JFaceResources.getString(IDialogLabelKeys.HIDE_DETAILS_LABEL_KEY)</code>
    * to retrieve the label dynamically when using multiple locales.
    * 
    * @since 3.7
    */
   public String HIDE_DETAILS_LABEL_KEY = "Hide details"; //$NON-NLS-1$

   /**
    * The key used to retrieve the label for back buttons. Clients should use the
    * pattern
    * <code>JFaceResources.getString(IDialogLabelKeys.BACK_LABEL_KEY)</code> to
    * retrieve the label dynamically when using multiple locales.
    * 
    * @since 3.7
    */
   public String BACK_LABEL_KEY = "backButton"; //$NON-NLS-1$

   /**
    * The key used to retrieve the label for next buttons. Clients should use the
    * pattern
    * <code>JFaceResources.getString(IDialogLabelKeys.NEXT_LABEL_KEY)</code> to
    * retrieve the label dynamically when using multiple locales.
    * 
    * @since 3.7
    */
   public String NEXT_LABEL_KEY = "nextButton"; //$NON-NLS-1$

   /**
    * The key used to retrieve the label for finish buttons. Clients should use the
    * pattern
    * <code>JFaceResources.getString(IDialogLabelKeys.FINISH_LABEL_KEY)</code> to
    * retrieve the label dynamically when using multiple locales.
    * 
    * @since 3.7
    */
   public String FINISH_LABEL_KEY = "Finish"; //$NON-NLS-1$

   /**
    * The key used to retrieve the label for help buttons. Clients should use the
    * pattern
    * <code>JFaceResources.getString(IDialogLabelKeys.HELP_LABEL_KEY)</code> to
    * retrieve the label dynamically when using multiple locales.
    * 
    * @since 3.7
    */
   public String HELP_LABEL_KEY = "Help"; //$NON-NLS-1$

}
