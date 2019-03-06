/**
 */
package com.make.equo.ui.helper.provider.model;

import org.eclipse.e4.ui.model.application.ui.basic.impl.BasicPackageImpl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.make.equo.ui.helper.provider.model.WebdialogFactory
 * @model kind="package"
 * @generated
 */
public interface WebdialogPackage extends EPackage {
   /**
    * The package name.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   String eNAME = "webdialog";

   /**
    * The package namespace URI.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   String eNS_URI = "http://www.maketechnology.io/UIModel/webdialog";

   /**
    * The package namespace name.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   String eNS_PREFIX = "webdialog";

   /**
    * The singleton instance of the package.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   WebdialogPackage eINSTANCE = com.make.equo.ui.helper.provider.model.impl.WebdialogPackageImpl.init();

   /**
    * The meta object id for the '{@link com.make.equo.ui.helper.provider.model.impl.MWebDialogImpl <em>MWeb Dialog</em>}' class.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see com.make.equo.ui.helper.provider.model.impl.MWebDialogImpl
    * @see com.make.equo.ui.helper.provider.model.impl.WebdialogPackageImpl#getMWebDialog()
    * @generated
    */
   int MWEB_DIALOG = 0;

   /**
    * The feature id for the '<em><b>Element Id</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__ELEMENT_ID = BasicPackageImpl.WINDOW__ELEMENT_ID;

   /**
    * The feature id for the '<em><b>Persisted State</b></em>' map.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__PERSISTED_STATE = BasicPackageImpl.WINDOW__PERSISTED_STATE;

   /**
    * The feature id for the '<em><b>Tags</b></em>' attribute list.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__TAGS = BasicPackageImpl.WINDOW__TAGS;

   /**
    * The feature id for the '<em><b>Contributor URI</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__CONTRIBUTOR_URI = BasicPackageImpl.WINDOW__CONTRIBUTOR_URI;

   /**
    * The feature id for the '<em><b>Transient Data</b></em>' map.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__TRANSIENT_DATA = BasicPackageImpl.WINDOW__TRANSIENT_DATA;

   /**
    * The feature id for the '<em><b>Widget</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__WIDGET = BasicPackageImpl.WINDOW__WIDGET;

   /**
    * The feature id for the '<em><b>Renderer</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__RENDERER = BasicPackageImpl.WINDOW__RENDERER;

   /**
    * The feature id for the '<em><b>To Be Rendered</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__TO_BE_RENDERED = BasicPackageImpl.WINDOW__TO_BE_RENDERED;

   /**
    * The feature id for the '<em><b>On Top</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__ON_TOP = BasicPackageImpl.WINDOW__ON_TOP;

   /**
    * The feature id for the '<em><b>Visible</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__VISIBLE = BasicPackageImpl.WINDOW__VISIBLE;

   /**
    * The feature id for the '<em><b>Parent</b></em>' container reference.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__PARENT = BasicPackageImpl.WINDOW__PARENT;

   /**
    * The feature id for the '<em><b>Container Data</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__CONTAINER_DATA = BasicPackageImpl.WINDOW__CONTAINER_DATA;

   /**
    * The feature id for the '<em><b>Cur Shared Ref</b></em>' reference.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__CUR_SHARED_REF = BasicPackageImpl.WINDOW__CUR_SHARED_REF;

   /**
    * The feature id for the '<em><b>Visible When</b></em>' containment reference.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__VISIBLE_WHEN = BasicPackageImpl.WINDOW__VISIBLE_WHEN;

   /**
    * The feature id for the '<em><b>Accessibility Phrase</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__ACCESSIBILITY_PHRASE = BasicPackageImpl.WINDOW__ACCESSIBILITY_PHRASE;

   /**
    * The feature id for the '<em><b>Localized Accessibility Phrase</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__LOCALIZED_ACCESSIBILITY_PHRASE = BasicPackageImpl.WINDOW__LOCALIZED_ACCESSIBILITY_PHRASE;

   /**
    * The feature id for the '<em><b>Children</b></em>' containment reference list.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__CHILDREN = BasicPackageImpl.WINDOW__CHILDREN;

   /**
    * The feature id for the '<em><b>Selected Element</b></em>' reference.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__SELECTED_ELEMENT = BasicPackageImpl.WINDOW__SELECTED_ELEMENT;

   /**
    * The feature id for the '<em><b>Label</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__LABEL = BasicPackageImpl.WINDOW__LABEL;

   /**
    * The feature id for the '<em><b>Icon URI</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__ICON_URI = BasicPackageImpl.WINDOW__ICON_URI;

   /**
    * The feature id for the '<em><b>Tooltip</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__TOOLTIP = BasicPackageImpl.WINDOW__TOOLTIP;

   /**
    * The feature id for the '<em><b>Localized Label</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__LOCALIZED_LABEL = BasicPackageImpl.WINDOW__LOCALIZED_LABEL;

   /**
    * The feature id for the '<em><b>Localized Tooltip</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__LOCALIZED_TOOLTIP = BasicPackageImpl.WINDOW__LOCALIZED_TOOLTIP;

   /**
    * The feature id for the '<em><b>Context</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__CONTEXT = BasicPackageImpl.WINDOW__CONTEXT;

   /**
    * The feature id for the '<em><b>Variables</b></em>' attribute list.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__VARIABLES = BasicPackageImpl.WINDOW__VARIABLES;

   /**
    * The feature id for the '<em><b>Properties</b></em>' map.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__PROPERTIES = BasicPackageImpl.WINDOW__PROPERTIES;

   /**
    * The feature id for the '<em><b>Handlers</b></em>' containment reference list.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__HANDLERS = BasicPackageImpl.WINDOW__HANDLERS;

   /**
    * The feature id for the '<em><b>Binding Contexts</b></em>' reference list.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__BINDING_CONTEXTS = BasicPackageImpl.WINDOW__BINDING_CONTEXTS;

   /**
    * The feature id for the '<em><b>Snippets</b></em>' containment reference list.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__SNIPPETS = BasicPackageImpl.WINDOW__SNIPPETS;

   /**
    * The feature id for the '<em><b>Main Menu</b></em>' containment reference.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__MAIN_MENU = BasicPackageImpl.WINDOW__MAIN_MENU;

   /**
    * The feature id for the '<em><b>X</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__X = BasicPackageImpl.WINDOW__X;

   /**
    * The feature id for the '<em><b>Y</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__Y = BasicPackageImpl.WINDOW__Y;

   /**
    * The feature id for the '<em><b>Width</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__WIDTH = BasicPackageImpl.WINDOW__WIDTH;

   /**
    * The feature id for the '<em><b>Height</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__HEIGHT = BasicPackageImpl.WINDOW__HEIGHT;

   /**
    * The feature id for the '<em><b>Windows</b></em>' containment reference list.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__WINDOWS = BasicPackageImpl.WINDOW__WINDOWS;

   /**
    * The feature id for the '<em><b>Shared Elements</b></em>' containment reference list.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__SHARED_ELEMENTS = BasicPackageImpl.WINDOW__SHARED_ELEMENTS;

   /**
    * The feature id for the '<em><b>Buttons</b></em>' reference list.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__BUTTONS = BasicPackageImpl.WINDOW_FEATURE_COUNT + 0;

   /**
    * The feature id for the '<em><b>Title</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__TITLE = BasicPackageImpl.WINDOW_FEATURE_COUNT + 1;

   /**
    * The feature id for the '<em><b>Message</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__MESSAGE = BasicPackageImpl.WINDOW_FEATURE_COUNT + 2;

   /**
    * The feature id for the '<em><b>Parent Shell</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__PARENT_SHELL = BasicPackageImpl.WINDOW_FEATURE_COUNT + 3;

   /**
    * The feature id for the '<em><b>Blocker</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__BLOCKER = BasicPackageImpl.WINDOW_FEATURE_COUNT + 4;

   /**
    * The feature id for the '<em><b>Response</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__RESPONSE = BasicPackageImpl.WINDOW_FEATURE_COUNT + 5;

   /**
    * The feature id for the '<em><b>Type</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    * @ordered
    */
   int MWEB_DIALOG__TYPE = BasicPackageImpl.WINDOW_FEATURE_COUNT + 6;

