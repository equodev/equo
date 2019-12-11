/**
 */
package com.make.equo.ui.helper.provider.model.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.make.equo.ui.helper.provider.model.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class WebdialogFactoryImpl extends EFactoryImpl implements WebdialogFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static WebdialogFactory init() {
		try {
			WebdialogFactory theWebdialogFactory = (WebdialogFactory)EPackage.Registry.INSTANCE.getEFactory(WebdialogPackage.eNS_URI);
			if (theWebdialogFactory != null) {
				return theWebdialogFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new WebdialogFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WebdialogFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case WebdialogPackage.MWEB_DIALOG: return createMWebDialog();
			case WebdialogPackage.MBUTTON: return createMButton();
			case WebdialogPackage.MBUTTON_TOGGLE: return createMButtonToggle();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MWebDialog createMWebDialog() {
		MWebDialogImpl mWebDialog = new MWebDialogImpl();
		return mWebDialog;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MButton createMButton() {
		MButtonImpl mButton = new MButtonImpl();
		return mButton;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MButtonToggle createMButtonToggle() {
		MButtonToggleImpl mButtonToggle = new MButtonToggleImpl();
		return mButtonToggle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public WebdialogPackage getWebdialogPackage() {
		return (WebdialogPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static WebdialogPackage getPackage() {
		return WebdialogPackage.eINSTANCE;
	}

} //WebdialogFactoryImpl
