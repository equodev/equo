package com.make.equo.application.addon;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.ui.model.application.MAddon;
import org.eclipse.e4.ui.model.application.MApplication;

import com.make.equo.application.server.EquoHttpProxyServer;
import com.make.equo.application.util.FrameworkUtils;
import com.make.equo.application.util.IConstants;

public class EquoProxyServerAddon {

	@Inject
	private MAddon thisAddon;

	private EquoHttpProxyServer equoHttpProxyServer;

	public static final String CONTRIBUTION_URI = "bundleclass://com.make.equo.application.provider/com.make.equo.application.addon.EquoProxyServerAddon";

	@PostConstruct
	public void init(MApplication mApplication, ECommandService commandService, EHandlerService handlerService) {
		startEquoServer();
	}

	@SuppressWarnings("unchecked")
	private void startEquoServer() {
		System.out.println("Creating Equo server proxy...");
		Map<String, Object> transientData = thisAddon.getTransientData();
		equoHttpProxyServer = new EquoHttpProxyServer((List<String>) transientData.get(IConstants.URLS_TO_PROXY), transientData,
				FrameworkUtils.INSTANCE.getAppBundlePath(), FrameworkUtils.INSTANCE.getFrameworkName());
		equoHttpProxyServer.startProxy();
	}

	@PreDestroy
	public void stop() {
		if (equoHttpProxyServer != null) {
			equoHttpProxyServer.stop();
		}
	}

}
