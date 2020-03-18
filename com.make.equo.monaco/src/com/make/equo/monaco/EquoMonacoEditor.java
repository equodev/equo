package com.make.equo.monaco;

import static com.make.equo.monaco.util.IMonacoConstants.EQUO_MONACO_CONTRIBUTION_NAME;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.eclipse.swt.chromium.Browser;
import org.eclipse.swt.widgets.Composite;

import com.google.gson.JsonObject;
import com.make.equo.ws.api.IEquoEventHandler;
import com.make.equo.ws.api.IEquoRunnable;

public class EquoMonacoEditor {
	
	private volatile boolean loaded;

	private Browser browser;
	private IEquoEventHandler equoEventHandler;
	private final Semaphore lock = new Semaphore(1);
	private String namespace;

	private List<IEquoRunnable<Void>> onLoadListeners;

	public EquoMonacoEditor(Composite parent, int style, IEquoEventHandler handler, String contents, String fileName) {
		this.equoEventHandler = handler;
		namespace = "editor" + Integer.toHexString(fileName.hashCode());
		browser = new Browser(parent, style);
		browser.setUrl("http://" + EQUO_MONACO_CONTRIBUTION_NAME + "?namespace=" + namespace);
		onLoadListeners = new ArrayList<IEquoRunnable<Void>>();
		loaded = false;
		createEditor(contents, fileName);
	}

	private void createEditor(String contents, String fileName) {
		equoEventHandler.on("_createEditor", (IEquoRunnable<Void>) runnable -> handleCreateEditor(contents, fileName));
	}

	private void handleCreateEditor(String contents, String fileName) {
		Map<String, String> editorData = new HashMap<String, String>();
		editorData.put("text", contents);
		editorData.put("name", fileName);
		editorData.put("namespace", namespace);
		equoEventHandler.send("_doCreateEditor", editorData);
		loaded = true;
		for (IEquoRunnable<Void> onLoadListener : onLoadListeners) {
			onLoadListener.run(null);
		}
		
		equoEventHandler.on(namespace + "_canPaste", (IEquoRunnable<Void>) runnable ->{
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
		
		equoEventHandler.on(namespace + "_canSelectAll", (IEquoRunnable<Void>) runnable ->{
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
		equoEventHandler.send(namespace + "_doPaste");
	}
	
	public void selectAll() {
		equoEventHandler.send(namespace + "_doSelectAll");
	}
	
	public void configSelection(IEquoRunnable<Boolean> selectionFunction) {
		equoEventHandler.on(namespace + "_selection", (JsonObject contents) -> {
			selectionFunction.run(contents.get("endColumn").getAsInt() != contents.get("startColumn").getAsInt()
					|| contents.get("endLineNumber").getAsInt() != contents.get("startLineNumber").getAsInt());
		});
	}
	
	public void configSave(IEquoRunnable<Void> saveFunction) {
		equoEventHandler.on(namespace + "_doSave", saveFunction);
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

}
