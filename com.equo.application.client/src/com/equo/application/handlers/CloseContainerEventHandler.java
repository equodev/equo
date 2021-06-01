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

package com.equo.application.handlers;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

/**
 * Handle when a side part container is closed.
 */
public class CloseContainerEventHandler implements EventHandler {

  private IEventBroker broker;
  @SuppressWarnings("rawtypes")
  private MElementContainer elementContainer;

  public CloseContainerEventHandler(
      @SuppressWarnings("rawtypes") MElementContainer elementContainer, IEventBroker broker) {
    this.elementContainer = elementContainer;
    this.broker = broker;
  }

  @Override
  public void handleEvent(Event event) {
    if (elementContainer.getChildren().size() == 0) {
      MElementContainer<MUIElement> parent = elementContainer.getParent();
      if (parent != null) {
        parent.getChildren().remove(elementContainer);
        parent.setToBeRendered(true);
        broker.unsubscribe(this);
      }
    }
  }
}
