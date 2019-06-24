package com.make.equo.server.contribution.parser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.BundleTracker;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

@Component
public class EquoConfigParser {
	
	private static final String CONFIG_FILE_NAME = "equoConfig.json";
	
	private BundleTracker<URL> tracker;
	
	@Activate
	void activate(BundleContext context) {
		tracker = new BundleTracker<URL>(context, Bundle.STARTING, null) {
			
			@Override
			public URL addingBundle(Bundle bundle, BundleEvent event) {
				URL configFile = bundle.getResource(CONFIG_FILE_NAME);
				if (configFile != null) {
					Runnable runnable = () -> {
						parse(configFile, bundle);
					};
					Thread thread = new Thread(runnable, "Parsing");
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
	
	private void parse(URL configFile, Bundle bundle) {
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
		
	}

}
