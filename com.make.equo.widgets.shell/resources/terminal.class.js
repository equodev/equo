/* ****************************************************************
    Create a terminal receiver or server.
    Currently the server only accepts one connection at a time and
    does not support SSL. If you wish to use to connect to a
    terminal over the internet, PLEASE do not use this class as-is.

    
    --SERVER-------------------------------------------------------
    
    Server requirements:
        - node-pty
        - ws
    Server usage:
        terminalServer = new Terminal({
            role: "server",
            port: 3000, // 3000 if empty
            shell: "bash"  // Command to run, "bash" by default
        })
    Server events:
        terminalServer.onopened  // Connected to a receiver
                      .ondisconnected  // Disconnected from receiver
                      .onclosed // Terminal was exited
                            // Args: code, signal
                      .onresized // Terminal was resized
                            // Args: cols, rows
    Server methods:
        None.
        
    --CLIENT--------------------------------------------------------
    
    Client requirements:
            - xterm.js
            - browser with WebSocket support
    Client usage:
        terminalClient = new Terminal({
            role: "client",
            parentId: "someid", // ID of the terminal container element
            port: 3000, // 3000 if empty
            host: "127.0.0.1" // Localhost by default
        })
    Client events:
        None.
    Client methods:
        terminalClient.fit() // Resizes the terminal to match the container's size
                      .resize(cols, rows) // Manual resize
    
******************************************************************* */

class Terminal {
    constructor(opts) {
         if (opts.role === "server") { ///////------------------------------------------------------------------------

            this.Pty = require("node-pty");
            this.Websocket = require("ws").Server;

            this.onclosed = () => {};
            this.onopened = () => {};
            this.onresize = () => {};
            this.ondisconnected = () => {};
            this.getPort = () => {
                return this.wss._server.address().port
            };

            try {
                this.tty = this.Pty.spawn(opts.shell || "bash", [], {
                    name: 'xterm-color',
                    cols: 80,
                    rows: 24,
                    cwd: process.env.PWD,
                    env: process.env
                });
            } catch (e) {
                console.log('e',e)
            }

            this.tty.on('exit', (code, signal) => {
                this.onclosed(code, signal);
            });
            this.wss = new this.Websocket({
                port: 0,
                clientTracking: true,
                verifyClient: () => {
                    if (this.wss.clients.length >= 1) {
                        return false;
                    } else {
                        return true;
                    }
                }
            });
            this.wss.on('connection', (ws) => {
                this.onopened();
                ws.on('message', (msg) => {
                    if (msg.startsWith("ESCAPED|-- ")) {
                        if (msg.startsWith("ESCAPED|-- RESIZE:")) {
                            msg = msg.substr(18);
                            let cols = msg.slice(0, -4);
                            let rows = msg.substr(4);
                            this.tty.resize(Number(cols), Number(rows));
                            this.onresized(cols, rows);
                        }
                    } else {
                        this.tty.write(msg);
                    }
                });
                this.tty.on('data', (data) => {
                    try {
                        ws.send(data);
                    } catch (e) {
                        // Websocket closed
                    }
                });
            });

            this.wss.on('close', () => {
                this.ondisconnected();
            });
        } else {
            throw "Unknow purpose";
        }
    }
}

module.exports = {
    Terminal
};