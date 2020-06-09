package com.make.equo.filesystem.api;

import java.io.File;

public interface IEquoFileSystem {
	public File openFile();

	public File openFolder();

	public FileInfo fileInfo(File file);

	public boolean newFolder(File folder);

	public boolean saveFile(File file, String content);

	public File saveFileAs(String content);

	public String readFile(File file);

	public boolean renameFile(File file, String newName);

	public boolean moveFile(File file, File folderDest);
}
