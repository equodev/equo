/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of Equo Framework.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equoplatform.com/terms.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3 as published by the Free Software
** Foundation. Please review the following
** information to ensure the GNU General Public License requirements will
** be met: https://www.gnu.org/licenses/gpl-3.0.html.
**
****************************************************************************/

package com.equo.contribution.parser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.util.tracker.BundleTracker;

import com.equo.contribution.services.IContributionConfigService;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

/**
 * Parses configurations from an "equoConfig.json" file.
 */
@Component
public class EquoConfigParser {

  private static final String CONFIG_FILE_NAME = "equoConfig.json";

  private IContributionConfigService equoContributionConfigService;

  private BundleTracker<URL> tracker;

  @Activate
  void activate(BundleContext context) {
    tracker = new BundleTracker<URL>(context, Bundle.ACTIVE, null) {

      @Override
      public URL addingBundle(Bundle bundle, BundleEvent event) {
        URL configFile = bundle.getEntry(CONFIG_FILE_NAME);
        if (configFile != null) {
          Runnable runnable = () -> {
            parse(configFile, bundle);
          };
          Thread thread = new Thread(runnable, "EquoConfigParser");
          thread.start();
        }
        return configFile;
      }

    };
    tracker.open();
  }

  @Deactivate
  void deactivate() {
    tracker.close();
  }

  /**
   * Parse JSON file and add its contributions.
   */
  public void parse(URL configFile, Bundle bundle) {
    JsonReader jsonReader = null;
    try {
      jsonReader = new JsonReader(new InputStreamReader(configFile.openStream()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    JsonParser jsonParser = new JsonParser();
    JsonElement jsonDefinition = null;
    if (jsonReader != null) {
      jsonDefinition = jsonParser.parse(jsonReader);
    }
    equoContributionConfigService.defineContributions(jsonDefinition.getAsJsonObject(), bundle);
    try {
      jsonReader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
  public void
      setEquoContributionConfigService(IContributionConfigService equoContributionConfigService) {
    this.equoContributionConfigService = equoContributionConfigService;
  }

  public void
      unsetEquoContributionConfigService(IContributionConfigService equoContributionConfigService) {
    this.equoContributionConfigService = null;
  }

}
