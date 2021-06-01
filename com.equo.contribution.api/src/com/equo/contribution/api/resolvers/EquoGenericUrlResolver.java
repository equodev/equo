package com.equo.contribution.api.resolvers;

import java.net.URL;

/**
 * Resolver used to load resources.
 */
public class EquoGenericUrlResolver implements IEquoContributionUrlResolver {

  private ClassLoader classLoader;

  public EquoGenericUrlResolver(ClassLoader classLoader) {
    this.classLoader = classLoader;
  }

  @Override
  public URL resolve(String pathToResolve) {
    return classLoader.getResource(pathToResolve);
  }
}