/**
 */
package com.make.equo.ui.helper.provider.model;

import org.eclipse.e4.ui.model.application.ui.basic.MWindow;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MWeb Dialog</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.make.equo.ui.helper.provider.model.MWebDialog#getButtons <em>Buttons</em>}</li>
 *   <li>{@link com.make.equo.ui.helper.provider.model.MWebDialog#getTitle <em>Title</em>}</li>
 *   <li>{@link com.make.equo.ui.helper.provider.model.MWebDialog#getMessage <em>Message</em>}</li>
 *   <li>{@link com.make.equo.ui.helper.provider.model.MWebDialog#getParentShell <em>Parent Shell</em>}</li>
 *   <li>{@link com.make.equo.ui.helper.provider.model.MWebDialog#isBlocker <em>Blocker</em>}</li>
 *   <li>{@link com.make.equo.ui.helper.provider.model.MWebDialog#getResponse <em>Response</em>}</li>
 *   <li>{@link com.make.equo.ui.helper.provider.model.MWebDialog#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see com.make.equo.ui.helper.provider.model.WebdialogPackage#getMWebDialog()
 * @model
 * @generated
 */
public interface MWebDialog extends EObject, MWindow {
   /**
    * Returns the value of the '<em><b>Buttons</b></em>' reference list.
    * The list contents are of type {@link com.make.equo.ui.helper.provider.model.MButton}.
    * <!-- begin-user-doc -->
    * <p>
    * If the meaning of the '<em>Buttons</em>' reference list isn't clear,
    * there really should be more of a description here...
    * </p>
    * <!-- end-user-doc -->
    * @return the value of the '<em>Buttons</em>' reference list.
    * @see com.make.equo.ui.helper.provider.model.WebdialogPackage#getMWebDialog_Buttons()
    * @model
    * @generated
    */
   EList<MButton> getButtons();

   /**
    * Returns the value of the '<em><b>Title</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <p>
    * If the meaning of the '<em>Title</em>' attribute isn't clear,
    * there really should be more of a description here...
    * </p>
    * <!-- end-user-doc -->
    * @return the value of the '<em>Title</em>' attribute.
    * @see #setTitle(String)
    * @see com.make.equo.ui.helper.provider.model.WebdialogPackage#getMWebDialog_Title()
    * @model required="true"
    * @generated
    */
   String getTitle();

   /**
    * Sets the value of the '{@link com.make.equo.ui.helper.provider.model.MWebDialog#getTitle <em>Title</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @param value the new value of the '<em>Title</em>' attribute.
    * @see #getTitle()
    * @generated
    */
   void setTitle(String value);

   /**
    * Returns the value of the '<em><b>Message</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <p>
    * If the meaning of the '<em>Message</em>' attribute isn't clear,
    * there really should be more of a description here...
    * </p>
    * <!-- end-user-doc -->
    * @return the value of the '<em>Message</em>' attribute.
    * @see #setMessage(String)
    * @see com.make.equo.ui.helper.provider.model.WebdialogPackage#getMWebDialog_Message()
    * @model required="true"
    * @generated
    */
   String getMessage();

   /**
    * Sets the value of the '{@link com.make.equo.ui.helper.provider.model.MWebDialog#getMessage <em>Message</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @param value the new value of the '<em>Message</em>' attribute.
    * @see #getMessage()
    * @generated
    */
   void setMessage(String value);

   /**
    * Returns the value of the '<em><b>Parent Shell</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <p>
    * If the meaning of the '<em>Parent Shell</em>' attribute isn't clear,
    * there really should be more of a description here...
    * </p>
    * <!-- end-user-doc -->
    * @return the value of the '<em>Parent Shell</em>' attribute.
    * @see #setParentShell(Object)
    * @see com.make.equo.ui.helper.provider.model.WebdialogPackage#getMWebDialog_ParentShell()
    * @model
    * @generated
    */
   Object getParentShell();

   /**
    * Sets the value of the '{@link com.make.equo.ui.helper.provider.model.MWebDialog#getParentShell <em>Parent Shell</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @param value the new value of the '<em>Parent Shell</em>' attribute.
    * @see #getParentShell()
    * @generated
    */
   void setParentShell(Object value);

   /**
    * Returns the value of the '<em><b>Blocker</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <p>
    * If the meaning of the '<em>Blocker</em>' attribute isn't clear,
    * there really should be more of a description here...
    * </p>
    * <!-- end-user-doc -->
    * @return the value of the '<em>Blocker</em>' attribute.
    * @see #setBlocker(boolean)
    * @see com.make.equo.ui.helper.provider.model.WebdialogPackage#getMWebDialog_Blocker()
    * @model required="true"
    * @generated
    */
   boolean isBlocker();

   /**
    * Sets the value of the '{@link com.make.equo.ui.helper.provider.model.MWebDialog#isBlocker <em>Blocker</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @param value the new value of the '<em>Blocker</em>' attribute.
    * @see #isBlocker()
    * @generated
    */
   void setBlocker(boolean value);

   /**
    * Returns the value of the '<em><b>Response</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <p>
    * If the meaning of the '<em>Response</em>' attribute isn't clear,
    * there really should be more of a description here...
    * </p>
    * <!-- end-user-doc -->
    * @return the value of the '<em>Response</em>' attribute.
    * @see #setResponse(int)
    * @see com.make.equo.ui.helper.provider.model.WebdialogPackage#getMWebDialog_Response()
    * @model required="true"
    * @generated
    */
   int getResponse();

   /**
    * Sets the value of the '{@link com.make.equo.ui.helper.provider.model.MWebDialog#getResponse <em>Response</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @param value the new value of the '<em>Response</em>' attribute.
    * @see #getResponse()
    * @generated
    */
   void setResponse(int value);

   /**
    * Returns the value of the '<em><b>Type</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <p>
    * If the meaning of the '<em>Type</em>' attribute isn't clear,
    * there really should be more of a description here...
    * </p>
    * <!-- end-user-doc -->
    * @return the value of the '<em>Type</em>' attribute.
    * @see #setType(int)
    * @see com.make.equo.ui.helper.provider.model.WebdialogPackage#getMWebDialog_Type()
    * @model required="true"
    * @generated
    */
   int getType();

   /**
    * Sets the value of the '{@link com.make.equo.ui.helper.provider.model.MWebDialog#getType <em>Type</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @param value the new value of the '<em>Type</em>' attribute.
    * @see #getType()
    * @generated
    */
   void setType(int value);

} // MWebDialog
