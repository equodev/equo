package com.make.equo.monaco;

import static com.make.equo.monaco.util.IMonacoConstants.EQUO_MONACO_CONTRIBUTION_NAME;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

import org.eclipse.swt.chromium.Browser;
import org.eclipse.swt.widgets.Composite;

import com.google.gson.JsonObject;
import com.make.equo.monaco.lsp.LspProxy;
import com.make.equo.ws.api.IEquoEventHandler;
import com.make.equo.ws.api.IEquoRunnable;

public class EquoMonacoEditor {

	private static LspProxy lspProxy = new LspProxy();
	private static Map<String, String> lspServers = new HashMap<>();

	private volatile boolean loaded;

	private final Semaphore lock = new Semaphore(1);

	private Browser browser;
	private String namespace;
	private List<IEquoRunnable<Void>> onLoadListeners;

	protected IEquoEventHandler equoEventHandler;

	public EquoMonacoEditor(Composite parent, int style, IEquoEventHandler handler) {
		this();
		this.equoEventHandler = handler;
		browser = new Browser(parent, style);
		browser.setUrl("http://" + EQUO_MONACO_CONTRIBUTION_NAME + "?namespace=" + namespace);
	}

	public EquoMonacoEditor() {
		namespace = "editor" + Double.toHexString(Math.random());
		onLoadListeners = new ArrayList<IEquoRunnable<Void>>();
		loaded = false;
	}

	protected void createEditor(String contents, String fileName) {
		equoEventHandler.on("_createEditor", (IEquoRunnable<Void>) runnable -> handleCreateEditor(contents, fileName));
	}

	protected String getLspServerForFile(String fileName) {
		String extension = "";
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			extension = fileName.substring(i + 1);
		}
		return lspServers.getOrDefault(extension, null);
	}

	protected void handleCreateEditor(String contents, String fileName) {
		new Thread(() -> lspProxy.startServer()).start();
		Map<String, String> editorData = new HashMap<String, String>();
		editorData.put("text", contents);
		editorData.put("name", fileName);
		editorData.put("namespace", namespace);
		editorData.put("lspPath", getLspServerForFile(fileName));
		equoEventHandler.send("_doCreateEditor", editorData);
		loaded = true;
		for (IEquoRunnable<Void> onLoadListener : onLoadListeners) {
			onLoadListener.run(null);
		}

		equoEventHandler.on(namespace + "_canPaste", (IEquoRunnable<Void>) runnable -> {
			try {
				Robot robot = new Robot();
				// Simulate a key press
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_CONTROL);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		});

		equoEventHandler.on(namespace + "_canSelectAll", (IEquoRunnable<Void>) runnable -> {
			try {
				Robot robot = new Robot();
				// Simulate a key press
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_A);
				robot.keyRelease(KeyEvent.VK_A);
				robot.keyRelease(KeyEvent.VK_CONTROL);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		});
	}

	public void addOnLoadListener(IEquoRunnable<Void> listener) {
		if (!loaded) {
			onLoadListeners.add(listener);
		} else {
			listener.run(null);
		}
	}

	public void getContentsSync(IEquoRunnable<String> runnable) {
		if (lock.tryAcquire()) {
			equoEventHandler.on(namespace + "_doGetContents", (JsonObject contents) -> {
				try {
					runnable.run(contents.get("contents").getAsString());
				} finally {
					synchronized (lock) {
						lock.notify();
						lock.release();
					}
				}
			});

			equoEventHandler.send(namespace + "_getContents");
			synchronized (lock) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void getContentsAsync(IEquoRunnable<String> runnable) {
		equoEventHandler.on(namespace + "_doGetContents", (JsonObject contents) -> {
			runnable.run(contents.get("contents").getAsString());
		});
		equoEventHandler.send(namespace + "_getContents");
	}

	public void handleAfterSave() {
		equoEventHandler.send(namespace + "_didSave");
	}

	public void undo() {
		equoEventHandler.send(namespace + "_undo");
	}

	public void redo() {
		equoEventHandler.send(namespace + "_redo");
	}

	public void copy() {
		equoEventHandler.send(namespace + "_doCopy");
	}

	public void cut() {
		equoEventHandler.send(namespace + "_doCut");
	}

	public void find() {
		equoEventHandler.send(namespace + "_doFind");
	}

	public void paste() {
		browser.setFocus();
		equoEventHandler.send(namespace + "_doPaste");
	}

	public void selectAll() {
		browser.setFocus();
		equoEventHandler.send(namespace + "_doSelectAll");
	}

	public void configSelection(IEquoRunnable<Boolean> selectionFunction) {
		equoEventHandler.on(namespace + "_selection", (JsonObject contents) -> {
			selectionFunction.run(contents.get("endColumn").getAsInt() != contents.get("startColumn").getAsInt()
					|| contents.get("endLineNumber").getAsInt() != contents.get("startLineNumber").getAsInt());
		});
	}

	public void subscribeChanges(IEquoRunnable<Boolean> dirtyListener, IEquoRunnable<Boolean> undoListener,
			IEquoRunnable<Boolean> redoListener) {
		equoEventHandler.on(namespace + "_changesNotification", (JsonObject changes) -> {
			dirtyListener.run(changes.get("isDirty").getAsBoolean());
			undoListener.run(changes.get("canUndo").getAsBoolean());
			redoListener.run(changes.get("canRedo").getAsBoolean());
		});

		if (loaded) {
			equoEventHandler.send(namespace + "_subscribeModelChanges");
		} else {
			addOnLoadListener((IEquoRunnable<Void>) runnable -> {
				equoEventHandler.send(namespace + "_subscribeModelChanges");
			});
		}
	}

	/**
	 * Add a lsp websocket server to be used by the editors on the files with the
	 * given extensions
	 * 
	 * @param fullServerPath The full path to the lsp server. Example:
	 *                       ws://127.0.0.1:3000/lspServer
	 * @param extensions     A collection of extensions for what the editor will use
	 *                       the given lsp server. The extensions must not have the
	 *                       initial dot. Example: ["php", "php4"]
	 */
	public static void addLspWsServer(String fullServerPath, Collection<String> extensions) {
		for (String extension : extensions) {
			lspServers.put(extension, fullServerPath);
		}
	}

	/**
	 * Add a lsp server to be used by the editors on the files with the given
	 * extensions
	 * 
	 * @param excecutionParameters The parameters needed to start the lsp server
	 *                             through stdio. Example: ["html-languageserver",
	 *                             "--stdio"]
	 * @param extensions           A collection of extensions for what the editor
	 *                             will use the given lsp server. The extensions
	 *                             must not have the initial dot. Example: ["php",
	 *                             "php4"]
	 */
	public static void addLspServer(Collection<String> excecutionParameters, Collection<String> extensions) {
		String nameForServer = extensions.stream().map(s -> s.replace(" ", "")).collect(Collectors.joining());
		lspProxy.addServer(nameForServer, excecutionParameters);
		addLspWsServer("ws://127.0.0.1:" + lspProxy.getPort() + "/" + nameForServer, extensions);
	}

	public void dispose() {
		lspProxy.stopServer();
	}

}
