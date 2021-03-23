package com.make.equo.application;

import com.make.equo.aer.internal.api.IEquoCrashReporter;

public interface IDependencyInjector {

	public IEquoCrashReporter getEquoCrashReporter();

}
