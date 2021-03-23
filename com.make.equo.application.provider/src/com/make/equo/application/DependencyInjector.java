package com.make.equo.application;

import javax.inject.Inject;

import org.osgi.service.component.annotations.Component;

import com.make.equo.aer.internal.api.IEquoCrashReporter;

@Component
public class DependencyInjector implements IDependencyInjector {
	@Inject
	private IEquoCrashReporter equoCrashReporter;

	@Override
	public IEquoCrashReporter getEquoCrashReporter() {
		return equoCrashReporter;
	}

}
