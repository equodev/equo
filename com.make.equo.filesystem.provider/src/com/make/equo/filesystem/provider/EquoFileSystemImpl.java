package com.make.equo.filesystem.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.osgi.service.component.annotations.Component;

import com.make.equo.filesystem.api.FileInfo;
import com.make.equo.filesystem.api.IEquoFileSystem;

@Component
public class EquoFileSystemImpl implements IEquoFileSystem {

	@Override
	public File openFile() {
		FileDialog dialog = new FileDialog(Display.getDefault().getShells()[0], SWT.OPEN);
		dialog.setText("Open file");
		String result = dialog.open();
		if (result == null) {
			return null;
		}
		return new File(result);
	}

	@Override
	public File openFolder() {
		DirectoryDialog dialog = new DirectoryDialog(Display.getDefault().getShells()[0], SWT.OPEN);
		dialog.setText("Open Folder");
		String result = dialog.open();
		if (result == null) {
			return null;
		}
		return new File(result);
	}

	@Override
	public FileInfo fileInfo(File file) {
		FileInfoBuilder response = getItem(file);
		if (file.isDirectory()) {
			List<FileInfo> children = new ArrayList<>();
			for (File f : file.listFiles()) {
				children.add(getItem(f).build());
			}
			response.setChildren(children);
		}
		return response.build();
	}

	private static FileInfoBuilder getItem(File file) {
		FileInfoBuilder response = new FileInfoBuilder();
		response.setCanExecute(file.canExecute());
		response.setCanRead(file.canRead());
		response.setCanWrite(file.canWrite());
		response.setDirectory(file.isDirectory());
		response.setExists(file.exists());
		response.setName(file.getName());
		response.setPath(file.getAbsolutePath());
		return response;
	}

	@Override
	public boolean newFolder(File folder) {
		return folder.mkdirs();
	}

	@Override
	public boolean saveFile(File file, String content) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(file);
			writer.print(content);
			writer.close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		}
	}

	@Override
	public File saveFileAs(String content) {
		FileDialog dialog = new FileDialog(Display.getDefault().getShells()[0], SWT.SAVE);
		dialog.setText("Save file");
		String result = dialog.open();
		if (result == null) {
			return null;
		}
		File savedFile = new File(result);
		if (!saveFile(savedFile, content)) {
			return null;
		}
		return savedFile;
	}

	@Override
	public String readFile(File file) {
		Path filePath = FileSystems.getDefault().getPath(file.getAbsolutePath());
		try {
			String content = Files.lines(filePath).collect(Collectors.joining("\n"));
			return content;
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public boolean renameFile(File file, String newName) {
		File fileDest = new File(file.getParentFile(), newName);
		return internalMoveFile(file, fileDest);
	}

	@Override
	public boolean moveFile(File file, File folderDest) {
		File fileDest = new File(folderDest, file.getName());
		return internalMoveFile(file, fileDest);
	}

	protected boolean internalMoveFile(File file, File fileDest) {
		if (fileDest.exists()) {
			return false;
		}
		fileDest.getParentFile().mkdirs();
		return file.renameTo(fileDest);
	}

	@Override
	public boolean copyFile(File file, File folderDest) {
		File fileDest = new File(folderDest, file.getName());
		try {
			if (file.isDirectory()) {
				FileUtils.copyDirectory(file, fileDest);
			} else {
				FileUtils.copyFile(file, fileDest);
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}

}
