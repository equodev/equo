/**
 */
package com.make.equo.ui.helper.provider.model;

import org.eclipse.e4.ui.model.application.ui.basic.MWindowElement;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MButton Toggle</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link webdialog.MButtonToggle#getAction <em>Action</em>}</li>
 *   <li>{@link webdialog.MButtonToggle#getLabel <em>Label</em>}</li>
 *   <li>{@link webdialog.MButtonToggle#getMessage <em>Message</em>}</li>
 *   <li>{@link webdialog.MButtonToggle#isStatus <em>Status</em>}</li>
 *   <li>{@link webdialog.MButtonToggle#isDefault <em>Default</em>}</li>
 * </ul>
 *
 * @see webdialog.WebdialogPackage#getMButtonToggle()
 * @model
 * @generated
 */
public interface MButtonToggle extends EObject, MWindowElement {
	/**
	 * Returns the value of the '<em><b>Action</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Action</em>' attribute.
	 * @see #setAction(int)
	 * @see webdialog.WebdialogPackage#getMButtonToggle_Action()
	 * @model required="true"
	 * @generated
	 */
	int getAction();

	/**
	 * Sets the value of the '{@link webdialog.MButtonToggle#getAction <em>Action</em>}' attribute.
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
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Label</em>' attribute.
	 * @see #setLabel(String)
	 * @see webdialog.WebdialogPackage#getMButtonToggle_Label()
	 * @model required="true"
	 * @generated
	 */
	String getLabel();

	/**
	 * Sets the value of the '{@link webdialog.MButtonToggle#getLabel <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Label</em>' attribute.
	 * @see #getLabel()
	 * @generated
	 */
	void setLabel(String value);

	/**
	 * Returns the value of the '<em><b>Message</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Message</em>' attribute.
	 * @see #setMessage(String)
	 * @see webdialog.WebdialogPackage#getMButtonToggle_Message()
	 * @model required="true"
	 * @generated
	 */
	String getMessage();

	/**
	 * Sets the value of the '{@link webdialog.MButtonToggle#getMessage <em>Message</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Message</em>' attribute.
	 * @see #getMessage()
	 * @generated
	 */
	void setMessage(String value);

	/**
	 * Returns the value of the '<em><b>Status</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Status</em>' attribute.
	 * @see #setStatus(boolean)
	 * @see webdialog.WebdialogPackage#getMButtonToggle_Status()
	 * @model required="true"
	 * @generated
	 */
	boolean isStatus();

	/**
	 * Sets the value of the '{@link webdialog.MButtonToggle#isStatus <em>Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Status</em>' attribute.
	 * @see #isStatus()
	 * @generated
	 */
	void setStatus(boolean value);

	/**
	 * Returns the value of the '<em><b>Default</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default</em>' attribute.
	 * @see #setDefault(boolean)
	 * @see webdialog.WebdialogPackage#getMButtonToggle_Default()
	 * @model required="true"
	 * @generated
	 */
	boolean isDefault();

	/**
	 * Sets the value of the '{@link webdialog.MButtonToggle#isDefault <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default</em>' attribute.
	 * @see #isDefault()
	 * @generated
	 */
	void setDefault(boolean value);

} // MButtonToggle
