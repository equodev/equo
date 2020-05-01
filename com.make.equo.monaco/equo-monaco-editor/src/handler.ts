import { listen, MessageConnection } from 'vscode-ws-jsonrpc';
import {
	MonacoLanguageClient, CloseAction, ErrorAction,
	MonacoServices, createConnection
} from 'monaco-languageclient';
import normalizeUrl = require('normalize-url');
const ReconnectingWebSocket = require('reconnecting-websocket');
import * as monaco from 'monaco-editor';

export class EquoMonacoEditor {

	private lastSavedVersionId!: number;
	private editor!: monaco.editor.IStandaloneCodeEditor;
	private model!: monaco.editor.ITextModel;
	private namespace: string | undefined;
	private wasCreated: boolean = false;
	private equo: any;

	constructor() {
		this.equo = (window as any).equo;
	}

	public create(element: HTMLElement): void {
		this.equo.on("_doCreateEditor", (values: { text: string; name: string; namespace: string; lspPath: string | null }) => {
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
					}
				});

				this.lastSavedVersionId = this.model.getAlternativeVersionId();

				this.bindEquoFunctions();

				if (values.lspPath != null) {
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

		this.equo.send("_createEditor");
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
			this.equo.send(this.namespace + "_selection", e.selection);
		});


		this.equo.on(this.namespace + "_doFind", () => {
			this.editor.getAction("actions.find").run();
		});


		this.equo.on(this.namespace + "_getContents", () => {
			this.equo.send(this.namespace + "_doGetContents", { contents: this.editor.getValue() });
		});

		this.equo.on(this.namespace + "_undo", () => {
			(this.model as any).undo();
		});


		this.equo.on(this.namespace + "_redo", () => {
			(this.model as any).redo();
		});

		this.equo.on(this.namespace + "_didSave", () => {
			this.lastSavedVersionId = this.model.getAlternativeVersionId();
			this.notifyChanges();
		});


		this.equo.on(this.namespace + "_subscribeModelChanges", () => {
			this.editor.onDidChangeModelContent(() => {
				this.notifyChanges();
			});
		});


		this.equo.on(this.namespace + "_doCopy", () => {
			this.editor.getAction("editor.action.clipboardCopyAction").run();
		});


		this.equo.on(this.namespace + "_doCut", () => {
			this.editor.getAction("editor.action.clipboardCutAction").run();
		});


		this.equo.on(this.namespace + "_doPaste", () => {
			this.editor.focus();
			this.equo.send(this.namespace + "_canPaste");
		});


		this.equo.on(this.namespace + "_doSelectAll", () => {
			this.editor.focus();
			this.equo.send(this.namespace + "_canSelectAll");
		});
	}

	private notifyChanges(): void {
		this.equo.send(this.namespace + "_changesNotification",
			{ isDirty: this.lastSavedVersionId !== this.model.getAlternativeVersionId(), canRedo: (this.model as any).canRedo(), canUndo: (this.model as any).canUndo() });
	}
}

export namespace EquoMonaco {
	export function create(element: HTMLElement): void {
		return new EquoMonacoEditor().create(element);
	}
}