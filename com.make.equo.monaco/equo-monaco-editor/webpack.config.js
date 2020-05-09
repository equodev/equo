const path = require('path');
const lib = path.resolve(__dirname, "lib");
const resources = path.resolve(__dirname, "../resources/");
const MonacoWebpackPlugin = require('monaco-editor-webpack-plugin');

const common = {
    entry: {
        "main": path.resolve(lib, "main.js"),
    },
    output: {
        filename: '[name].bundle.js',
        path: resources
    },
    module: {
        rules: [{
            test: /\.css$/,
            use: ['style-loader', 'css-loader']
        }, {
		  test: /\.ttf$/,
		  use: ['file-loader']
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
        },
        extensions: ['.js', '.ttf']
    },
    plugins: [
        new MonacoWebpackPlugin()
    ]
};

module.exports = common;
