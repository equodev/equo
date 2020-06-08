import { listen, MessageConnection } from 'vscode-ws-jsonrpc';
import {
	MonacoLanguageClient, CloseAction, ErrorAction,
	MonacoServices, createConnection
} from 'monaco-languageclient';
import normalizeUrl = require('normalize-url');
const ReconnectingWebSocket = require('reconnecting-websocket');
import * as monaco from 'monaco-editor';
import { EquoWebSocketService, EquoWebSocket } from '@equo/websocket'

export class EquoMonacoEditor {

	private lastSavedVersionId!: number;
	private editor!: monaco.editor.IStandaloneCodeEditor;
	private model!: monaco.editor.ITextModel;
	private namespace!: string;
	private wasCreated: boolean = false;
	private webSocket: EquoWebSocket;

	constructor() {
		var equoWebSocketService: EquoWebSocketService = EquoWebSocketService.get();
		this.webSocket = equoWebSocketService.service;
	}


	public getEditor(): monaco.editor.IStandaloneCodeEditor {
		return this.editor;
	}

	public dispose(): void {
		this.model.dispose();
		this.webSocket.send(this.namespace + "_disposeEditor");
	}

	public create(element: HTMLElement, filePath?: string): void {
		this.webSocket.on("_doCreateEditor", (values: { text: string; name: string; namespace: string; lspPath?: string }) => {
			if (!this.wasCreated) {
				this.namespace = values.namespace;

				let l = this.getLanguageOfFile(values.name);
				let language = '';

				if (l) {
					monaco.languages.register(l);
					language = l.id;
				} else {
					language = 'userdefinedlanguage'
					monaco.languages.register({
						id: language
					});
				}

				this.model = monaco.editor.createModel(
					values.text,
					language,
					monaco.Uri.file(values.name) // uri
				);

				this.editor = monaco.editor.create(element, {
					model: this.model,
					lightbulb: {
						enabled: true
					},
					automaticLayout: true
				});

				this.lastSavedVersionId = this.model.getAlternativeVersionId();

				this.bindEquoFunctions();

				if (values.lspPath) {
					MonacoServices.install(this.editor);

					// create the web socket
					const url = normalizeUrl(values.lspPath)
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
				}


				function createLanguageClient(connection: MessageConnection): MonacoLanguageClient {
					return new MonacoLanguageClient({
						name: "Sample Language Client",
						clientOptions: {
							// use a language id as a document selector
							documentSelector: [language],
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

				this.wasCreated = true;
			}
		});

		this.webSocket.send("_createEditor", {filePath: filePath});
	}

	private getLanguageOfFile(name: string): monaco.languages.ILanguageExtensionPoint | undefined {
		let ext = '.' + name.split('.').pop();
		let languages = monaco.languages.getLanguages();
		for (let l of languages) {
			if (l.extensions) {
				for (let e of l.extensions) {
					if (e === ext) {
						return l;
					}
				}
			}
		}
		return undefined;
	}

	private bindEquoFunctions(): void {
		this.editor.onDidChangeCursorSelection((e: any) => {
			this.webSocket.send(this.namespace + "_selection", e.selection);
		});


		this.webSocket.on(this.namespace + "_doFind", () => {
			this.editor.getAction("actions.find").run();
		});


		this.webSocket.on(this.namespace + "_getContents", () => {
			this.webSocket.send(this.namespace + "_doGetContents", { contents: this.editor.getValue() });
		});

		this.webSocket.on(this.namespace + "_undo", () => {
			(this.model as any).undo();
		});

		this.webSocket.on(this.namespace + "_redo", () => {
			(this.model as any).redo();
		});

		this.webSocket.on(this.namespace + "_didSave", () => {
			this.lastSavedVersionId = this.model.getAlternativeVersionId();
			this.notifyChanges();
		});


		this.webSocket.on(this.namespace + "_subscribeModelChanges", () => {
			this.editor.onDidChangeModelContent(() => {
				this.notifyChanges();
			});
		});


		this.webSocket.on(this.namespace + "_doCopy", () => {
			this.editor.getAction("editor.action.clipboardCopyAction").run();
		});


		this.webSocket.on(this.namespace + "_doCut", () => {
			this.editor.getAction("editor.action.clipboardCutAction").run();
		});


		this.webSocket.on(this.namespace + "_doPaste", () => {
			this.editor.focus();
			this.webSocket.send(this.namespace + "_canPaste");
		});


		this.webSocket.on(this.namespace + "_doSelectAll", () => {
			this.editor.focus();
			this.webSocket.send(this.namespace + "_canSelectAll");
		});
	}

	private notifyChanges(): void {
		this.webSocket.send(this.namespace + "_changesNotification",
			{ isDirty: this.lastSavedVersionId !== this.model.getAlternativeVersionId(), canRedo: (this.model as any).canRedo(), canUndo: (this.model as any).canUndo() });
	}
}

export namespace EquoMonaco {
	export function create(element: HTMLElement, filePath?: string): EquoMonacoEditor {
		let monacoEditor = new EquoMonacoEditor();
		monacoEditor.create(element, filePath);
		return monacoEditor;
	}
}