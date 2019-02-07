package com.make.equo.application;


import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.osgi.service.component.annotations.Component;

@Component(property = { "service.context.key:String=org.eclipse.e4.core.services.statusreporter.StatusReporter" })
public class ContextFunctionImpl extends ContextFunction implements IContextFunction {

	@Override
	public Object compute(IEclipseContext context, String contextKey) {
		return ContextInjectionFactory.make(EquoStatusReporter.class, context);
	}

}
