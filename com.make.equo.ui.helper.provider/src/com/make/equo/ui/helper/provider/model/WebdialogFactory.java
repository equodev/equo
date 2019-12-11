/**
 */
package com.make.equo.ui.helper.provider.model;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.make.equo.ui.helper.provider.model.WebdialogPackage
 * @generated
 */
public interface WebdialogFactory extends EFactory {
   /**
    * The singleton instance of the factory.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @generated
    */
   WebdialogFactory eINSTANCE = com.make.equo.ui.helper.provider.model.impl.WebdialogFactoryImpl.init();

   /**
    * Returns a new object of class '<em>MWeb Dialog</em>'.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @return a new object of class '<em>MWeb Dialog</em>'.
    * @generated
    */
   MWebDialog createMWebDialog();

   /**
    * Returns a new object of class '<em>MButton</em>'.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @return a new object of class '<em>MButton</em>'.
    * @generated
    */
   MButton createMButton();
   
   MButtonToggle createMButtonToggle();

   /**
    * Returns the package supported by this factory.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @return the package supported by this factory.
    * @generated
    */
   WebdialogPackage getWebdialogPackage();

} //WebdialogFactory
