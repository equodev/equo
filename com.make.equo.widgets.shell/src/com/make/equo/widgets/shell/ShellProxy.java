package com.make.equo.widgets.shell;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import org.eclipse.core.runtime.FileLocator;

@Component
public class ShellProxy {
	private static final String TEMP_SERVER_DIRNAME = "shellServer";
	private static final String SHELL_SERVER_FILENAME = "shell-server.js";
	private boolean serverOn = false;
	private Process process = null;
	private String serverPath;

	@Activate
	public void start() {
		try {
			serverPath = FileLocator.getBundleFile(FrameworkUtil.getBundle(this.getClass())).toString();
			new Thread(() -> this.startServer()).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("shell-server started");
	}

	@Deactivate
	public void finish() {
		this.stopServer();
		System.out.println("shell-server stopped");
	}

	public synchronized void startServer() {
		if (!serverOn) {
			try {
				String serverAbsolutePath = extractServerFiles();
				ProcessBuilder processBuilder;
				String so = System.getProperty("os.name").toLowerCase();
				if(so.equals("linux") || so.equals("mac")) {
					processBuilder = new ProcessBuilder("bash", "-c", "cd "+ serverAbsolutePath + " ; yarn install");
				}else {
					processBuilder = new ProcessBuilder("cmd.exe", "/c", "cd "+ serverAbsolutePath + " ; yarn install");
				}
				
				process = processBuilder.start();
				while(process.isAlive());
				processBuilder = new ProcessBuilder("node", serverAbsolutePath + '/'  + SHELL_SERVER_FILENAME);
				process = processBuilder.start();
			} catch (IOException e) {
				process = null;
				e.printStackTrace();
				return;
			}
			serverOn = true;
		}
	}

	@SuppressWarnings("resource")
	private String extractServerFiles() throws IOException, FileNotFoundException {
		Path tempDir = Files.createTempDirectory(TEMP_SERVER_DIRNAME);
		JarFile jar = new JarFile(serverPath);
		Enumeration<JarEntry> enumEntries = jar.entries();
		while (enumEntries.hasMoreElements()) {
			JarEntry file = (JarEntry) enumEntries.nextElement();
			File f = new java.io.File(tempDir + java.io.File.separator + file.getName());
			if(f.getAbsolutePath().contains("node_modules")) {
				continue;
			}
			if (file.isDirectory()) { 
				f.mkdir();
				continue;
			}
			f.getParentFile().mkdirs();
			f.createNewFile();

			InputStream in = new BufferedInputStream(jar.getInputStream(file));
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
		return new File(tempDir.toString()).toString();

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