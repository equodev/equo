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

	private static final String LOGGING_EVENT_KEY = "loggingEvent";


	private IEquoLoggingService equoLoggingService;
	private IEquoEventHandler equoEventHandler;

	@Activate
	public void start() {
		System.out.println("Initializing Logging Client provider...");
		equoEventHandler.on(LOGGING_EVENT_KEY, new LoggingEventPayloadRunnable());
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

	private class LoggingEventPayloadRunnable implements JsonPayloadEquoRunnable {

		private static final long serialVersionUID = 1L;
		private static final String SEGMENTATION_KEY = "tags";
		private static final String MESSAGE_LOG = "message";
		private static final String TYPE_LOG = "type";
		private static final String TYPE_INFO = "info";
		private static final String TYPE_WARNING = "warning";
		private static final String TYPE_ERROR = "error";

		@Override
		public void run(JsonObject payload) {
			System.out.println("Log Info Event json payload is " + payload);
			JsonElement messageJsonElement = payload.get(MESSAGE_LOG);
			JsonElement typeJsonElement = payload.get(TYPE_LOG);

			if ((typeJsonElement == null) || (messageJsonElement == null)) {
				throw new RuntimeException(
						"A \"type\" nor \"message\" member which identified the event name must be defined in the Logging object.");
			}
			String type = typeJsonElement.getAsString();
			String message = messageJsonElement.getAsString();
			JsonObject segmentationJsonObject = payload.getAsJsonObject(SEGMENTATION_KEY);
			switch(type) {
			case TYPE_INFO:
				logInfo(message,segmentationJsonObject);
				break;
			case TYPE_WARNING:
				logWarning(message,segmentationJsonObject);
				break;
			case TYPE_ERROR:
				logError(message,segmentationJsonObject);
				break;
			}
		}
	}


}
