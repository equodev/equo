require.config({ paths: { 'vs': '../node_modules/monaco-editor/min/vs' } });
require(['vs/editor/editor.main'], function () {
    equo.on("_createEditor", values => {
        editor = monaco.editor.create(document.getElementById('container'), {
            value: values.text,
            language: values.language
        });
    })
});