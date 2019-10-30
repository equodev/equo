package com.make.equo.aer.client.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import com.make.equo.aer.client.api.ICrashReporterApi;
import com.make.equo.server.contribution.EquoContributionBuilder;

@Component
public class CrashReporterApiContribution {

	private static final String LOGGING_CONTRIBUTION_NAME = "equologging";
	private static final String LOGGING_JS_API = "loggingApi.js"; // y esto? necesito un script, pero cual?
	
	@SuppressWarnings("unused")
	private ICrashReporterApi crashReporterApi;

	private EquoContributionBuilder builder;
	
	@Activate
	protected void activate() {
		builder
			.withContributionName(LOGGING_CONTRIBUTION_NAME)
			.withScriptFile(LOGGING_JS_API)
			.withURLResolver(new CrashReporterURLResolver())
			.build();
	}

	@Reference
	void setEquoBuilder(EquoContributionBuilder builder) {
		this.builder = builder;
	}

	void unsetEquoBuilder(EquoContributionBuilder builder) {
		this.builder = null;
	}
}
