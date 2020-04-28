package com.make.equo.monaco.lsp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.FrameworkUtil;

public class LspProxy {
	private static final String SERVER_FILE = "server.js";
	private int proxyPort;
	private ServerSocket socketPortReserve;
	private String serversFile = null;
	private String proxyFile = null;
	private boolean serverOn = false;
	private Process process = null;
	private int instancesUsingServer = 0;
	private Map<String, Collection<String>> servers = new HashMap<>();

	public LspProxy() {
		File bundle;
		try {
			bundle = FileLocator.getBundleFile(FrameworkUtil.getBundle(this.getClass()));
			if (bundle.isDirectory()) {
				proxyFile = new File(bundle, SERVER_FILE).toString();
			} else {
				proxyFile = extractServerFile(bundle.toString());
			}
			proxyPort = reservePortForProxy(0);
			File fileForServer = File.createTempFile("serversLsp", ".yml");
			fileForServer.deleteOnExit();
			serversFile = fileForServer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getPort() {
		return proxyPort;
	}

	private String formatServerName(String name) {
		return name.replace(" ", "");
	}

	public LspProxy addServer(String name, Collection<String> excecutionParameters) {
		servers.put(formatServerName(name), excecutionParameters);
		return this;
	}

	/**
	 * Extracts the ws proxy file along with the node modules from the jar into a
	 * temp directory
	 * 
	 * @param jarFile The jar file from which to extract the ws proxy
	 * 
	 * @return the String for the path of the ws proxy file
	 */
	public static String extractServerFile(String jarFile) throws IOException {
		Path tempDir = Files.createTempDirectory("lspserver");
		JarFile jar = new JarFile(jarFile);
		Enumeration<JarEntry> enumEntries = jar.entries();
		while (enumEntries.hasMoreElements()) {
			JarEntry file = (JarEntry) enumEntries.nextElement();
			String name = file.getName();
			if (!name.contains("node_modules") && !name.equals(SERVER_FILE)) { // Only unpack the needed files
				continue;
			}
			File f = new java.io.File(tempDir + java.io.File.separator + name);
			if (file.isDirectory()) { // if it's a directory, create it
				f.mkdir();
				continue;
			}
			f.getParentFile().mkdirs();
			f.createNewFile();
			InputStream in = new BufferedInputStream(jar.getInputStream(file));
			@SuppressWarnings("resource")
			OutputStream out = new BufferedOutputStream(new FileOutputStream(f));
			byte[] buffer = new byte[2048];
			for (;;) {
				int nBytes = in.read(buffer);
				if (nBytes <= 0) {
					break;
				}
				out.write(buffer, 0, nBytes);
			}
			out.flush();
			out.close();
			in.close();
		}
		jar.close();
		markForDelete(tempDir.toFile());
		return new File(tempDir.toString(), SERVER_FILE).toString();
	}

	/**
	 * Reserves a port to be used later by the ws proxy. It mantains a socket listen
	 * on that port until the proxy is started
	 * 
	 * @param port The port to be reserved. Use 0 to reserve a random port
	 * 
	 * @return the port reserved
	 */
	private int reservePortForProxy(int port) {
		try {
			socketPortReserve = new ServerSocket(port);
			socketPortReserve.setReuseAddress(true);
			return socketPortReserve.getLocalPort();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (int) (Math.random() * 10000 + 40000);
	}

	public synchronized void startServer() {
		saveServersInFile();
		if (!serverOn) {
			try {
				socketPortReserve.close();
				ProcessBuilder processBuilder = new ProcessBuilder("node", proxyFile, "--port",
						Integer.valueOf(getPort()).toString(), "--languageServers", serversFile);
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

	private static void markForDelete(File directoryToBeDeleted) {
		directoryToBeDeleted.deleteOnExit();
		File[] allContents = directoryToBeDeleted.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				markForDelete(file);
			}
		}
	}

	public synchronized void stopServer() {
		if (serverOn && --instancesUsingServer == 0) {
			if (process != null) {
				process.destroy();
				try {
					process.waitFor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				process = null;
			}
			reservePortForProxy(getPort());
			serverOn = false;
		}
	}

	public boolean isRunning() {
		return serverOn;
	}
}
