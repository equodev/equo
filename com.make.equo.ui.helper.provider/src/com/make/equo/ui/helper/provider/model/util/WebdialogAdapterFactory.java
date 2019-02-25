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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import com.make.equo.ui.helper.provider.model.MButton;
import com.make.equo.ui.helper.provider.model.MWebDialog;
import com.make.equo.ui.helper.provider.model.WebdialogPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see com.make.equo.ui.helper.provider.model.WebdialogPackage
 * @generated
 */
public class WebdialogAdapterFactory extends AdapterFactoryImpl {
   /**
    * The cached model package.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   protected static WebdialogPackage modelPackage;

   /**
    * Creates an instance of the adapter factory.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   public WebdialogAdapterFactory() {
      if (modelPackage == null) {
         modelPackage = WebdialogPackage.eINSTANCE;
      }
   }

   /**
    * Returns whether this factory is applicable for the type of the object.
    * <!-- begin-user-doc -->
    * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
    * <!-- end-user-doc -->
    * @return whether this factory is applicable for the type of the object.
    * @generated
    */
   @Override
   public boolean isFactoryForType(Object object) {
      if (object == modelPackage) {
         return true;
      }
      if (object instanceof EObject) {
         return ((EObject)object).eClass().getEPackage() == modelPackage;
      }
      return false;
   }

   /**
    * The switch that delegates to the <code>createXXX</code> methods.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   protected WebdialogSwitch<Adapter> modelSwitch =
      new WebdialogSwitch<Adapter>() {
         @Override
         public Adapter caseMWebDialog(MWebDialog object) {
            return createMWebDialogAdapter();
         }
         @Override
         public Adapter caseMButton(MButton object) {
            return createMButtonAdapter();
         }
         @Override
         public Adapter caseApplicationElement(MApplicationElement object) {
            return createApplicationElementAdapter();
         }
         @Override
         public Adapter caseLocalizable(MLocalizable object) {
            return createLocalizableAdapter();
         }
         @Override
         public Adapter caseUIElement(MUIElement object) {
            return createUIElementAdapter();
         }
         @Override
         public <T extends MUIElement> Adapter caseElementContainer(MElementContainer<T> object) {
            return createElementContainerAdapter();
         }
         @Override
         public Adapter caseUILabel(MUILabel object) {
            return createUILabelAdapter();
         }
         @Override
         public Adapter caseContext(MContext object) {
            return createContextAdapter();
         }
         @Override
         public Adapter caseHandlerContainer(MHandlerContainer object) {
            return createHandlerContainerAdapter();
         }
         @Override
         public Adapter caseBindings(MBindings object) {
            return createBindingsAdapter();
         }
         @Override
         public Adapter caseSnippetContainer(MSnippetContainer object) {
            return createSnippetContainerAdapter();
         }
         @Override
         public Adapter caseWindow(MWindow object) {
            return createWindowAdapter();
         }
         @Override
         public Adapter caseWindowElement(MWindowElement object) {
            return createWindowElementAdapter();
         }
         @Override
         public Adapter defaultCase(EObject object) {
            return createEObjectAdapter();
         }
      };

   /**
    * Creates an adapter for the <code>target</code>.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @param target the object to adapt.
    * @return the adapter for the <code>target</code>.
    * @generated
    */
   @Override
   public Adapter createAdapter(Notifier target) {
      return modelSwitch.doSwitch((EObject)target);
   }


   /**
    * Creates a new adapter for an object of class '{@link com.make.equo.ui.helper.provider.model.MWebDialog <em>MWeb Dialog</em>}'.
    * <!-- begin-user-doc -->
    * This default implementation returns null so that we can easily ignore cases;
    * it's useful to ignore a case when inheritance will catch all the cases anyway.
    * <!-- end-user-doc -->
    * @return the new adapter.
    * @see com.make.equo.ui.helper.provider.model.MWebDialog
    * @generated
    */
   public Adapter createMWebDialogAdapter() {
      return null;
   }

   /**
    * Creates a new adapter for an object of class '{@link com.make.equo.ui.helper.provider.model.MButton <em>MButton</em>}'.
    * <!-- begin-user-doc -->
    * This default implementation returns null so that we can easily ignore cases;
    * it's useful to ignore a case when inheritance will catch all the cases anyway.
    * <!-- end-user-doc -->
    * @return the new adapter.
    * @see com.make.equo.ui.helper.provider.model.MButton
    * @generated
    */
   public Adapter createMButtonAdapter() {
      return null;
   }

   /**
    * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.MApplicationElement <em>Element</em>}'.
    * <!-- begin-user-doc -->
    * This default implementation returns null so that we can easily ignore cases;
    * it's useful to ignore a case when inheritance will catch all the cases anyway.
    * <!-- end-user-doc -->
    * @return the new adapter.
    * @see org.eclipse.e4.ui.model.application.MApplicationElement
    * @since 1.0
    * @generated
    */
   public Adapter createApplicationElementAdapter() {
      return null;
   }

