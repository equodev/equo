package com.make.equo.renderers;

import static com.make.equo.renderers.util.IRendererConstants.EQUO_RENDERERS_CONTRIBUTION_NAME;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.swt.chromium.Browser;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.make.equo.application.api.IEquoApplication;
import com.make.equo.server.api.IEquoServer;
import com.make.equo.ws.api.IEquoEventHandler;
import com.make.equo.ws.api.StringPayloadEquoRunnable;

public interface IEquoRenderer {

	/**
	 * The initial point to start an Equo render process.
	 * 
	 * @param parent
	 */
	default void configureAndStartRenderProcess(Composite parent) {
		Browser browser = createBrowserComponent(parent);

		String rendererURL = EQUO_RENDERERS_CONTRIBUTION_NAME + "/" + getEquoRendererName();

		String namespace = getNamespace();
		browser.setUrl(rendererURL + "?" + "namespace=" + namespace);

		sendEclipse4Model();
		onActionPerformedOnElement();
	}

	default Map<String, Map<String, String>> getContributionsFromFiles(String parentPath,
			List<String> contributionJSONFileNames, Class<?> clazz) {
		Map<String, Map<String, String>> modelContributions = new HashMap<String, Map<String, String>>();
		for (String fileName : contributionJSONFileNames) {
			Optional<Map<String, Map<String, String>>> modelContributionsFromFile = getModelContributionsFromFile(
					parentPath + fileName, clazz);
			if (modelContributionsFromFile.isPresent()) {
				modelContributions.putAll(modelContributionsFromFile.get());
			}
		}
		return modelContributions;
	}

	default void sendEclipse4Model() {
		String namespace = getNamespace();
		IEquoEventHandler equoEventHandler = getEquoEventHandler();
		equoEventHandler.on(namespace + "_getModelContributions",
				(StringPayloadEquoRunnable stringPayloadEquoRunnable) -> {
					Map<String, Map<String, String>> modelContributions = getModelContributions();
					if (!modelContributions.isEmpty()) {
						equoEventHandler.send(namespace + "_modelContributions", modelContributions);
					}
				});
		equoEventHandler.on(namespace + "_getModel", (StringPayloadEquoRunnable stringPayloadEquoRunnable) -> {
			List<Map<String, String>> eclipse4Model = getEclipse4Model(namespace);
			if (!eclipse4Model.isEmpty()) {
				equoEventHandler.send(namespace + "_model", eclipse4Model);
			}
		});
	};

	/**
	 * Gets both the framework and application model contributions for the specific
	 * element.
	 * 
	 * @return a map containing all the model contributions
	 */
	default Map<String, Map<String, String>> getModelContributions() {
		Map<String, Map<String, String>> modelContributions = new HashMap<String, Map<String, String>>();

		List<String> frameworkContributionJSONFileNames = getFrameworkContributionJSONFileNames();
		List<String> applicationContributionJSONFileNames = getApplicationContributionJSONFileNames();

		Map<String, Map<String, String>> frameworkContributions = getContributionsFromFiles("",
				frameworkContributionJSONFileNames, this.getClass());
		Map<String, Map<String, String>> applicationContributionsFromJsonFiles = getContributionsFromFiles(
				getModelContributionPath(), applicationContributionJSONFileNames, getEquoApplication().getClass());
		Map<String, Map<String, String>> applicationContributionsFromJavaModel = getContributionsFromJavaModel();

		modelContributions.putAll(frameworkContributions);
		modelContributions.putAll(applicationContributionsFromJsonFiles);
		modelContributions.putAll(applicationContributionsFromJavaModel);

		return modelContributions;
	}

	default Map<String, Map<String, String>> getContributionsFromJavaModel() {
		return Maps.newHashMap();
	}

	/**
	 * Returns a list of JSON file names which contributes to an Eclipse model
	 * element (i.e. Toolbar). This files are contributed by the user developer in
	 * the Equo Application. To contribute to a specific model element even the
	 * parent folder name is important. For more information, check out the Equo
	 * guidelines to contribute items to the the model.
	 * 
	 * @return a list of JSON file names
	 */
	default List<String> getApplicationContributionJSONFileNames() {
		Bundle bundle = FrameworkUtil.getBundle(getEquoApplication().getClass());

		URL fileURL = bundle.getEntry(getModelContributionPath());
		if (fileURL == null) {
			return Lists.newArrayList();
		}
		File toolBarFolder = null;
		List<String> fileNames = new ArrayList<String>();
		try {
			URL resolvedFileURL = FileLocator.toFileURL(fileURL);

			URI resolvedURI = new URI(resolvedFileURL.getProtocol(), resolvedFileURL.getPath(), null);
			toolBarFolder = new File(resolvedURI);
			if (toolBarFolder.exists() && toolBarFolder.isDirectory()) {
				try (Stream<Path> paths = Files.walk(Paths.get(toolBarFolder.getAbsolutePath()))) {
					paths.filter(Files::isRegularFile).forEach(path -> fileNames.add(path.toFile().getName()));
				}
			}
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
			// TODO log the message
		} catch (IOException e1) {
			e1.printStackTrace();
			// TODO log the message
		}
		return fileNames;
	}

	static Optional<Map<String, Map<String, String>>> getModelContributionsFromFile(String fileName, Class<?> clazz) {
		JsonObject json = null;
		try (InputStreamReader inputStreamReader = new InputStreamReader(
				clazz.getClassLoader().getResourceAsStream(fileName))) {
			JsonElement element = new JsonParser().parse(inputStreamReader);
			json = element.getAsJsonObject();
			Gson gson = new Gson();
			Type type = new TypeToken<Map<String, Map<String, String>>>() {
			}.getType();
			Map<String, Map<String, String>> modelContributions = gson.fromJson(json, type);
			return Optional.of(modelContributions);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	/**
	 * Return a name for the Rendeder.
	 * 
	 * @return a String with the Equo Renderer's name
	 */
	String getEquoRendererName();

	/**
	 * Receives a message when an action is performed on an element in the
	 * Javascript side(i.e. click on a toolbar item).
	 */
	void onActionPerformedOnElement();

	IEquoEventHandler getEquoEventHandler();

	List<Map<String, String>> getEclipse4Model(String namespace);

	/**
	 * Returns a list of JSON file names which contributes to an Eclipse model
	 * element (i.e. Toolbar). This files are contributed by the Equo framework
	 * itself. To contribute to a specific model element even the parent folder name
	 * is important. For more information, check out the Equo guidelines to
	 * contribute items to the the model.
	 * 
	 * @return a list of JSON file names
	 */
	List<String> getFrameworkContributionJSONFileNames();

	IEquoApplication getEquoApplication();

	/**
	 * The unique path that identitifes the model to contribute to.
	 * 
	 * @return the contribution path
	 */
	String getModelContributionPath();

	/**
	 * Returns a unique namespace for the element to be rendered.
	 * 
	 * @return a namespace
	 */
	String getNamespace();

	IEquoServer getEquoProxyServer();

	Browser createBrowserComponent(Composite parent);

}
