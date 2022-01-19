/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of the Equo SDK.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equo.dev/terms.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3 as published by the Free Software
** Foundation. Please review the following
** information to ensure the GNU General Public License requirements will
** be met: https://www.gnu.org/licenses/gpl-3.0.html.
**
****************************************************************************/

package com.equo.comm.api;

import java.util.function.Consumer;
import java.util.function.Function;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Helper class to get a Comm service instance.
 */
public class EquoCommHelper {
  /**
   * Gets the implementation of the Comm service.
   */
  public static IEquoCommService getCommService() {

    Bundle ctxBundle = FrameworkUtil.getBundle(IEquoCommService.class);
    if (ctxBundle == null) {
      return new LazyEquoCommService();
    }
    BundleContext ctx = ctxBundle.getBundleContext();
    if (ctx != null) {
      @SuppressWarnings("unchecked")
      ServiceReference<IEquoCommService> serviceReference = (ServiceReference<IEquoCommService>) ctx
          .getServiceReference(IEquoCommService.class.getName());
      if (serviceReference != null) {
        return ctx.getServiceObjects(serviceReference).getService();
      }
    }
    return null;
  }

  private static class LazyEquoCommService implements IEquoCommService {
    private IEquoCommService instance;

    private void obtainInstance() {
      if (instance == null) {
        instance = EquoCommHelper.getCommService();
      }
    }

    @Override
    public <T, R> void addEventHandler(String actionId, Function<T, R> actionHandler,
        Class<?>... paramTypes) {
      obtainInstance();
      instance.addEventHandler(actionId, actionHandler, paramTypes);
    }

    @Override
    public <T> void addEventHandler(String actionId, Consumer<T> actionHandler,
        Class<?>... paramTypes) {
      obtainInstance();
      instance.addEventHandler(actionId, actionHandler, paramTypes);
    }

    @Override
    public void send(String userEvent, Object payload) {
      obtainInstance();
      instance.send(userEvent, payload);
    }

    @Override
    public int getPort() {
      obtainInstance();
      return instance.getPort();
    }

  }
}