   /**
    * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.MLocalizable <em>Localizable</em>}'.
    * <!-- begin-user-doc -->
    * This default implementation returns null so that we can easily ignore cases;
    * it's useful to ignore a case when inheritance will catch all the cases anyway.
    * <!-- end-user-doc -->
    * @return the new adapter.
    * @see org.eclipse.e4.ui.model.application.ui.MLocalizable
    * @since 1.1
    * @generated
    */
   public Adapter createLocalizableAdapter() {
      return null;
   }

   /**
    * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.MUIElement <em>UI Element</em>}'.
    * <!-- begin-user-doc -->
    * This default implementation returns null so that we can easily ignore cases;
    * it's useful to ignore a case when inheritance will catch all the cases anyway.
    * <!-- end-user-doc -->
    * @return the new adapter.
    * @see org.eclipse.e4.ui.model.application.ui.MUIElement
    * @since 1.0
    * @generated
    */
   public Adapter createUIElementAdapter() {
      return null;
   }

   /**
    * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.MElementContainer <em>Element Container</em>}'.
    * <!-- begin-user-doc -->
    * This default implementation returns null so that we can easily ignore cases;
    * it's useful to ignore a case when inheritance will catch all the cases anyway.
    * <!-- end-user-doc -->
    * @return the new adapter.
    * @see org.eclipse.e4.ui.model.application.ui.MElementContainer
    * @since 1.0
    * @generated
    */
   public Adapter createElementContainerAdapter() {
      return null;
   }

   /**
    * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.MUILabel <em>UI Label</em>}'.
    * <!-- begin-user-doc -->
    * This default implementation returns null so that we can easily ignore cases;
    * it's useful to ignore a case when inheritance will catch all the cases anyway.
    * <!-- end-user-doc -->
    * @return the new adapter.
    * @see org.eclipse.e4.ui.model.application.ui.MUILabel
    * @since 1.0
    * @generated
    */
   public Adapter createUILabelAdapter() {
      return null;
   }

   /**
    * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.MContext <em>Context</em>}'.
    * <!-- begin-user-doc -->
    * This default implementation returns null so that we can easily ignore cases;
    * it's useful to ignore a case when inheritance will catch all the cases anyway.
    * <!-- end-user-doc -->
    * @return the new adapter.
    * @see org.eclipse.e4.ui.model.application.ui.MContext
    * @since 1.0
    * @generated
    */
   public Adapter createContextAdapter() {
      return null;
   }

   /**
    * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.commands.MHandlerContainer <em>Handler Container</em>}'.
    * <!-- begin-user-doc -->
    * This default implementation returns null so that we can easily ignore cases;
    * it's useful to ignore a case when inheritance will catch all the cases anyway.
    * <!-- end-user-doc -->
    * @return the new adapter.
    * @see org.eclipse.e4.ui.model.application.commands.MHandlerContainer
    * @since 1.0
    * @generated
    */
   public Adapter createHandlerContainerAdapter() {
      return null;
   }

   /**
    * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.commands.MBindings <em>Bindings</em>}'.
    * <!-- begin-user-doc -->
    * This default implementation returns null so that we can easily ignore cases;
    * it's useful to ignore a case when inheritance will catch all the cases anyway.
    * <!-- end-user-doc -->
    * @return the new adapter.
    * @see org.eclipse.e4.ui.model.application.commands.MBindings
    * @since 1.0
    * @generated
    */
   public Adapter createBindingsAdapter() {
      return null;
   }

   /**
    * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.MSnippetContainer <em>Snippet Container</em>}'.
    * <!-- begin-user-doc -->
    * This default implementation returns null so that we can easily ignore cases;
    * it's useful to ignore a case when inheritance will catch all the cases anyway.
    * <!-- end-user-doc -->
    * @return the new adapter.
    * @see org.eclipse.e4.ui.model.application.ui.MSnippetContainer
    * @since 1.0
    * @generated
    */
   public Adapter createSnippetContainerAdapter() {
      return null;
   }

   /**
    * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.basic.MWindow <em>Window</em>}'.
    * <!-- begin-user-doc -->
    * This default implementation returns null so that we can easily ignore cases;
    * it's useful to ignore a case when inheritance will catch all the cases anyway.
    * <!-- end-user-doc -->
    * @return the new adapter.
    * @see org.eclipse.e4.ui.model.application.ui.basic.MWindow
    * @since 1.0
    * @generated
    */
   public Adapter createWindowAdapter() {
      return null;
   }

   /**
    * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.basic.MWindowElement <em>Window Element</em>}'.
    * <!-- begin-user-doc -->
    * This default implementation returns null so that we can easily ignore cases;
    * it's useful to ignore a case when inheritance will catch all the cases anyway.
    * <!-- end-user-doc -->
    * @return the new adapter.
    * @see org.eclipse.e4.ui.model.application.ui.basic.MWindowElement
    * @since 1.0
    * @generated
    */
   public Adapter createWindowElementAdapter() {
      return null;
   }

   /**
    * Creates a new adapter for the default case.
    * <!-- begin-user-doc -->
    * This default implementation returns null.
    * <!-- end-user-doc -->
    * @return the new adapter.
    * @generated
    */
   public Adapter createEObjectAdapter() {
      return null;
   }

} //WebdialogAdapterFactory
