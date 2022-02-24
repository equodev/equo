const path = require('path')

module.exports = {
  entry: path.resolve(__dirname, 'lib/index.js'),
  mode: 'production',
  output: {
    library: {
      type: 'var',
      export: 'EquoCommService',
      name: 'EquoCommService'
    },
    path: path.resolve(__dirname, 'lib-browser'),
    filename: 'equo-comm.js'
  }
}
