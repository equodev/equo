package com.make.equo.aer.client.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.make.equo.aer.client.api.ILoggingApi;
import com.make.equo.aer.api.IEquoLoggingService;
import com.make.equo.ws.api.IEquoEventHandler;
import com.make.equo.ws.api.JsonPayloadEquoRunnable;

@Component
public class LoggingApiImpl implements ILoggingApi {

	private static final String LOGGING_INFO_EVENT_KEY = "loggingInfoEvent";
	private static final String LOGGING_WARNING_EVENT_KEY = "loggingWarningEvent";
	private static final String LOGGING_ERROR_EVENT_KEY = "loggingErrorEvent";
	private IEquoLoggingService equoLoggingService;
	private IEquoEventHandler equoEventHandler;

	@Activate
	public void start() {
		System.out.println("Initializing Logging Client provider...");
		equoEventHandler.on(LOGGING_INFO_EVENT_KEY, new LoggingInfoEventPayloadRunnable());
		equoEventHandler.on(LOGGING_ERROR_EVENT_KEY, new LoggingErrorEventPayloadRunnable());
		equoEventHandler.on(LOGGING_WARNING_EVENT_KEY, new LoggingWarningEventPayloadRunnable());
	}

	@Override
	public void logError(String message, JsonObject tags) {
		equoLoggingService.logError(message, tags);

	}

	@Override
	public void logInfo(String message, JsonObject tags) {
		equoLoggingService.logInfo(message, tags);
	}

	@Override
	public void logWarning(String message, JsonObject tags) {
		equoLoggingService.logWarning(message, tags);

	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	void setLoggingService(IEquoEventHandler equoEventHandler) {
		this.equoEventHandler = equoEventHandler;
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	void setEquoEventHandler(IEquoLoggingService equoLoggingService) {
		this.equoLoggingService = equoLoggingService;
	}

	private class LoggingInfoEventPayloadRunnable implements JsonPayloadEquoRunnable {

		private static final long serialVersionUID = 1L;
		private static final String SEGMENTATION_KEY = "tags";
		private static final String MESSAGE_LOG = "message";

		@Override
		public void run(JsonObject payload) {
			System.out.println("Log Info Event json payload is " + payload);
			JsonElement messageJsonElement = payload.get(MESSAGE_LOG);

			if (messageJsonElement == null) {
				throw new RuntimeException(
						"A \"message\" member which identified the event name must be defined in the Logging object.");
			}
			String message = messageJsonElement.getAsString();
			JsonObject segmentationJsonObject = payload.getAsJsonObject(SEGMENTATION_KEY);
			logInfo(message, segmentationJsonObject);
		}
	}

	private class LoggingWarningEventPayloadRunnable implements JsonPayloadEquoRunnable {

		private static final long serialVersionUID = 1L;
		private static final String SEGMENTATION_KEY = "tags";
		private static final String MESSAGE_LOG = "message";

		@Override
		public void run(JsonObject payload) {
			System.out.println("Log Warning Event json payload is " + payload);
			JsonElement messageJsonElement = payload.get(MESSAGE_LOG);

			if (messageJsonElement == null) {
				throw new RuntimeException(
						"A  \"message\" member which identified the event name must be defined in the Logging object.");
			}
			String message = messageJsonElement.getAsString();
			JsonObject segmentationJsonObject = payload.getAsJsonObject(SEGMENTATION_KEY);
			logWarning(message, segmentationJsonObject);
		}
	}

	private class LoggingErrorEventPayloadRunnable implements JsonPayloadEquoRunnable {

		private static final long serialVersionUID = 1L;
		private static final String SEGMENTATION_KEY = "tags";
		private static final String MESSAGE_LOG = "message";

		@Override
		public void run(JsonObject payload) {
			System.out.println("Log Error Event json payload is " + payload);
			JsonElement messageJsonElement = payload.get(MESSAGE_LOG);

			if (messageJsonElement == null) {
				throw new RuntimeException(
						"A \"message\" member which identified the event name must be defined in the Logging object.");
			}
			String message = messageJsonElement.getAsString();
			JsonObject segmentationJsonObject = payload.getAsJsonObject(SEGMENTATION_KEY);
			logError(message, segmentationJsonObject);
		}
	}

}
