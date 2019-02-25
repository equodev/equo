/**
 */
package com.make.equo.ui.helper.provider.model.impl;

import java.util.Collection;

import org.eclipse.e4.ui.model.application.ui.basic.impl.WindowImpl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.swt.SWT;

import com.make.equo.ui.helper.provider.dialogs.util.IDialogConstants;
import com.make.equo.ui.helper.provider.model.MButton;
import com.make.equo.ui.helper.provider.model.MWebDialog;
import com.make.equo.ui.helper.provider.model.WebdialogPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MWeb Dialog</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.make.equo.ui.helper.provider.model.impl.MWebDialogImpl#getButtons <em>Buttons</em>}</li>
 *   <li>{@link com.make.equo.ui.helper.provider.model.impl.MWebDialogImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link com.make.equo.ui.helper.provider.model.impl.MWebDialogImpl#getMessage <em>Message</em>}</li>
 *   <li>{@link com.make.equo.ui.helper.provider.model.impl.MWebDialogImpl#getParentShell <em>Parent Shell</em>}</li>
 *   <li>{@link com.make.equo.ui.helper.provider.model.impl.MWebDialogImpl#isBlocker <em>Blocker</em>}</li>
 *   <li>{@link com.make.equo.ui.helper.provider.model.impl.MWebDialogImpl#getResponse <em>Response</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MWebDialogImpl extends WindowImpl implements MWebDialog {
   /**
    * The cached value of the '{@link #getButtons() <em>Buttons</em>}' reference list.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #getButtons()
    * @generated
    * @ordered
    */
   protected EList<MButton> buttons;

   /**
    * The default value of the '{@link #getTitle() <em>Title</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #getTitle()
    * @generated
    * @ordered
    */
   protected static final String TITLE_EDEFAULT = "";

   /**
    * The cached value of the '{@link #getTitle() <em>Title</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #getTitle()
    * @generated
    * @ordered
    */
   protected String title = TITLE_EDEFAULT;

   /**
    * The default value of the '{@link #getMessage() <em>Message</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #getMessage()
    * @generated
    * @ordered
    */
   protected static final String MESSAGE_EDEFAULT = "";

   /**
    * The cached value of the '{@link #getMessage() <em>Message</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #getMessage()
    * @generated
    * @ordered
    */
   protected String message = MESSAGE_EDEFAULT;

   /**
    * The default value of the '{@link #getParentShell() <em>Parent Shell</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #getParentShell()
    * @generated
    * @ordered
    */
   protected static final Object PARENT_SHELL_EDEFAULT = null;

   /**
    * The cached value of the '{@link #getParentShell() <em>Parent Shell</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #getParentShell()
    * @generated
    * @ordered
    */
   protected Object parentShell = PARENT_SHELL_EDEFAULT;

   /**
    * The default value of the '{@link #isBlocker() <em>Blocker</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #isBlocker()
    * @generated
    * @ordered
    */
   protected static final boolean BLOCKER_EDEFAULT = false;

   /**
    * The cached value of the '{@link #isBlocker() <em>Blocker</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #isBlocker()
    * @generated
    * @ordered
    */
   protected boolean blocker = BLOCKER_EDEFAULT;

   /**
    * The default value of the '{@link #getResponse() <em>Response</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #getResponse()
    * @generated
    * @ordered
    */
   protected static final int RESPONSE_EDEFAULT = SWT.DEFAULT;

   /**
    * The cached value of the '{@link #getResponse() <em>Response</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #getResponse()
    * @generated
    * @ordered
    */
   protected int response = RESPONSE_EDEFAULT;

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   protected MWebDialogImpl() {
      super();
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   protected EClass eStaticClass() {
      return WebdialogPackage.Literals.MWEB_DIALOG;
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public EList<MButton> getButtons() {
      if (buttons == null) {
         buttons = new EObjectResolvingEList<MButton>(MButton.class, this, WebdialogPackage.MWEB_DIALOG__BUTTONS);
      }
      return buttons;
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public String getTitle() {
      return title;
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public void setTitle(String newTitle) {
      String oldTitle = title;
      title = newTitle;
      if (eNotificationRequired())
         eNotify(new ENotificationImpl(this, Notification.SET, WebdialogPackage.MWEB_DIALOG__TITLE, oldTitle, title));
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public String getMessage() {
      return message;
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public void setMessage(String newMessage) {
      String oldMessage = message;
      message = newMessage;
      if (eNotificationRequired())
         eNotify(new ENotificationImpl(this, Notification.SET, WebdialogPackage.MWEB_DIALOG__MESSAGE, oldMessage, message));
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public Object getParentShell() {
      return parentShell;
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public void setParentShell(Object newParentShell) {
      Object oldParentShell = parentShell;
      parentShell = newParentShell;
      if (eNotificationRequired())
         eNotify(new ENotificationImpl(this, Notification.SET, WebdialogPackage.MWEB_DIALOG__PARENT_SHELL, oldParentShell, parentShell));
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public boolean isBlocker() {
      return blocker;
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public void setBlocker(boolean newBlocker) {
      boolean oldBlocker = blocker;
      blocker = newBlocker;
      if (eNotificationRequired())
         eNotify(new ENotificationImpl(this, Notification.SET, WebdialogPackage.MWEB_DIALOG__BLOCKER, oldBlocker, blocker));
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public int getResponse() {
      return response;
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public void setResponse(int newResponse) {
      int oldResponse = response;
      response = newResponse;
      if (eNotificationRequired())
         eNotify(new ENotificationImpl(this, Notification.SET, WebdialogPackage.MWEB_DIALOG__RESPONSE, oldResponse, response));
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public Object eGet(int featureID, boolean resolve, boolean coreType) {
      switch (featureID) {
         case WebdialogPackage.MWEB_DIALOG__BUTTONS:
            return getButtons();
         case WebdialogPackage.MWEB_DIALOG__TITLE:
            return getTitle();
         case WebdialogPackage.MWEB_DIALOG__MESSAGE:
            return getMessage();
         case WebdialogPackage.MWEB_DIALOG__PARENT_SHELL:
            return getParentShell();
         case WebdialogPackage.MWEB_DIALOG__BLOCKER:
            return isBlocker();
         case WebdialogPackage.MWEB_DIALOG__RESPONSE:
            return getResponse();
      }
      return super.eGet(featureID, resolve, coreType);
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @SuppressWarnings("unchecked")
   @Override
   public void eSet(int featureID, Object newValue) {
      switch (featureID) {
         case WebdialogPackage.MWEB_DIALOG__BUTTONS:
            getButtons().clear();
            getButtons().addAll((Collection<? extends MButton>)newValue);
            return;
         case WebdialogPackage.MWEB_DIALOG__TITLE:
            setTitle((String)newValue);
            return;
         case WebdialogPackage.MWEB_DIALOG__MESSAGE:
            setMessage((String)newValue);
            return;
         case WebdialogPackage.MWEB_DIALOG__PARENT_SHELL:
            setParentShell(newValue);
            return;
         case WebdialogPackage.MWEB_DIALOG__BLOCKER:
            setBlocker((Boolean)newValue);
            return;
         case WebdialogPackage.MWEB_DIALOG__RESPONSE:
            setResponse((Integer)newValue);
            return;
      }
      super.eSet(featureID, newValue);
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public void eUnset(int featureID) {
      switch (featureID) {
         case WebdialogPackage.MWEB_DIALOG__BUTTONS:
            getButtons().clear();
            return;
         case WebdialogPackage.MWEB_DIALOG__TITLE:
            setTitle(TITLE_EDEFAULT);
            return;
         case WebdialogPackage.MWEB_DIALOG__MESSAGE:
            setMessage(MESSAGE_EDEFAULT);
            return;
         case WebdialogPackage.MWEB_DIALOG__PARENT_SHELL:
            setParentShell(PARENT_SHELL_EDEFAULT);
            return;
         case WebdialogPackage.MWEB_DIALOG__BLOCKER:
            setBlocker(BLOCKER_EDEFAULT);
            return;
         case WebdialogPackage.MWEB_DIALOG__RESPONSE:
            setResponse(RESPONSE_EDEFAULT);
            return;
      }
      super.eUnset(featureID);
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public boolean eIsSet(int featureID) {
      switch (featureID) {
         case WebdialogPackage.MWEB_DIALOG__BUTTONS:
            return buttons != null && !buttons.isEmpty();
         case WebdialogPackage.MWEB_DIALOG__TITLE:
            return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
         case WebdialogPackage.MWEB_DIALOG__MESSAGE:
            return MESSAGE_EDEFAULT == null ? message != null : !MESSAGE_EDEFAULT.equals(message);
         case WebdialogPackage.MWEB_DIALOG__PARENT_SHELL:
            return PARENT_SHELL_EDEFAULT == null ? parentShell != null : !PARENT_SHELL_EDEFAULT.equals(parentShell);
         case WebdialogPackage.MWEB_DIALOG__BLOCKER:
            return blocker != BLOCKER_EDEFAULT;
         case WebdialogPackage.MWEB_DIALOG__RESPONSE:
            return response != RESPONSE_EDEFAULT;
      }
      return super.eIsSet(featureID);
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public String toString() {
      if (eIsProxy()) return super.toString();

      StringBuilder result = new StringBuilder(super.toString());
      result.append(" (title: ");
      result.append(title);
      result.append(", message: ");
      result.append(message);
      result.append(", parentShell: ");
      result.append(parentShell);
      result.append(", blocker: ");
      result.append(blocker);
      result.append(", response: ");
      result.append(response);
      result.append(')');
      return result.toString();
   }

} //MWebDialogImpl
