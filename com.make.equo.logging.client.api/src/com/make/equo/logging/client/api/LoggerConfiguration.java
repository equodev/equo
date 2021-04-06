package com.make.equo.logging.client.api;

import org.slf4j.LoggerFactory;

public class LoggerConfiguration {
	private static Level DEFAULT_LEVEL = Level.INFO;

	public static Level getGlobalLevel() {
		ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory
				.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
		return Level.toLevel(root.getLevel().toString(), DEFAULT_LEVEL);
	}

	public static void setGlobalLevel(Level level) {
		ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory
				.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
		root.setLevel(ch.qos.logback.classic.Level.toLevel(level.toString()));
	}
}
