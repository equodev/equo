const path = require('path');
const lib = path.resolve(__dirname, "lib");
const resources = path.resolve(__dirname, "../resources/");

const common = {
    entry: {
        "index": path.resolve(lib, "index.js"),
    },
    output: {
        filename: '[name].bundle.js',
        path: resources
    },
    target: 'web',
    node: {
        fs: 'empty',
        child_process: 'empty',
        net: 'empty',
        crypto: 'empty'
    },
    resolve: {
        modules: [path.resolve(__dirname, "../../com.make.equo.node.packages/node_modules/"), "node_modules"],
        extensions: ['.js', '.ttf']
    }
};

module.exports = common;
