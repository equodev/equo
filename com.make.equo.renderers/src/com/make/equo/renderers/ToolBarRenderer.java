package com.make.equo.renderers;

import static com.make.equo.renderers.util.IRendererConstants.TOOLBAR_RENDERER_NAME;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.e4.ui.workbench.renderers.swt.ToolBarManagerRenderer;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.chromium.Browser;
import org.eclipse.swt.widgets.Composite;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.make.equo.application.api.IEquoApplication;
import com.make.equo.renderers.util.IRendererConstants;
import com.make.equo.server.api.IEquoServer;
import com.make.equo.ws.api.IEquoEventHandler;

public class ToolBarRenderer extends ToolBarManagerRenderer implements IEquoRenderer {

	private static final String UNKNOWN_EQUO_COMMAND = "unknownEquoCommand";

	@Inject
	private IEquoApplication equoApplication;

	@Inject
	private static IEquoEventHandler equoEventHandler;

	@Inject
	private IEquoServer equoProxyServer;

	@Inject
	private MApplication mApplication;

	@Inject
	private UISynchronize sync;

	private MToolBar mToolBar;

	private String namespace;

	@Override
	public Object createWidget(MUIElement mUIToolbar, Object parent) {
		if (!(mUIToolbar instanceof MToolBar) || !(parent instanceof Composite)) {
			return null;
		}
		setToolbarContext(mUIToolbar);

		Composite parentComp = (Composite) parent;

		Composite browserComposite = new Composite(parentComp, 1000);

		configureAndStartRenderProcess(browserComposite);

		return parentComp;
	}

	public void setToolbarContext(MUIElement mUIToolbar) {
		this.mToolBar = (MToolBar) mUIToolbar;
		this.namespace = "ToolBar" + Integer.toHexString(mUIToolbar.hashCode());
	}

	@Override
	public Map<String, Map<String, String>> getContributionsFromJavaModel() {
		Map<String, Map<String, String>> modelContributions = new HashMap<String, Map<String, String>>();
		for (MToolBarElement e : this.mToolBar.getChildren()) {
			if (e instanceof MHandledToolItem) {
				Map<String, String> toolItemsPayloads = new HashMap<String, String>();
				MHandledToolItem toolItem = (MHandledToolItem) e;
				toolItemsPayloads.put("icon", toolItem.getIconURI());
				if (toolItem.getCommand() != null) {
					toolItemsPayloads.put("commandId", toolItem.getCommand().getElementId());
				} else {
					toolItemsPayloads.put("commandId", UNKNOWN_EQUO_COMMAND);
				}
				Boolean isAnEquoModelElement = (Boolean) toolItem.getTransientData()
						.get(IRendererConstants.IS_AN_EQUO_MODEL_ELEMENT);
				if (isAnEquoModelElement != null) {
					toolItemsPayloads.put(IRendererConstants.IS_AN_EQUO_MODEL_ELEMENT,
							isAnEquoModelElement ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
				} else {
					toolItemsPayloads.put(IRendererConstants.IS_AN_EQUO_MODEL_ELEMENT, Boolean.FALSE.toString());
				}
				String userEvent = (String) toolItem.getTransientData()
						.get(IRendererConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT);
				if (userEvent != null) {
					toolItemsPayloads.put("userEvent", userEvent);
				}
				toolItemsPayloads.put("tooltip", toolItem.getTooltip());
				modelContributions.put(toolItem.getElementId(), toolItemsPayloads);
			}
		}
		return modelContributions;
	}

	@Override
	public IEquoServer getEquoProxyServer() {
		return equoProxyServer;
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public IEquoEventHandler getEquoEventHandler() {
		return equoEventHandler;
	}

	@Override
	public List<String> getFrameworkContributionJSONFileNames() {
		return Lists.newArrayList(getModelContributionPath() + "IDEMainToolBarContribution.json");
	}

	@Override
	public Browser createBrowserComponent(Composite toolBarParent) {
		GridLayoutFactory.fillDefaults().applyTo(toolBarParent);

		toolBarParent.getHorizontalBar().setVisible(false);
		toolBarParent.getVerticalBar().setVisible(false);
		Browser browser = new Browser(toolBarParent, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).hint(2000, 24).applyTo(browser);

		return browser;
	}

	@Override
	public void onActionPerformedOnElement() {
		getEquoEventHandler().on(namespace + "_itemClicked", (JsonObject payload) -> {
			JsonElement commandAsJson = payload.get("commandId");
			String commandId = commandAsJson.getAsString();

			if (UNKNOWN_EQUO_COMMAND.equals(commandId)) {
				// TODO Do a real log of the toolBarElement id which does not have an associated
				// command
				String toolBarElementId = payload.get("toolBarElementId").getAsString();
				System.out.println("There is no command for the toolBar element " + toolBarElementId);
				return;
			}

			IEclipseContext eclipseContext = mApplication.getContext();
			ECommandService commandService = eclipseContext.get(ECommandService.class);
			Command command = commandService.getCommand(commandId);

			if (command != null) {
				sync.asyncExec(() -> {
					JsonElement isModelElementAsJson = payload.get(IRendererConstants.IS_AN_EQUO_MODEL_ELEMENT);
					boolean isAnEquoModelElement = isModelElementAsJson != null ? isModelElementAsJson.getAsBoolean()
							: false;
					Parameterization[] params = null;
					if (isAnEquoModelElement) {
						try {
							JsonElement userEventAsJson = payload
									.get(IRendererConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT);
							String userEvent = userEventAsJson != null ? userEventAsJson.getAsString() : null;
							params = new Parameterization[] {
									new Parameterization(command.getParameter("commandId"), command.getId()),
									new Parameterization(
											command.getParameter(IRendererConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT),
											userEvent) };
						} catch (NotDefinedException e) {
							e.printStackTrace();
						}
					}
					ParameterizedCommand parametrizedCommand = new ParameterizedCommand(command, params);
					EHandlerService handlerService = eclipseContext.get(EHandlerService.class);
					handlerService.executeHandler(parametrizedCommand);
				});
			}
		});
	}

	@Override
	public List<Map<String, String>> getEclipse4Model(String namespace) {
		List<Map<String, String>> e4Model = new ArrayList<Map<String, String>>();
		List<MToolBarElement> mToolBarElements = mToolBar.getChildren();

		List<MToolBarElement> toolBarElementsToRendered = mToolBarElements.stream().filter(toolItem -> {
			return toolItem.isToBeRendered() && toolItem.isVisible();
		}).collect(Collectors.toList());

		for (MToolBarElement mToolBarElement : toolBarElementsToRendered) {
			if (mToolBarElement instanceof MUILabel) {
				HashMap<String, String> elementModel = new HashMap<String, String>();
				String elementId = mToolBarElement.getElementId();
				elementModel.put("id", elementId);
				e4Model.add(elementModel);
			}
		}
		return e4Model;
	}

	@Override
	public IEquoApplication getEquoApplication() {
		return equoApplication;
	}

	@Override
	public String getModelContributionPath() {
		return "contributions/mainToolBar/";
	}

	@Override
	public String getEquoRendererName() {
		return TOOLBAR_RENDERER_NAME;
	}

}
