package com.make.equo.logging.client.api;

import java.util.Optional;

public interface Logger {

	public void debug(String message);

	public void debug(String message, Object... args);

	public void debug(String message, Throwable throwable);

	public void info(String message);

	public void info(String message, Object... args);

	public void info(String message, Throwable throwable);

	public void warn(String message);

	public void warn(String message, Object... args);

	public void warn(String message, Throwable throwable);

	public void error(String message);

	public void error(String message, Object... args);

	public void error(String message, Throwable throwable);

	public void trace(String message);

	public void trace(String message, Object... args);

	public void trace(String message, Throwable throwable);

	public boolean isDebugEnabled();

	public boolean isInfoEnabled();

	public boolean isWarnEnabled();

	public boolean isErrorEnabled();

	public boolean isTraceEnabled();

	/**
	 * 
	 * @return current custom level for this logger or {@code null} if there isn't
	 *         any custom level (common case, when the global level obtained with
	 *         {@link com.make.equo.logging.client.api.LoggerConfiguration
	 *         LoggerConfiguration} is used).
	 */
	public Optional<Level> getLoggerLevel();

	/**
	 * Set a custom level for this logger. Once setted, the logger will ignore
	 * global level and use the configured one.
	 * 
	 * @param level The new custom level for this logger. {@code null} to disable it
	 *              (and use the global level again).
	 */
	public void setLoggerLevel(Level level);

}
