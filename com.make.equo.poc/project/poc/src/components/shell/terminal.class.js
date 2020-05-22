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

export class Terminal {
    constructor (opts) {
        if (opts.role === "client") {
            if (!opts.parentId) throw "Missing options";
            this.xTerm = require("xterm");
            this.xTerm = new Terminal();
            console.log(this.xTerm.loadAddon)
            this.xTerm.loadAddon('attach');
            this.xTerm.loadAddon('fit');

            this.sendSizeToServer = () => {
                let cols = this.term.cols.toString();
                let rows = this.term.rows.toString();
                while (cols.length < 3) {
                    cols = "0"+cols;
                }
                while (rows.length < 3) {
                    rows = "0"+rows;
                }
                this.socket.send("ESCAPED|-- RESIZE:"+cols+";"+rows);
            };

            this.term = new this.xTerm({
                cols: 80,
                rows: 24
            });
            this.term.open(document.getElementById(opts.parentId), true);

            let sockHost = opts.host || "127.0.0.1";
            let sockPort = opts.port || 3000;

            this.socket = new WebSocket("ws://"+sockHost+":"+sockPort);
            this.socket.onopen = () => {
                this.term.attach(this.socket);
            };
            this.socket.onerror = (e) => {throw e};

            this.fit = () => {
                this.term.fit();
                setTimeout(() => {
                    this.sendSizeToServer();
                }, 50);
            }

            this.resize = (cols, rows) => {
                this.term.resize(cols, rows);
                this.sendSizeToServer();
            }
        } else if (opts.role === "server") {

            this.Pty = require("node-pty");
            this.Websocket = require("ws").Server;

            this.onclosed = () => {};
            this.onopened = () => {};
            this.onresize = () => {};
            this.ondisconnected = () => {};

            this.tty = this.Pty.spawn(opts.shell || "bash", [], {
                name: 'xterm-color',
                cols: 80,
                rows: 24,
                cwd: process.env.PWD,
                env: process.env
            });

            this.tty.on('exit', (code, signal) => {
                this.onclosed(code, signal);
            });
            this.wss = new this.Websocket({
                port: opts.port || 3000,
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

// module.exports = {
//     Terminal
// };