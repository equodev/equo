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

export class EquoTerminal {
    constructor (opts) {
        if (opts.role === "client") {
            if (!opts.parentId) throw "Missing options";
            const FitAddon = require('xterm-addon-fit').FitAddon;
            const AttachAddon = require('xterm-addon-attach').AttachAddon;
            const Terminal = require('xterm').Terminal;
            this.xTerm = new Terminal({
                rows:15
            });

            let sockHost = opts.host || "127.0.0.1";
            let sockPort = opts.port || 3000;

            this.socket = new WebSocket("ws://"+sockHost+":"+sockPort);
            this.xTerm.loadAddon(new AttachAddon(this.socket));
            this.xTerm.loadAddon(new FitAddon());

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

            // this.term = new Terminal({
            //     cols: 80,
            //     rows: 24
            // });
            this.xTerm.open(document.getElementById(opts.parentId), true);
            this.xTerm.write(' ')

            this.socket.onopen = () => {
            };
            this.socket.onerror = (e) => {throw e};

            this.fit = () => {
                this.xTerm.fit();
                setTimeout(() => {
                    this.sendSizeToServer();
                }, 50);
            }

            this.resize = (cols, rows) => {
                this.xTerm.resize(cols, rows);
                this.sendSizeToServer();
            }
        } 
    }
}