   /**
    * The number of structural features of the '<em>MWeb Dialog</em>' class.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    * @ordered
    */
   int MWEB_DIALOG_FEATURE_COUNT = BasicPackageImpl.WINDOW_FEATURE_COUNT + 7;

   /**
    * The operation id for the '<em>Update Localization</em>' operation.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.1
    * @generated
    * @ordered
    */
   int MWEB_DIALOG___UPDATE_LOCALIZATION = BasicPackageImpl.WINDOW___UPDATE_LOCALIZATION;

   /**
    * The number of operations of the '<em>MWeb Dialog</em>' class.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    * @ordered
    */
   int MWEB_DIALOG_OPERATION_COUNT = BasicPackageImpl.WINDOW_OPERATION_COUNT + 0;

   /**
    * The meta object id for the '{@link com.make.equo.ui.helper.provider.model.impl.MButtonImpl <em>MButton</em>}' class.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @see com.make.equo.ui.helper.provider.model.impl.MButtonImpl
    * @see com.make.equo.ui.helper.provider.model.impl.WebdialogPackageImpl#getMButton()
    * @generated
    */
   int MBUTTON = 1;

   /**
    * The feature id for the '<em><b>Element Id</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MBUTTON__ELEMENT_ID = BasicPackageImpl.WINDOW_ELEMENT__ELEMENT_ID;

   /**
    * The feature id for the '<em><b>Persisted State</b></em>' map.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MBUTTON__PERSISTED_STATE = BasicPackageImpl.WINDOW_ELEMENT__PERSISTED_STATE;

   /**
    * The feature id for the '<em><b>Tags</b></em>' attribute list.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MBUTTON__TAGS = BasicPackageImpl.WINDOW_ELEMENT__TAGS;

   /**
    * The feature id for the '<em><b>Contributor URI</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MBUTTON__CONTRIBUTOR_URI = BasicPackageImpl.WINDOW_ELEMENT__CONTRIBUTOR_URI;

   /**
    * The feature id for the '<em><b>Transient Data</b></em>' map.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MBUTTON__TRANSIENT_DATA = BasicPackageImpl.WINDOW_ELEMENT__TRANSIENT_DATA;

   /**
    * The feature id for the '<em><b>Widget</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MBUTTON__WIDGET = BasicPackageImpl.WINDOW_ELEMENT__WIDGET;

   /**
    * The feature id for the '<em><b>Renderer</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MBUTTON__RENDERER = BasicPackageImpl.WINDOW_ELEMENT__RENDERER;

   /**
    * The feature id for the '<em><b>To Be Rendered</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MBUTTON__TO_BE_RENDERED = BasicPackageImpl.WINDOW_ELEMENT__TO_BE_RENDERED;

   /**
    * The feature id for the '<em><b>On Top</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MBUTTON__ON_TOP = BasicPackageImpl.WINDOW_ELEMENT__ON_TOP;

   /**
    * The feature id for the '<em><b>Visible</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MBUTTON__VISIBLE = BasicPackageImpl.WINDOW_ELEMENT__VISIBLE;

   /**
    * The feature id for the '<em><b>Parent</b></em>' container reference.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MBUTTON__PARENT = BasicPackageImpl.WINDOW_ELEMENT__PARENT;

   /**
    * The feature id for the '<em><b>Container Data</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MBUTTON__CONTAINER_DATA = BasicPackageImpl.WINDOW_ELEMENT__CONTAINER_DATA;

   /**
    * The feature id for the '<em><b>Cur Shared Ref</b></em>' reference.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MBUTTON__CUR_SHARED_REF = BasicPackageImpl.WINDOW_ELEMENT__CUR_SHARED_REF;

   /**
    * The feature id for the '<em><b>Visible When</b></em>' containment reference.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MBUTTON__VISIBLE_WHEN = BasicPackageImpl.WINDOW_ELEMENT__VISIBLE_WHEN;

   /**
    * The feature id for the '<em><b>Accessibility Phrase</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MBUTTON__ACCESSIBILITY_PHRASE = BasicPackageImpl.WINDOW_ELEMENT__ACCESSIBILITY_PHRASE;

   /**
    * The feature id for the '<em><b>Localized Accessibility Phrase</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.0
    * @generated
    * @ordered
    */
   int MBUTTON__LOCALIZED_ACCESSIBILITY_PHRASE = BasicPackageImpl.WINDOW_ELEMENT__LOCALIZED_ACCESSIBILITY_PHRASE;

