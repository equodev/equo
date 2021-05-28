package com.equo.application;

import javax.inject.Inject;

import org.osgi.service.component.annotations.Component;

import com.equo.aer.internal.api.IEquoCrashReporter;

/**
 * Provides loaded implementation of the optional Equo modules.
 *
 */
@Component
public class DependencyInjector implements IDependencyInjector {
  @Inject
  private IEquoCrashReporter equoCrashReporter;

  @Override
  public IEquoCrashReporter getEquoCrashReporter() {
    return equoCrashReporter;
  }

}
