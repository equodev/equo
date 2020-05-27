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
    resolve: {
      alias: {
        'vscode': require.resolve('monaco-languageclient/lib/vscode-compatibility')
      },
    },
    plugins: [
      new MonacoWebpackPlugin()
    ]
  }
}