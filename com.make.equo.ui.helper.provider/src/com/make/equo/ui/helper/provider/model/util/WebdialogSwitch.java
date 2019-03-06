/**
 */
package com.make.equo.ui.helper.provider.model.util;

import org.eclipse.e4.ui.model.application.MApplicationElement;

import org.eclipse.e4.ui.model.application.commands.MBindings;
import org.eclipse.e4.ui.model.application.commands.MHandlerContainer;

import org.eclipse.e4.ui.model.application.ui.MContext;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MLocalizable;
import org.eclipse.e4.ui.model.application.ui.MSnippetContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.MUILabel;

import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindowElement;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import com.make.equo.ui.helper.provider.model.MButton;
import com.make.equo.ui.helper.provider.model.MWebDialog;
import com.make.equo.ui.helper.provider.model.WebdialogPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see com.make.equo.ui.helper.provider.model.WebdialogPackage
 * @generated
 */
public class WebdialogSwitch<T1> extends Switch<T1> {
   /**
    * The cached model package
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   protected static WebdialogPackage modelPackage;

   /**
    * Creates an instance of the switch.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   public WebdialogSwitch() {
      if (modelPackage == null) {
         modelPackage = WebdialogPackage.eINSTANCE;
      }
   }

   /**
    * Checks whether this is a switch for the given package.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @param ePackage the package in question.
    * @return whether this is a switch for the given package.
    * @generated
    */
   @Override
   protected boolean isSwitchFor(EPackage ePackage) {
      return ePackage == modelPackage;
   }

   /**
    * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @return the first non-null result returned by a <code>caseXXX</code> call.
    * @generated
    */
   @Override
   protected T1 doSwitch(int classifierID, EObject theEObject) {
      switch (classifierID) {
         case WebdialogPackage.MWEB_DIALOG: {
            MWebDialog mWebDialog = (MWebDialog)theEObject;
            T1 result = caseMWebDialog(mWebDialog);
            if (result == null) result = caseWindow(mWebDialog);
            if (result == null) result = caseElementContainer(mWebDialog);
            if (result == null) result = caseUILabel(mWebDialog);
            if (result == null) result = caseContext(mWebDialog);
            if (result == null) result = caseHandlerContainer(mWebDialog);
            if (result == null) result = caseBindings(mWebDialog);
            if (result == null) result = caseSnippetContainer(mWebDialog);
            if (result == null) result = caseUIElement(mWebDialog);
            if (result == null) result = caseApplicationElement(mWebDialog);
            if (result == null) result = caseLocalizable(mWebDialog);
            if (result == null) result = defaultCase(theEObject);
            return result;
         }
         case WebdialogPackage.MBUTTON: {
            MButton mButton = (MButton)theEObject;
            T1 result = caseMButton(mButton);
            if (result == null) result = caseWindowElement(mButton);
            if (result == null) result = caseUIElement(mButton);
            if (result == null) result = caseApplicationElement(mButton);
            if (result == null) result = caseLocalizable(mButton);
            if (result == null) result = defaultCase(theEObject);
            return result;
         }
         default: return defaultCase(theEObject);
      }
   }

   /**
    * Returns the result of interpreting the object as an instance of '<em>MWeb Dialog</em>'.
    * <!-- begin-user-doc -->
    * This implementation returns null;
    * returning a non-null result will terminate the switch.
    * <!-- end-user-doc -->
    * @param object the target of the switch.
    * @return the result of interpreting the object as an instance of '<em>MWeb Dialog</em>'.
    * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
    * @generated
    */
   public T1 caseMWebDialog(MWebDialog object) {
      return null;
   }

   /**
    * Returns the result of interpreting the object as an instance of '<em>MButton</em>'.
    * <!-- begin-user-doc -->
    * This implementation returns null;
    * returning a non-null result will terminate the switch.
    * <!-- end-user-doc -->
    * @param object the target of the switch.
    * @return the result of interpreting the object as an instance of '<em>MButton</em>'.
    * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
    * @generated
    */
   public T1 caseMButton(MButton object) {
      return null;
   }

   /**
    * Returns the result of interpreting the object as an instance of '<em>Element</em>'.
    * <!-- begin-user-doc -->
    * This implementation returns null;
    * returning a non-null result will terminate the switch.
    * <!-- end-user-doc -->
    * @param object the target of the switch.
    * @return the result of interpreting the object as an instance of '<em>Element</em>'.
    * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
    * @since 1.0
    * @generated
    */
   public T1 caseApplicationElement(MApplicationElement object) {
      return null;
   }

   /**
    * Returns the result of interpreting the object as an instance of '<em>Localizable</em>'.
    * <!-- begin-user-doc -->
    * This implementation returns null;
    * returning a non-null result will terminate the switch.
    * <!-- end-user-doc -->
    * @param object the target of the switch.
    * @return the result of interpreting the object as an instance of '<em>Localizable</em>'.
    * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
    * @since 1.1
    * @generated
    */
   public T1 caseLocalizable(MLocalizable object) {
      return null;
   }