   /**
    * The feature id for the '<em><b>Action</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    * @ordered
    */
   int MBUTTON__ACTION = BasicPackageImpl.WINDOW_ELEMENT_FEATURE_COUNT + 0;

   /**
    * The feature id for the '<em><b>Label</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    * @ordered
    */
   int MBUTTON__LABEL = BasicPackageImpl.WINDOW_ELEMENT_FEATURE_COUNT + 1;

   /**
    * The feature id for the '<em><b>Default</b></em>' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    * @ordered
    */
   int MBUTTON__DEFAULT = BasicPackageImpl.WINDOW_ELEMENT_FEATURE_COUNT + 2;

   /**
    * The number of structural features of the '<em>MButton</em>' class.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    * @ordered
    */
   int MBUTTON_FEATURE_COUNT = BasicPackageImpl.WINDOW_ELEMENT_FEATURE_COUNT + 3;

   /**
    * The operation id for the '<em>Update Localization</em>' operation.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @since 1.1
    * @generated
    * @ordered
    */
   int MBUTTON___UPDATE_LOCALIZATION = BasicPackageImpl.WINDOW_ELEMENT___UPDATE_LOCALIZATION;

   /**
    * The number of operations of the '<em>MButton</em>' class.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    * @ordered
    */
   int MBUTTON_OPERATION_COUNT = BasicPackageImpl.WINDOW_ELEMENT_OPERATION_COUNT + 0;


