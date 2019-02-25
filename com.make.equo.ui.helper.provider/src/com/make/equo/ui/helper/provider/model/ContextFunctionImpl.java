package com.make.equo.ui.helper.provider.model;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Component;

@Component(name="ModelElementInjector", service=IContextFunction.class, property="service.context.key:String=com.make.equo.ui.helper.provider.model.ModelElementInjector")
public class ContextFunctionImpl extends ContextFunction implements IContextFunction {
	
	@Override
	public Object compute(IEclipseContext context, String contextKey) {
		   ModelElementInjector injector = ContextInjectionFactory.make(ModelElementInjector.class, context);
		
        context.set(ModelElementInjector.class, injector);

        Bundle bundle = FrameworkUtil.getBundle(this.getClass());
        BundleContext bundleContext = bundle.getBundleContext();
        bundleContext.registerService(ModelElementInjector.class, injector, null);
        
		return injector;
	}

}
