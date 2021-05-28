package com.equo.aer.api;

import com.google.gson.JsonObject;

/**
 * API for error reporting service.
 *
 */
public interface IEquoLoggingService {

  /**
   * Logs an error with an associated message.
   * @param message Log message
   */
  public void logError(String message);

  /**
   * Logs an error with an associated message and custom tags.
   * @param message Log message
   * @param tags    JsonObject tags
   */
  public void logError(String message, JsonObject tags);

  /**
   * Logs info with an associated message.
   * @param message Log message
   */
  public void logInfo(String message);

  /**
   * Logs info with an associated message and custom tags.
   * @param message Log message
   * @param tags    JsonObject tags
   */
  public void logInfo(String message, JsonObject tags);

  /**
   * Logs a warning with an associated message.
   * @param message Log message
   */
  public void logWarning(String message);

  /**
   * Logs a warning with an associated message and custom tags.
   * @param message Log message
   * @param tags    JsonObject tags
   */
  public void logWarning(String message, JsonObject tags);
}
