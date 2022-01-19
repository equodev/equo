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

package com.equo.builders.tests.mocks;

import java.net.URI;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

public class EmptyClient extends WebSocketClient {

	private String message;

	public static EmptyClient create(URI uri) {
		return new EmptyClient(uri);
	}

	public static EmptyClient create(URI serverUri, Draft draft) {
		return new EmptyClient(serverUri, draft);
	}

	public EmptyClient(URI serverUri, Draft draft) {
		super(serverUri, draft);
	}

	private EmptyClient(URI serverURI) {
		super(serverURI);
	}


	public String getMessage() {
		return message;
	}

	public EmptyClient singleMessage(String message) {
		this.message = message;
		return this;
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		send(message);
		System.out.println("new connection opened");
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("closed with exit code " + code + " additional info: " + reason);
	}

	@Override
	public void onMessage(String message) {
		System.out.println("received message: " + message);
	}

	@Override
	public void onMessage(ByteBuffer message) {
		System.out.println("received ByteBuffer");
	}

	@Override
	public void onError(Exception ex) {
		System.err.println("an error occurred:" + ex);
	}

}