   /**
    * Returns the meta object for class '{@link com.make.equo.ui.helper.provider.model.MWebDialog <em>MWeb Dialog</em>}'.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @return the meta object for class '<em>MWeb Dialog</em>'.
    * @see com.make.equo.ui.helper.provider.model.MWebDialog
    * @generated
    */
   EClass getMWebDialog();

   /**
    * Returns the meta object for the reference list '{@link com.make.equo.ui.helper.provider.model.MWebDialog#getButtons <em>Buttons</em>}'.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @return the meta object for the reference list '<em>Buttons</em>'.
    * @see com.make.equo.ui.helper.provider.model.MWebDialog#getButtons()
    * @see #getMWebDialog()
    * @generated
    */
   EReference getMWebDialog_Buttons();

   /**
    * Returns the meta object for the attribute '{@link com.make.equo.ui.helper.provider.model.MWebDialog#getTitle <em>Title</em>}'.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @return the meta object for the attribute '<em>Title</em>'.
    * @see com.make.equo.ui.helper.provider.model.MWebDialog#getTitle()
    * @see #getMWebDialog()
    * @generated
    */
   EAttribute getMWebDialog_Title();

   /**
    * Returns the meta object for the attribute '{@link com.make.equo.ui.helper.provider.model.MWebDialog#getMessage <em>Message</em>}'.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @return the meta object for the attribute '<em>Message</em>'.
    * @see com.make.equo.ui.helper.provider.model.MWebDialog#getMessage()
    * @see #getMWebDialog()
    * @generated
    */
   EAttribute getMWebDialog_Message();

