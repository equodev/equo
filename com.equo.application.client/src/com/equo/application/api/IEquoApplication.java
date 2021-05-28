package com.equo.application.api;

import com.equo.application.model.EquoApplicationBuilder;

/**
 * <p>
 * API for Equo applications.
 * </p>
 * <p>
 * To build an app, it is necessary to implement this interface and build your
 * app inside {@code buildApp(EquoApplicationBuilder)} method method.
 * </p>
 */
public interface IEquoApplication {

  public EquoApplicationBuilder buildApp(EquoApplicationBuilder appBuilder);

}
