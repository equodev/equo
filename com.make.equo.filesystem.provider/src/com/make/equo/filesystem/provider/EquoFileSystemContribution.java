package com.make.equo.filesystem.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.contribution.api.EquoContributionBuilder;
import com.make.equo.contribution.api.resolvers.EquoGenericURLResolver;
import com.make.equo.filesystem.provider.handlers.*;
import com.make.equo.ws.api.IEquoEventHandler;

@Component
public class EquoFileSystemContribution {
	private static final String FILE_SYSTEM_CONTRIBUTION_NAME = "equofilesystem";
	private static final String COMMANDS_JS_API = "equoCommands.js";

	private EquoContributionBuilder builder;

	@Reference
	IEquoEventHandler equoEventHandler;

	@Activate
	protected void activate() {
		registerHandlers();

		builder.withContributionName(FILE_SYSTEM_CONTRIBUTION_NAME).withScriptFile(COMMANDS_JS_API)
				.withURLResolver(new EquoGenericURLResolver(EquoFileSystemContribution.class.getClassLoader())).build();
	}

	private void registerHandlers() {
		new FileInfoHandler().register(equoEventHandler);
		new DeleteFileHandler().register(equoEventHandler);
		new MoveFileHandler().register(equoEventHandler);
		new NewFolderHandler().register(equoEventHandler);
		new OpenFileHandler().register(equoEventHandler);
		new OpenFolderHandler().register(equoEventHandler);
		new ReadFileHandler().register(equoEventHandler);
		new RenameFileHandler().register(equoEventHandler);
		new SaveFileAsHandler().register(equoEventHandler);
		new SaveFileHandler().register(equoEventHandler);
	}

	@Reference
	void setEquoBuilder(EquoContributionBuilder builder) {
		this.builder = builder;
	}

	void unsetEquoBuilder(EquoContributionBuilder builder) {
		this.builder = null;
	}
}