   /**
    * Returns the result of interpreting the object as an instance of '<em>UI Element</em>'.
    * <!-- begin-user-doc -->
    * This implementation returns null;
    * returning a non-null result will terminate the switch.
    * <!-- end-user-doc -->
    * @param object the target of the switch.
    * @return the result of interpreting the object as an instance of '<em>UI Element</em>'.
    * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
    * @since 1.0
    * @generated
    */
   public T1 caseUIElement(MUIElement object) {
      return null;
   }

   /**
    * Returns the result of interpreting the object as an instance of '<em>Element Container</em>'.
    * <!-- begin-user-doc -->
    * This implementation returns null;
    * returning a non-null result will terminate the switch.
    * <!-- end-user-doc -->
    * @param object the target of the switch.
    * @return the result of interpreting the object as an instance of '<em>Element Container</em>'.
    * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
    * @since 1.0
    * @generated
    */
   public <T extends MUIElement> T1 caseElementContainer(MElementContainer<T> object) {
      return null;
   }

   /**
    * Returns the result of interpreting the object as an instance of '<em>UI Label</em>'.
    * <!-- begin-user-doc -->
    * This implementation returns null;
    * returning a non-null result will terminate the switch.
    * <!-- end-user-doc -->
    * @param object the target of the switch.
    * @return the result of interpreting the object as an instance of '<em>UI Label</em>'.
    * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
    * @since 1.0
    * @generated
    */
   public T1 caseUILabel(MUILabel object) {
      return null;
   }

   /**
    * Returns the result of interpreting the object as an instance of '<em>Context</em>'.
    * <!-- begin-user-doc -->
    * This implementation returns null;
    * returning a non-null result will terminate the switch.
    * <!-- end-user-doc -->
    * @param object the target of the switch.
    * @return the result of interpreting the object as an instance of '<em>Context</em>'.
    * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
    * @since 1.0
    * @generated
    */
   public T1 caseContext(MContext object) {
      return null;
   }

   /**
    * Returns the result of interpreting the object as an instance of '<em>Handler Container</em>'.
    * <!-- begin-user-doc -->
    * This implementation returns null;
    * returning a non-null result will terminate the switch.
    * <!-- end-user-doc -->
    * @param object the target of the switch.
    * @return the result of interpreting the object as an instance of '<em>Handler Container</em>'.
    * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
    * @since 1.0
    * @generated
    */
   public T1 caseHandlerContainer(MHandlerContainer object) {
      return null;
   }

   /**
    * Returns the result of interpreting the object as an instance of '<em>Bindings</em>'.
    * <!-- begin-user-doc -->
    * This implementation returns null;
    * returning a non-null result will terminate the switch.
    * <!-- end-user-doc -->
    * @param object the target of the switch.
    * @return the result of interpreting the object as an instance of '<em>Bindings</em>'.
    * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
    * @since 1.0
    * @generated
    */
   public T1 caseBindings(MBindings object) {
      return null;
   }

   /**
    * Returns the result of interpreting the object as an instance of '<em>Snippet Container</em>'.
    * <!-- begin-user-doc -->
    * This implementation returns null;
    * returning a non-null result will terminate the switch.
    * <!-- end-user-doc -->
    * @param object the target of the switch.
    * @return the result of interpreting the object as an instance of '<em>Snippet Container</em>'.
    * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
    * @since 1.0
    * @generated
    */
   public T1 caseSnippetContainer(MSnippetContainer object) {
      return null;
   }

   /**
    * Returns the result of interpreting the object as an instance of '<em>Window</em>'.
    * <!-- begin-user-doc -->
    * This implementation returns null;
    * returning a non-null result will terminate the switch.
    * <!-- end-user-doc -->
    * @param object the target of the switch.
    * @return the result of interpreting the object as an instance of '<em>Window</em>'.
    * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
    * @since 1.0
    * @generated
    */
   public T1 caseWindow(MWindow object) {
      return null;
   }

   /**
    * Returns the result of interpreting the object as an instance of '<em>Window Element</em>'.
    * <!-- begin-user-doc -->
    * This implementation returns null;
    * returning a non-null result will terminate the switch.
    * <!-- end-user-doc -->
    * @param object the target of the switch.
    * @return the result of interpreting the object as an instance of '<em>Window Element</em>'.
    * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
    * @since 1.0
    * @generated
    */
   public T1 caseWindowElement(MWindowElement object) {
      return null;
   }

   /**
    * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
    * <!-- begin-user-doc -->
    * This implementation returns null;
    * returning a non-null result will terminate the switch, but this is the last case anyway.
    * <!-- end-user-doc -->
    * @param object the target of the switch.
    * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
    * @see #doSwitch(org.eclipse.emf.ecore.EObject)
    * @generated
    */
   @Override
   public T1 defaultCase(EObject object) {
      return null;
   }

} //WebdialogSwitch