   /**
    * Returns the meta object for the attribute '{@link com.make.equo.ui.helper.provider.model.MWebDialog#getParentShell <em>Parent Shell</em>}'.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @return the meta object for the attribute '<em>Parent Shell</em>'.
    * @see com.make.equo.ui.helper.provider.model.MWebDialog#getParentShell()
    * @see #getMWebDialog()
    * @generated
    */
   EAttribute getMWebDialog_ParentShell();

   /**
    * Returns the meta object for the attribute '{@link com.make.equo.ui.helper.provider.model.MWebDialog#isBlocker <em>Blocker</em>}'.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @return the meta object for the attribute '<em>Blocker</em>'.
    * @see com.make.equo.ui.helper.provider.model.MWebDialog#isBlocker()
    * @see #getMWebDialog()
    * @generated
    */
   EAttribute getMWebDialog_Blocker();

   /**
    * Returns the meta object for the attribute '{@link com.make.equo.ui.helper.provider.model.MWebDialog#getResponse <em>Response</em>}'.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @return the meta object for the attribute '<em>Response</em>'.
    * @see com.make.equo.ui.helper.provider.model.MWebDialog#getResponse()
    * @see #getMWebDialog()
    * @generated
    */
   EAttribute getMWebDialog_Response();

   /**
    * Returns the meta object for the attribute '{@link com.make.equo.ui.helper.provider.model.MWebDialog#getType <em>Type</em>}'.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @return the meta object for the attribute '<em>Type</em>'.
    * @see com.make.equo.ui.helper.provider.model.MWebDialog#getType()
    * @see #getMWebDialog()
    * @generated
    */
   EAttribute getMWebDialog_Type();

   /**
    * Returns the meta object for class '{@link com.make.equo.ui.helper.provider.model.MButton <em>MButton</em>}'.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @return the meta object for class '<em>MButton</em>'.
    * @see com.make.equo.ui.helper.provider.model.MButton
    * @generated
    */
   EClass getMButton();

   /**
    * Returns the meta object for the attribute '{@link com.make.equo.ui.helper.provider.model.MButton#getAction <em>Action</em>}'.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @return the meta object for the attribute '<em>Action</em>'.
    * @see com.make.equo.ui.helper.provider.model.MButton#getAction()
    * @see #getMButton()
    * @generated
    */
   EAttribute getMButton_Action();

   /**
    * Returns the meta object for the attribute '{@link com.make.equo.ui.helper.provider.model.MButton#getLabel <em>Label</em>}'.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @return the meta object for the attribute '<em>Label</em>'.
    * @see com.make.equo.ui.helper.provider.model.MButton#getLabel()
    * @see #getMButton()
    * @generated
    */
   EAttribute getMButton_Label();

   /**
    * Returns the meta object for the attribute '{@link com.make.equo.ui.helper.provider.model.MButton#isDefault <em>Default</em>}'.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @return the meta object for the attribute '<em>Default</em>'.
    * @see com.make.equo.ui.helper.provider.model.MButton#isDefault()
    * @see #getMButton()
    * @generated
    */
   EAttribute getMButton_Default();

   /**
    * Returns the factory that creates the instances of the model.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @return the factory that creates the instances of the model.
    * @generated
    */
   WebdialogFactory getWebdialogFactory();

