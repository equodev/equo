const MonacoWebpackPlugin = require('monaco-editor-webpack-plugin');

module.exports = {
  "transpileDependencies": [
    "vuetify"
  ],
  chainWebpack: config => {
    config.module
      .rule('node')
      .test(/\.node$/)
      .use('node-loader')
      .loader('node-loader')
      .end();
  },
   configureWebpack: {
    devtool: 'source-map',
    node: {
        "fs": "empty",
        "global": true,
        "crypto": "empty",
        "tls": "empty",
        "net": "empty",
        "process": true,
        "module": false,
        "clearImmediate": false,
        "setImmediate": true
    },
    resolve: {
      alias: {
        'vscode': require.resolve('monaco-languageclient/lib/vscode-compatibility')
      }
    },
    plugins: [
      new MonacoWebpackPlugin()
    ]
  }
}