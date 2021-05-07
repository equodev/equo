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
