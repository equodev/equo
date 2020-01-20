require.config({ paths: { 'vs': './node_modules/monaco-editor/min/vs' } });

var editor;
var lastSavedVersionId; 
var model;

require(['vs/editor/editor.main'], function () {
    equo.on("_doCreateEditor", values => {
        model = monaco.editor.createModel(
            values.text,
            undefined, // language
            monaco.Uri.file(values.name) // uri
          );
          
        editor = monaco.editor.create(document.getElementById('container'));
        editor.setModel(model)
        lastSavedVersionId = model.getAlternativeVersionId();
    });
    
    equo.send("_createEditor");
});

equo.on("_getContents", () => {
   equo.send("_doGetContents", { contents: editor.getValue() });
});

equo.on("_didSave", () => {
    lastSavedVersionId = model.getAlternativeVersionId();
});

equo.on("_subscribeIsDirty", () => {
    editor.onDidChangeModelContent(() => {
        equo.send("_isDirtyNotification", { isDirty: lastSavedVersionId !== model.getAlternativeVersionId() });
    });
});