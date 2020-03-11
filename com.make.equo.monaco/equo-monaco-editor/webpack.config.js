const path = require('path');
const lib = path.resolve(__dirname, "../resources");

const common = {
    entry: {
        "main": path.resolve(lib, "main.js"),
        "editor.worker": 'monaco-editor-core/esm/vs/editor/editor.worker.js'
    },
    output: {
        filename: '[name].bundle.js',
        path: lib
    },
    module: {
        rules: [{
            test: /\.css$/,
            use: ['style-loader', 'css-loader']
        }]
    },
    target: 'web',
    node: {
        fs: 'empty',
        child_process: 'empty',
        net: 'empty',
        crypto: 'empty'
    },
    resolve: {
        alias: {
            'vscode': require.resolve('monaco-languageclient/lib/vscode-compatibility')
        }
    }
};

module.exports = common;