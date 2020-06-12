package com.make.equo.filesystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.make.equo.filesystem.api.FileInfo;
import com.make.equo.filesystem.api.IEquoFileSystem;
import com.make.equo.filesystem.provider.EquoFileSystemImpl;

public class EquoFileSystemTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	IEquoFileSystem equoFileSystem = new EquoFileSystemImpl();

	private static final String FOLDER1 = "folder1";
	private static final String FILE1 = "file1";
	private static final String FILE2 = "file2";

	private File folder1;

	@Before
	public void fillFolder() {
		File tmpFolder = folder.getRoot();
		folder1 = new File(tmpFolder, FOLDER1);
		equoFileSystem.newFolder(folder1);
		equoFileSystem.saveFile(new File(folder1, "someFile"), "");
		equoFileSystem.saveFile(new File(tmpFolder, FILE1), "");
		equoFileSystem.saveFile(new File(tmpFolder, FILE2), "");
	}

	@Test
	public void writeReadTest() throws IOException {
		File createdFile = folder.newFile("myfile.txt");
		String content = "Some test content";
		equoFileSystem.saveFile(createdFile, content);
		assertThat(equoFileSystem.readFile(createdFile)).isEqualTo(content);
	}

	@Test
	public void moveFileTest() throws IOException {
		String nameOfFile = "myfiletomove.txt";
		File createdFile = folder.newFile(nameOfFile);
		String content = "Content of moved file";
		equoFileSystem.saveFile(createdFile, content);

		equoFileSystem.moveFile(createdFile, folder1);

		File movedFile = new File(folder1, nameOfFile);
		assertTrue(!equoFileSystem.fileInfo(createdFile).exists());
		assertTrue(equoFileSystem.fileInfo(movedFile).exists());
		assertThat(equoFileSystem.readFile(movedFile)).isEqualTo(content);
	}

	@Test
	public void renameFileTest() throws IOException {
		String oldName = "myfiletorename.txt";
		File createdFile = folder.newFile(oldName);
		String content = "Content of renamed file";
		equoFileSystem.saveFile(createdFile, content);

		String newName = "renamedfile.txt";
		equoFileSystem.renameFile(createdFile, newName);

		File renamedFile = new File(folder.getRoot(), newName);
		assertTrue(!equoFileSystem.fileInfo(createdFile).exists());
		assertTrue(equoFileSystem.fileInfo(renamedFile).exists());
		assertThat(equoFileSystem.readFile(renamedFile)).isEqualTo(content);
	}

	@Test
	public void fileInfoOfFolder() throws IOException {
		FileInfo info = equoFileSystem.fileInfo(folder.getRoot());
		assertTrue(info.exists());
		assertTrue(info.isDirectory());

		boolean isFolder1 = false;
		boolean isFile1 = false;
		boolean isFile2 = false;

		for (FileInfo children : info.getChildren()) {
			assertThat(children.getChildren()).isEmpty();
			if (children.getName().equals(FOLDER1)) {
				assertTrue(children.isDirectory());
				isFolder1 = true;
			} else {
				assertTrue(!children.isDirectory());
				if (children.getName().equals(FILE1))
					isFile1 = true;
				if (children.getName().equals(FILE2))
					isFile2 = true;
			}
		}
		assertTrue(isFolder1 && isFile1 && isFile2);
	}
}
