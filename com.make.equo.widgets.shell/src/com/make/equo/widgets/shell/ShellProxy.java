package com.make.equo.widgets.shell;

import java.io.IOException;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;


@Component
public class ShellProxy {
	private boolean serverOn = false;
	private Process process = null;

	@Activate
	public void start() {
		new Thread(() -> this.startServer()).start();
		System.out.println("shell-server started");
	}
	
	public synchronized void startServer() {
		if (!serverOn) {
			try {                                           /* hardcoded path */
				ProcessBuilder processBuilder = new ProcessBuilder("node", "/home/make2019/Desktop/Shell/Server/shell-server.js");
				process = processBuilder.start();
			} catch (IOException e) {
				process = null;
				e.printStackTrace();
				return;
			}
			serverOn = true;
		}
	}
	
	public synchronized void stopServer() {
		if (serverOn) {
			if (process != null) {
				process.destroy();
				process = null;
			}
			serverOn = false;
		}
	}

	public boolean isRunning() {
		return serverOn;
	}


}