require.config({ paths: { 'vs': '../node_modules/monaco-editor/min/vs' } });

var editor;

require(['vs/editor/editor.main'], function () {
    equo.on("_doCreateEditor", values => {
        const model = monaco.editor.createModel(
            values.text,
            undefined, // language
            monaco.Uri.file(values.name) // uri
          )
          
          editor = monaco.editor.create(document.getElementById('container'));
          editor.setModel(model)
       
        });
    
    equo.send("_createEditor");
});

equo.on("_getContents", () => {
   equo.send("_doGetContents", { contents: editor.getValue() });
});