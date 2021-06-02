/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of Equo Framework.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equoplatform.com/terms.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3 as published by the Free Software
** Foundation. Please review the following
** information to ensure the GNU General Public License requirements will
** be met: https://www.gnu.org/licenses/gpl-3.0.html.
**
****************************************************************************/

package com.equo.logging.client.api;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Factory class to obtain a new Logger instance.
 */
public class LoggerFactory {
  private static org.slf4j.Logger factoryLogger =
      org.slf4j.LoggerFactory.getLogger(LoggerFactory.class);
  private static boolean loaded = false;

  static <T extends Object> T getService(final Class<T> clazz) {
    final BundleContext bundleContext = FrameworkUtil.getBundle(clazz).getBundleContext();
    if (!loaded) {
      String bundleName =
          System.getProperty(IConstants.LOGGER_BUNDLE_PROPERTY, IConstants.DEFAULT_LOGGER);
      for (Bundle bundle : bundleContext.getBundles()) {
        if (bundle.getSymbolicName().startsWith(bundleName)) {
          try {
            bundle.start();
          } catch (BundleException e) {
            factoryLogger.error("Error activating Equo Logger", e);
          }
          break;
        }
      }
      loaded = true;
    }

    final ServiceReference<T> ref = bundleContext.getServiceReference(clazz);
    return bundleContext.getServiceObjects(ref).getService();
  }

  /**
   * Gets a new Logger instance.
   * @param  clazz a class associated to the new logger
   * @return       new logger
   */
  @SuppressWarnings("rawtypes")
  public static Logger getLogger(final Class clazz) {
    Logger logger = getService(Logger.class);
    if (logger instanceof AbstractLogger) {
      ((AbstractLogger) logger).init(clazz);
    }
    return logger;
  }
}
