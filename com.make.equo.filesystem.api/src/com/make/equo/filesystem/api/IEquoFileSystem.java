package com.make.equo.filesystem.api;

import java.io.File;

public interface IEquoFileSystem {

	/**
	 * @return The file selected. Null if it's canceled
	 */
	public File openFile();

	/**
	 * @return The folder selected. Null if it's canceled
	 */
	public File openFolder();

	public FileInfo fileInfo(File file);

	public boolean newFolder(File folder);

	public boolean saveFile(File file, String content);

	/**
	 * @return The file that was selected and saved. Null if it's canceled or there
	 *         was an error
	 */
	public File saveFileAs(String content);

	/**
	 * @return The content of the file. Null if there was an error
	 */
	public String readFile(File file);

	public boolean renameFile(File file, String newName);

	public boolean moveFile(File file, File folderDest);

	public boolean copyFile(File file, File folderDest);
}
