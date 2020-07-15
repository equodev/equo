package com.make.equo.node.packages.tests.mocks;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;

import com.google.gson.JsonObject;
import com.make.equo.analytics.internal.api.AnalyticsService;

@Component
public class AnalyticsServiceMock implements AnalyticsService {

	private List<String> receivedMessages = new ArrayList<>();

	@Override
	public void registerEvent(String eventKey, double value) {
	}

	@Override
	public void registerEvent(String eventKey, double value, JsonObject segmentation) {
		receivedMessages.add(eventKey + '-' + segmentation.toString());
	}

	@Override
	public void registerEvent(String eventKey, double value, String segmentationAsString) {
		receivedMessages.add(eventKey + '-' + segmentationAsString);
	}

	@Override
	public void registerLaunchApp() {
	}

	@Override
	public void enableAnalytics() {
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
