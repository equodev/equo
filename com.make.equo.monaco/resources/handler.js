require.config({ paths: { 'vs': '../node_modules/monaco-editor/min/vs' } });

var editor;

require(['vs/editor/editor.main'], function () {
    equo.on("_doCreateEditor", values => {
        editor = monaco.editor.create(document.getElementById('container'), {
            value: values.text,
            language: values.language
        });
        console.log(editor.getValue());
    })
    equo.send("_createEditor");
});

equo.on("_getContents", () => {
    equo.send("_doGetContents", { contents: editor.getValue() });
});