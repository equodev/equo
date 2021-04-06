package com.make.equo.logging.client.api;

public interface ILoggingApi {

	public void debug(String message);

	public void debug(String message, Throwable throwable);

	public void info(String message);

	public void info(String message, Throwable throwable);

	public void warn(String message);

	public void warn(String message, Throwable throwable);

	public void error(String message);

	public void error(String message, Throwable throwable);

	public void trace(String message);

	public void trace(String message, Throwable throwable);

	public boolean isDebugEnabled();

	public boolean isInfoEnabled();

	public boolean isWarnEnabled();

	public boolean isErrorEnabled();

	public boolean isTraceEnabled();

}
