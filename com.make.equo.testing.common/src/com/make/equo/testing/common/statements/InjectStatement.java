package com.make.equo.testing.common.statements;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.junit.runners.model.Statement;
import org.osgi.framework.FrameworkUtil;

public class InjectStatement extends Statement {

	private Statement base;
	private Object testCase;

	public InjectStatement(Statement base, Object testCase) {
		this.base = base;
		this.testCase = testCase;
	}

	@Override
	public void evaluate() throws Throwable {
		inject(testCase);
		try {
			base.evaluate();
		} finally {
		}
	}

	private void inject(Object caseTest) {
		IEclipseContext eclipseContext = EclipseContextFactory
				.getServiceContext(FrameworkUtil.getBundle(caseTest.getClass()).getBundleContext());
		ContextInjectionFactory.inject(caseTest, eclipseContext);
	}

}
