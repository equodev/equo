package com.equo.logging.client.api;

import java.util.Optional;

public interface Logger {
	/**
	 * Logs a message at the DEBUG level.
	 * 
	 * @param message
	 */
	public void debug(String message);

	/**
	 * Logs a message at the DEBUG level according to the specified format and
	 * arguments.
	 * 
	 * @param message
	 * @param args
	 */
	public void debug(String message, Object... args);

	/**
	 * Logs an exception (throwable) at the DEBUG level with an accompanying message.
	 * 
	 * @param message
	 * @param throwable
	 */
	public void debug(String message, Throwable throwable);

	/**
	 * Logs a message at the INFO level.
	 * 
	 * @param message
	 */
	public void info(String message);

	/**
	 * Logs a message at the INFO level according to the specified format and
	 * arguments.
	 * 
	 * @param message
	 * @param args
	 */
	public void info(String message, Object... args);

	/**
	 * Logs an exception (throwable) at the INFO level with an accompanying message.
	 * 
	 * @param message
	 * @param throwable
	 */
	public void info(String message, Throwable throwable);

	/**
	 * Logs a message at the WARN level.
	 * 
	 * @param message
	 */
	public void warn(String message);

	/**
	 * Logs a message at the WARN level according to the specified format and
	 * arguments.
	 * 
	 * @param message
	 * @param args
	 */
	public void warn(String message, Object... args);

	/**
	 * Logs an exception (throwable) at the WARN level with an accompanying message.
	 * 
	 * @param message
	 * @param throwable
	 */
	public void warn(String message, Throwable throwable);

	/**
	 * Logs a message at the ERROR level.
	 * 
	 * @param message
	 */
	public void error(String message);

	/**
	 * Logs a message at the ERROR level according to the specified format and
	 * arguments.
	 * 
	 * @param message
	 * @param args
	 */
	public void error(String message, Object... args);

	/**
	 * Logs an exception (throwable) at the ERROR level with an accompanying message.
	 * 
	 * @param message
	 * @param throwable
	 */
	public void error(String message, Throwable throwable);

	/**
	 * Logs a message at the TRACE level.
	 * 
	 * @param message
	 */
	public void trace(String message);

	/**
	 * Logs a message at the TRACE level according to the specified format and
	 * arguments.
	 * 
	 * @param message
	 * @param args
	 */
	public void trace(String message, Object... args);

	/**
	 * Logs an exception (throwable) at the TRACE level with an accompanying message.
	 * 
	 * @param message
	 * @param throwable
	 */
	public void trace(String message, Throwable throwable);

	/**
	 * Returns is the logger instance enabled for the DEBUG level?
	 * 
	 * @return is the logger instance enabled for the DEBUG level?
	 */
	public boolean isDebugEnabled();

	/**
	 * Returns is the logger instance enabled for the INFO level?
	 * 
	 * @return is the logger instance enabled for the INFO level?
	 */
	public boolean isInfoEnabled();

	/**
	 * Returns is the logger instance enabled for the WARN level?
	 * 
	 * @return is the logger instance enabled for the WARN level?
	 */
	public boolean isWarnEnabled();

	/**
	 * Returns is the logger instance enabled for the ERROR level?
	 * 
	 * @return is the logger instance enabled for the ERROR level?
	 */
	public boolean isErrorEnabled();

	/**
	 * Returns is the logger instance enabled for the TRACE level?
	 * 
	 * @return is the logger instance enabled for the TRACE level?
	 */
	public boolean isTraceEnabled();

	/**
	 * 
	 * @return current custom level for this logger or {@code Optional.empty()} if
	 *         there isn't any custom level (common case, when the global level
	 *         obtained with
	 *         {@link com.equo.logging.client.api.LoggerConfiguration
	 *         LoggerConfiguration} is used).
	 */
	public Optional<Level> getLoggerLevel();

	/**
	 * Sets a custom level for this logger. Once setted, the logger will ignore
	 * global level and use the configured one.
	 * 
	 * @param level The new custom level for this logger. {@code null} to disable it
	 *              (and use the global level again).
	 */
	public void setLoggerLevel(Level level);

}