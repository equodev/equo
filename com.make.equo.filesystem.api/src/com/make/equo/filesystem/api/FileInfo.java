package com.make.equo.filesystem.api;

import java.util.Collection;
import java.util.Collections;

public class FileInfo {
	private boolean exists;
	private String path;
	private String name;
	private boolean canRead;
	private boolean canWrite;
	private boolean canExecute;
	private boolean isDirectory;
	private Collection<FileInfo> children;

	public FileInfo(boolean exists, String path, String name, boolean canRead, boolean canWrite, boolean canExecute,
			boolean isDirectory, Collection<FileInfo> children) {
		this.exists = exists;
		this.path = path;
		this.name = name;
		this.canRead = canRead;
		this.canWrite = canWrite;
		this.canExecute = canExecute;
		this.isDirectory = isDirectory;
		if (children == null) {
			this.children = Collections.emptyList();
		} else {
			this.children = children;
		}
	}

	public boolean exists() {
		return exists;
	}

	public String getPath() {
		return path;
	}

	public String getName() {
		return name;
	}

	public boolean canRead() {
		return canRead;
	}

	public boolean canWrite() {
		return canWrite;
	}

	public boolean canExecute() {
		return canExecute;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	/**
	 * 
	 * @return A non-recursive collection of childrens. To know the childrens of
	 *         your childrens, you will have to obtain a new FileInfo for them
	 */
	public Collection<FileInfo> getChildren() {
		return children;
	}
}