   /**
    * <!-- begin-user-doc -->
    * Defines literals for the meta objects that represent
    * <ul>
    *   <li>each class,</li>
    *   <li>each feature of each class,</li>
    *   <li>each operation of each class,</li>
    *   <li>each enum,</li>
    *   <li>and each data type</li>
    * </ul>
    * <!-- end-user-doc -->
    * @generated
    */
   interface Literals {
      /**
       * The meta object literal for the '{@link com.make.equo.ui.helper.provider.model.impl.MWebDialogImpl <em>MWeb Dialog</em>}' class.
       * <!-- begin-user-doc -->
       * <!-- end-user-doc -->
       * @see com.make.equo.ui.helper.provider.model.impl.MWebDialogImpl
       * @see com.make.equo.ui.helper.provider.model.impl.WebdialogPackageImpl#getMWebDialog()
       * @generated
       */
      EClass MWEB_DIALOG = eINSTANCE.getMWebDialog();

      /**
       * The meta object literal for the '<em><b>Buttons</b></em>' reference list feature.
       * <!-- begin-user-doc -->
       * <!-- end-user-doc -->
       * @generated
       */
      EReference MWEB_DIALOG__BUTTONS = eINSTANCE.getMWebDialog_Buttons();

      /**
       * The meta object literal for the '<em><b>Title</b></em>' attribute feature.
       * <!-- begin-user-doc -->
       * <!-- end-user-doc -->
       * @generated
       */
      EAttribute MWEB_DIALOG__TITLE = eINSTANCE.getMWebDialog_Title();

      /**
       * The meta object literal for the '<em><b>Message</b></em>' attribute feature.
       * <!-- begin-user-doc -->
       * <!-- end-user-doc -->
       * @generated
       */
      EAttribute MWEB_DIALOG__MESSAGE = eINSTANCE.getMWebDialog_Message();

      /**
       * The meta object literal for the '<em><b>Parent Shell</b></em>' attribute feature.
       * <!-- begin-user-doc -->
       * <!-- end-user-doc -->
       * @generated
       */
      EAttribute MWEB_DIALOG__PARENT_SHELL = eINSTANCE.getMWebDialog_ParentShell();

      /**
       * The meta object literal for the '<em><b>Blocker</b></em>' attribute feature.
       * <!-- begin-user-doc -->
       * <!-- end-user-doc -->
       * @generated
       */
      EAttribute MWEB_DIALOG__BLOCKER = eINSTANCE.getMWebDialog_Blocker();

      /**
       * The meta object literal for the '<em><b>Response</b></em>' attribute feature.
       * <!-- begin-user-doc -->
       * <!-- end-user-doc -->
       * @generated
       */
      EAttribute MWEB_DIALOG__RESPONSE = eINSTANCE.getMWebDialog_Response();

      /**
       * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
       * <!-- begin-user-doc -->
       * <!-- end-user-doc -->
       * @generated
       */
      EAttribute MWEB_DIALOG__TYPE = eINSTANCE.getMWebDialog_Type();

      /**
       * The meta object literal for the '{@link com.make.equo.ui.helper.provider.model.impl.MButtonImpl <em>MButton</em>}' class.
       * <!-- begin-user-doc -->
       * <!-- end-user-doc -->
       * @see com.make.equo.ui.helper.provider.model.impl.MButtonImpl
       * @see com.make.equo.ui.helper.provider.model.impl.WebdialogPackageImpl#getMButton()
       * @generated
       */
      EClass MBUTTON = eINSTANCE.getMButton();

      /**
       * The meta object literal for the '<em><b>Action</b></em>' attribute feature.
       * <!-- begin-user-doc -->
       * <!-- end-user-doc -->
       * @generated
       */
      EAttribute MBUTTON__ACTION = eINSTANCE.getMButton_Action();

      /**
       * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
       * <!-- begin-user-doc -->
       * <!-- end-user-doc -->
       * @generated
       */
      EAttribute MBUTTON__LABEL = eINSTANCE.getMButton_Label();

      /**
       * The meta object literal for the '<em><b>Default</b></em>' attribute feature.
       * <!-- begin-user-doc -->
       * <!-- end-user-doc -->
       * @generated
       */
      EAttribute MBUTTON__DEFAULT = eINSTANCE.getMButton_Default();

   }

} //WebdialogPackage
