package com.make.equo.builders.tests;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.osgi.framework.FrameworkUtil;

public class EquoRule implements TestRule {

	private EquoInjectableTest caseTest;

	public EquoRule(EquoInjectableTest equoTestingInjector) {
		this.caseTest = equoTestingInjector;
	}

	public Statement apply(Statement base, Description description) {
		return statement(base);
	}

	private Statement statement(final Statement base) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				inject(caseTest);
				try {
					base.evaluate();
				} finally {
				}
			}
		};
	}

	private void inject(EquoInjectableTest caseTest) {
		IEclipseContext eclipseContext = EclipseContextFactory
				.getServiceContext(FrameworkUtil.getBundle(caseTest.getClass()).getBundleContext());
		ContextInjectionFactory.inject(caseTest, eclipseContext);
	}

}
