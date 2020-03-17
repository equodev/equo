import { listen, MessageConnection } from 'vscode-ws-jsonrpc';
import {
    MonacoLanguageClient, CloseAction, ErrorAction,
    MonacoServices, createConnection
} from 'monaco-languageclient';
import normalizeUrl = require('normalize-url');
const ReconnectingWebSocket = require('reconnecting-websocket');

let lastSavedVersionId: number;
let editor: monaco.editor.IStandaloneCodeEditor;
let model: monaco.editor.ITextModel;
let namespace: string;
let wasCreated: boolean = false;

// register Monaco languages
monaco.languages.register({
    id: 'json',
    extensions: ['.json', '.bowerrc', '.jshintrc', '.jscsrc', '.eslintrc', '.babelrc'],
    aliases: ['JSON', 'json'],
    mimetypes: ['application/json'],
});

// @ts-ignore
equo.on("_doCreateEditor", (values: { text: string; name: string; namespace: string; }) => {
	if (!wasCreated){
		namespace = values.namespace;

		model = monaco.editor.createModel(
		    values.text,
		    "json", // language
		    monaco.Uri.file(values.name) // uri
		);

		editor = monaco.editor.create(document.getElementById('container')!, {
		    model: model,
		    lightbulb: {
		        enabled: true
		    }
		});

		lastSavedVersionId = model.getAlternativeVersionId();

		MonacoServices.install(editor);

		editor.onDidChangeCursorSelection((e: any) => {
		    // @ts-ignore
		    equo.send(namespace + "_selection", e.selection);
		});
		
		// @ts-ignore
		equo.on(namespace + "_doFind", () => {
			editor.getAction("actions.find").run();
		});
		
		// @ts-ignore
		equo.on(namespace + "_getContents", () => {
			// @ts-ignore
			equo.send(namespace + "_doGetContents", { contents: editor.getValue() });
		});

		function notifyDirty() {
			// @ts-ignore
			equo.send(namespace + "_isDirtyNotification", { isDirty: lastSavedVersionId !== model.getAlternativeVersionId() });
		}
		
		document.onkeydown = keydown;

		function keydown(evt: any) {
			if (evt.ctrlKey && evt.keyCode == 83) { //CTRL + S
				// @ts-ignore
				equo.send(namespace + "_doSave");
			}
		}

		// @ts-ignore
		equo.on(namespace + "_didSave", () => {
			lastSavedVersionId = model.getAlternativeVersionId();
			notifyDirty();
		});

		// @ts-ignore
		equo.on(namespace + "_subscribeIsDirty", () => {
			editor.onDidChangeModelContent(() => {
				notifyDirty();
			});
		});

		// @ts-ignore
		equo.on(namespace + "_doCopy", () => {
			editor.getAction("editor.action.clipboardCopyAction").run();
		});

		// @ts-ignore
		equo.on(namespace + "_doCut", () => {
			editor.getAction("editor.action.clipboardCutAction").run();
		});

		// @ts-ignore
		equo.on(namespace + "_doPaste", () => {
			editor.focus();
			// @ts-ignore
			equo.send(namespace + "_canPaste");
		});

		// @ts-ignore
		equo.on(namespace + "_doSelectAll", () => {
			editor.focus();
			// @ts-ignore
			equo.send(namespace + "_canSelectAll");
		});

		wasCreated = true;
	}
});

// @ts-ignore
equo.send("_createEditor");

// create the web socket
const url = createUrl('/sampleServer')
const webSocket = createWebSocket(url);
// listen when the web socket is opened
listen({
    webSocket,
    onConnection: connection => {
        // create and start the language client
        const languageClient = createLanguageClient(connection);
        const disposable = languageClient.start();
        connection.onClose(() => disposable.dispose());
    }
});

function createLanguageClient(connection: MessageConnection): MonacoLanguageClient {
    return new MonacoLanguageClient({
        name: "Sample Language Client",
        clientOptions: {
            // use a language id as a document selector
            documentSelector: ['json'],
            // disable the default error handler
            errorHandler: {
                error: () => ErrorAction.Continue,
                closed: () => CloseAction.DoNotRestart
            }
        },
        // create a language client connection from the JSON RPC connection on demand
        connectionProvider: {
            get: (errorHandler, closeHandler) => {
                return Promise.resolve(createConnection(connection, errorHandler, closeHandler));
            }
        }
    });
}

function createUrl(path: string): string {
    const protocol = location.protocol === 'https:' ? 'wss' : 'ws';
    return normalizeUrl(`${protocol}://${location.host}${location.pathname}${path}`);
}

function createWebSocket(url: string): WebSocket {
    const socketOptions = {
        maxReconnectionDelay: 10000,
        minReconnectionDelay: 1000,
        reconnectionDelayGrowFactor: 1.3,
        connectionTimeout: 10000,
        maxRetries: Infinity,
        debug: false
    };
    return new ReconnectingWebSocket(url, [], socketOptions);
}
