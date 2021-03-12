window.equo = window.equo || {};

function sendFileSystemCommand(callback, commandId, filePath = null, content = null) {
    let responseId = (Math.random() + 1).toString(36).substring(7);
    equo.on(responseId, callback);
    equo.sendToWebSocketServer(commandId, {
        commandId: commandId + ".command",
        responseId: responseId,
        filePath: filePath,
        content: content
    });
};

(function (equo) {

    equo.saveFile = function (filePath, content, callback) {
        sendFileSystemCommand(callback, "_SaveFile", filePath, content);
    };

    equo.saveFileAs = function (content, callback) {
        sendFileSystemCommand(callback, "_SaveFileAs", null, content);
    };

    equo.newFolder = function (path, callback) {
        sendFileSystemCommand(callback, "_NewFolder", path);
    };

    equo.renameFile = function (filePath, newName, callback) {
        sendFileSystemCommand(callback, "_RenameFile", filePath, newName);
    };

    equo.moveFile = function (filePath, directoryDest, callback) {
        sendFileSystemCommand(callback, "_MoveFile", filePath, directoryDest);
    };

    equo.copyFile = function (filePath, directoryDest, callback) {
        sendFileSystemCommand(callback, "_CopyFile", filePath, directoryDest);
    };

    equo.openFile = function (callback) {
        sendFileSystemCommand(callback, "_OpenFile");
    };

    equo.readFile = function (filePath, callback) {
        sendFileSystemCommand(callback, "_ReadFile", filePath);
    };

    equo.deleteFile = function (filePath, callback) {
        sendFileSystemCommand(callback, "_DeleteFile", filePath);
    };

    equo.fileInfo = function (filePath, callback) {
        sendFileSystemCommand(callback, "_FileInfo", filePath);
    };

    equo.openFolder = function (callback) {
        sendFileSystemCommand(callback, "_OpenFolder");
    };

}(equo));
