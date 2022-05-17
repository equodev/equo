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

package com.equo.node.packages.tests.mocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import com.equo.logging.client.api.Level;
import com.equo.logging.client.api.Logger;
import com.equo.logging.client.api.impl.LoggingCoreImpl;

@Component(scope = ServiceScope.PROTOTYPE, service = Logger.class, property = { "service.ranking:Integer=100000" })
public class LoggingServiceMock extends LoggingCoreImpl {

	private static List<String> receivedMessages = Collections.synchronizedList(new ArrayList<String>());

	@Override
	public void debug(String message) {
		super.debug(message);
		receivedMessages.add(message);
	}

	@Override
	public void info(String message) {
		super.info(message);
		receivedMessages.add(message);
	}

	@Override
	public void warn(String message) {
		super.warn(message);
		receivedMessages.add(message);
	}

	@Override
	public void error(String message) {
		super.error(message);
		receivedMessages.add(message);
	}

	@Override
	public void trace(String message) {
		super.trace(message);
		receivedMessages.add(message);
	}

	@Override
	public void setLoggerLevel(Level level) {
		super.setLoggerLevel(level);
		receivedMessages.add("Changed to " + level.toString());
	}

}
