package com.make.equo.application.consumer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import com.make.equo.application.api.ApplicationLauncher;

public class ApplicationLauncherServiceTracker extends ServiceTracker<ApplicationLauncher, Object> {

	public ApplicationLauncherServiceTracker(BundleContext context) {
		super(context, ApplicationLauncher.class.getName(), null);
	}

	public Object addingService(ServiceReference<ApplicationLauncher> reference) {
		Object o = context.getService(reference); // the returned value uses service impl OSGi ClassLoader
		if (o == null)
			return null;
		// I thought that BundleContext#getService() returned null when the returned
		// object was incompatible ?
		// we get non-null value here

		// Obviously this side can see the Java type since I can declare the variable
		// here (since it is provided in the normal java.exe -classpath using J2SE
		// ClassLoader)
		ApplicationLauncher applicationLauncherService = null;
		try {
			applicationLauncherService = (ApplicationLauncher) o; // this is the ClassCastException (same name, //
																	// different ClassLoaders, so not same type)
		} catch (ClassCastException eat) {
			// execution ends up in here
			// yet it is the same Java type name and both the bundle and this code here can
			// see and have loaded the type
			System.out.println("print the eat exception here. Se come la excepcion.");
		}
		if (applicationLauncherService == null) {
			ClassLoader cl = ApplicationLauncher.class.getClassLoader();
			o = Proxy.newProxyInstance(cl, new Class<?>[] { ApplicationLauncher.class },
					new Invoker(o));
			applicationLauncherService = (ApplicationLauncher) o; // this works now, no exception occurs
		}

		// OK now we INVOKE a method inside activated OSGi bundle
		// this method call also works, running the InvocationHandler and entering the
		// code inside the bundle.
		// applicationLauncherService.myMethod(new Integer(42), new String("foo"), new
		// Boolean(true)); // we send simple boxed primitive data

		return o;
	}

	public void removedService(ServiceReference<ApplicationLauncher> reference, Object service) {
		context.ungetService(reference);
	}

	protected class Invoker implements InvocationHandler {
		private Object target;

		protected Invoker(Object target) {
			this.target = target;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			// We lookup the class from the target ClassLoader
			Class<?> cls = target.getClass().getClassLoader().loadClass(method.getDeclaringClass().getName());
			// We lookup the counterpart Method object for the target ClassLoader
			Method targetMethod = cls.getMethod(method.getName(), method.getParameterTypes());

			return targetMethod.invoke(target, args);
		}
	}
}