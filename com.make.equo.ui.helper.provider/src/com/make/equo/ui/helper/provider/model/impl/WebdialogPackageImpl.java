/**
 */
package com.make.equo.ui.helper.provider.model.impl;

import org.eclipse.e4.ui.model.application.impl.ApplicationPackageImpl;

import org.eclipse.e4.ui.model.application.ui.basic.impl.BasicPackageImpl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.make.equo.ui.helper.provider.model.MButton;
import com.make.equo.ui.helper.provider.model.MButtonToggle;
import com.make.equo.ui.helper.provider.model.MWebDialog;
import com.make.equo.ui.helper.provider.model.WebdialogFactory;
import com.make.equo.ui.helper.provider.model.WebdialogPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class WebdialogPackageImpl extends EPackageImpl implements WebdialogPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mWebDialogEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mButtonEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mButtonToggleEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see webdialog.WebdialogPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private WebdialogPackageImpl() {
		super(eNS_URI, WebdialogFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link WebdialogPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static WebdialogPackage init() {
		if (isInited) return (WebdialogPackage)EPackage.Registry.INSTANCE.getEPackage(WebdialogPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredWebdialogPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		WebdialogPackageImpl theWebdialogPackage = registeredWebdialogPackage instanceof WebdialogPackageImpl ? (WebdialogPackageImpl)registeredWebdialogPackage : new WebdialogPackageImpl();

		isInited = true;

		// Initialize simple dependencies
		ApplicationPackageImpl.eINSTANCE.eClass();

		// Create package meta-data objects
		theWebdialogPackage.createPackageContents();

		// Initialize created meta-data
		theWebdialogPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theWebdialogPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(WebdialogPackage.eNS_URI, theWebdialogPackage);
		return theWebdialogPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMWebDialog() {
		return mWebDialogEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMWebDialog_Buttons() {
		return (EReference)mWebDialogEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMWebDialog_Toggle() {
		return (EReference)mWebDialogEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMWebDialog_Title() {
		return (EAttribute)mWebDialogEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMWebDialog_Message() {
		return (EAttribute)mWebDialogEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMWebDialog_ParentShell() {
		return (EAttribute)mWebDialogEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMWebDialog_Blocker() {
		return (EAttribute)mWebDialogEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMWebDialog_Response() {
		return (EAttribute)mWebDialogEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMWebDialog_Type() {
		return (EAttribute)mWebDialogEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMButton() {
		return mButtonEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMButton_Action() {
		return (EAttribute)mButtonEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMButton_Label() {
		return (EAttribute)mButtonEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMButton_Default() {
		return (EAttribute)mButtonEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMButtonToggle() {
		return mButtonToggleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMButtonToggle_Action() {
		return (EAttribute)mButtonToggleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMButtonToggle_Label() {
		return (EAttribute)mButtonToggleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMButtonToggle_Message() {
		return (EAttribute)mButtonToggleEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMButtonToggle_Status() {
		return (EAttribute)mButtonToggleEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMButtonToggle_Default() {
		return (EAttribute)mButtonToggleEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public WebdialogFactory getWebdialogFactory() {
		return (WebdialogFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		mWebDialogEClass = createEClass(MWEB_DIALOG);
		createEReference(mWebDialogEClass, MWEB_DIALOG__BUTTONS);
		createEReference(mWebDialogEClass, MWEB_DIALOG__TOGGLE);
		createEAttribute(mWebDialogEClass, MWEB_DIALOG__TITLE);
		createEAttribute(mWebDialogEClass, MWEB_DIALOG__MESSAGE);
		createEAttribute(mWebDialogEClass, MWEB_DIALOG__PARENT_SHELL);
		createEAttribute(mWebDialogEClass, MWEB_DIALOG__BLOCKER);
		createEAttribute(mWebDialogEClass, MWEB_DIALOG__RESPONSE);
		createEAttribute(mWebDialogEClass, MWEB_DIALOG__TYPE);

		mButtonEClass = createEClass(MBUTTON);
		createEAttribute(mButtonEClass, MBUTTON__ACTION);
		createEAttribute(mButtonEClass, MBUTTON__LABEL);
		createEAttribute(mButtonEClass, MBUTTON__DEFAULT);

		mButtonToggleEClass = createEClass(MBUTTON_TOGGLE);
		createEAttribute(mButtonToggleEClass, MBUTTON_TOGGLE__ACTION);
		createEAttribute(mButtonToggleEClass, MBUTTON_TOGGLE__LABEL);
		createEAttribute(mButtonToggleEClass, MBUTTON_TOGGLE__MESSAGE);
		createEAttribute(mButtonToggleEClass, MBUTTON_TOGGLE__STATUS);
		createEAttribute(mButtonToggleEClass, MBUTTON_TOGGLE__DEFAULT);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		BasicPackageImpl theBasicPackage = (BasicPackageImpl)EPackage.Registry.INSTANCE.getEPackage(BasicPackageImpl.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		mWebDialogEClass.getESuperTypes().add(theBasicPackage.getWindow());
		mButtonEClass.getESuperTypes().add(theBasicPackage.getWindowElement());
		mButtonToggleEClass.getESuperTypes().add(theBasicPackage.getWindowElement());

		// Initialize classes, features, and operations; add parameters
		initEClass(mWebDialogEClass, MWebDialog.class, "MWebDialog", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMWebDialog_Buttons(), this.getMButton(), null, "buttons", null, 0, -1, MWebDialog.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMWebDialog_Toggle(), this.getMButtonToggle(), null, "toggle", null, 1, 1, MWebDialog.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMWebDialog_Title(), ecorePackage.getEString(), "title", null, 1, 1, MWebDialog.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMWebDialog_Message(), ecorePackage.getEString(), "message", null, 1, 1, MWebDialog.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMWebDialog_ParentShell(), ecorePackage.getEJavaObject(), "parentShell", null, 0, 1, MWebDialog.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMWebDialog_Blocker(), ecorePackage.getEBoolean(), "blocker", null, 1, 1, MWebDialog.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMWebDialog_Response(), ecorePackage.getEInt(), "response", null, 1, 1, MWebDialog.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMWebDialog_Type(), ecorePackage.getEInt(), "type", null, 1, 1, MWebDialog.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mButtonEClass, MButton.class, "MButton", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMButton_Action(), ecorePackage.getEInt(), "action", null, 1, 1, MButton.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMButton_Label(), ecorePackage.getEString(), "label", null, 1, 1, MButton.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMButton_Default(), ecorePackage.getEBoolean(), "default", null, 1, 1, MButton.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mButtonToggleEClass, MButtonToggle.class, "MButtonToggle", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMButtonToggle_Action(), ecorePackage.getEInt(), "action", null, 1, 1, MButtonToggle.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMButtonToggle_Label(), ecorePackage.getEString(), "label", null, 1, 1, MButtonToggle.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMButtonToggle_Message(), ecorePackage.getEString(), "message", null, 1, 1, MButtonToggle.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMButtonToggle_Status(), ecorePackage.getEBoolean(), "status", null, 1, 1, MButtonToggle.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMButtonToggle_Default(), ecorePackage.getEBoolean(), "default", null, 1, 1, MButtonToggle.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //WebdialogPackageImpl
