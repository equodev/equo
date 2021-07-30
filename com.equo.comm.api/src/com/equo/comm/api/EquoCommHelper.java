package com.equo.comm.api;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Helper class to get a Comm service instance.
 */
public class EquoCommHelper {
  /**
   * Gets the implementation of the Comm service.
   */
  public static IEquoCommService getCommService() {

    Bundle ctxBundle = FrameworkUtil.getBundle(IEquoCommService.class);
    if (ctxBundle == null) {
      return new LazyEquoCommService();
    }
    BundleContext ctx = ctxBundle.getBundleContext();
    if (ctx != null) {
      @SuppressWarnings("unchecked")
      ServiceReference<IEquoCommService> serviceReference = (ServiceReference<IEquoCommService>) ctx
          .getServiceReference(IEquoCommService.class.getName());
      if (serviceReference != null) {
        return ctx.getServiceObjects(serviceReference).getService();
      }
    }
    return null;
  }

  private static class LazyEquoCommService implements IEquoCommService {
    private IEquoCommService instance;

    private void obtainInstance() {
      if (instance == null) {
        instance = EquoCommHelper.getCommService();
      }
    }

    @Override
    public void addEventHandler(String actionId, IEquoRunnableParser<?> equoRunnableParser) {
      obtainInstance();
      instance.addEventHandler(actionId, equoRunnableParser);
    }

    @Override
    public void send(String userEvent, Object payload) {
      obtainInstance();
      instance.send(userEvent, payload);
    }

    @Override
    public int getPort() {
      obtainInstance();
      return instance.getPort();
    }

  }
}
