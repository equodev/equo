/**
 */
package com.make.equo.ui.helper.provider.model.impl;

import org.eclipse.e4.ui.model.application.ui.impl.UIElementImpl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.make.equo.ui.helper.provider.dialogs.util.IDialogConstants;
import com.make.equo.ui.helper.provider.model.MButton;
import com.make.equo.ui.helper.provider.model.WebdialogPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MButton</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.make.equo.ui.helper.provider.model.impl.MButtonImpl#getAction <em>Action</em>}</li>
 *   <li>{@link com.make.equo.ui.helper.provider.model.impl.MButtonImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link com.make.equo.ui.helper.provider.model.impl.MButtonImpl#isDefault <em>Default</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MButtonImpl extends UIElementImpl implements MButton {
   /**
    * The default value of the '{@link #getAction() <em>Action</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #getAction()
    * @generated
    * @ordered
    */
   protected static final int ACTION_EDEFAULT = IDialogConstants.OK_ID;

   /**
    * The cached value of the '{@link #getAction() <em>Action</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #getAction()
    * @generated
    * @ordered
    */
   protected int action = ACTION_EDEFAULT;

   /**
    * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #getLabel()
    * @generated
    * @ordered
    */
   protected static final String LABEL_EDEFAULT = "";

   /**
    * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #getLabel()
    * @generated
    * @ordered
    */
   protected String label = LABEL_EDEFAULT;

   /**
    * The default value of the '{@link #isDefault() <em>Default</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #isDefault()
    * @generated
    * @ordered
    */
   protected static final boolean DEFAULT_EDEFAULT = false;

   /**
    * The cached value of the '{@link #isDefault() <em>Default</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see #isDefault()
    * @generated
    * @ordered
    */
   protected boolean default_ = DEFAULT_EDEFAULT;

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   protected MButtonImpl() {
      super();
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   protected EClass eStaticClass() {
      return WebdialogPackage.Literals.MBUTTON;
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public int getAction() {
      return action;
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public void setAction(int newAction) {
      int oldAction = action;
      action = newAction;
      if (eNotificationRequired())
         eNotify(new ENotificationImpl(this, Notification.SET, WebdialogPackage.MBUTTON__ACTION, oldAction, action));
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public String getLabel() {
      return label;
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public void setLabel(String newLabel) {
      String oldLabel = label;
      label = newLabel;
      if (eNotificationRequired())
         eNotify(new ENotificationImpl(this, Notification.SET, WebdialogPackage.MBUTTON__LABEL, oldLabel, label));
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public boolean isDefault() {
      return default_;
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public void setDefault(boolean newDefault) {
      boolean oldDefault = default_;
      default_ = newDefault;
      if (eNotificationRequired())
         eNotify(new ENotificationImpl(this, Notification.SET, WebdialogPackage.MBUTTON__DEFAULT, oldDefault, default_));
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public Object eGet(int featureID, boolean resolve, boolean coreType) {
      switch (featureID) {
         case WebdialogPackage.MBUTTON__ACTION:
            return getAction();
         case WebdialogPackage.MBUTTON__LABEL:
            return getLabel();
         case WebdialogPackage.MBUTTON__DEFAULT:
            return isDefault();
      }
      return super.eGet(featureID, resolve, coreType);
   }

   /**
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   @Override
   public void eSet(int featureID, Object newValue) {
      switch (featureID) {
         case WebdialogPackage.MBUTTON__ACTION:
            setAction((Integer)newValue);
            return;
         case WebdialogPackage.MBUTTON__LABEL:
            setLabel((String)newValue);
            return;
         case WebdialogPackage.MBUTTON__DEFAULT:
            setDefault((Boolean)newValue);
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
         case WebdialogPackage.MBUTTON__ACTION:
            setAction(ACTION_EDEFAULT);
            return;
         case WebdialogPackage.MBUTTON__LABEL:
            setLabel(LABEL_EDEFAULT);
            return;
         case WebdialogPackage.MBUTTON__DEFAULT:
            setDefault(DEFAULT_EDEFAULT);
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
         case WebdialogPackage.MBUTTON__ACTION:
            return action != ACTION_EDEFAULT;
         case WebdialogPackage.MBUTTON__LABEL:
            return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
         case WebdialogPackage.MBUTTON__DEFAULT:
            return default_ != DEFAULT_EDEFAULT;
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
      result.append(" (action: ");
      result.append(action);
      result.append(", label: ");
      result.append(label);
      result.append(", default: ");
      result.append(default_);
      result.append(')');
      return result.toString();
   }

} //MButtonImpl
