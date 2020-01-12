package com.make.equo.monaco;

import static com.make.equo.monaco.util.IMonacoConstants.EQUO_MONACO_CONTRIBUTION_NAME;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.eclipse.swt.chromium.Browser;
import org.eclipse.swt.widgets.Composite;

import com.google.gson.JsonObject;
import com.make.equo.ws.api.IEquoEventHandler;
import com.make.equo.ws.api.IEquoRunnable;

public class EquoMonacoEditor {

	private Browser browser;
	private IEquoEventHandler equoEventHandler;
	private final Semaphore lock = new Semaphore(1);

	public EquoMonacoEditor(Composite parent, int style, IEquoEventHandler handler, String contents, String fileName) {
		this.equoEventHandler = handler;
		browser = new Browser(parent, style);
		browser.setUrl("http://" + EQUO_MONACO_CONTRIBUTION_NAME);
		createEditor(contents, fileName);
	}

	private void createEditor(String contents, String fileName) {
		equoEventHandler.on("_createEditor", (IEquoRunnable<Void>) runnable -> handleCreateEditor(contents, fileName));
	}

	private void handleCreateEditor(String contents, String fileName) {
		Map<String, String> editorData = new HashMap<String, String>();
		editorData.put("text", contents);
		editorData.put("name", fileName);
		equoEventHandler.send("_doCreateEditor", editorData);
	}

	public void getContentsSync(IEquoRunnable<String> runnable) {
		if (lock.tryAcquire()) {
			equoEventHandler.on("_doGetContents", (JsonObject contents) -> {
				try {
					runnable.run(contents.get("contents").getAsString());
				} finally {
					synchronized (lock) {
						lock.notify();
						lock.release();
					}
				}
			});

			equoEventHandler.send("_getContents");
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
		equoEventHandler.on("_doGetContents", (JsonObject contents) -> {
			runnable.run(contents.get("contents").getAsString());
		});
		equoEventHandler.send("_getContents");
	}

}
