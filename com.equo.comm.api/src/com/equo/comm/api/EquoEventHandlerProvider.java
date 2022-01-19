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

import java.util.Optional;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * A helper class to obtain an EquoEventHandler instance.
 */
public class EquoEventHandlerProvider {

  private IEquoEventHandler equoEventHandler;

  public EquoEventHandlerProvider() {
    loadEventHandlerInstance();
  }

  private void loadEventHandlerInstance() {
    Bundle ctxBundle = FrameworkUtil.getBundle(this.getClass());
    if (ctxBundle != null) {
      BundleContext ctx = ctxBundle.getBundleContext();
      if (ctx != null) {
        @SuppressWarnings("unchecked")
        ServiceReference<IEquoEventHandler> serviceReference = (ServiceReference<
            IEquoEventHandler>) ctx.getServiceReference(IEquoEventHandler.class.getName());
        if (serviceReference != null) {
          equoEventHandler = ctx.getService(serviceReference);
        }
      }
    }
  }

  /**
   * Gets the app event handler or {@code Optional.empty()} if it couldn't yet be
   * loaded.
   */
  public Optional<IEquoEventHandler> getEquoEventHandler() {
    if (equoEventHandler == null) {
      loadEventHandlerInstance();
    }
    return Optional.ofNullable(equoEventHandler);
  }

}
