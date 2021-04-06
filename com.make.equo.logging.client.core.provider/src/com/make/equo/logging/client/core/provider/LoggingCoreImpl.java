package com.make.equo.logging.client.core.provider;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import com.make.equo.logging.client.api.AbstractLogger;
import com.make.equo.logging.client.api.ILoggingApi;

@Component(scope = ServiceScope.PROTOTYPE, service = ILoggingApi.class)
public class LoggingCoreImpl extends AbstractLogger {

	@Override
	public void debug(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void debug(String message, Throwable throwable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void info(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void info(String message, Throwable throwable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void warn(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void warn(String message, Throwable throwable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(String message, Throwable throwable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void trace(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void trace(String message, Throwable throwable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDebugEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInfoEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isWarnEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isErrorEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTraceEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void init(Class clazz) {
		// TODO Auto-generated method stub
		
	}

}
