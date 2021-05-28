package com.equo.application;

import com.equo.aer.internal.api.IEquoCrashReporter;

/**
 * API for a dependency injector which can optionally provide implementations of
 * the Equo APIs.
 *
 */
public interface IDependencyInjector {

  public IEquoCrashReporter getEquoCrashReporter();

}
