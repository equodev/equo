package com.make.equo.monaco.lsp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LspProxy {
	private final String serversFile = "/home/leandro/Descargas/jsonrpc-ws-proxy/servers.yml";
	private boolean serverOn = false;
	private Process process = null;
	private int instancesUsingServer = 0;
	private Map<String, Collection<String>> servers = new HashMap<>();

	private String formatServerName(String name) {
		return name.replace(" ", "");
	}

	public LspProxy addServer(String name, Collection<String> excecutionParameters) {
		servers.put(formatServerName(name), excecutionParameters);
		return this;
	}

	public synchronized void startServer() {
		saveServersInFile();
		if (!serverOn) {
			try {
				ProcessBuilder processBuilder = new ProcessBuilder("node", "/home/leandro/Descargas/jsonrpc-ws-proxy/dist/server.js", 
						"--port", "3000", "--languageServers", serversFile);
				process = processBuilder.start();
			} catch (IOException e) {
				process = null;
				e.printStackTrace();
				return;
			}
			serverOn = true;
		}
		instancesUsingServer++;
	}

	private void saveServersInFile() {
		StringBuilder content = new StringBuilder();
		content.append("langservers:\n");
		for (Entry<String, Collection<String>> server : servers.entrySet()) {
			content.append("    ");
			content.append(server.getKey());
			content.append(":\n");
			for (String parameter : server.getValue()) {
				content.append("        - ");
				content.append(parameter);
				content.append("\n");
			}
		}

		try {
			PrintWriter writer = new PrintWriter(serversFile, "UTF-8");
			writer.print(content);
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public synchronized void stopServer() {
		if (serverOn && --instancesUsingServer == 0) {
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
