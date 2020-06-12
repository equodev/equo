package com.make.equo.filesystem.provider;

import java.util.Collection;

import com.make.equo.filesystem.api.FileInfo;

public class FileInfoBuilder {
	private boolean exists;
	private String path;
	private String name;
	private boolean canRead;
	private boolean canWrite;
	private boolean canExecute;
	private boolean isDirectory;
	private Collection<FileInfo> children;

	protected void setExists(boolean exists) {
		this.exists = exists;
	}

	protected void setPath(String path) {
		this.path = path;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected void setCanRead(boolean canRead) {
		this.canRead = canRead;
	}

	protected void setCanWrite(boolean canWrite) {
		this.canWrite = canWrite;
	}

	protected void setCanExecute(boolean canExecute) {
		this.canExecute = canExecute;
	}

	protected void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	protected void setChildren(Collection<FileInfo> children) {
		this.children = children;
	}

	protected FileInfo build() {
		return new FileInfo(exists, path, name, canRead, canWrite, canExecute, isDirectory, children);
	}
}
