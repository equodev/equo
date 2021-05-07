package com.equo.application;

import com.equo.aer.internal.api.IEquoCrashReporter;

public interface IDependencyInjector {

	public IEquoCrashReporter getEquoCrashReporter();

}
