/* --------------------------------------------------------------------------------------------
 * Copyright (c) 2018 TypeFox GmbH (http://www.typefox.io). All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 * ------------------------------------------------------------------------------------------ */
require('monaco-editor');
import '../node_modules/monaco-editor/min/vs/editor/editor.main.css'
import { EquoMonaco } from './handler';
(self as any).MonacoEnvironment = { 
    getWorkerUrl: function (moduleId:string, label:string) {
      if (label === 'json') return './json.worker.js';
      if (label === 'css') return './css.worker.js';
      if (label === 'html') return './html.worker.js';
      if (label === 'typescript' || label === 'javascript') return './typescript.worker.js';
      return './editor.worker.js';
    }
};
EquoMonaco.create(document.getElementById('container')!);