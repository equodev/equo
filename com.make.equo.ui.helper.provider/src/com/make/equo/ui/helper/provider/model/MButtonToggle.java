/**
 */
package com.make.equo.ui.helper.provider.model;

import org.eclipse.e4.ui.model.application.ui.basic.MWindowElement;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MButton</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.make.equo.ui.helper.provider.model.MButton#getAction <em>Action</em>}</li>
 *   <li>{@link com.make.equo.ui.helper.provider.model.MButton#getLabel <em>Label</em>}</li>
 *   <li>{@link com.make.equo.ui.helper.provider.model.MButton#isDefault <em>Default</em>}</li>
 * </ul>
 *
 * @see com.make.equo.ui.helper.provider.model.WebdialogPackage#getMButton()
 * @model
 * @generated
 */
public interface MButtonToggle extends EObject, MWindowElement {
   /**
    * Returns the value of the '<em><b>Action</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <p>
    * If the meaning of the '<em>Action</em>' attribute isn't clear,
    * there really should be more of a description here...
    * </p>
    * <!-- end-user-doc -->
    * @return the value of the '<em>Action</em>' attribute.
    * @see #setAction(int)
    * @see com.make.equo.ui.helper.provider.model.WebdialogPackage#getMButton_Action()
    * @model required="true"
    * @generated
    */
   int getAction();

   /**
    * Sets the value of the '{@link com.make.equo.ui.helper.provider.model.MButton#getAction <em>Action</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @param value the new value of the '<em>Action</em>' attribute.
    * @see #getAction()
    * @generated
    */
   void setAction(int value);

   /**
    * Returns the value of the '<em><b>Label</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <p>
    * If the meaning of the '<em>Label</em>' attribute isn't clear,
    * there really should be more of a description here...
    * </p>
    * <!-- end-user-doc -->
    * @return the value of the '<em>Label</em>' attribute.
    * @see #setLabel(String)
    * @see com.make.equo.ui.helper.provider.model.WebdialogPackage#getMButton_Label()
    * @model required="true"
    * @generated
    */
   String getLabel();

   String getMessageToggle();
   
   void setMessageToggle(String message);
   
   boolean isToggled();
   
   void setToggleStatus(boolean status);
   
   /**
    * Sets the value of the '{@link com.make.equo.ui.helper.provider.model.MButton#getLabel <em>Label</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @param value the new value of the '<em>Label</em>' attribute.
    * @see #getLabel()
    * @generated
    */
   void setLabel(String value);

   /**
    * Returns the value of the '<em><b>Default</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <p>
    * If the meaning of the '<em>Default</em>' attribute isn't clear,
    * there really should be more of a description here...
    * </p>
    * <!-- end-user-doc -->
    * @return the value of the '<em>Default</em>' attribute.
    * @see #setDefault(boolean)
    * @see com.make.equo.ui.helper.provider.model.WebdialogPackage#getMButton_Default()
    * @model required="true"
    * @generated
    */
   boolean isDefault();

   /**
    * Sets the value of the '{@link com.make.equo.ui.helper.provider.model.MButton#isDefault <em>Default</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @param value the new value of the '<em>Default</em>' attribute.
    * @see #isDefault()
    * @generated
    */
   void setDefault(boolean value);

} // MButton
