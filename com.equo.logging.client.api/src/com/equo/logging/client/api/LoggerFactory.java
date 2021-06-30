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

import java.util.Optional;

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
    Bundle ctxBundle = FrameworkUtil.getBundle(clazz);
    if (ctxBundle == null) {
      return null;
    }
    final BundleContext bundleContext = ctxBundle.getBundleContext();
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
    if (logger == null) {
      return new LazyLogger(clazz);
    }
    if (logger instanceof AbstractLogger) {
      ((AbstractLogger) logger).init(clazz);
    }
    return logger;
  }

  @SuppressWarnings("rawtypes")
  private static class LazyLogger implements Logger {
    private Logger instance;
    private Class clazz;

    LazyLogger(Class clazz) {
      this.clazz = clazz;
    }

    private void getInstance() {
      if (instance == null) {
        instance = LoggerFactory.getLogger(clazz);
      }
    }

    @Override
    public void debug(String message) {
      getInstance();
      instance.debug(message);
    }

    @Override
    public void debug(String message, Object... args) {
      getInstance();
      instance.debug(message, args);
    }

    @Override
    public void debug(String message, Throwable throwable) {
      getInstance();
      instance.debug(message, throwable);
    }

    @Override
    public void info(String message) {
      getInstance();
      instance.info(message);
    }

    @Override
    public void info(String message, Object... args) {
      getInstance();
      instance.info(message, args);
    }

    @Override
    public void info(String message, Throwable throwable) {
      getInstance();
      instance.info(message, throwable);
    }

    @Override
    public void warn(String message) {
      getInstance();
      instance.warn(message);
    }

    @Override
    public void warn(String message, Object... args) {
      getInstance();
      instance.warn(message, args);
    }

    @Override
    public void warn(String message, Throwable throwable) {
      getInstance();
      instance.warn(message, throwable);
    }

    @Override
    public void error(String message) {
      getInstance();
      instance.error(message);
    }

    @Override
    public void error(String message, Object... args) {
      getInstance();
      instance.error(message, args);
    }

    @Override
    public void error(String message, Throwable throwable) {
      getInstance();
      instance.error(message, throwable);
    }

    @Override
    public void trace(String message) {
      getInstance();
      instance.trace(message);
    }

    @Override
    public void trace(String message, Object... args) {
      getInstance();
      instance.trace(message, args);
    }

    @Override
    public void trace(String message, Throwable throwable) {
      getInstance();
      instance.trace(message, throwable);
    }

    @Override
    public boolean isDebugEnabled() {
      getInstance();
      return instance.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
      getInstance();
      return instance.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
      getInstance();
      return instance.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
      getInstance();
      return instance.isErrorEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
      getInstance();
      return instance.isTraceEnabled();
    }

    @Override
    public Optional<Level> getLoggerLevel() {
      getInstance();
      return instance.getLoggerLevel();
    }

    @Override
    public void setLoggerLevel(Level level) {
      getInstance();
      instance.setLoggerLevel(level);
    }

  }
}
