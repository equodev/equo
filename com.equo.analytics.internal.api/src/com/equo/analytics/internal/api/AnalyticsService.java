package com.equo.analytics.internal.api;

import com.google.gson.JsonObject;

/**
 * API for internal analytics report.
 *
 */
public interface AnalyticsService {

  void registerEvent(String eventKey, double value);

  void registerEvent(String eventKey, double value, JsonObject segmentation);

  void registerEvent(String eventKey, double value, String segmentationAsString);

  void registerLaunchApp();

  void enableAnalytics();

  boolean isEnabled();

}
