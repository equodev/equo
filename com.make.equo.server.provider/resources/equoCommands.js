window.equo = window.equo || {};

(function (equo) {

    equo.saveFile = function (filePath, content, callback) {
        equo.executeCommand(callback, "org.eclipse.ui.file.save", filePath, content);
    };

    equo.saveFileAs = function (content, callback) {
        equo.executeCommand(callback, "org.eclipse.ui.file.saveAs", null, content);
    };

    equo.renameFile = function (filePath, newName, callback) {
        equo.executeCommand(callback, "org.eclipse.ui.edit.rename", filePath, newName);
    };

    equo.moveFile = function (filePath, directoryDest, callback) {
        equo.executeCommand(callback, "org.eclipse.ui.edit.move", filePath, directoryDest);
    };

    equo.openFile = function (callback) {
        equo.executeCommand(callback, "org.eclipse.ui.file.open");
    };

    equo.readFile = function (filePath, callback) {
        equo.executeCommand(callback, "org.eclipse.ui.file.read", filePath);
    };

    equo.deleteFile = function (filePath, callback) {
        equo.executeCommand(callback, "org.eclipse.ui.file.delete", filePath);
    };

    equo.fileInfo = function (filePath, callback) {
        equo.executeCommand(callback, "org.eclipse.ui.file.properties", filePath);
    };

    equo.openFolder = function (callback) {
        equo.executeCommand(callback, "org.eclipse.ui.project.openProject");
    };

    equo.save = function (callback) {
        equo.executeCommand(callback, "org.eclipse.ui.file.save");
    };

}(equo));
