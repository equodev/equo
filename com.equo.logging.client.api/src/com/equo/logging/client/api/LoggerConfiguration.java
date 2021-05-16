package com.equo.logging.client.api;

import org.slf4j.LoggerFactory;

public class LoggerConfiguration {
	private static Level DEFAULT_LEVEL = Level.INFO;

	/**
	 * Gets a global log level.
	 * 
	 * @return a global log level.
	 */
	public static Level getGlobalLevel() {
		ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory
				.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
		return Level.toLevel(root.getLevel().toString(), DEFAULT_LEVEL);
	}

	/**
	 * Sets a global log level.
	 * 
	 * @param level log level.
	 */
	public static void setGlobalLevel(Level level) {
		if (level == null) {
			return;
		}
		ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory
				.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
		root.setLevel(ch.qos.logback.classic.Level.toLevel(level.toString()));
	}
}
